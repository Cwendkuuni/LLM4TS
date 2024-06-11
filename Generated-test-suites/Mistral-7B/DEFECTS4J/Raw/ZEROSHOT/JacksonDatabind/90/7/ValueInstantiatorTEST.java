package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

public class ValueInstantiatorTest {

 private ValueInstantiator valueInstantiator;

 @Before
 public void setUp() {
 valueInstantiator = new ValueInstantiator.Base(String.class);
 }

 @Test
 public void testCanCreateFromDefault() throws Exception {
 assertTrue(valueInstantiator.canInstantiate());
 assertNull(valueInstantiator.getDefaultCreator());
 assertNull(valueInstantiator.createUsingDefault(mock(DeserializationContext.class)));
 }

 @Test
 public void testCanCreateFromString() throws Exception {
 assertFalse(valueInstantiator.canCreateFromString());
 assertNull(valueInstantiator.createFromString(mock(DeserializationContext.class), "test"));
 }

 @Test
 public void testCanCreateFromInt() throws Exception {
 assertFalse(valueInstantiator.canCreateFromInt());
 assertNull(valueInstantiator.createFromInt(mock(DeserializationContext.class), 1));
 }

 @Test
 public void testCanCreateFromLong() throws Exception {
 assertFalse(valueInstantiator.canCreateFromLong());
 assertNull(valueInstantiator.createFromLong(mock(DeserializationContext.class), 1L));
 }

 @Test
 public void testCanCreateFromDouble() throws Exception {
 assertFalse(valueInstantiator.canCreateFromDouble());
 assertNull(valueInstantiator.createFromDouble(mock(DeserializationContext.class), 1.0));
 }

 @Test
 public void testCanCreateFromBoolean() throws Exception {
 assertFalse(valueInstantiator.canCreateFromBoolean());
 assertNull(valueInstantiator.createFromBoolean(mock(DeserializationContext.class), true));
 }

 @Test
 public void testCanCreateUsingDelegate() throws Exception {
 assertFalse(valueInstantiator.canCreateUsingDelegate());
 assertNull(valueInstantiator.createUsingDelegate(mock(DeserializationContext.class), mock(Object.class)));
 }

 @Test
 public void testCreateFromObjectWith() throws Exception {
 AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
 SettableBeanProperty[] properties = new SettableBeanProperty[1];
 PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
 when(buffer.getParameters(properties)).thenReturn(new Object[0]);
 when(valueInstantiator.getFromObjectArguments(mock(DeserializationConfig.class))).thenReturn(properties);
 when(valueInstantiator.createFromObjectWith(mock(DeserializationContext.class), properties, buffer)).thenReturn(new Object());

 Object result = valueInstantiator.createFromObjectWith(mock(DeserializationContext.class), new Object[0]);
 assertEquals(result, new Object());
 }

 @Test
 public void testCreateUsingDelegate() throws Exception {
 AnnotatedWithParams delegate = mock(AnnotatedWithParams.class);
 when(valueInstantiator.getDelegateCreator()).thenReturn(delegate);
 Object result = valueInstantiator.createUsingDelegate(mock(DeserializationContext.class), new Object());
 assertEquals(result, new Object());
 }
}