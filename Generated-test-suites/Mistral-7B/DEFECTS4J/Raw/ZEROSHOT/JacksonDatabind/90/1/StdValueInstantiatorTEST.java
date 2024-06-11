package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JacksonStdImpl
public class StdValueInstantiatorTest {

    private static final StdValueInstantiator instance = new StdValueInstantiator(null, null);

    @org.junit.Test
    public void testGetValueTypeDesc() {
        String expected = "UNKNOWN TYPE";
        assertEquals(expected, instance.getValueTypeDesc());

        instance.setValueTypeDesc("TestType");
        expected = "TestType";
        assertEquals(expected, instance.getValueTypeDesc());
    }

    @org.junit.Test
    public void testGetValueClass() {
        Class<?> expected = Object.class;
        assertEquals(expected.getName(), instance.getValueClass().toString());

        instance.setValueClass(String.class);
        expected = String.class;
        assertEquals(expected.getName(), instance.getValueClass().toString());
    }

    @org.junit.Test
    public void testCanCreateFromString() {
        assertFalse(instance.canCreateFromString());

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call()).thenReturn("Instance");
        instance.configureFromStringCreator(creator);
        assertTrue(instance.canCreateFromString());
    }

    @org.junit.Test
    public void testCanCreateFromInt() {
        assertFalse(instance.canCreateFromInt());

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call1(Integer.valueOf(1))).thenReturn(1);
        instance.configureFromIntCreator(creator);
        assertTrue(instance.canCreateFromInt());
    }

    @org.junit.Test
    public void testCanCreateFromLong() {
        assertFalse(instance.canCreateFromLong());

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call1(Long.valueOf(1L))).thenReturn(1L);
        instance.configureFromLongCreator(creator);
        assertTrue(instance.canCreateFromLong());
    }

    @org.junit.Test
    public void testCanCreateFromDouble() {
        assertFalse(instance.canCreateFromDouble());

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call1(Double.valueOf(1.0))).thenReturn(1.0);
        instance.configureFromDoubleCreator(creator);
        assertTrue(instance.canCreateFromDouble());
    }

    @org.junit.Test
    public void testCanCreateFromBoolean() {
        assertFalse(instance.canCreateFromBoolean());

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call1(Boolean.valueOf(true))).thenReturn(true);
        instance.configureFromBooleanCreator(creator);
        assertTrue(instance.canCreateFromBoolean());
    }

    @org.junit.Test
    public void testCanCreateUsingDefault() {
        assertFalse(instance.canCreateUsingDefault());

        instance.setDefaultCreator(mock(AnnotatedWithParams.class));
        assertTrue(instance.canCreateUsingDefault());
    }

    @org.junit.Test
    public void testCanCreateUsingDelegate() {
        assertFalse(instance.canCreateUsingDelegate());

        JavaType delegateType = mock(JavaType.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[0];
        instance.configureFromDelegateSettings(delegateType, creator, delegateArguments);
        assertTrue(instance.canCreateUsingDelegate());
    }

    @org.junit.Test
    public void testCreateUsingDefault() throws IOException {
        Object result = instance.createUsingDefault(mock(DeserializationContext.class));
        assertNull(result);

        ObjectNode node = mock(ObjectNode.class);
        when(node.get("defaultCreator")).thenReturn(mock(AnnotatedWithParams.class));
        when(node.get("defaultCreator").call()).thenReturn("Instance");

        instance.configureFromObjectSettings(mock(AnnotatedWithParams.class), null, null, null, null, null);
        when(instance.createUsingDefault(mock(DeserializationContext.class))).thenReturn("Instance");

        Object result2 = instance.createUsingDefault(mock(DeserializationContext.class), node);
        assertEquals("Instance", result2);
    }

    @org.junit.Test
    public void testCreateFromObjectWith() throws IOException {
        Object result = instance.createFromObjectWith(mock(DeserializationContext.class), new Object[0]);
        assertNull(result);

        ObjectNode node = mock(ObjectNode.class);
        SettableBeanProperty[] constructorArguments = { mock(SettableBeanProperty.class) };
        when(node.get("withArgsCreator")).thenReturn(mock(AnnotatedWithParams.class));
        when(node.get("withArgsCreator").call(new Object[0])).thenReturn("Instance");
        when(node.get("constructorArguments")).thenReturn(constructorArguments);

        instance.configureFromObjectSettings(null, null, null, null, mock(AnnotatedWithParams.class), constructorArguments);
        when(instance.createFromObjectWith(mock(DeserializationContext.class), new Object[0])).thenReturn("Instance");

        Object result2 = instance.createFromObjectWith(mock(DeserializationContext.class), node);
        assertEquals("Instance", result2);
    }

    @org.junit.Test
    public void testCreateUsingDelegate() throws IOException {
        Object result = instance.createUsingDelegate(mock(DeserializationContext.class), null);
        assertNull(result);

        JavaType delegateType = mock(JavaType.class);
        AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[0];
        Object delegate = new Object();

        when(instance.createUsingDelegate(mock(DeserializationContext.class), delegate)).thenThrow(new IOException());
        instance.configureFromDelegateSettings(delegateType, delegateCreator, delegateArguments);

        Object result2 = instance.createUsingDelegate(mock(DeserializationContext.class), delegate);
        assertNull(result2);

        when(instance.createUsingDelegate(mock(DeserializationContext.class), delegate)).thenReturn("DelegateInstance");
        Object result3 = instance.createUsingDelegate(mock(DeserializationContext.class), delegate);
        assertEquals("DelegateInstance", result3);
    }

    @org.junit.Test
    public void testCreateUsingArrayDelegate() throws IOException {
        Object result = instance.createUsingArrayDelegate(mock(DeserializationContext.class), null);
        assertNull(result);

        JavaType arrayDelegateType = mock(JavaType.class);
        AnnotatedWithParams arrayDelegateCreator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[0];
        Object arrayDelegate = new Object[0][];

        when(instance.createUsingArrayDelegate(mock(DeserializationContext.class), arrayDelegate)).thenThrow(new IOException());
        instance.configureFromArraySettings(arrayDelegateCreator, arrayDelegateType, arrayDelegateArguments);

        Object result2 = instance.createUsingArrayDelegate(mock(DeserializationContext.class), arrayDelegate);
        assertNull(result2);

        when(instance.createUsingArrayDelegate(mock(DeserializationContext.class), arrayDelegate)).thenReturn("ArrayDelegateInstance");
        Object result3 = instance.createUsingArrayDelegate(mock(DeserializationContext.class), arrayDelegate);
        assertEquals("ArrayDelegateInstance", result3);
    }

    @org.junit.Test
    public void testCreateFromString() throws IOException {
        Object result = instance.createFromString(mock(DeserializationContext.class), "StringValue");
        assertNull(result);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call1("StringValue")).thenReturn("Instance");
        instance.configureFromStringCreator(creator);

        Object result2 = instance.createFromString(mock(DeserializationContext.class), "StringValue");
        assertEquals("Instance", result2);
    }

    @org.junit.Test
    public void testCreateFromInt() throws IOException {
        Object result = instance.createFromInt(mock(DeserializationContext.class), 1);
        assertNull(result);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call1(Integer.valueOf(1))).thenReturn("Instance");
        instance.configureFromIntCreator(creator);

        Object result2 = instance.createFromInt(mock(DeserializationContext.class), 1);
        assertEquals("Instance", result2);
    }

    @org.junit.Test
    public void testCreateFromLong() throws IOException {
        Object result = instance.createFromLong(mock(DeserializationContext.class), 1L);
        assertNull(result);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call1(Long.valueOf(1L))).thenReturn("Instance");
        instance.configureFromLongCreator(creator);

        Object result2 = instance.createFromLong(mock(DeserializationContext.class), 1L);
        assertEquals("Instance", result2);
    }

    @org.junit.Test
    public void testCreateFromDouble() throws IOException {
        Object result = instance.createFromDouble(mock(DeserializationContext.class), 1.0);
        assertNull(result);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call1(Double.valueOf(1.0))).thenReturn("Instance");
        instance.configureFromDoubleCreator(creator);

        Object result2 = instance.createFromDouble(mock(DeserializationContext.class), 1.0);
        assertEquals("Instance", result2);
    }

    @org.junit.Test
    public void testCreateFromBoolean() throws IOException {
        Object result = instance.createFromBoolean(mock(DeserializationContext.class), true);
        assertNull(result);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.call1(Boolean.valueOf(true))).thenReturn("Instance");
        instance.configureFromBooleanCreator(creator);

        Object result2 = instance.createFromBoolean(mock(DeserializationContext.class), true);
        assertEquals("Instance", result2);
    }
}