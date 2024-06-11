package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

 private AsWrapperTypeDeserializer deserializer;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 deserializer = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class, String.class, null, As.WRAPPER_OBJECT);
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#AsWrapperTypeDeserializer(JavaType, TypeIdResolver, String, boolean, Class)}
 */
 @Test
 public void testAsWrapperTypeDeserializerConstructor() {
 // TODO: Implement this test method
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#AsWrapperTypeDeserializer(AsWrapperTypeDeserializer, BeanProperty)}
 */
 @Test
 public void testAsWrapperTypeDeserializerCopyConstructor() {
 // TODO: Implement this test method
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#getTypeInclusion()}
 */
 @Test
 public void testgetTypeInclusion() {
 assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromObject(JsonParser, DeserializationContext)}
 */
 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 // Test normal case
 JsonParser parser = createParser("{\"type\":\"String\",\"value\":\"Hello World\"}");
 Object value = deserializer.deserializeTypedFromObject(parser, context);
 assertEquals("Hello World", value);

 // Test null typeId
 parser = createParser("{\"value\":\"Hello World\"}");
 try {
 deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }

 // Test invalid typeId
 parser = createParser("{\"type\":\"Integer\",\"value\":\"Hello World\"}");
 try {
 deserializer.deserializeTypedFromObject(parser, context);
 fail("Expected IOException for invalid typeId");
 } catch (IOException e) {
 // Expected
 }
 }

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromArray(JsonParser, DeserializationContext)}
 */
 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 // Test normal case
 JsonParser parser = createParser("[{\"type\":\"String\",\"value\":\"Hello World\"}]");
 Object[] values = (Object[]) deserializer.deserializeTypedFromArray(parser, context);
 assertEquals(1, values.length);
 assertEquals("Hello World", values[0]);

 // Test empty array
 parser = createParser("[]");
 Object[] valuesEmpty = (Object[]) deserializer.deserializeTypedFromArray(parser, context);
 assertEquals(0, valuesEmpty.length);

 // Test null values
 parser = createParser("[null]");
 Object[] valuesNull = (Object[]) deserializer.deserializeTypedFromArray(parser, context);
 assertEquals(1, valuesNull.length);
 assertEquals(null, valuesNull[0]);
}

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromScalar(JsonParser, DeserializationContext)}
 */
 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 // Test normal case
 JsonParser parser = createParser("\"Hello World\"");
 Object value = deserializer.deserializeTypedFromScalar(parser, context);
 assertEquals("Hello World", value);

 // Test missing typeId
 parser = createParser("\"\"");
 try {
 deserializer.deserializeTypedFromScalar(parser, context);
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }
}

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeTypedFromAny(JsonParser, DeserializationContext)}
 */
 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 // Test normal case
 JsonParser parser = createParser("{\"type\":\"String\",\"value\":\"Hello World\"}");
 Object value = deserializer.deserializeTypedFromAny(parser, context);
 assertEquals("Hello World", value);

 // Test null typeId
 parser = createParser("{\"value\":\"Hello World\"}");
 try {
 deserializer.deserializeTypedFromAny(parser, context);
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }

 // Test invalid typeId
 parser = createParser("{\"type\":\"InvalidType\",\"value\":\"Hello World\"}");
 try {
 deserializer.deserializeTypedFromAny(parser, context);
 fail("Expected IOException for invalid typeId");
 } catch (IOException e) {
 // Expected
 }
}

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserializeWithNativeTypeId(JsonParser, DeserializationContext, Object)}
 */
 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 // Test normal case
 JsonParser parser = createParser("{\"@type\":\"String\",\"value\":\"Hello World\"}");
 Object value = deserializer.deserializeWithNativeTypeId(parser, context, "String");
 assertEquals("Hello World", value);

 // Test null typeId
 parser = createParser("{\"value\":\"Hello World\"}");
 try {
 deserializer.deserializeWithNativeTypeId(parser, context, null);
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }

 // Test invalid typeId
 parser = createParser("{\"@type\":\"InvalidType\",\"value\":\"Hello World\"}");
 try {
 deserializer.deserializeWithNativeTypeId(parser, context, "InvalidType");
 fail("Expected IOException for invalid typeId");
 } catch (IOException e) {
 // Expected
 }
}

 /**
 * Test method for {@link AsWrapperTypeDeserializer#findDeserializer(DeserializationContext, String)}
 */
 @Test
 public void testFindDeserializer() throws IOException {
 // Test normal case
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 Object value = deserializer.findDeserializer(context, "String").deserialize(createParser("\"Hello World\""), context);
 assertEquals("Hello World", value);

 // Test null typeId
 try {
 deserializer.findDeserializer(context, null);
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }

 // Test invalid typeId
 try {
 deserializer.findDeserializer(context, "InvalidType").deserialize(createParser("\"Hello World\""), context);
 fail("Expected IOException for invalid typeId");
 } catch (IOException e) {
 // Expected
 }
}

 /**
 * Test method for {@link AsWrapperTypeDeserializer#findDeserializer(DeserializationContext, String)} with custom deserializer
 */
 @Test
 public void testFindDeserializerCustom() throws IOException {
 // Test normal case
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 Object value = deserializer.findDeserializer(context, "StringCustom").deserialize(createParser("{\"type\":\"StringCustom\",\"value\":\"Hello World\"}"), context);
 assertEquals("Hello World", value);

 // Test null typeId
 try {
 deserializer.findDeserializer(context, null).deserialize(createParser("{\"value\":\"Hello World\"}"), context);
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }

 // Test invalid typeId
 try {
 deserializer.findDeserializer(context, "InvalidType").deserialize(createParser("{\"type\":\"InvalidType\",\"value\":\"Hello World\"}"), context);
 fail("Expected IOException for invalid typeId");
 } catch (IOException e) {
 // Expected
 }
}

 /**
 * Test method for {@link AsWrapperTypeDeserializer#mergeTypeId(JsonParser, String)}
 */
 @Test
 public void testMergeTypeId() throws IOException {
 JsonParser parser = createParser("{\"type\":\"String\",\"value\":\"Hello World\"}");
 deserializer.mergeTypeId(parser, "_typeIdKey");
 assertEquals("String", parser.getText());

 parser = createParser("{\"type\":\"String\",\"value\":\"Hello World\"}");
 deserializer.mergeTypeId(parser, "_typeIdKey2");
 assertEquals("String", parser.getText());

 parser = createParser("{\"type\":\"String\",\"value\":\"Hello World\"}");
 deserializer.mergeTypeId(parser, "_typeIdKey");
 assertEquals("String", parser.getText());

 parser = createParser("{\"value\":\"Hello World\"}");
 try {
 deserializer.mergeTypeId(parser, "_typeIdKey");
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }
}

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserialize(JsonParser, DeserializationContext)}
 */
 @Test
 public void testDeserialize() throws IOException {
 // Test normal case
 JsonParser parser = createParser("{\"type\":\"String\",\"value\":\"Hello World\"}");
 Object value = deserializer.deserialize(parser, context);
 assertEquals("Hello World", value);

 // Test null typeId
 parser = createParser("{\"value\":\"Hello World\"}");
 try {
 deserializer.deserialize(parser, context);
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }

 // Test invalid typeId
 parser = createParser("{\"type\":\"InvalidType\",\"value\":\"Hello World\"}");
 try {
 deserializer.deserialize(parser, context);
 fail("Expected IOException for invalid typeId");
 } catch (IOException e) {
 // Expected
 }
}

 /**
 * Test method for {@link AsWrapperTypeDeserializer#deserialize(JsonParser, DeserializationContext)} with custom deserializer
 */
 @Test
 public void testDeserializeCustom() throws IOException {
 // Test normal case
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser parser = createParser("{\"type\":\"StringCustom\",\"value\":\"Hello World\"}");
 Object value = deserializer.deserialize(parser, context);
 assertEquals("Hello World", value);

 // Test null typeId
 parser = createParser("{\"value\":\"Hello World\"}");
 try {
 deserializer.deserialize(parser, context);
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }

 // Test invalid typeId
 parser = createParser("{\"type\":\"InvalidType\",\"value\":\"Hello World\"}");
 try {
 deserializer.deserialize(parser, context);
 fail("Expected IOException for invalid typeId");
 } catch (IOException e) {
 // Expected
 }
}

 /**
 * Helper method to create JsonParser
 */
 private JsonParser createParser(String jsonString) throws IOException {
 return mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes()));
 }
}