import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(11, allDeserializers.size());
 }

 // Test for Base class methods
 @Test
 public void testBaseGetSchema() throws JsonProcessingException {
 JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
 assertEquals("number", shortSerializer.getSchema(mapper.getSerializerProvider(), Short.class));
 }

 @Test
 public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 shortSerializer.acceptJsonFormatVisitor(visitor, Short.class.getType());
 verify(visitor).expectNumberFormat(Short.class.getType());
 }

 @Test
 public void testBaseCreateContextual() {
 JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
 assertSame(shortSerializer, shortSerializer.createContextual(null, null));
 }

 // Test for IntegerSerializer class
 @Test
 public void testIntegerSerializerSerialize() throws JsonProcessingException {
 IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 Object value = Integer.valueOf(123);
 integerSerializer.serialize(value, gen, mapper.getSerializerProvider());
 verify(gen).writeNumber(123);
 }

 @Test
 public void testIntegerSerializerSerializeWithType() throws JsonProcessingException {
 IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 Object value = Integer.valueOf(123);
 integerSerializer.serializeWithType(value, gen, mapper.getSerializerProvider(), mapper.getTypeSerializer());
 verify(gen).writeNumber(123);
 }

 // Test for IntLikeSerializer class
 @Test
 public void testIntLikeSerializerSerialize() throws JsonProcessingException {
 IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 Object value = Byte.valueOf((byte) 123);
 intLikeSerializer.serialize(value, gen, mapper.getSerializerProvider());
 verify(gen).writeNumber(123);
 }

 // Test for LongSerializer class
 @Test
 public void testLongSerializerSerialize() throws JsonProcessingException {
 LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 Object value = Long.valueOf(456);
 longSerializer.serialize(value, gen, mapper.getSerializerProvider());
 verify(gen).writeNumber(456);
 }

 // Test for FloatSerializer class
 @Test
 public void testFloatSerializerSerialize() throws JsonProcessingException {
 FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 Object value = Float.valueOf(3.14f);
 floatSerializer.serialize(value, gen, mapper.getSerializerProvider());
 verify(gen).writeNumber(3.14f);
 }

 // Test for DoubleSerializer class
 @Test
 public void testDoubleSerializerSerialize() throws JsonProcessingException {
 DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 Object value = Double.valueOf(5.67);
 doubleSerializer.serialize(value, gen, mapper.getSerializerProvider());
 verify(gen).writeNumber(5.67);
 }

 // Edge cases and exception handling tests
 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNullMap() {
 Map<String, JsonSerializer<?>> allDeserializers = null;
 NumberSerializers.addAll(allDeserializers);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegerSerializerNegativeValue() throws JsonProcessingException {
 IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 Object value = Integer.valueOf(-123);
 integerSerializer.serialize(value, gen, mapper.getSerializerProvider());
 verifyNoMoreInteractions(gen);
 throw new AssertionError("Expected an IOException or JsonProcessingException");
 }

 @Test(expected = IOException.class)
 public void testIntegerSerializerWriteNumberThrowsIOException() throws JsonProcessingException {
 IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 doThrow(IOException.class).when(gen).writeNumber(123);
 Object value = Integer.valueOf(123);
 integerSerializer.serialize(value, gen, mapper.getSerializerProvider());
 }

 @Test
 public void testLongSerializerLargeValue() throws JsonProcessingException {
 LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 Object value = Long.valueOf(Long.MAX_VALUE);
 longSerializer.serialize(value, gen, mapper.getSerializerProvider());
 // Verify that writeNumber method is called with the correct argument
 long maxValue = Long.MAX_VALUE;
 verify(gen).writeNumber(maxValue);
 }

 @Test(expected = IOException.class)
 public void testLongSerializerWriteNumberThrowsIOException() throws JsonProcessingException {
 LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
 JsonGenerator gen = mock(JsonGenerator.class);
 doThrow(IOException.class).when(gen).writeNumber(Long.MAX_VALUE);
 Object value = Long.valueOf(Long.MAX_VALUE);
 longSerializer.serialize(value, gen, mapper.getSerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testBaseAcceptJsonFormatVisitorThrowsJsonMappingException() throws JsonMappingException {
 JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 doThrow(JsonMappingException.class).when(visitor).expectNumberFormat(Short.class.getType());
 shortSerializer.acceptJsonFormatVisitor(visitor, Short.class.getType());
 }
}