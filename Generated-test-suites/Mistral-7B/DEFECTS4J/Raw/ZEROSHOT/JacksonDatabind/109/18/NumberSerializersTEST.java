import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.SerializerProvider.Feature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Before
 public void setUp() {
 ObjectMapper mapper = new ObjectMapper();
 _mapper = mapper;
 }

 private ObjectMapper _mapper;

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer input = 123;
 JsonNode expectedNode = JsonFactory.instance.createObjectNode();
 expectedNode.put("c", expectedNode.numberValue(123));

 JsonNode resultNode = _mapper.writeValueAsTree(input);
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testIntegerDeserializer() throws JsonProcessingException {
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123);
 Integer result = _mapper.readValue(inputNode.traverse(), Integer.class);
 assertEquals(123, result.intValue());
 }

 @Test
 public void testIntegerSerializerWithType() throws JsonProcessingException {
 Integer input = 123;
 JsonNode expectedNode = _mapper.writerWithType(TypeFactory.defaultInstance().constructType(Integer.class))
 .writeValueAsTree(input);
 JsonNode resultNode = _mapper.readTree(expectedNode.traversed().getRawValue());
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testIntegerDeserializerWithType() throws JsonProcessingException {
 Class<Integer> intType = Integer.class;
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123);
 TypeDeserializer typeDeserializer = _mapper.getTypeFactory().constructDeserializer(intType);
 JsonNode resultNode = _mapper.readTree(inputNode.traverse());
 JsonNode deserializedNode = typeDeserializer.deserialize(resultNode, intType);
 Integer result = (Integer) typeDeserializer.getDeserializer().deserialize(deserializedNode, intType);
 assertEquals(123, result.intValue());
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 Number input = 123;
 JsonNode expectedNode = JsonFactory.instance.createObjectNode();
 expectedNode.put("c", expectedNode.numberValue(123));

 JsonNode resultNode = _mapper.writeValueAsTree(input);
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testIntLikeDeserializer() throws JsonProcessingException {
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123);
 Number result = _mapper.readValue(inputNode.traverse(), Number.class);
 assertEquals(123, result.intValue());
 }

 @Test
 public void testIntLikeSerializerWithType() throws JsonProcessingException {
 Number input = 123;
 JsonNode expectedNode = _mapper.writerWithType(TypeFactory.defaultInstance().constructType(Number.class))
 .writeValueAsTree(input);
 JsonNode resultNode = _mapper.readTree(expectedNode.traversed().getRawValue());
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testIntLikeDeserializerWithType() throws JsonProcessingException {
 Class<Number> numberType = Number.class;
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123);
 TypeDeserializer typeDeserializer = _mapper.getTypeFactory().constructDeserializer(numberType);
 JsonNode resultNode = _mapper.readTree(inputNode.traverse());
 JsonNode deserializedNode = typeDeserializer.deserialize(resultNode, numberType);
 Number result = (Number) typeDeserializer.getDeserializer().deserialize(deserializedNode, numberType);
 assertEquals(123, result.intValue());
 }

 @Test
 public void testShortSerializer() throws JsonProcessingException {
 Short input = 123;
 JsonNode expectedNode = JsonFactory.instance.createObjectNode();
 expectedNode.put("c", expectedNode.numberValue(123));

 JsonNode resultNode = _mapper.writeValueAsTree(input);
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testShortDeserializer() throws JsonProcessingException {
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123);
 Short result = _mapper.readValue(inputNode.traverse(), Short.class);
 assertEquals((short) 123, result);
 }

 @Test
 public void testShortSerializerWithType() throws JsonProcessingException {
 Short input = 123;
 JsonNode expectedNode = _mapper.writerWithType(TypeFactory.defaultInstance().constructType(Short.class))
 .writeValueAsTree(input);
 JsonNode resultNode = _mapper.readTree(expectedNode.traversed().getRawValue());
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testShortDeserializerWithType() throws JsonProcessingException {
 Class<Short> shortType = Short.class;
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123);
 TypeDeserializer typeDeserializer = _mapper.getTypeFactory().constructDeserializer(shortType);
 JsonNode resultNode = _mapper.readTree(inputNode.traverse());
 JsonNode deserializedNode = typeDeserializer.deserialize(resultNode, shortType);
 Short result = (Short) typeDeserializer.getDeserializer().deserialize(deserializedNode, shortType);
 assertEquals((short) 123, result);
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long input = 123L;
 JsonNode expectedNode = JsonFactory.instance.createObjectNode();
 expectedNode.put("c", expectedNode.numberValue(123));

 JsonNode resultNode = _mapper.writeValueAsTree(input);
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testLongDeserializer() throws JsonProcessingException {
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123L);
 Long result = _mapper.readValue(inputNode.traverse(), Long.class);
 assertEquals(123L, result);
 }

 @Test
 public void testLongSerializerWithType() throws JsonProcessingException {
 Long input = 123L;
 JsonNode expectedNode = _mapper.writerWithType(TypeFactory.defaultInstance().constructType(Long.class))
 .writeValueAsTree(input);
 JsonNode resultNode = _mapper.readTree(expectedNode.traversed().getRawValue());
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testLongDeserializerWithType() throws JsonProcessingException {
 Class<Long> longType = Long.class;
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123L);
 TypeDeserializer typeDeserializer = _mapper.getTypeFactory().constructDeserializer(longType);
 JsonNode resultNode = _mapper.readTree(inputNode.traverse());
 JsonNode deserializedNode = typeDeserializer.deserialize(resultNode, longType);
 Long result = (Long) typeDeserializer.getDeserializer().deserialize(deserializedNode, longType);
 assertEquals(123L, result);
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float input = 123.45f;
 JsonNode expectedNode = JsonFactory.instance.createObjectNode();
 expectedNode.put("c", expectedNode.numberValue(123.45f));

 JsonNode resultNode = _mapper.writeValueAsTree(input);
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testFloatDeserializer() throws JsonProcessingException {
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123.45f);
 Float result = _mapper.readValue(inputNode.traverse(), Float.class);
 assertEquals(123.45f, result, 0.001f);
 }

 @Test
 public void testFloatSerializerWithType() throws JsonProcessingException {
 Float input = 123.45f;
 JsonNode expectedNode = _mapper.writerWithType(TypeFactory.defaultInstance().constructType(Float.class))
 .writeValueAsTree(input);
 JsonNode resultNode = _mapper.readTree(expectedNode.traversed().getRawValue());
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testFloatDeserializerWithType() throws JsonProcessingException {
 Class<Float> floatType = Float.class;
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123.45f);
 TypeDeserializer typeDeserializer = _mapper.getTypeFactory().constructDeserializer(floatType);
 JsonNode resultNode = _mapper.readTree(inputNode.traverse());
 JsonNode deserializedNode = typeDeserializer.deserialize(resultNode, floatType);
 Float result = (Float) typeDeserializer.getDeserializer().deserialize(deserializedNode, floatType);
 assertEquals(123.45f, result, 0.001f);
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double input = 123.45;
 JsonNode expectedNode = JsonFactory.instance.createObjectNode();
 expectedNode.put("c", expectedNode.numberValue(123.45));

 JsonNode resultNode = _mapper.writeValueAsTree(input);
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testDoubleDeserializer() throws JsonProcessingException {
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123.45);
 Double result = _mapper.readValue(inputNode.traverse(), Double.class);
 assertEquals(123.45, result, 0.001);
 }

 @Test
 public void testDoubleSerializerWithType() throws JsonProcessingException {
 Double input = 123.45;
 JsonNode expectedNode = _mapper.writerWithType(TypeFactory.defaultInstance().constructType(Double.class))
 .writeValueAsTree(input);
 JsonNode resultNode = _mapper.readTree(expectedNode.traversed().getRawValue());
 assertEquals(expectedNode, resultNode);
 }

 @Test
 public void testDoubleDeserializerWithType() throws JsonProcessingException {
 Class<Double> doubleType = Double.class;
 JsonNode inputNode = JsonFactory.instance.createObjectNode();
 inputNode.put("c", 123.45);
 TypeDeserializer typeDeserializer = _mapper.getTypeFactory().constructDeserializer(doubleType);
 JsonNode resultNode = _mapper.readTree(inputNode.traverse());
 JsonNode deserializedNode = typeDeserializer.deserialize(resultNode, doubleType);
 Double result = (Double) typeDeserializer.getDeserializer().deserialize(deserializedNode, doubleType);
 assertEquals(123.45, result, 0.001);
 }

 @Test
 public void testCustomJsonFormat() throws JsonProcessingException {
 @JsonFormat(shape = JsonFormat.Shape.STRING, serializableAs = Number.class)
 static class CustomNumber {
 Integer value;
 }

 CustomNumber input = new CustomNumber();
 input.value = 123;
 JsonNode expectedNode = JsonFactory.instance.createObjectNode();
 expectedNode.put("c", "123");

 JsonNode resultNode = _mapper.writeValueAsTree(input);
 assertEquals(expectedNode, resultNode);
 }
}