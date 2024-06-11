package com.example.junit.aswrappertypedeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#AsWrapperTypeDeserializer(JavaType, TypeIdResolver, String, boolean, Class)}
 */
 @Test
 public void testConstructor() {
 // Add constructor test cases here
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#getTypeInclusion()}
 */
 @Test
 public void testGetTypeInclusion() {
 AsWrapperTypeDeserializer actual = new AsWrapperTypeDeserializer(null, null, null, false, null);
 assertEquals(As.WRAPPER_OBJECT, actual.getTypeInclusion());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#forProperty(BeanProperty)}
 */
 @Test
 public void testForProperty() {
 // Add forProperty test cases here
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#_deserialize(JsonParser, DeserializationContext)}
 * @throws IOException
 */
 @Test
 public void testDeserialize() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForType(deserializer.getClass());
 String json = "{\"typeId\":\"com.example.MyClass\",\"value\":{\"name\":\"John\",\"age\":30}}";
 ObjectNode node = new ObjectNode(JsonFactory.instance.createParser(json).readValueAsTree());
 JsonParser jp = node.asParser();
 Object result = reader.readValue(jp);
 assertEquals(MyClass.class, result.getClass());
 assertEquals("John", ((MyClass) result).getName());
 assertEquals(30, ((MyClass) result).getAge());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#_deserialize(JsonParser, DeserializationContext)} with null typeId
 * @throws IOException
 */
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithNullTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForType(deserializer.getClass());
 String json = "{\"value\":{\"name\":\"John\",\"age\":30}}";
 ObjectNode node = new ObjectNode(JsonFactory.instance.createParser(json).readValueAsTree());
 JsonParser jp = node.asParser();
 reader.readValue(jp);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#_deserialize(JsonParser, DeserializationContext)} with unknown typeId
 * @throws IOException
 */
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithUnknownTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForType(deserializer.getClass());
 String json = "{\"typeId\":\"com.example.UnknownClass\",\"value\":{\"name\":\"John\",\"age\":30}}";
 ObjectNode node = new ObjectNode(JsonFactory.instance.createParser(json).readValueAsTree());
 JsonParser jp = node.asParser();
 reader.readValue(jp);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#_deserialize(JsonParser, DeserializationContext)} with missing typeId
 * @throws IOException
 */
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithMissingTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForType(deserializer.getClass());
 String json = "{\"value\":{\"name\":\"John\",\"age\":30}}";
 ObjectNode node = new ObjectNode(JsonFactory.instance.createParser(json).readValueAsTree());
 JsonParser jp = node.asParser();
 reader.readValue(jp);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#_deserializeWithNativeTypeId(JsonParser, DeserializationContext, Object)}
 * @throws IOException
 */
 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForType(deserializer.getClass());
 String json = "{\"typeId\":\"com.example.MyClass\",\"value\":{\"name\":\"John\",\"age\":30}}";
 ObjectNode node = new ObjectNode(JsonFactory.instance.createParser(json).readValueAsTree());
 JsonParser jp = node.asParser();
 Object result = deserializer._deserializeWithNativeTypeId(jp, null, "com.example.MyClass");
 assertEquals(MyClass.class, result.getClass());
 assertEquals("John", ((MyClass) result).getName());
 assertEquals(30, ((MyClass) result).getAge());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#_findDeserializer(DeserializationContext, Object)}
 * @throws IOException
 * @throws JsonMappingException
 */
 @Test
 public void testFindDeserializer() throws IOException, JsonMappingException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader rootReader = objectMapper.readerForType(Object.class);
 SimpleModule module = new SimpleModule();
 module.addDeserializer(MyClass.class, new StubMyClassDeserializer());
 objectMapper.registerModule(module);
 ObjectReader myClassReader = objectMapper.readerForType(MyClass.class);
 ObjectNode node = JsonFactory.instance.createObjectNode();
 node.put("typeId", "com.example.MyClass");
 JsonParser jp = node.asParser();
 JsonDeserializer<Object> result = deserializer._findDeserializer(deserializer.getDeserializationContext(), "com.example.MyClass");
 assertEquals(myClassReader, result);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromObject(JsonParser, DeserializationContext)}
 * @throws IOException
 */
 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForType(deserializer.getClass());
 String json = "{\"typeId\":\"com.example.MyClass\",\"value\":{\"name\":\"John\",\"age\":30}}";
 ObjectNode node = new ObjectNode(JsonFactory.instance.createParser(json).readValueAsTree());
 JsonParser jp = node.asParser();
 Object result = deserializer.deserializeTypedFromObject(jp, null);
 assertEquals(MyClass.class, result.getClass());
 assertEquals("John", ((MyClass) result).getName());
 assertEquals(30, ((MyClass) result).getAge());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromArray(JsonParser, DeserializationContext)}
 * @throws IOException
 */
 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForType(deserializer.getClass());
 String json = "[{\"typeId\":\"com.example.MyClass\",\"value\":{\"name\":\"John\",\"age\":30}}]";
 ObjectNode node = new ObjectNode(JsonFactory.instance.createParser(json).readValueAsTree());
 JsonParser jp = node.asParser();
 Object[] results = deserializer.deserializeTypedFromArray(jp, null);
 assertEquals(1, results.length);
 assertEquals(MyClass.class, results[0].getClass());
 assertEquals("John", ((MyClass) results[0]).getName());
 assertEquals(30, ((MyClass) results[0]).getAge());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromScalar(JsonParser, DeserializationContext)}
 * @throws IOException
 */
 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForType(deserializer.getClass());
 String json = "{\"value\":30}";
 ObjectNode node = new ObjectNode(JsonFactory.instance.createParser(json).readValueAsTree());
 JsonParser jp = node.asParser();
 Object result = deserializer.deserializeTypedFromScalar(jp, null);
 assertNull(result);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromAny(JsonParser, DeserializationContext)}
 * @throws IOException
 */
 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, NullType.INSTANCE);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader reader = objectMapper.readerForType(deserializer.getClass());
 String json = "{\"value\":30}";
 ObjectNode node = new ObjectNode(JsonFactory.instance.createParser(json).readValueAsTree());
 JsonParser jp = node.asParser();
 Object result = deserializer.deserializeTypedFromAny(jp, null);
 assertNull(result);
 }
}