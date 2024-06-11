package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdValueInstantiatorTest {

 private StdValueInstantiator instantiator;
 private DeserializationContext context;
 private ObjectMapper mapper;

 @BeforeMethod
 public void setup() {
 mapper = new ObjectMapper();
 context = mock(DeserializationContext.class);
 instantiator = new StdValueInstantiator(context, mapper.constructType(String.class));
 }

 @Test
 public void testCreateUsingDefault() throws IOException {
 // Default constructor test
 StdValueInstantiator defaultConstructor = new StdValueInstantiator(context, null);
 assertEquals("UNKNOWN TYPE", defaultConstructor.getValueTypeDesc());
 assertNull(defaultConstructor.getValueClass());
 assertNull(defaultConstructor.getDefaultCreator());

 // Test with valid default constructor
 instantiator = new StdValueInstantiator(context, mapper.constructType(String.class));
 assertEquals("java.lang.String", instantiator.getValueTypeDesc());
 assertEquals(String.class, instantiator.getValueClass());
 assertNotNull(instantiator.getDefaultCreator());

 // Test with null valueType and valueClass
 StdValueInstantiator nullValueTypeAndClass = new StdValueInstantiator(context, null);
 assertEquals("UNKNOWN TYPE", nullValueTypeAndClass.getValueTypeDesc());
 assertNull(nullValueTypeAndClass.getValueClass());
 assertNull(nullValueTypeAndClass.getDefaultCreator());
 }

 @Test
 public void testCreateUsingDelegate() throws IOException {
 // Test with valid delegate constructor
 AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
 SettableBeanProperty[] delegateArguments = new SettableBeanProperty[0];
 when(delegateCreator.call(delegateArguments)).thenReturn(new Object());
 instantiator.configureUsingDelegate(delegateCreator, delegateArguments);
 assertEquals(delegateCreator, instantiator.getDelegateCreator());
 assertEquals(delegateArguments.length, instantiator.getDelegateArguments().length);

 // Test with null delegateCreator
 StdValueInstantiator nullDelegateCreator = new StdValueInstantiator(context, mapper.constructType(String.class));
 nullDelegateCreator.configureUsingDelegate(null, null);
 assertNull(nullDelegateCreator.getDelegateCreator());
 assertNull(nullDelegateCreator.getDelegateArguments());
 }

 @Test
 public void testCreateFromObjectWith() throws IOException {
 // Test with valid withArgsCreator
 AnnotatedWithParams withArgsCreator = mock(AnnotatedWithParams.class);
 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[1];
 constructorArguments[0] = mock(SettableBeanProperty.class);
 when(withArgsCreator.call(constructorArguments)).thenReturn(new Object());
 instantiator.configureFromObjectSettings(null, null, null, null, withArgsCreator, constructorArguments);
 assertEquals(withArgsCreator, instantiator.getWithArgsCreator());
 assertEquals(constructorArguments.length, instantiator.getConstructorArguments().length);
 assertEquals(constructorArguments[0], instantiator.getConstructorArguments()[0]);

 // Test with null withArgsCreator
 StdValueInstantiator nullWithArgsCreator = new StdValueInstantiator(context, mapper.constructType(String.class));
 nullWithArgsCreator.configureFromObjectSettings(null, null, null, null, null, null);
 assertNull(nullWithArgsCreator.getWithArgsCreator());
 assertNull(nullWithArgsCreator.getConstructorArguments());
 }

 @Test
 public void testCreateUsingArrayDelegate() throws IOException {
 // Test with valid arrayDelegateCreator
 AnnotatedWithParams arrayDelegateCreator = mock(AnnotatedWithParams.class);
 JavaType arrayDelegateType = mapper.constructType(int[].class);
 SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[1];
 arrayDelegateArguments[0] = mock(SettableBeanProperty.class);
 when(arrayDelegateCreator.call(arrayDelegateArguments)).thenReturn(new int[1][1]);
 instantiator.configureFromArraySettings(arrayDelegateCreator, arrayDelegateType, arrayDelegateArguments);
 assertEquals(arrayDelegateCreator, instantiator.getArrayDelegateCreator());
 assertEquals(arrayDelegateType, instantiator.getArrayDelegateType());
 assertEquals(arrayDelegateArguments.length, instantiator.getArrayDelegateArguments().length);
 assertEquals(arrayDelegateArguments[0], instantiator.getArrayDelegateArguments()[0]);

 // Test with null arrayDelegateCreator
 StdValueInstantiator nullArrayDelegateCreator = new StdValueInstantiator(context, mapper.constructType(String.class));
 nullArrayDelegateCreator.configureFromArraySettings(null, null, null);
 assertNull(nullArrayDelegateCreator.getArrayDelegateCreator());
 assertNull(nullArrayDelegateCreator.getArrayDelegateType());
 assertNull(nullArrayDelegateCreator.getArrayDelegateArguments());
 }

 @Test
 public void testCreateFromObjectWithNullInjectables() throws IOException {
 // Test with valid withArgsCreator and null injectables
 AnnotatedWithParams withArgsCreator = mock(AnnotatedWithParams.class);
 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[1];
 constructorArguments[0] = null;
 when(withArgsCreator.call(constructorArguments)).thenReturn(new Object());
 instantiator.configureFromObjectSettings(null, null, null, null, withArgsCreator, constructorArguments);
 assertNull(instantiator.getWithArgsCreator());
 assertNull(instantiator.getConstructorArguments());
 }

 @Test
 public void testCreateFromString() throws IOException {
 // Test with valid fromStringCreator
 AnnotatedWithParams fromStringCreator = mock(AnnotatedWithParams.class);
 when(fromStringCreator.call1("test")).thenReturn(new Object());
 instantiator.configureFromStringCreator(fromStringCreator);
 assertEquals("java.lang.String", instantiator.getValueTypeDesc());
 assertEquals(String.class, instantiator.getValueClass());
 assertNotNull(instantiator.getFromStringCreator());
 instantiator.createFromString(context, "test");
 verify(fromStringCreator).call1("test");

 // Test with null fromStringCreator
 StdValueInstantiator nullFromStringCreator = new StdValueInstantiator(context, mapper.constructType(String.class));
 nullFromStringCreator.configureFromStringCreator(null);
 assertNull(nullFromStringCreator.getFromStringCreator());
 }

 @Test
 public void testCreateFromInt() throws IOException {
 // Test with valid fromIntCreator
 AnnotatedWithParams fromIntCreator = mock(AnnotatedWithParams.class);
 when(fromIntCreator.call1(1)).thenReturn(new Object());
 instantiator.configureFromIntCreator(fromIntCreator);
 assertEquals("java.lang.Integer", instantiator.getValueTypeDesc());
 assertEquals(Integer.class, instantiator.getValueClass());
 assertNotNull(instantiator.getFromIntCreator());
 instantiator.createFromInt(context, 1);
 verify(fromIntCreator).call1(1);

 // Test with null fromIntCreator
 StdValueInstantiator nullFromIntCreator = new StdValueInstantiator(context, mapper.constructType(String.class));
 nullFromIntCreator.configureFromIntCreator(null);
 assertNull(nullFromIntCreator.getFromIntCreator());
 }

 @Test
 public void testCreateFromLong() throws IOException {
 // Test with valid fromLongCreator
 AnnotatedWithParams fromLongCreator = mock(AnnotatedWithParams.class);
 when(fromLongCreator.call1(1L)).thenReturn(new Object());
 instantiator.configureFromLongCreator(fromLongCreator);
 assertEquals("java.lang.Long", instantiator.getValueTypeDesc());
 assertEquals(Long.class, instantiator.getValueClass());
 assertNotNull(instantiator.getFromLongCreator());
 instantiator.createFromLong(context, 1L);
 verify(fromLongCreator).call1(1L);

 // Test with null fromLongCreator
 StdValueInstantiator nullFromLongCreator = new StdValueInstantiator(context, mapper.constructType(String.class));
 nullFromLongCreator.configureFromLongCreator(null);
 assertNull(nullFromLongCreator.getFromLongCreator());
 }

 @Test
 public void testCreateFromDouble() throws IOException {
 // Test with valid fromDoubleCreator
 AnnotatedWithParams fromDoubleCreator = mock(AnnotatedWithParams.class);
 when(fromDoubleCreator.call1(1D)).thenReturn(new Object());
 instantiator.configureFromDoubleCreator(fromDoubleCreator);
 assertEquals("java.lang.Double", instantiator.getValueTypeDesc());
 assertEquals(Double.class, instantiator.getValueClass());
 assertNotNull(instantiator.getFromDoubleCreator());
 instantiator.createFromDouble(context, 1D);
 verify(fromDoubleCreator).call1(1D);

 // Test with null fromDoubleCreator
 StdValueInstantiator nullFromDoubleCreator = new StdValueInstantiator(context, mapper.constructType(String.class));
 nullFromDoubleCreator.configureFromDoubleCreator(null);
 assertNull(nullFromDoubleCreator.getFromDoubleCreator());
 }

 @Test
 public void testCreateFromBoolean() throws IOException {
 // Test with valid fromBooleanCreator
 AnnotatedWithParams fromBooleanCreator = mock(AnnotatedWithParams.class);
 when(fromBooleanCreator.call1(true)).thenReturn(new Object());
 instantiator.configureFromBooleanCreator(fromBooleanCreator);
 assertEquals("java.lang.Boolean", instantiator.getValueTypeDesc());
 assertEquals(Boolean.class, instantiator.getValueClass());
 assertNotNull(instantiator.getFromBooleanCreator());
 instantiator.createFromBoolean(context, true);
 verify(fromBooleanCreator).call1(true);

 // Test with null fromBooleanCreator
 StdValueInstantiator nullFromBooleanCreator = new StdValueInstantiator(context, mapper.constructType(String.class));
 nullFromBooleanCreator.configureFromBooleanCreator(null);
 assertNull(nullFromBooleanCreator.getFromBooleanCreator());
 }

 @Test
 public void testCreateUsingDelegateWithNullDelegate() throws IOException {
 StdValueInstantiator delegateInstantiator = new StdValueInstantiator(context, mapper.constructType(String.class));
 JavaType delegateType = mapper.constructType(String.class);
 AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
 SettableBeanProperty[] delegateArguments = new SettableBeanProperty[1];
 delegateArguments[0] = mock(SettableBeanProperty.class);
 when(delegateCreator.call(delegateArguments)).thenReturn(new Object());
 delegateInstantiator.configureUsingDelegate(delegateCreator, delegateArguments);

 try {
 delegateInstantiator.createUsingDelegate(context, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }
}