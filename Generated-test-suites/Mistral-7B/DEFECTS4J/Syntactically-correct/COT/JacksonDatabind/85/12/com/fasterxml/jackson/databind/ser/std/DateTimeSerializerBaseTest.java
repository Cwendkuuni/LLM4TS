import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longSerializer;
 private DateTimeSerializerBase<String> stringSerializer;

 @Before
 public void setup() {
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
 longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null) {
 @Override
 protected long _timestamp(Long value) {
 return value.longValue();
 }
 };
 stringSerializer = new DateTimeSerializerBase<String>(String.class, false, dateFormat) {
 @Override
 protected long _timestamp(String value) {
 try {
 return dateFormat.parse(value).getTime();
 } catch (ParseException e) {
 throw new RuntimeException("Failed to parse date string: " + value, e);
 }
 }
 };
 }

 @Test
 public void testIsEmpty() {
 assertFalse(longSerializer.isEmpty(null));
 assertFalse(longSerializer.isEmpty(new Long(0L)));
 assertFalse(longSerializer.isEmpty(new Long(1L)));
 assertFalse(stringSerializer.isEmpty(null));
 assertFalse(stringSerializer.isEmpty(""));
 assertFalse(stringSerializer.isEmpty("01-Jan-2023 12:00:00 EST"));
 }

 @Test
 public void testGetSchema() {
 ObjectCodec codec = new ObjectMapper().findValueSerializer(Long.TYPE);
 JsonNode schema = longSerializer.getSchema(codec.getSerializerProvider(), Long.TYPE);
 assertEquals("number", schema.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 AnnotatedClass ac = new AnnotatedClass(longSerializer.getClass());
 AnnotatedMember am = ac.findMember("acceptJsonFormatVisitor");
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getDefaultSerializerProvider().getRootValueSerializerProvider().findValueSerializer(Long.TYPE);
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.NUMBER);
 longSerializer.acceptJsonFormatVisitor(visitor, JsonType.construct(Long.TYPE), format);
 assertTrue(visitor.hasSerializableValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAcceptJsonFormatVisitor_NullProvider() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getRootValueSerializerProvider().findValueSerializer(Long.TYPE);
 longSerializer.acceptJsonFormatVisitor(visitor, JsonType.construct(Long.TYPE), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerialize_NullValue() throws IOException {
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 longSerializer.serialize(null, gen, null);
 String result = gen.getOutputFactory().getBuffer().toString();
 assertNull(result);
 }

 @Test
 public void testSerialize_LongValue() throws IOException {
 Long value = 1234567890L;
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 longSerializer.serialize(value, gen, null);
 String result = gen.getOutputFactory().getBuffer().toString();
 assertEquals("1234567890", result);
 }

 @Test
 public void testSerialize_WithCustomFormat() throws IOException {
 Long value = 1234567890123L;
 SimpleDateFormat customDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 DateTimeSerializerBase<Long> customSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, customDateFormat) {
 @Override
 protected long _timestamp(Long value) {
 return value.longValue();
 }
 };
 customSerializer.serialize(value, gen, null);
 String result = gen.getOutputFactory().getBuffer().toString();
 assertEquals("27-Jan-2024 11:52:12 PST", result);
 }

 @Test(expected = IOException.class)
 public void testSerialize_Exception() throws IOException {
 Long value = 1234567890L;
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 when(gen.getOutputFactory().getBuffer()).thenReturn(new StringWriter() {
 @Override
 public String toString() {
 throw new IOException("Writer buffer exhausted");
 }
 });
 longSerializer.serialize(value, gen, null);
 }
}