import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 serializer = new StdDateSerializer();
 }

 @Test
 public void testWithFormat() {
 DateTimeSerializerBase<Date> serializerWithFormat = serializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy"));
 assertEquals(serializerWithFormat.getClass(), serializerWithFormat.getClass());
 assertNotNull(serializerWithFormat._customFormat);
 }

 @Test
 public void testCreateContextualWithFormat() throws JsonMappingException {
 Date date = new Date();
 JsonFormat.Value format = new JsonFormat.Value().shape(JsonFormat.Shape.STRING).pattern("dd-MM-yyyy").timeZone(TimeZone.getTimeZone("UTC"));
 SerializerProvider provider = mapper.getSerializerProvider();
 JsonSerializer<?> serializerWithFormat = serializer.createContextual(provider, null);
 assertNotNull(serializerWithFormat);
 assertSame(serializerWithFormat.getClass(), serializerWithFormat.getClass());
 assertTrue(serializerWithFormat instanceof DateTimeSerializerBase);
 DateTimeSerializerBase<? extends Object> dtSerializer = (DateTimeSerializerBase<? extends Object>) serializerWithFormat;
 assertEquals(dtSerializer._customFormat.getPattern(), "dd-MM-yyyy");
 assertEquals(dtSerializer._customFormat.getTimeZone(), TimeZone.getTimeZone("UTC"));
 }

 @Test
 public void testSerializeWithFormat() throws JsonProcessingException, JsonGenerationException, IOException {
 Date date = new Date();
 ObjectNode node = mapper.createObjectNode();
 JsonSerializer<Date> serializerWithFormat = serializer.createContextual(mapper.getSerializerProvider(), null);
 JsonGenerator gen = mapper.getFactory().createGenerator(mapper.getOutputFactory(), node);
 serializerWithFormat.serialize(date, gen, mapper.getSerializerProvider());
 gen.flush();
 assertEquals("\"dd-MM-yyyy\"", node.get("dateOfBirth").asText());
 }

 @Test
 public void testSerializeWithoutFormat() throws JsonProcessingException, JsonGenerationException, IOException {
 Date date = new Date();
 ObjectNode node = mapper.createObjectNode();
 JsonSerializer<Date> serializerWithoutFormat = serializer.createContextual(mapper.getSerializerProvider(), null);
 JsonGenerator gen = mapper.getFactory().createGenerator(mapper.getOutputFactory(), node);
 serializerWithoutFormat.serialize(date, gen, mapper.getSerializerProvider());
 gen.flush();
 assertNull(node.get("dateOfBirth"));
 }

 @Test
 public void testSerializeWithFormatUseTimestamp() throws JsonProcessingException, JsonGenerationException, IOException {
 Date date = new Date();
 ObjectMapper mapperWithTimestamp = new ObjectMapper();
 mapperWithTimestamp.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
 ObjectNode node = mapperWithTimestamp.createObjectNode();
 JsonSerializer<Date> serializerWithFormat = serializer.createContextual(mapperWithTimestamp.getSerializerProvider(), null);
 JsonGenerator gen = mapperWithTimestamp.getFactory().createGenerator(mapperWithTimestamp.getOutputFactory(), node);
 serializerWithFormat.serialize(date, gen, mapperWithTimestamp.getSerializerProvider());
 gen.flush();
 assertEquals("null", node.get("dateOfBirth").asText());
 }
}