import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializer;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 }

 @Test
 public void testSerializationWithDefaultFormat() throws JsonGenerationException {
 Date date = new Date();
 JsonGenerator gen = new JsonFactory().createGenerator();
 dateTimeSerializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 String result = gen.getOutputBuffer().toString();
 assertEquals("\"2022-12-31T12:00:00.000Z\"", result);
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonGenerationException {
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<>(Date.class, false, customFormat);
 Date date = new Date();
 JsonGenerator gen = new JsonFactory().createGenerator();
 customSerializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 String result = gen.getOutputBuffer().toString();
 assertEquals("\"2022-12-31 12:00:00 EST\"", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidFormat() throws JsonGenerationException {
 DateFormat invalidFormat = new SimpleDateFormat("DD-MM-yyyy HH:mm:ss z");
 DateTimeSerializerBase<Date> invalidSerializer = new DateTimeSerializerBase<>(Date.class, false, invalidFormat);
 Date date = new Date();
 JsonGenerator gen = new JsonFactory().createGenerator();
 try {
 invalidSerializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testSerializationAsTimestamp() throws JsonGenerationException {
 DateTimeSerializerBase<Long> timestampSerializer = new DateTimeSerializerBase<>(Long.class, true, null);
 Long timestamp = 1647461200000L; // Thu Dec 31 00:00:00 UTC 2022
 JsonGenerator gen = new JsonFactory().createGenerator();
 timestampSerializer.serialize(timestamp, gen, new ObjectMapper().getSerializerProvider());
 String result = gen.getOutputBuffer().toString();
 assertEquals("1647461200000", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationAsTimestampWithInvalidUseTimestamp() throws JsonGenerationException {
 DateTimeSerializerBase<Long> invalidTimestampSerializer = new DateTimeSerializerBase<>(Long.class, false, null);
 Long timestamp = 1647461200000L; // Thu Dec 31 00:00:00 UTC 2022
 try {
 invalidTimestampSerializer.serialize(timestamp, new JsonFactory().createGenerator(), new ObjectMapper().getSerializerProvider());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}