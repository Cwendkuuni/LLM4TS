package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException.ReferenceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

 private StdValueInstantiator valueInstantiator;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 valueInstantiator = mapper.findValueInstantiator(String.class);
 }

 /**
 * Test default constructor when no arguments are provided
 */
 @Test
 public void testDefaultConstructor() throws Exception {
 String value = valueInstantiator.createUsingDefault(mapper.getDeserializationContext());
 assertEquals("", value);
 }

 /**
 * Test with-args constructor when arguments are provided
 */
 @Test
 public void testWithArgsConstructor() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("length", javaType) };
 valueInstantiator.configureFromObjectSettings(null, null, null, null, null, constructorArguments);
 String[] values = new String[] { "test" };
 String[] valueFromObject = valueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), values);
 assertArrayEquals(values, valueFromObject);
 }

 /**
 * Test delegate constructor when a delegate is provided
 */
 @Test
 public void testDelegateConstructor() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 AnnotatedWithParams delegateCreator = mapper.findValueInstantiator(javaType);
 SettableBeanProperty[] delegateArguments = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("value", mapper.getValueFactory().valueElementInstance(new TextNode("test"))) };
 valueInstantiator.configureFromDelegateSettings(delegateCreator, delegateArguments);
 String valueFromDelegate = valueInstantiator.createUsingDelegate(mapper.getDeserializationContext(), mapper.getRootValueInstance());
 assertEquals("test", valueFromDelegate);
 }

 /**
 * Test array delegate constructor when an array delegate is provided
 */
 @Test
 public void testArrayDelegateConstructor() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 AnnotatedWithParams arrayDelegateCreator = mapper.findValueInstantiator(javaType);
 SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("value", mapper.getValueFactory().valueNumberInstance(1)) };
 valueInstantiator.configureFromArraySettings(arrayDelegateCreator, javaType, arrayDelegateArguments);
 int[] valuesFromArrayDelegate = valueInstantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), mapper.getRootValueInstance());
 assertArrayEquals(new int[] { 1 }, valuesFromArrayDelegate);
 }

 /**
 * Test from-string creator when a from-string creator is provided
 */
 @Test
 public void testFromStringCreator() throws Exception {
 AnnotatedWithParams fromStringCreator = mapper.findValueInstantiator(String.class, "test");
 valueInstantiator.configureFromStringCreator(fromStringCreator);
 String valueFromString = valueInstantiator.createFromString(mapper.getDeserializationContext(), "test");
 assertEquals("test", valueFromString);
 }

 /**
 * Test from-int creator when a from-int creator is provided
 */
 @Test
 public void testFromIntCreator() throws Exception {
 AnnotatedWithParams fromIntCreator = mapper.findValueInstantiator(Integer.class, 1);
 valueInstantiator.configureFromIntCreator(fromIntCreator);
 Integer valueFromInt = valueInstantiator.createFromInt(mapper.getDeserializationContext(), 1);
 assertEquals(1, valueFromInt.intValue());
 }

 /**
 * Test from-long creator when a from-long creator is provided
 */
 @Test
 public void testFromLongCreator() throws Exception {
 AnnotatedWithParams fromLongCreator = mapper.findValueInstantiator(Long.class, 1L);
 valueInstantiator.configureFromLongCreator(fromLongCreator);
 Long valueFromLong = valueInstantiator.createFromLong(mapper.getDeserializationContext(), 1L);
 assertEquals(1L, valueFromLong.longValue());
 }

 /**
 * Test from-double creator when a from-double creator is provided
 */
 @Test
 public void testFromDoubleCreator() throws Exception {
 AnnotatedWithParams fromDoubleCreator = mapper.findValueInstantiator(Double.class, 1.0);
 valueInstantiator.configureFromDoubleCreator(fromDoubleCreator);
 Double valueFromDouble = valueInstantiator.createFromDouble(mapper.getDeserializationContext(), 1.0);
 assertEquals(1.0, valueFromDouble.doubleValue(), 0.001);
 }

 /**
 * Test from-boolean creator when a from-boolean creator is provided
 */
 @Test
 public void testFromBooleanCreator() throws Exception {
 AnnotatedWithParams fromBooleanCreator = mapper.findValueInstantiator(Boolean.class, true);
 valueInstantiator.configureFromBooleanCreator(fromBooleanCreator);
 Boolean valueFromBoolean = valueInstantiator.createFromBoolean(mapper.getDeserializationContext(), true);
 assertTrue(valueFromBoolean);
 }

 /**
 * Test error handling when no suitable constructor is found
 */
 @Test(expected = JsonMappingException.class)
 public void testErrorHandling() throws Exception {
 valueInstantiator = mapper.findValueInstantiator(Integer[].class);
 valueInstantiator.createUsingDefault(mapper.getDeserializationContext());
 }

 /**
 * Test error handling when a constructor throws an exception
 */
 @Test(expected = JsonMappingException.class)
 public void testErrorHandlingWithException() throws Exception {
 AnnotatedWithParams creator = mapper.findValueInstantiator(String.class, "test");
 creator.setAnnotationIntrospector(new ThrowingAnnotationIntrospector());
 valueInstantiator.configureFromStringCreator(creator);
 valueInstantiator.createFromString(mapper.getDeserializationContext(), "test");
 }

 /**
 * Test error handling when an injectable value is missing
 */
 @Test(expected = JsonMappingException.class)
 public void testErrorHandlingWithMissingInjectable() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("length", javaType),
 new SettableBeanProperty.Std("value", null) };
 valueInstantiator.configureFromObjectSettings(null, null, null, null, null, constructorArguments);
 String[] values = new String[] { "test" };
 valueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), values);
 }

 /**
 * Test error handling when a delegate is missing
 */
 @Test(expected = JsonMappingException.class)
 public void testErrorHandlingWithMissingDelegate() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
 valueInstantiator.configureFromDelegateSettings(null, null);
 valueInstantiator.createUsingDelegate(mapper.getDeserializationContext(), mapper.getRootValueInstance());
 }

 /**
 * Test error handling when an array delegate is missing
 */
 @Test(expected = JsonMappingException.class)
 public void testErrorHandlingWithMissingArrayDelegate() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 valueInstantiator.configureFromArraySettings(null, javaType, null);
 valueInstantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), mapper.getRootValueInstance());
 }
}