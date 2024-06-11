package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtil;
import com.fasterxml.jackson.test.BaseTest;

@RunWith(BaseTest.class)
public class FieldPropertyTest {

 private static final String TEST_CLASS = "com.test.TestClass";
 private static final String TEST_FIELD = "testField";
 private static final String TEST_NULLABLE_FIELD = "testNullField";
 private static final String TEST_FIELD_ANNOTATION = "testAnnotation";
 private static final String TEST_TYPE = "java.lang.String";
 private static final String TEST_TYPE_WITH_ANNOTATION = "com.test.TestType";
 private static final String TEST_ANNOTATION_VALUE = "testValue";

 private Object testInstance;
 private FieldProperty fieldProperty;
 private Field testField;
 private Field testNullField;
 private AnnotatedField testAnnotatedField;
 private BeanPropertyDefinition testPropertyDefinition;
 private TypeDeserializer testTypeDeserializer;
 private JsonParser testParser;
 private JsonParser testParserWithToken;
 private ObjectMapper testObjectMapper;
 private ObjectMapper testObjectMapperWithType;
 private JsonDeserializer<Object> testValueDeserializer;
 private NullValueProvider testNullValueProvider;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
 testInstance = Mockito.spy(Class.forName(TEST_CLASS).newInstance());
 testField = testInstance.getClass().getDeclaredField(TEST_FIELD);
 testField.setAccessible(true);
 testNullField = testInstance.getClass().getDeclaredField(TEST_NULLABLE_FIELD);
 testNullField.setAccessible(true);
 testAnnotatedField = Mockito.spy(new AnnotatedField(testField, null, null, null, null));
 testPropertyDefinition = Mockito.spy(new BeanPropertyDefinition(testAnnotatedField, null, null, null, null, null, null, null, null, null, null));
 testTypeDeserializer = Mockito.spy(Mockito.mock(TypeDeserializer.class));
 testParser = Mockito.spy(Mockito.mock(JsonParser.class));
 testParserWithToken = Mockito.spy(Mockito.mock(JsonParser.class));
 testObjectMapper = Mockito.spy(ObjectMapperFactory.getObjectMapper());
 testObjectMapperWithType = Mockito.spy(ObjectMapperFactory.getObjectMapper(true));
 testValueDeserializer = Mockito.spy(Mockito.mock(JsonDeserializer.class));
 testNullValueProvider = Mockito.spy(Mockito.mock(NullValueProvider.class));

 fieldProperty = new FieldProperty(testPropertyDefinition, ObjectType.valueOf(TEST_TYPE), testTypeDeserializer, Annotations.empty(), testAnnotatedField);
 }

 @Test
 public void testConstructor() {
 assertNotNull(fieldProperty);
 assertEquals(testPropertyDefinition, fieldProperty.getPropertyDefinition());
 assertEquals(ObjectType.valueOf(TEST_TYPE), fieldProperty.getType());
 assertNull(fieldProperty.getValueDeserializer());
 assertNull(fieldProperty.getNullProvider());
 assertEquals(testAnnotatedField, fieldProperty.getAnnotated());
 assertNotNull(fieldProperty.getField());
 assertFalse(fieldProperty.isSkipNulls());
 }

 @Test
 public void testConstructorWithValueDeserializer() {
 testValueDeserializer = Mockito.spy(new StubJsonDeserializer());
 fieldProperty = new FieldProperty(fieldProperty, testValueDeserializer, testNullValueProvider);
 assertEquals(testValueDeserializer, fieldProperty.getValueDeserializer());
 assertSame(testNullValueProvider, fieldProperty.getNullProvider());
 }

 @Test
 public void testConstructorWithName() {
 PropertyName newName = Mockito.mock(PropertyName.class);
 fieldProperty = new FieldProperty(fieldProperty, newName);
 assertEquals(newName, fieldProperty.getName());
 }

 @Test
 public void testGetAnnotation() {
 JsonProperty jsonPropertyAnnotation = new JsonProperty();
 jsonPropertyAnnotation.value(TEST_FIELD_ANNOTATION);
 testAnnotatedField.setValueAnnotation(jsonPropertyAnnotation);
 JsonPropertyAnnotation testJsonPropertyAnnotation = (JsonPropertyAnnotation) fieldProperty.getAnnotation(JsonProperty.class);
 assertEquals(TEST_FIELD_ANNOTATION, testJsonPropertyAnnotation.value());
 }

 @Test
 public void testGetMember() {
 assertEquals(testAnnotatedField, fieldProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String testValue = "testValue";
 when(testParser.getValueAsString()).thenReturn(testValue);
 when(testParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

 fieldProperty.deserializeAndSet(testParser, testObjectMapper, testInstance);
 assertEquals(testValue, testField.get(testInstance));
 }

 @Test
 public void testDeserializeAndSetNull() throws IOException {
 when(testParser.getValueAsString()).thenReturn(null);
 when(testParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

 fieldProperty.deserializeAndSet(testParser, testObjectMapper, testInstance);
 assertNull(testField.get(testInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String testValue = "testValue";
 when(testParser.getValueAsString()).thenReturn(testValue);
 when(testParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Object testReturn = fieldProperty.deserializeSetAndReturn(testParser, testObjectMapper, testInstance);
 assertEquals(testInstance, testReturn);
 assertEquals(testValue, testField.get(testInstance));
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws IOException {
 when(testParser.getValueAsString()).thenReturn(null);
 when(testParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 Object testReturn = fieldProperty.deserializeSetAndReturn(testParser, testObjectMapper, testInstance);
 assertNull(testReturn);
 assertNull(testField.get(testInstance));
 }

 @Test
 public void testSet() throws IOException {
 String testValue = "testValue";
 fieldProperty.set(testInstance, testValue);
 assertEquals(testValue, testField.get(testInstance));
 }

 @Test
 public void testSetNull() throws IOException {
 fieldProperty.set(testInstance, null);
 assertNull(testField.get(testInstance));
 }

 @Test
 public void testSetWithTypeDeserializer() throws IOException {
 Converter<String, TestType> testConverter = ConverterUtil.valueConverter(String.class, TestType.class);
 testTypeDeserializer = Mockito.spy(new StubTypeDeserializer(testConverter));
 String testValue = "testValue";
 when(testParser.getValueAsString()).thenReturn(testValue);
 when(testParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(testParserWithToken.getValueAsString()).thenReturn(TEST_ANNOTATION_VALUE);
 when(testParserWithToken.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(testParserWithToken.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(testParserWithToken.getCurrentName()).thenReturn(TEST_FIELD_ANNOTATION);
 when(testParserWithToken.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(testParserWithToken.getValueAsString()).thenReturn(TEST_TYPE_WITH_ANNOTATION);
 when(testParserWithToken.nextToken()).thenReturn(JsonToken.END_OBJECT);

 fieldProperty.deserializeAndSet(testParserWithToken, testObjectMapperWithType, testInstance);
 TestType testType = (TestType) testField.get(testInstance);
 assertEquals(TEST_ANNOTATION_VALUE, testType.getAnnotation(JsonProperty.class).value());
 assertEquals(TEST_TYPE_WITH_ANNOTATION, testType.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSet_IOException() throws IOException {
 when(testParser.getValueAsString()).thenThrow(new IOException());
 fieldProperty.deserializeAndSet(testParser, testObjectMapper, testInstance);
 fail("IOException should be thrown");
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturn_IOException() throws IOException {
 when(testParser.getValueAsString()).thenThrow(new IOException());
 fieldProperty.deserializeSetAndReturn(testParser, testObjectMapper, testInstance);
 fail("IOException should be thrown");
 }

 @Test(expected = IOException.class)
 public void testSet_IOException() throws IOException {
 when(testParser.nextToken()).thenThrow(new IOException());
 fieldProperty.set(testInstance, null);
 fail("IOException should be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidField() throws InstantiationException, IllegalAccessException {
 Field invalidField = testInstance.getClass().getDeclaredField("invalidField");
 invalidField.setAccessible(true);
 try {
 new FieldProperty(testPropertyDefinition, ObjectType.valueOf(TEST_TYPE), testTypeDeserializer, Annotations.empty(), invalidField);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 }
 }

 @Test
 public void testFixAccess() throws IOException {
 try {
 fieldProperty.fixAccess(testObjectMapper);
 } catch (IllegalAccessException e) {
 fail("IllegalAccessException should not be thrown");
 }
 }

}

 class StubJsonDeserializer implements JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "testValue";
 }
 }

 class StubTypeDeserializer implements TypeDeserializer {
 Converter<String, TestType> testConverter;

 public StubTypeDeserializer(Converter<String, TestType> converter) {
 this.testConverter = converter;
 }

 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
 TestType testType = testConverter.convert(p.getValueAsString());
 return testType;
 }
 }