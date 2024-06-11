package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

public class AsWrapperTypeDeserializerTest extends BaseMapTest {

 private static final java.lang.Class<?> baseType = AsWrapperTypeDeserializer.class;

 @SuppressWarnings("unchecked")
 private static <T> AsWrapperTypeDeserializer<T> createAsWrapperTypeDeserializer(JavaType javaType, TypeIdResolver typeIdResolver) {
 return new AsWrapperTypeDeserializer<T>(javaType, typeIdResolver, "_typePropertyName", false, null);
 }

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 _mapper = new ObjectMapper();
 }

 /**
 * Test for constructor
 */
 @Test
 public void testAsWrapperTypeDeserializerConstructor() {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 assertNotNull(deserializer);
 }

 /**
 * Test for getTypeInclusion method
 */
 @Test
 public void testGetTypeInclusion() {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
 }

 /**
 * Test for deserializeTypedFromObject method with valid input
 */
 @Test
 public void testDeserializeTypedFromObjectWithValidInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
 Object result = deserializer.deserializeTypedFromObject(parser, _mapper.getDeserializationContext());
 assertEquals("test", result);
 }

 /**
 * Test for deserializeTypedFromObject method with null input
 */
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWithNullInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 deserializer.deserializeTypedFromObject(null, _mapper.getDeserializationContext());
 }

 /**
 * Test for deserializeTypedFromObject method with invalid input
 */
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWithInvalidInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 JsonParser parser = _mapper.getFactory().createParser("{\"invalid\":\"json\"}");
 deserializer.deserializeTypedFromObject(parser, _mapper.getDeserializationContext());
 }

 /**
 * Test for deserializeTypedFromArray method with valid input
 */
 @Test
 public void testDeserializeTypedFromArrayWithValidInput() throws IOException {
 AsWrapperTypeDeserializer<String[]> deserializer = createAsWrapperTypeDeserializer(constructArrayType(String[].class), _mapper.getTypeIdResolver());
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("[[\"type\":\"java.lang.String\",\"value\":\"test1\"],\"type\":\"java.lang.String\",\"value\":\"test2\"]]");
 Object result = deserializer.deserializeTypedFromArray(parser, _mapper.getDeserializationContext());
 assertEquals(new String[]{"test1", "test2"}, result);
 }

 /**
 * Test for deserializeTypedFromArray method with null input
 */
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromArrayWithNullInput() throws IOException {
 AsWrapperTypeDeserializer<String[]> deserializer = createAsWrapperTypeDeserializer(constructArrayType(String[].class), _mapper.getTypeIdResolver());
 deserializer.deserializeTypedFromArray(null, _mapper.getDeserializationContext());
 }

 /**
 * Test for deserializeTypedFromArray method with invalid input
 */
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromArrayWithInvalidInput() throws IOException {
 AsWrapperTypeDeserializer<String[]> deserializer = createAsWrapperTypeDeserializer(constructArrayType(String[].class), _mapper.getTypeIdResolver());
 JsonParser parser = _mapper.getFactory().createParser("{\"invalid\":\"json\"}");
 deserializer.deserializeTypedFromArray(parser, _mapper.getDeserializationContext());
 }

 /**
 * Test for deserializeTypedFromScalar method with valid input
 */
 @Test
 public void testDeserializeTypedFromScalarWithValidInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
 Object result = deserializer.deserializeTypedFromScalar(parser, _mapper.getDeserializationContext());
 assertEquals("test", result);
 }

 /**
 * Test for deserializeTypedFromScalar method with null input
 */
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromScalarWithNullInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 deserializer.deserializeTypedFromScalar(null, _mapper.getDeserializationContext());
 }

 /**
 * Test for deserializeTypedFromScalar method with invalid input
 */
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromScalarWithInvalidInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 JsonParser parser = _mapper.getFactory().createParser("{\"invalid\":\"json\"}");
 deserializer.deserializeTypedFromScalar(parser, _mapper.getDeserializationContext());
 }

 /**
 * Test for deserializeTypedFromAny method with valid input
 */
 @Test
 public void testDeserializeTypedFromAnyWithValidInput() throws IOException {
 AsWrapperTypeDeserializer<Object> deserializer = createAsWrapperTypeDeserializer(constructType(Object.class), _mapper.getTypeIdResolver());
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
 Object result = deserializer.deserializeTypedFromAny(parser, _mapper.getDeserializationContext());
 assertEquals("test", result);
 }

 /**
 * Test for deserializeTypedFromAny method with null input
 */
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromAnyWithNullInput() throws IOException {
 AsWrapperTypeDeserializer<Object> deserializer = createAsWrapperTypeDeserializer(constructType(Object.class), _mapper.getTypeIdResolver());
 deserializer.deserializeTypedFromAny(null, _mapper.getDeserializationContext());
 }

 /**
 * Test for deserializeTypedFromAny method with invalid input
 */
 @Test(expected = IOException.class)
 public void testDeserializeTypedFromAnyWithInvalidInput() throws IOException {
 AsWrapperTypeDeserializer<Object> deserializer = createAsWrapperTypeDeserializer(constructType(Object.class), _mapper.getTypeIdResolver());
 JsonParser parser = _mapper.getFactory().createParser("{\"invalid\":\"json\"}");
 deserializer.deserializeTypedFromAny(parser, _mapper.getDeserializationContext());
 }

 /**
 * Test for _deserialize method with valid input and visible type id
 */
 @Test
 public void test_deserializeWithVisibleTypeIdAndValidInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"_typePropertyName\":\"java.lang.String\",\"value\":\"test\"}");
 Object result = deserializer._deserialize(parser, _mapper.getDeserializationContext());
 assertEquals("test", result);
 }

 /**
 * Test for _deserialize method with valid input and non-visible type id
 */
 @Test
 public void test_deserializeWithNonVisibleTypeIdAndValidInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = mapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
 Object result = deserializer._deserialize(parser, _mapper.getDeserializationContext());
 assertEquals("test", result);
 }

 /**
 * Test for _deserialize method with null input
 */
 @Test(expected = IOException.class)
 public void test_deserializeWithNullInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 Object result = deserializer._deserialize(null, _mapper.getDeserializationContext());
 fail("Method should have thrown IOException");
 }

 /**
 * Test for _deserialize method with invalid input
 */
 @Test(expected = IOException.class)
 public void test_deserializeWithInvalidInput() throws IOException {
 AsWrapperTypeDeserializer<String> deserializer = createAsWrapperTypeDeserializer(constructType(String.class), _mapper.getTypeIdResolver());
 JsonParser parser = _mapper.getFactory().createParser("{\"invalid\":\"json\"}");
 Object result = deserializer._deserialize(parser, _mapper.getDeserializationContext());
 fail("Method should have thrown IOException");
 }

}