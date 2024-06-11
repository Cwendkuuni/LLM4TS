package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

 private ObjectMapper objectMapper;
 private StdValueInstantiator instantiator;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 instantiator = new StdValueInstantiator(objectMapper.getConfig(), JavaType.valueOf(String.class));
 }

 @Test
 public void testCreateUsingDefault() throws Exception {
 // Test creating an instance using the default constructor
 Object instance = instantiator.createUsingDefault(objectMapper.getDeserializationContext());
 assertEquals("Instance created using default constructor", instance.getClass().getName(), "java.lang.String");
 }

 @Test
 public void testCreateFromString() throws Exception {
 // Test creating an instance from a string using the String creator
 instantiator.configureFromStringCreator(objectMapper.findCreatorAnnotatedWith(String.class, AnnotatedMethod.class, "valueOf"));
 String value = "Hello World";
 Object instance = instantiator.createFromString(objectMapper.getDeserializationContext(), value);
 assertEquals("Instance created from string", instance, value);
 }

 @Test
 public void testCreateFromLong() throws Exception {
 // Test creating an instance from a long using the Long creator
 instantiator.configureFromLongCreator(objectMapper.findCreatorAnnotatedWith(Long.class, AnnotatedMethod.class, "valueOf"));
 Long value = 42L;
 Object instance = instantiator.createFromLong(objectMapper.getDeserializationContext(), value);
 assertEquals("Instance created from long", instance, value);
 }

 @Test
 public void testCreateFromObjectWithArguments() throws Exception {
 // Test creating an instance with arguments using the constructor with arguments
 AnnotatedConstructor constructor = AnnotationIntrospector.findConstructor(String.class, String.class, "String", String.class);
 instantiator.configureFromObjectSettings(objectMapper.findValueDeserializer(constructor), null, null, null, null, new Object[] { "Test" });
 Object instance = instantiator.createFromObjectWith(objectMapper.getDeserializationContext(), new Object[] { "Test" });
 assertEquals("Instance created with arguments", instance, "Test");
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromInvalidString() throws Exception {
 // Test creating an instance from an invalid string
 instantiator.configureFromStringCreator(objectMapper.findCreatorAnnotatedWith(String.class, AnnotatedMethod.class, "valueOf"));
 String invalidValue = "";
 Object instance = instantiator.createFromString(objectMapper.getDeserializationContext(), invalidValue);
 fail("Expected a JsonMappingException, but got: " + instance);
 }

 @Test
 public void testCreateUsingDelegate() throws Exception {
 // Test creating an instance using a delegate
 JavaType delegateType = JavaType.construct(String[].class, String.class);
 instantiator.configureFromArraySettings(objectMapper.findValueDeserializer(delegateType), delegateType, new Object[] { null });
 Object[] arguments = new Object[] { null };
 Object instance = instantiator.createUsingDelegate(objectMapper.getDeserializationContext(), arguments);
 assertEquals("Instance created using delegate", instance.getClass().getName(), "java.lang.String[]");
 }
}