import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson. core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test addAll method
 */
 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(6, allDeserializers.size());
 }

 /**
 * Test IntegerSerializer serialization
 */
 @Test
 public void testIntegerSerializerSerialization() throws JsonProcessingException {
 Integer integer = 123;
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 IntegerSerializer serializer = new IntegerSerializer(Integer.class);
 serializer.serialize(integer, gen, new SerializerProvider());
 gen.flush();
 }

 /**
 * Test IntegerSerializer deserialization
 */
 @Test
 public void testIntegerSerializerDeserialization() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "123";
 Integer deserializedInteger = objectMapper.readValue(jsonString, Integer.class);
 assertEquals(123, deserializedInteger.intValue());
 }

 /**
 * Test IntegerSerializer with big integer value
 */
 @Test
 public void testIntegerSerializerWithBigIntegerValue() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "12345678901234567890";
 Long deserializedLong = objectMapper.readValue(jsonString, Long.class);
 assertEquals(12345678901234567890L, deserializedLong.longValue());
 }

 /**
 * Test IntLikeSerializer serialization
 */
 @Test
 public void testIntLikeSerializerSerialization() throws JsonProcessingException {
 Integer integer = 123;
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 IntLikeSerializer serializer = IntLikeSerializer.instance;
 serializer.serialize(integer, gen, new SerializerProvider());
 gen.flush();
 }

 /**
 * Test IntLikeSerializer deserialization
 */
 @Test
 public void testIntLikeSerializerDeserialization() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "123";
 Number deserializedNumber = objectMapper.readValue(jsonString, Number.class);
 assertEquals(123, deserializedNumber.intValue());
 }

 /**
 * Test LongSerializer serialization
 */
 @Test
 public void testLongSerializerSerialization() throws JsonProcessingException {
 Long longNumber = 12345678901234567890L;
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 LongSerializer serializer = new LongSerializer(Long.class);
 serializer.serialize(longNumber, gen, new SerializerProvider());
 gen.flush();
 }

 /**
 * Test LongSerializer deserialization
 */
 @Test
 public void testLongSerializerDeserialization() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "12345678901234567890";
 Long deserializedLong = objectMapper.readValue(jsonString, Long.class);
 assertEquals(12345678901234567890L, deserializedLong.longValue());
 }

 /**
 * Test FloatSerializer serialization
 */
 @Test
 public void testFloatSerializerSerialization() throws JsonProcessingException {
 Float floatNumber = 123.45f;
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 FloatSerializer serializer = FloatSerializer.instance;
 serializer.serialize(floatNumber, gen, new SerializerProvider());
 gen.flush();
 }

 /**
 * Test FloatSerializer deserialization
 */
 @Test
 public void testFloatSerializerDeserialization() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "123.45";
 Float deserializedFloat = objectMapper.readValue(jsonString, Float.class);
 assertEquals(123.45f, deserializedFloat, 0.01f);
 }

 /**
 * Test DoubleSerializer serialization
 */
 @Test
 public void testDoubleSerializerSerialization() throws JsonProcessingException {
 Double doubleNumber = 123.45;
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 DoubleSerializer serializer = new DoubleSerializer(Double.class);
 serializer.serialize(doubleNumber, gen, new SerializerProvider());
 gen.flush();
 }

 /**
 * Test DoubleSerializer deserialization
 */
 @Test
 public void testDoubleSerializerDeserialization() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "123.45";
 Double deserializedDouble = objectMapper.readValue(jsonString, Double.class);
 assertEquals(123.45, deserializedDouble, 0.01);
 }

 /**
 * Test serialization with JsonFormat annotation
 */
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 public static class TestClass {
 private Long number;

 public Long getNumber() {
 return number;
 }

 public void setNumber(Long number) {
 this.number = number;
 }
 }

 @Test
 public void testSerializationWithJsonFormatAnnotation() throws JsonProcessingException {
 TestClass testClass = new TestClass();
 testClass.setNumber(12345678901234567890L);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 ObjectWriter writer = objectMapper.writer();
 writer.writeValue(gen, testClass);
 gen.flush();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithJsonFormatAnnotationException() throws JsonProcessingException {
 TestClass testClass = new TestClass();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 ObjectWriter writer = objectMapper.writer();
 writer.writeValue(gen, testClass);
 gen.flush();
 }
}