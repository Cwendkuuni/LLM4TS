import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonFormat;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @Test
 public void testDefaultSerialization() throws JsonGenerationException, JsonMappingException, IOException {
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(date, gen, new SerializerProvider());
 assertEquals("2023-03-16 13:31:05.527", gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
 serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(new Date(), gen, new SerializerProvider());
 assertEquals("2023-03-16 13:31:05.527 PST", gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializationWithUseTimestampTrue() throws JsonGenerationException, JsonMappingException, IOException {
 serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(new Date(), gen, new SerializerProvider());
 assertEquals("1647346365527", gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializationWithCustomLocaleAndTimeZone() throws JsonGenerationException, JsonMappingException, IOException {
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 customFormat.setLocale(Locale.CHINESE);
 customFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
 serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(new Date(), gen, new SerializerProvider());
 assertEquals("2023-03-16 21:31:05 CST", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDeserialization() throws JsonMappingException, JsonParseException, IOException {
 String jsonString = "2023-03-16 13:31:05.527 PST";
 Date date = mapper.readValue(new TextNode(jsonString), Date.class);
 assertEquals(new Date(), date);
 }

 @Test
 public void testDeserializationWithCustomFormat() throws JsonMappingException, JsonParseException, IOException {
 String jsonString = "2023-03-16 13:31:05.527 PST";
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
 mapper.setDateFormatInstances(customFormat);
 Date date = mapper.readValue(new TextNode(jsonString), Date.class);
 assertEquals(new Date(), date);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidJsonString() throws JsonMappingException, JsonParseException, IOException {
 String jsonString = "InvalidJsonString";
 mapper.readValue(new TextNode(jsonString), Date.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithNullJsonString() throws JsonMappingException, JsonParseException, IOException {
 mapper.readValue(JsonNode.nullNode, Date.class);
 }

 @Test
 public void testSerializationWithJsonFormatAnnotation() throws JsonGenerationException, JsonMappingException, IOException {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS zzz")
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(date, gen, new SerializerProvider());
 assertEquals("2023-03-16 13:31:05.527 PST", gen.getOutputBuffer().toString());
 }

 @Test
 public void testDeserializationWithJsonFormatAnnotation() throws JsonMappingException, JsonParseException, IOException {
 @JsonDeserialize(using = DateTimeDeserializer.class)
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS zzz")
 Date date = mapper.readValue(new TextNode("2023-03-16 13:31:05.527 PST"), Date.class);
 assertEquals(new Date(), date);
 }
}

class DateTimeDeserializer extends StdScalarDeserializer<Date> {

 public DateTimeDeserializer() {
 this(null);
 }

 protected DateTimeDeserializer(Class<Date> vc) {
 super(Date.class);
 }

 @Override
 public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 DateFormat customFormat = ctxt.getContextualValue(JsonFormatTypes.DateFormatContextualizer.class, Locale.getDefault(), null);
 if (customFormat != null) {
 try {
 return customFormat.parse(p.getText());
 } catch (ParseException e) {
 throw new JsonMappingException(String.format("Failed to parse date string: %s", p.getText()), e);
 }
 } else {
 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
 return df.parse(p.getText());
 }
 }
}