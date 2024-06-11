import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializerWithTimestampFlag() throws JsonGenerationException {
 DateTimeSerializerBase<Long> timestampSerializer = mapper.findValueSerializer(Long.class);
 Long timestamp = 1633046236000L;
 String expected = "1633046236000";

 assertNull(timestampSerializer._customFormat);
 assertTrue(timestampSerializer._useTimestamp);

 String actual = timestampSerializer.serialize(timestamp, mapper.getFactory().createGenerator(null), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializerWithCustomFormat() throws JsonGenerationException {
 DateTimeSerializerBase<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
 java.util.Date date = new java.util.Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
 DateTimeSerializerBase<java.util.Date> customSerializer = dateSerializer.withFormat(Boolean.FALSE, customFormat);
 String expected = "22-11-2022";

 assertNotNull(customSerializer._customFormat);
 assertFalse(customSerializer._useTimestamp);

 String actual = customSerializer.serialize(date, mapper.getFactory().createGenerator(null), null);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSerializerWithInvalidFormat() throws JsonGenerationException {
 DateTimeSerializerBase<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
 DateTimeSerializerBase<java.util.Date> invalidSerializer = dateSerializer.withFormat(Boolean.FALSE, null);
 }

 @Test
 public void testDeserializerWithDefaultFormat() throws IOException {
 DateTimeSerializerBase<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
 String json = "1633046236000";
 Long timestamp = 1633046236000L;
 java.util.Date expected = new java.util.Date(timestamp);
 java.util.Date actual = mapper.readValue(json, java.util.Date.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializerWithCustomFormat() throws IOException {
 DateTimeSerializerBase<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
 DateTimeSerializerBase<java.util.Date> customSerializer = dateSerializer.withFormat(Boolean.FALSE, customFormat);
 String json = "22-11-2022";
 java.util.Date expected = new java.util.Date();
 expected.setTime(customFormat.parse("22-11-2022").getTime());
 java.util.Date actual = mapper.readValue(json, java.util.Date.class);
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerWithInvalidFormat() throws IOException {
 DateTimeSerializerBase<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
 String json = "invalid";
 mapper.readValue(json, java.util.Date.class);
 }

}