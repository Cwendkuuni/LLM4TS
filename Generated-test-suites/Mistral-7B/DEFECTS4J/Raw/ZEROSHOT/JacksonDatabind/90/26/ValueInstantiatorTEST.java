package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.ClassIdResolver;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ValueInstantiatorTest {

 private static final ClassIdResolver DEFAULT_CLASS_ID_RESOLVER = mock(ClassIdResolver.class);

 private ValueInstantiator testInstance;

 @Before
 public void setUp() throws Exception {
 testInstance = new ValueInstantiator.Base(String.class) {
 @Override
 public boolean canCreateFromString() {
 return true;
 }

 @Override
 public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
 return value;
 }
 };
 }

 @Test
 public void testGetValueTypeDesc() {
 assertEquals("java.lang.String", testInstance.getValueTypeDesc());
 }

 @Test
 public void testGetValueClass() {
 assertEquals(String.class, testInstance.getValueClass());
 }

 @Test
 public void testCanInstantiate() {
 assertTrue(testInstance.canInstantiate());
 }

 @Test
 public void testGetDefaultCreator() {
 assertNull(testInstance.getDefaultCreator());
 }

 @Test
 public void testCreateUsingDefault() throws Exception {
 Object result = testInstance.createUsingDefault(mock(DeserializationContext.class));
 assertNull(result);
 }

 @Test
 public void testCanCreateUsingDelegate() {
 assertFalse(testInstance.canCreateUsingDelegate());
 }

 @Test
 public void testCreateUsingDelegate_whenDelegateCreatorExists_thenCreateInstance() throws Exception {
 AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
 when(annotatedClass.getAnnotatedConstructor(anyInt())).thenReturn(mock(AnnotatedConstructor.class));

 ValueInstantiator delegate = spy(new StdValueInstantiator(String.class) {
 @Override
 public AnnotatedWithParams getDelegateCreator() {
 return new AnnotatedWithParams() {
 @Override
 public AnnotatedClass getAnnotatedClass() {
 return annotatedClass;
 }
 };
 }
 });

 testInstance.setDelegate(delegate);

 Object result = testInstance.createUsingDelegate(mock(DeserializationContext.class), mock(Object.class));
 verify(delegate, times(1)).createUsingDelegate(any(DeserializationContext.class), any(Object.class));
 }

 @Test
 public void testCreateUsingDelegate_whenDelegateCreatorDoesNotExist_thenThrowException() throws Exception {
 testInstance.setDelegate(new StdValueInstantiator(String.class));

 try {
 testInstance.createUsingDelegate(mock(DeserializationContext.class), mock(Object.class));
 fail();
 } catch (IOException e) {
 assertEquals("no delegate creator specified", e.getMessage());
 }
 }

 @Test
 public void testCreateFromObjectWith() throws Exception {
 Object[] args = new Object[]{1L, "test"};

 BeanPropertyDefinition prop1 = mock(BeanPropertyDefinition.class);
 BeanPropertyDefinition prop2 = mock(BeanPropertyDefinition.class);
 Set<BeanPropertyDefinition> properties = new HashSet<>();
 properties.add(prop1);
 properties.add(prop2);

 PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
 when(buffer.getParameters(prop1)).thenReturn(prop1);
 when(buffer.getParameters(prop2)).thenReturn(prop2);

 Object result = testInstance.createFromObjectWith(mock(DeserializationContext.class), properties.toArray(new BeanPropertyDefinition[0]), buffer);
 assertEquals("test", result);
 }

 @Test
 public void testGetDelegateType() throws Exception {
 JavaType delegateType = mock(JavaType.class);
 when(testInstance.getDelegateType(mock(DeserializationConfig.class))).thenReturn(delegateType);
 assertEquals(delegateType, testInstance.getDelegateType(mock(DeserializationConfig.class)));
 }

 @Test
 public void testGetArrayDelegateType() throws Exception {
 JavaType arrayDelegateType = mock(JavaType.class);
 when(testInstance.getArrayDelegateType(mock(DeserializationConfig.class))).thenReturn(arrayDelegateType);
 assertEquals(arrayDelegateType, testInstance.getArrayDelegateType(mock(DeserializationConfig.class)));
 }

 @Test
 public void testCreateFromObjectWith_whenAnnotatedParametersAreMissing_thenThrowException() throws Exception {
 testInstance = new ValueInstantiator.Base(MyClass.class) {
 @Override
 public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
 return new SettableBeanProperty[0];
 }
 };

 try {
 testInstance.createFromObjectWith(mock(DeserializationContext.class), mock(Object[].class));
 fail();
 } catch (IOException e) {
 assertEquals("no creator with arguments specified", e.getMessage());
 }
 }
}

class MyClass {
 // empty class
}