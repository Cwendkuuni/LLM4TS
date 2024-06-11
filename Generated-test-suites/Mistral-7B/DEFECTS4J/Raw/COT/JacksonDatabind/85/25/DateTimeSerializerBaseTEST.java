import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import junit.framework.TestCase;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializer;
 private ObjectMapper mapper;

 @Before
 public void setUp() {
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 mapper = new ObjectMapper();
 }

 @Test
 public void testConstructor() {
 assertTrue(dateTimeSerializer instanceof DateTimeSerializerBase);
 assertFalse(dateTimeSerializer instanceof StdScalarSerializer);
 assertFalse(dateTimeSerializer instanceof ContextualSerializer);
 }

 @Test
 public void testGetClass() {
 assertEquals(Date.class, dateTimeSerializer.getClass());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(dateTimeSerializer.isEmpty(null));
 assertFalse(dateTimeSerializer.isEmpty(new Date()));
 assertTrue(dateTimeSerializer.isEmpty(dateTimeSerializer.withFormat(Boolean.TRUE, null).serialize(null, null, null)));
 }

 @Test
 public void testGetSchema() {
 JsonNode schema = dateTimeSerializer.getSchema(mapper.getSerializationConfig(), Date.class);
 assertEquals("number", schema.getClass().getSimpleName());
 }

 @Test
 public void testSerialize() throws IOException {
 Date date = new Date();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 dateTimeSerializer.serialize(date, gen, mapper.getSerializationConfig());
 String serializedDate = gen.getOutputString();
 assertTrue(serializedDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z"));

 gen.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateContextualNullProvider() throws JsonMappingException {
 dateTimeSerializer.createContextual(null, null);
 }

 @Test
 public void testCreateContextualWithFormat() throws JsonMappingException {
 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
 customFormat.setTimeZone(timeZone);
 JsonFormat.Shape shape = new JsonFormat.StandardShape("dateTime", JsonFormat.Value.custom(customFormat));
 DateTimeSerializerBase<Date> serializedDateSerializer = dateTimeSerializer.withFormat(Boolean.FALSE, customFormat);
 JsonFormat<Date> jsonFormat = new JsonDeserializer.WithResolvedFormatter<Date>(serializedDateSerializer) {}.construct(shape, null);
 ObjectReader objectReader = mapper.readerFor(Date.class);
 JsonParser jsonParser = mapper.getFactory().createParser("[\"25-06-2023 12:34:56\"]");
 jsonParser.setCodec(mapper);
 jsonParser.setCodecFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 Date deserializedDate = objectReader.readValue(jsonParser);
 assertEquals("25-Jun-2023 12:34:56", deserializedDate.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualInvalidFormat() throws JsonMappingException {
 JsonFormat.Shape shape = new JsonFormat.StandardShape("dateTime", JsonFormat.Value.dateFormat("INVALID_FORMAT"));
 DateTimeSerializerBase<Date> serializedDateSerializer = dateTimeSerializer.withFormat(Boolean.FALSE, null);
 JsonFormat<Date> jsonFormat = new JsonDeserializer.WithResolvedFormatter<Date>(serializedDateSerializer) {}.construct(shape, null);
 ObjectReader objectReader = mapper.readerFor(Date.class);
 JsonParser jsonParser = mapper.getFactory().createParser("[\"INVALID_FORMAT\"]");
 jsonParser.setCodec(mapper);
 jsonParser.setCodecFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 Date deserializedDate = objectReader.readValue(jsonParser);
 fail("Expected JsonMappingException");
 }
}