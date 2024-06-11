package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiatorCanCreateUsingDefault() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.createUsingDefault(mockDeserializationContext()));
    }

    @Test
    public void testValueInstantiatorCannotCreateUsingDefault() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.createUsingDefault(mockDeserializationContext()));
    }

    @Test
    public void testValueInstantiatorCanCreateFromObjectWith() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(MyClassWithConstructor.class);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromObjectWith());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.createUsingDefault(mockDeserializationContext()));
        Object[] args = new Object[]{new Integer(1), new String("test")};
        MyClassWithConstructor created = (MyClassWithConstructor) instantiator.createFromObjectWith(mockDeserializationContext(), args);
        assertEquals("Test One", created.getValue());
        assertEquals(1, created.getNumber());
    }

    @Test
    public void testValueInstantiatorCannotCreateFromObjectWith() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.createUsingDefault(mockDeserializationContext()));
        assertNull(instantiator.createFromObjectWith(mockDeserializationContext(), new Object[0]));
    }

    @Test
    public void testValueInstantiatorCanCreateFromString() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        String value = "Test String";
        String created = (String) instantiator.createFromString(mockDeserializationContext(), value);
        assertEquals(value, created);
    }

    @Test
    public void testValueInstantiatorCannotCreateFromString() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertNull(instantiator.createFromString(mockDeserializationContext(), "123"));
    }

    @Test
    public void testValueInstantiatorCanCreateFromInt() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertTrue(instantiator.canCreateFromInt());
        int intValue = 123;
        Integer created = (Integer) instantiator.createFromInt(mockDeserializationContext(), intValue);
        assertEquals(intValue, created.intValue());
    }

    @Test
    public void testValueInstantiatorCannotCreateFromInt() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromInt());
        assertNull(instantiator.createFromInt(mockDeserializationContext(), 123));
    }

    @Test
    public void testValueInstantiatorCanCreateFromLong() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Long.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertTrue(instantiator.canCreateFromLong());
        long longValue = 123L;
        Long created = (Long) instantiator.createFromLong(mockDeserializationContext(), longValue);
        assertEquals(longValue, created.longValue());
    }

    @Test
    public void testValueInstantiatorCannotCreateFromLong() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromLong());
        assertNull(instantiator.createFromLong(mockDeserializationContext(), 123L));
    }

    @Test
    public void testValueInstantiatorCanCreateFromBoolean() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Boolean.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertTrue(instantiator.canCreateFromBoolean());
        boolean booleanValue = true;
        Boolean created = (Boolean) instantiator.createFromBoolean(mockDeserializationContext(), booleanValue);
        assertEquals(booleanValue, created.booleanValue());
    }

    @Test
    public void testValueInstantiatorCannotCreateFromBoolean() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromBoolean());
        assertNull(instantiator.createFromBoolean(mockDeserializationContext(), true));
    }

    @Test
    public void testValueInstantiatorCanCreateUsingDelegate() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Delegate(MyClassWithDelegate.class);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
        assertTrue(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromLong());
        assertFalse(instantiator.canCreateFromBoolean());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.createUsingDefault(mockDeserializationContext()));
        Object[] args = new Object[]{new Integer(1), new String("test")};
        MyClassWithDelegate created = (MyClassWithDelegate) instantiator.createFromObjectWith(mockDeserializationContext(), args);
        assertEquals("Test One", created.getValue());
        assertEquals(1, created.getNumber());
        MyClassWithDelegate delegateCreated = new MyClassWithDelegate(1, "Test One");
        instantiator.setDelegateCreator(mockAnnotatedWithParams(delegateCreated.getClass(), new Class[0]));
        MyClassWithDelegate createdUsingDelegate = (MyClassWithDelegate) instantiator.createUsingDelegate(mockDeserializationContext(), delegateCreated);
        assertEquals("Test One", createdUsingDelegate.getValue());
        assertEquals(1, createdUsingDelegate.getNumber());
    }

    @Test
    public void testValueInstantiatorCannotCreateUsingDelegate() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromLong());
        assertFalse(instantiator.canCreateFromBoolean());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.createUsingDelegate(mockDeserializationContext(), mockObject()));
    }

    @Test
    public void testValueInstantiatorGetFromObjectArguments() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(MyClassWithProperties.class);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromObjectWith());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.createUsingDefault(mockDeserializationContext()));
        SettableBeanProperty[] properties = new SettableBeanProperty[]{
                new SettableBeanProperty("name", String.class),
                new SettableBeanProperty("number", Integer.class)
        };
        Set<SettableBeanProperty> arguments = instantiator.getFromObjectArguments(mockDeserializationConfig());
        assertEquals(2, arguments.size());
        assertTrue(arguments.contains(properties[0]));
        assertTrue(arguments.contains(properties[1]));
    }

    @Test
    public void testValueInstantiatorGetDelegateType() throws IOException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(MyClassWithDelegate.class);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateUsingDefault());
        assertTrue(instantiator.canCreateFromObjectWith());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromLong());
        assertFalse(instantiator.canCreateFromBoolean());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateType(mockDeserializationConfig()));
        assertNull(instantiator.createUsingDelegate(mockDeserializationContext(), mockObject()));
        instantiator.setDelegateType(mockJavaType(MyClassWithDelegate.class));
        JavaType delegateType = instantiator.getDelegateType(mockDeserializationConfig());
        assertEquals(MyClassWithDelegate.class, delegateType.getRawClass());
    }

    private Object mockDeserializationContext() {
        return mock(DeserializationContext.class);
    }

    private Object mockObject() {
        return mock(Object.class);
    }

    private AnnotatedWithParams mockAnnotatedWithParams(Class<?> clazz, Class<?>[] paramTypes) {
        return mock(AnnotatedWithParams.class, new Object[]{clazz, paramTypes});
    }

    private JavaType mockJavaType(Class<?> clazz) {
        return mock(JavaType.class, new Object[]{clazz.getName()});
    }

    private DeserializationConfig mockDeserializationConfig() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.findBeanProperties(MyClassWithProperties.class)).thenReturn(new PropertyName[] {});
        return config;
    }
}

class MyClassWithConstructor {
    int number;
    String value;

    public MyClassWithConstructor(int number, String value) {
        this.number = number;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getNumber() {
        return number;
    }
}

class MyClassWithDelegate {
    int number;
    String value;

    public MyClassWithDelegate(int number, String value) {
        this.number = number;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getNumber() {
        return number;
    }
}