import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> dateTimeSerializerBase;
 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 dateTimeSerializerBase = new DateSerializer(Long.class, false, null);
 }

 @Test
 public void testConstructor() {
 assertNotNull(dateTimeSerializerBase);
 assertEquals(Long.class, dateTimeSerializerBase.getType());
 assertEquals(false, dateTimeSerializerBase._useTimestamp);
 assertNull(dateTimeSerializerBase._customFormat);
 }

 @Test
 public void testWithFormat() {
 DateTimeSerializerBase<Long> newSerializer = dateTimeSerializerBase.withFormat(true, null);
 assertNotSame(dateTimeSerializerBase, newSerializer);
 assertEquals(true, newSerializer._useTimestamp);
 assertNull(newSerializer._customFormat);

 DateTimeSerializerBase<Long> newSerializerWithFormat = dateTimeSerializerBase.withFormat(false, new StdDateFormat());
 assertNotSame(dateTimeSerializerBase, newSerializerWithFormat);
 assertEquals(false, newSerializerWithFormat._useTimestamp);
 assertNotNull(newSerializerWithFormat._customFormat);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualNullProperty() throws Exception {
 JsonGenerator generator = mock(JsonGenerator.class);
 JsonParser parser = mock(JsonParser.class);
 ObjectCodec codec = mock(ObjectCodec.class);
 SerializerProvider serializers = new SerializerProvider();
 serializers.setCodec(codec);
 BeanProperty property = null;
 JsonFormat.Value format = null;
 Object value = new Object();
 when(codec.findValueSerializer(Long.class, null, null, null, null)).thenReturn(dateTimeSerializerBase);
 dateTimeSerializerBase.createContextual(serializers, property);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualNullValue() throws Exception {
 JsonGenerator generator = mock(JsonGenerator.class);
 JsonParser parser = mock(JsonParser.class);
 ObjectCodec codec = mock(ObjectCodec.class);
 SerializerProvider serializers = new SerializerProvider();
 serializers.setCodec(codec);
 BeanProperty property = mock(BeanProperty.class);
 Object value = null;
 when(codec.findValueSerializer(Long.class, null, null, null, null)).thenReturn(dateTimeSerializerBase);
 dateTimeSerializerBase.createContextual(serializers, property, value);
 }

 @Test
 public void testSerializeNullValue() throws Exception {
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider serializers = new SerializerProvider();
 Object value = null;
 dateTimeSerializerBase.serialize(value, generator, serializers);
 assertEquals("null", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeTimestampValue() throws Exception {
 Long timestamp = 1234567890123L;
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider serializers = new SerializerProvider();
 Object value = timestamp;
 when(generator.writeNumber(timestamp)).thenReturn(generator);
 dateTimeSerializerBase.serialize(value, generator, serializers);
 assertEquals("1234567890123", generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeStringValue() throws Exception {
 Long timestamp = 1234567890123L;
 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider serializers = new SerializerProvider();
 Object value = "2022-03-25T14:30:15Z";
 when(generator.writeString(value)).thenReturn(generator);
 dateTimeSerializerBase = new DateSerializer(Long.class, false, new StdDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
 dateTimeSerializerBase.serialize(value, generator, serializers);
 assertEquals("\"2022-03-25T14:30:15Z\"", generator.getOutputBuffer().toString());
 }

 @Test
 public void testGetSchema() throws Exception {
 JsonNode schema = dateTimeSerializerBase.getSchema(objectMapper, Long.class);
 assertEquals("number", schema.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws Exception {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = mock(JavaType.class);
 ObjectCodec codec = mock(ObjectCodec.class);
 Object value = new Object();
 SerializerProvider serializers = new SerializerProvider();
 serializers.setCodec(codec);
 when(codec.findValueSerializer(Long.class, null, null, null, null)).thenReturn(dateTimeSerializerBase);
 dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
 }
}