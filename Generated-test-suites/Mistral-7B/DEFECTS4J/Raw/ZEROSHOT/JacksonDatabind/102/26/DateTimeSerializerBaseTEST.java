import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateSerializer;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializer;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 dateTimeSerializer = objectMapper.findValueSerializer(Date.class);
 }

 @Test
 public void testDateTimeSerializerBaseConstruction() {
 assertInstanceOf(DateTimeSerializerBase.class, dateTimeSerializer);
 assertNull(dateTimeSerializer._customFormat);
 assertFalse(dateTimeSerializer._useTimestamp);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormatTimestamp() {
 DateTimeSerializerBase<Date> timestampSerializer = dateTimeSerializer.withFormat(true, null);
 assertInstanceOf(StdDateSerializer.class, timestampSerializer);
 assertTrue(timestampSerializer._useTimestamp);
 assertNull(timestampSerializer._customFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormatCustom() {
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);
 assertInstanceOf(DateTimeSerializerBase.class, customSerializer);
 assertFalse(customSerializer._useTimestamp);
 assertEquals(customFormat, customSerializer._customFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormatCustomReused() throws JsonProcessingException {
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);
 assertInstanceOf(DateTimeSerializerBase.class, customSerializer);
 assertFalse(customSerializer._useTimestamp);
 assertEquals(customFormat, customSerializer._customFormat);

 Date date = new Date();
 ObjectNode node = objectMapper.createObjectNode();
 customSerializer.serialize(date, objectMapper.getFactory().createGenerator(node), objectMapper.getSerializerProvider());
 assertEquals("2023-03-24 12:00:00 +0200", node.get("date").asText());

 DateFormat reusedFormat = customSerializer._reusedCustomFormat.getAndSet(null);
 assertNotNull(reusedFormat);
 assertEquals(customFormat, reusedFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormatJson() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
 JsonNode node = objectMapper.readTree("{\"date\":\"2023-03-24T12:00:00.000Z\"}");
 ObjectMapper testObjectMapper = new ObjectMapper();
 ObjectNode rootNode = testObjectMapper.readValueAsTree(node);
 DateTimeSerializerBase<Date> jsonSerializer = dateTimeSerializer.withFormat(false, null);
 Date date = jsonSerializer.deserialize(null, testObjectMapper.getFactory().createParser(node.get("date").asText()), testObjectMapper);
 assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2023-03-24T12:00:00.000Z"), date);
 }
}