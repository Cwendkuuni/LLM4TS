package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

 private JavaType baseType;
 private TypeIdResolver idResolver;
 private String typePropertyName = "typePropertyName";
 private boolean typeIdVisible = true;
 private Class<?> defaultImpl = Object.class;
 private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

 @Before
 public void setup() throws JsonProcessingException {
 baseType = SimpleBeanType.construct();
 idResolver = new TypeIdResolverBase();
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(createTestParser(), new TestContext());
 assertEquals(baseType.getRawClass(), result.getClass());
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(createTestParser(), new TestContext());
 assertEquals(baseType.getRawClass(), result.getClass());
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(createTestParser(), new TestContext());
 assertEquals(baseType.getRawClass(), result.getClass());
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(createTestParser(), new TestContext());
 assertEquals(baseType.getRawClass(), result.getClass());
 }

 @Test(expected = IOException.class)
 public void testInvalidJsonObject() throws IOException {
 asWrapperTypeDeserializer.deserializeTypedFromObject(createInvalidTestParser(), new TestContext());
 }

 @Test(expected = IOException.class)
 public void testMissingTypeIdKey() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("[{\"data\": \"value\"}]");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(parser, new TestContext());
 fail("Expected IOException, but got deserialized value: " + result);
 }

 @Test(expected = IOException.class)
 public void testMissingValueKey() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"type\": \"InvalidType\"}");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(parser, new TestContext());
 fail("Expected IOException, but got deserialized value: " + result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseType() {
 try {
 new AsWrapperTypeDeserializer(null, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 fail("Expected IllegalArgumentException, but no exception thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullIdResolver() {
 try {
 new AsWrapperTypeDeserializer(baseType, null, typePropertyName, typeIdVisible, defaultImpl);
 fail("Expected NullPointerException, but no exception thrown");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullTypePropertyName() {
 try {
 new AsWrapperTypeDeserializer(baseType, idResolver, null, typeIdVisible, defaultImpl);
 fail("Expected NullPointerException, but no exception thrown");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullDefaultImpl() {
 try {
 new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, null);
 fail("Expected NullPointerException, but no exception thrown");
 } catch (NullPointerException e) {
 // expected
 }
 }

 private JsonParser createTestParser() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 return jsonFactory.createParser("{\"type\": \"TestType\", \"data\": \"testData\"}");
}

 private JsonParser createInvalidTestParser() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 return jsonFactory.createParser("{\"data\": \"testData\"}");
}

 private static class TestContext extends DeserializationContext {

 }

}