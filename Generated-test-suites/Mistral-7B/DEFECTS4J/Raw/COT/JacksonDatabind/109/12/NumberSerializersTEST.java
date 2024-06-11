import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

public class NumberSerializersTest {

 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertNotNull(allDeserializers.get(Integer.class.getName()));
 assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
 assertNotNull(allDeserializers.get(Long.class.getName()));
 assertNotNull(allDeserializers.get(Long.TYPE.getName()));
 assertNotNull(allDeserializers.get(Byte.class.getName()));
 assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
 assertNotNull(allDeserializers.get(Short.class.getName()));
 assertNotNull(allDeserializers.get(Short.TYPE.getName()));
 assertNotNull(allDeserializers.get(Double.class.getName()));
 assertNotNull(allDeserializers.get(Double.TYPE.getName()));
 assertNotNull(allDeserializers.get(Float.class.getName()));
 assertNotNull(allDeserializers.get(Float.TYPE.getName()));
 }

 @Test
 public void testIntegerSerializerSerialize() {
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
 JsonGenerator gen = createJsonGenerator();
 try {
 integerSerializer.serialize(new Integer(1), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 String result = gen.getOutputContent();
 assertEquals("1", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test
 public void testIntegerSerializerSerializeWithType() {
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 JsonNode node = mapper.readTree("1");
 JsonSerializer<?> serializer = mapper.findValueSerializer(node, Integer.class);
 assertEquals(integerSerializer, serializer);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test
 public void testIntLikeSerializerSerialize() {
 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
 JsonGenerator gen = createJsonGenerator();
 try {
 intLikeSerializer.serialize(new Byte((byte)1), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 String result = gen.getOutputContent();
 assertEquals("1", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test
 public void testIntLikeSerializerSerializeWithTypeFailure() {
 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
 JsonGenerator gen = createJsonGenerator();
 try {
 intLikeSerializer.serializeWithType(new Byte((byte)1), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 fail("Expected IOException, but no exception occurred.");
 } catch (IOException e) {
 // Expected exception
 }
 }

 @Test
 public void testLongSerializerSerialize() {
 LongSerializer longSerializer = new LongSerializer(Long.class);
 JsonGenerator gen = createJsonGenerator();
 try {
 longSerializer.serialize(new Long(1), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 String result = gen.getOutputContent();
 assertEquals("1", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test
 public void testLongSerializerSerializeWithType() {
 LongSerializer longSerializer = new LongSerializer(Long.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 JsonNode node = mapper.readTree("1");
 JsonSerializer<?> serializer = mapper.findValueSerializer(node, Long.class);
 assertEquals(longSerializer, serializer);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test
 public void testFloatSerializerSerialize() {
 FloatSerializer floatSerializer = new FloatSerializer();
 JsonGenerator gen = createJsonGenerator();
 try {
 floatSerializer.serialize(new Float(1.0f), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 String result = gen.getOutputContent();
 assertEquals("1.0", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test
 public void testFloatSerializerSerializeWithType() {
 FloatSerializer floatSerializer = new FloatSerializer();
 ObjectMapper mapper = new ObjectMapper();
 try {
 JsonNode node = mapper.readTree("1.0");
 JsonSerializer<?> serializer = mapper.findValueSerializer(node, Float.class);
 assertEquals(floatSerializer, serializer);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test
 public void testDoubleSerializerSerialize() {
 DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
 JsonGenerator gen = createJsonGenerator();
 try {
 doubleSerializer.serialize(new Double(1.0), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 String result = gen.getOutputContent();
 assertEquals("1.0", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test
 public void testDoubleSerializerSerializeWithType() {
 DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
 ObjectMapper mapper = new ObjectMapper();
 try {
 JsonNode node = mapper.readTree("1.0");
 JsonSerializer<?> serializer = mapper.findValueSerializer(node, Double.class);
 assertEquals(doubleSerializer, serializer);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testIntegerSerializerSerializeWithTypeFailure() {
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
 JsonGenerator gen = createJsonGenerator();
 try {
 integerSerializer.serializeWithType(new Integer(1), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 fail("Expected JsonMappingException, but no exception occurred.");
 } catch (IOException e) {
 // Expected exception
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testLongSerializerSerializeWithTypeFailure() {
 LongSerializer longSerializer = new LongSerializer(Long.class);
 JsonGenerator gen = createJsonGenerator();
 try {
 longSerializer.serializeWithType(new Long(1), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 fail("Expected JsonMappingException, but no exception occurred.");
 } catch (IOException e) {
 // Expected exception
 }
 }

 @Test
 public void testShortSerializerSerialize() {
 ShortSerializer shortSerializer = new ShortSerializer();
 JsonGenerator gen = createJsonGenerator();
 try {
 shortSerializer.serialize(new Short((short)1), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 String result = gen.getOutputContent();
 assertEquals("1", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException occurred during test.");
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testShortSerializerSerializeWithTypeFailure() {
 ShortSerializer shortSerializer = new ShortSerializer();
 JsonGenerator gen = createJsonGenerator();
 try {
 shortSerializer.serializeWithType(new Short((short)1), gen, new AnnotationIntrospector(), new ObjectMapper(), new TypeSerializer());
 fail("Expected JsonMappingException, but no exception occurred.");
 } catch (IOException e) {
 // Expected exception
 }
 }

 private JsonGenerator createJsonGenerator() throws IOException {
 StringWriter writer = new StringWriter();
 JsonFactory factory = new JsonFactory();
 return factory.createGenerator(writer);
 }
}