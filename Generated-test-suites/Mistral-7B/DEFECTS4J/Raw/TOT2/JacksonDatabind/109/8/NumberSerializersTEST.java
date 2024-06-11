import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 // Test for addAll method
 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertTrue(allDeserializers.containsKey(Integer.class.getName()));
 assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
 // ... add assertions for other classes as well
 }

 // Test for IntegerSerializer
 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Integer.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree(123);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Integer.class);
 Integer deserialized = objectReader.readValue(node);
 assertEquals(123, deserialized.intValue());

 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 JsonSerializerProvider serializers = new JsonSerializerProvider();
 serializers.setSerializerInstance(Integer.class, serializer);
 serializer.serialize(123, gen, serializers);
 JsonNode actualNode = MapperFactory.getMapper().readTree(gen.getOutputStream());
 JsonNode expectedNode = MapperFactory.getMapper().valueToTree(123);
 assertEquals(expectedNode, actualNode);
 }

 // Test for IntegerSerializer with null value
 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerNull() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Integer.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree(null);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Integer.class);
 objectReader.readValue(node);
 }

 // Test for IntLikeSerializer
 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Number.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree(123);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Number.class);
 Number deserialized = objectReader.readValue(node);
 assertEquals(123, deserialized.intValue());

 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 JsonSerializerProvider serializers = new JsonSerializerProvider();
 serializers.setSerializerInstance(Number.class, serializer);
 serializer.serialize(123, gen, serializers);
 JsonNode actualNode = MapperFactory.getMapper().readTree(gen.getOutputStream());
 JsonNode expectedNode = MapperFactory.getMapper().valueToTree(123);
 assertEquals(expectedNode, actualNode);
 }

 // Test for IntLikeSerializer with null value
 @Test(expected = JsonMappingException.class)
 public void testIntLikeSerializerNull() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Number.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree(null);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Number.class);
 objectReader.readValue(node);
 }

 // Test for LongSerializer
 @Test
 public void testLongSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Long.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree(123L);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Long.class);
 Long deserialized = objectReader.readValue(node);
 assertEquals(123, deserialized.longValue());

 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 JsonSerializerProvider serializers = new JsonSerializerProvider();
 serializers.setSerializerInstance(Long.class, serializer);
 serializer.serialize(123L, gen, serializers);
 JsonNode actualNode = MapperFactory.getMapper().readTree(gen.getOutputStream());
 JsonNode expectedNode = MapperFactory.getMapper().valueToTree(123L);
 assertEquals(expectedNode, actualNode);
 }

 // Test for LongSerializer with null value
 @Test(expected = JsonMappingException.class)
 public void testLongSerializerNull() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Long.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree(null);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Long.class);
 objectReader.readValue(node);
 }

 // Test for FloatSerializer
 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Float.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree((float) 123.5);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Float.class);
 Float deserialized = objectReader.readValue(node);
 assertEquals((float) 123.5, deserialized, 0.001f);

 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 JsonSerializerProvider serializers = new JsonSerializerProvider();
 serializers.setSerializerInstance(Float.class, serializer);
 serializer.serialize((float) 123.5, gen, serializers);
 JsonNode actualNode = MapperFactory.getMapper().readTree(gen.getOutputStream());
 JsonNode expectedNode = MapperFactory.getMapper().valueToTree((float) 123.5);
 assertEquals(expectedNode, actualNode);
 }

 // Test for FloatSerializer with null value
 @Test(expected = JsonMappingException.class)
 public void testFloatSerializerNull() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Float.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree(null);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Float.class);
 objectReader.readValue(node);
 }

 // Test for DoubleSerializer
 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Double.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree(123.5);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Double.class);
 Double deserialized = objectReader.readValue(node);
 assertEquals(123.5, deserialized, 0.001);

 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 JsonSerializerProvider serializers = new JsonSerializerProvider();
 serializers.setSerializerInstance(Double.class, serializer);
 serializer.serialize(123.5, gen, serializers);
 JsonNode actualNode = MapperFactory.getMapper().readTree(gen.getOutputStream());
 JsonNode expectedNode = MapperFactory.getMapper().valueToTree(123.5);
 assertEquals(expectedNode, actualNode);
 }

 // Test for DoubleSerializer with null value
 @Test(expected = JsonMappingException.class)
 public void testDoubleSerializerNull() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(
 Double.class, new AnnotationIntrospector());
 JsonNode node = MapperFactory.getMapper().valueToTree(null);
 ObjectReader objectReader = MapperFactory.getMapper().readerForValueType(Double.class);
 objectReader.readValue(node);
 }
}