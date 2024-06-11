import org.junit.Before;
import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longSerializer;
 private DateTimeSerializerBase<String> stringSerializer;

 @Before
 public void setup() {
 longSerializer = new DateTimeSerializerBase<Long>() {
 @Override
 protected long _timestamp(Long value) {
 return value.longValue();
 }
 }.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
 stringSerializer = new DateTimeSerializerBase<String>() {
 @Override
 protected long _timestamp(String value) {
 try {
 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value).getTime();
 } catch (ParseException e) {
 throw new IllegalArgumentException("Invalid date format: " + value, e);
 }
 }
 }.withFormat(Boolean.TRUE, null);
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 assertNotNull(longSerializer);
 assertNotNull(stringSerializer);
 }

 /**
 * Test isEmpty method
 */
 @Test
 public void testIsEmpty() {
 assertFalse(longSerializer.isEmpty(null));
 assertFalse(longSerializer.isEmpty(new Long(0)));
 assertFalse(longSerializer.isEmpty(new Long(-1L), null));
 assertFalse(longSerializer.isEmpty(new Long(-1L), new JsonGenerator()));
 assertFalse(stringSerializer.isEmpty(null));
 assertFalse(stringSerializer.isEmpty("", null));
 assertFalse(stringSerializer.isEmpty("", new JsonGenerator()));
 assertTrue(stringSerializer.isEmpty("", new JsonGenerator(), new ObjectMapper()));
 }

 /**
 * Test getSchema method
 */
 @Test
 public void testGetSchema() {
 assertEquals("string", longSerializer.getSchema(null, Long.class.getType()).getClassID());
 assertEquals("number", longSerializer.getSchema(null, Long.TYPE).getClassID());
 assertEquals("string", stringSerializer.getSchema(null, String.class.getType()).getClassID());
 assertEquals("string", stringSerializer.getSchema(null, String.TYPE).getClassID());
 }

 /**
 * Test createContextual method - numeric format
 */
 @Test
 public void testCreateContextualNumericFormat() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormat.Value format = new JsonFormat.Value()
 .withShape(JsonFormat.Shape.NUMBER)
 .withNumberType(JsonParser.NumberType.LONG);
 JsonSerializer<String> serializer = longSerializer.createContextual(mapper.getSerializerProvider(), null);
 assertEquals(longSerializer.class, serializer.getClass());
 }

 /**
 * Test createContextual method - string format
 */
 @Test
 public void testCreateContextualStringFormat() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormat.Value format = new JsonFormat.Value()
 .withShape(JsonFormat.Shape.STRING)
 .withPattern("yyyy-MM-dd HH:mm:ss")
 .withLocale(Locale.US)
 .withTimeZone(TimeZone.getTimeZone("UTC"));
 JsonSerializer<String> serializer = stringSerializer.createContextual(mapper.getSerializerProvider(), null);
 assertEquals(Serializers.StringSerializer.class, serializer.getClass());
 }

 /**
 * Test createContextual method - invalid format
 */
 @Test(expected = JsonMappingException.class)
 public void testCreateContextualInvalidFormat() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormat.Value format = new JsonFormat.Value()
 .withShape(JsonFormat.Shape.STRING)
 .withPattern("yyyy-MM-dd HH:mm:ss")
 .withLocale(Locale.US)
 .withTimeZone(null); // missing TimeZone
 JsonSerializer<String> serializer = stringSerializer.createContextual(mapper.getSerializerProvider(), null);
 }

 /**
 * Test serialize method - numeric format
 */
 @Test
 public void testSerializeNumericFormat() throws IOException {
 Long value = 1234567890123L;
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 longSerializer.serialize(value, gen, null);
 JsonParser parser = new ObjectMapper().parse("1234567890123000");
 assertEquals(value, parser.readValueAs(Long.class));
 }

 /**
 * Test serialize method - string format
 */
 @Test
 public void testSerializeStringFormat() throws IOException {
 String value = "2022-06-01 12:34:56";
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 stringSerializer.serialize(value, gen, null);
 JsonParser parser = new ObjectMapper().parse("\"2022-06-01 12:34:56\"");
 assertEquals(value, parser.readValueAs(String.class));
 }

 /**
 * Test serialize method - null value
 */
 @Test
 public void testSerializeNullValue() throws IOException {
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 longSerializer.serialize(null, gen, null);
 JsonParser parser = new ObjectMapper().parse("null");
 assertNull(parser.readValueAs(Long.class));
 }

 /**
 * Test serialize method - invalid value
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSerializeInvalidValue() throws IOException {
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 stringSerializer.serialize("invalid date", gen, null);
 }

 /**
 * Test withFormat method - numeric format
 */
 @Test
 public void testWithFormatNumericFormat() {
 DateTimeSerializerBase<Long> serializer = longSerializer.withFormat(Boolean.TRUE, null);
 assertEquals(longSerializer.class, serializer.getClass());
 assertEquals(Boolean.TRUE, serializer._useTimestamp);
 assertNull(serializer._customFormat);
 }

 /**
 * Test withFormat method - string format
 */
 @Test
 public void testWithFormatStringFormat() {
 DateTimeSerializerBase<String> serializer = stringSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
 assertEquals(stringSerializer.class, serializer.getClass());
 assertEquals(Boolean.FALSE, serializer._useTimestamp);
 assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), serializer._customFormat);
 }
}