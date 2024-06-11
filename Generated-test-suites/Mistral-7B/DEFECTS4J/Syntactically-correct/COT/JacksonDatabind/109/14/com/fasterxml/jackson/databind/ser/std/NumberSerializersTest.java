import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(7, allDeserializers.size());
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 IntegerSerializer integerSerializer = NumberSerializers.findValueSerializer(mapper, Integer.class);
 ObjectNode node = mapper.createObjectNode();
 node.putPOJO("integer", new Integer(123));
 JsonSerializer<ObjectNode> serializer = integerSerializer.withContextualSerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(node, generator, new SerializerProvider());
 generator.flush();
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 IntLikeSerializer intLikeSerializer = NumberSerializers.findValueSerializer(mapper, Short.class);
 ObjectNode node = mapper.createObjectNode();
 node.putPOJO("short", new Short((short) 123));
 JsonSerializer<ObjectNode> serializer = intLikeSerializer.withContextualSerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(node, generator, new SerializerProvider());
 generator.flush();
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 LongSerializer longSerializer = NumberSerializers.findValueSerializer(mapper, Long.class);
 ObjectNode node = mapper.createObjectNode();
 node.putPOJO("long", new Long(1234567890L));
 JsonSerializer<ObjectNode> serializer = longSerializer.withContextualSerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(node, generator, new SerializerProvider());
 generator.flush();
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 FloatSerializer floatSerializer = NumberSerializers.findValueSerializer(mapper, Float.class);
 ObjectNode node = mapper.createObjectNode();
 node.putPOJO("float", new Float(123.45f));
 JsonSerializer<ObjectNode> serializer = floatSerializer.withContextualSerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(node, generator, new SerializerProvider());
 generator.flush();
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 DoubleSerializer doubleSerializer = NumberSerializers.findValueSerializer(mapper, Double.class);
 ObjectNode node = mapper.createObjectNode();
 node.putPOJO("double", new Double(123.456));
 JsonSerializer<ObjectNode> serializer = doubleSerializer.withContextualSerializer();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(node, generator, new SerializerProvider());
 generator.flush();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllWithNullMap() {
 NumberSerializers.addAll(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllWithDuplicateKey() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 allDeserializers.put(Integer.class.getName(), new IntegerSerializer(Integer.class));
 // Duplicate key
 NumberSerializers.addAll(allDeserializers);
 }

 @Test(expected = NullPointerException.class)
 public void testFindValueSerializerNullMapper() {
 NumberSerializers.findValueSerializer(null, Integer.class);
 }
}