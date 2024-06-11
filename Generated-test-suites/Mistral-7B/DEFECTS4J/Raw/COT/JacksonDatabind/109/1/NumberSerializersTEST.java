import com.fasterxml.jackson.annotations.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NumberSerializersTest {

 @Test
 void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 Assertions.assertEquals(7, allDeserializers.size());
 Assertions.assertTrue(allDeserializers.containsKey(Integer.class.getName()));
 Assertions.assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
 Assertions.assertTrue(allDeserializers.containsKey(Long.class.getName()));
 Assertions.assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
 Assertions.assertTrue(allDeserializers.containsKey(Short.class.getName()));
 Assertions.assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
 Assertions.assertTrue(allDeserializers.containsKey(Double.class.getName()));
 Assertions.assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
 Assertions.assertTrue(allDeserializers.containsKey(Float.class.getName()));
 Assertions.assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
 }

 @Test
 void testIntegerSerializerSerialize() {
 IntegerSerializer integerSerializer = NumberSerializers.new IntegerSerializer(Integer.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(new Integer(42));
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("42", json);
 }

 @Test
 void testIntegerSerializerSerializeWithType() {
 IntegerSerializer integerSerializer = NumberSerializers.new IntegerSerializer(Integer.class);
 TypeSerializer typeSerializer = new StandardSerializer<>(Integer.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(new Integer(42), typeSerializer);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("42", json);
 }

 @Test
 void testIntegerSerializerNull() {
 IntegerSerializer integerSerializer = NumberSerializers.new IntegerSerializer(Integer.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(null);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertNull(json);
 }

 @Test
 void testLongSerializerSerialize() {
 LongSerializer longSerializer = NumberSerializers.new LongSerializer(Long.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(new Long(42L));
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("42", json);
 }

 @Test
 void testLongSerializerSerializeWithType() {
 LongSerializer longSerializer = NumberSerializers.new LongSerializer(Long.class);
 TypeSerializer typeSerializer = new StandardSerializer<>(Long.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(new Long(42L), typeSerializer);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("42", json);
 }

 @Test
 void testLongSerializerNull() {
 LongSerializer longSerializer = NumberSerializers.new LongSerializer(Long.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(null);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertNull(json);
 }

 @Test
 void testShortSerializerSerialize() {
 ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString((short) 42);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("42", json);
 }

 @Test
 void testShortSerializerSerializeWithType() {
 ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
 TypeSerializer typeSerializer = new StandardSerializer<>(Short.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString((short) 42, typeSerializer);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("42", json);
 }

 @Test
 void testShortSerializerNull() {
 ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(null);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertNull(json);
 }

 @Test
 void testFloatSerializerSerialize() {
 FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(new Float(3.14f));
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("3.14", json);
 }

 @Test
 void testFloatSerializerSerializeWithType() {
 FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
 TypeSerializer typeSerializer = new StandardSerializer<>(Float.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(new Float(3.14f), typeSerializer);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("3.14", json);
 }

 @Test
 void testFloatSerializerNull() {
 FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(null);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertNull(json);
 }

 @Test
 void testDoubleSerializerSerialize() {
 DoubleSerializer doubleSerializer = NumberSerializers.new DoubleSerializer(Double.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(new Double(3.14));
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("3.14", json);
 }

 @Test
 void testDoubleSerializerSerializeWithType() {
 DoubleSerializer doubleSerializer = NumberSerializers.new DoubleSerializer(Double.class);
 TypeSerializer typeSerializer = new StandardSerializer<>(Double.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(new Double(3.14), typeSerializer);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("3.14", json);
 }

 @Test
 void testDoubleSerializerNull() {
 DoubleSerializer doubleSerializer = NumberSerializers.new DoubleSerializer(Double.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 json = objectWriter.writeValueAsString(null);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertNull(json);
 }

 @Test
 void testJsonFormatAnnotation() {
 JsonFormat annotation = new JsonFormat();
 annotation.setShape(JsonFormat.Shape.STRING);
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

 ObjectWriter objectWriter = new ObjectMapper().writer();
 String json = "";
 try {
 JsonNode node = objectWriter.writeValueAsTree(new Integer(42), new JsonSerializer.InstanceContext());
 json = objectWriter.writeValueAsString(node);
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }

 Assertions.assertEquals("\"42\"", json);
 }
}