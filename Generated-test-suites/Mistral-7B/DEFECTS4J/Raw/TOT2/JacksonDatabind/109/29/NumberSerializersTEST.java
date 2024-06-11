import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 // Test case for addAll method
 @Test
 public void testAddAll() throws JsonProcessingException {
 Map<String, JsonSerializer<?>> allDeserializers = new LinkedHashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(6, allDeserializers.size());
 }

 // Test case for IntegerSerializer
 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(new AnnotationIntrospector(), null, Integer.class);
 ObjectReader objectReader = new ObjectMapper().readerForValueType(Integer.class);
 Integer number = 123;
 String json = "123";

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());
 serializer.serialize(number, gen, new SerializerProvider());
 gen.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(gen.getOutputStream().toByteArray());
 Object readValue = objectReader.readValue(inputStream);
 assertEquals(number, readValue);

 // Test deserialization
 JsonParser parser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 Object readValue2 = objectReader.readValue(parser, Integer.class);
 assertEquals(number, readValue2);
 }

 // Test case for IntegerSerializer with negative number
 @Test
 public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(new AnnotationIntrospector(), null, Integer.class);
 ObjectReader objectReader = new ObjectMapper().readerForValueType(Integer.class);
 Integer number = -123;
 String json = "-123";

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());
 serializer.serialize(number, gen, new SerializerProvider());
 gen.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(gen.getOutputStream().toByteArray());
 Object readValue = objectReader.readValue(inputStream);
 assertEquals(number, readValue);

 // Test deserialization
 JsonParser parser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 Object readValue2 = objectReader.readValue(parser, Integer.class);
 assertEquals(number, readValue2);
 }

 // Test case for IntLikeSerializer
 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(new AnnotationIntrospector(), null, Number.class);
 ObjectReader objectReader = new ObjectMapper().readerForValueType(Number.class);
 Number number = 123;
 String json = "123";

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());
 serializer.serialize(number, gen, new SerializerProvider());
 gen.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(gen.getOutputStream().toByteArray());
 Object readValue = objectReader.readValue(inputStream);
 assertEquals(number, readValue);

 // Test deserialization
 JsonParser parser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 Object readValue2 = objectReader.readValue(parser, Number.class);
 assertEquals(number, readValue2);
 }

 // Test case for LongSerializer
 @Test
 public void testLongSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(new AnnotationIntrospector(), null, Long.class);
 ObjectReader objectReader = new ObjectMapper().readerForValueType(Long.class);
 Long number = 123L;
 String json = "123";

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());
 serializer.serialize(number, gen, new SerializerProvider());
 gen.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(gen.getOutputStream().toByteArray());
 Object readValue = objectReader.readValue(inputStream);
 assertEquals(number, readValue);

 // Test deserialization
 JsonParser parser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 Object readValue2 = objectReader.readValue(parser, Long.class);
 assertEquals(number, readValue2);
 }

 // Test case for FloatSerializer
 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(new AnnotationIntrospector(), null, Float.class);
 ObjectReader objectReader = new ObjectMapper().readerForValueType(Float.class);
 Float number = 123.45f;
 String json = "123.45";

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());
 serializer.serialize(number, gen, new SerializerProvider());
 gen.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(gen.getOutputStream().toByteArray());
 Object readValue = objectReader.readValue(inputStream);
 assertEquals(number, readValue, 0.001f);

 // Test deserialization
 JsonParser parser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 Object readValue2 = objectReader.readValue(parser, Float.class);
 assertEquals(number, readValue2, 0.001f);
 }

 // Test case for DoubleSerializer
 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(new AnnotationIntrospector(), null, Double.class);
 ObjectReader objectReader = new ObjectMapper().readerForValueType(Double.class);
 Double number = 123.45;
 String json = "123.45";

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());
 serializer.serialize(number, gen, new SerializerProvider());
 gen.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(gen.getOutputStream().toByteArray());
 Object readValue = objectReader.readValue(inputStream);
 assertEquals(number, readValue, 0.001);

 // Test deserialization
 JsonParser parser = new ObjectMapper().getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
 Object readValue2 = objectReader.readValue(parser, Double.class);
 assertEquals(number, readValue2, 0.001);
 }
}