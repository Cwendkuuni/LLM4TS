package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ValueInstantiatorTest {

    private static final JavaType VALUE_TYPE = new TypeReference<Object>() {}.getType();

    private ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);

    @org.junit.Before
    public void setup() {
        when(valueInstantiator.canCreateUsingDefault()).thenReturn(true);
        when(valueInstantiator.getDefaultCreator()).thenReturn(createMockCreator());
    }

    @org.junit.Test
    public void testCanInstantiate() {
        assertEquals(true, valueInstantiator.canInstantiate());
    }

    @org.junit.Test
    public void testGetValueTypeDesc() {
        assertEquals("java.lang.Object", valueInstantiator.getValueTypeDesc());
    }

    @org.junit.Test
    public void testGetValueClass() {
        assertEquals(Object.class, valueInstantiator.getValueClass());
    }

    @org.junit.Test
    public void testCreateUsingDefault() throws IOException {
        ObjectNode objectNode = createObjectNode();
        Object result = valueInstantiator.createUsingDefault(createMockContext(objectNode));
        verify(valueInstantiator).createUsingDefault(eq(createMockContext(objectNode)));
        assertEquals(Object.class, result.getClass());
    }

    @org.junit.Test
    public void testCreateFromObjectWith() throws IOException {
        ObjectNode objectNode = createObjectNode();
        Object[] arguments = new Object[0];
        when(valueInstantiator.getFromObjectArguments(any())).thenReturn(PropertyValueBuffer.emptyBuffer());

        Object result = valueInstantiator.createFromObjectWith(createMockContext(objectNode), arguments);
        verify(valueInstantiator).createUsingDefault(eq(createMockContext(objectNode)));
        assertEquals(Object.class, result.getClass());
    }

    @org.junit.Test
    public void testCreateUsingDelegate() throws IOException {
        ObjectNode objectNode = createObjectNode();
        Object delegate = createMockObject();
        when(valueInstantiator.getDelegateType(any())).thenReturn(VALUE_TYPE);

        Object result = valueInstantiator.createUsingDelegate(createMockContext(objectNode), delegate);
        verify(valueInstantiator).createUsingDelegate(eq(createMockContext(objectNode)), eq(delegate));
        assertEquals(VALUE_TYPE.getRawClass(), result.getClass());
    }

    @org.junit.Test
    public void testCreateFromInt() throws IOException {
        ObjectNode objectNode = createObjectNode();
        int value = 42;

        Object result = valueInstantiator.createFromInt(createMockContext(objectNode), value);
        verify(valueInstantiator).createFromInt(eq(createMockContext(objectNode)), value);
        assertEquals(java.lang.Integer.class, result.getClass());
    }

    @org.junit.Test
    public void testCreateFromLong() throws IOException {
        ObjectNode objectNode = createObjectNode();
        long value = 42L;

        Object result = valueInstantiator.createFromLong(createMockContext(objectNode), value);
        verify(valueInstantiator).createFromLong(eq(createMockContext(objectNode)), value);
        assertEquals(java.lang.Long.class, result.getClass());
    }

    @org.junit.Test
    public void testCreateFromDouble() throws IOException {
        ObjectNode objectNode = createObjectNode();
        double value = 42.0;

        Object result = valueInstantiator.createFromDouble(createMockContext(objectNode), value);
        verify(valueInstantiator).createFromDouble(eq(createMockContext(objectNode)), value);
        assertEquals(java.lang.Double.class, result.getClass());
    }

    @org.junit.Test
    public void testCreateFromBoolean() throws IOException {
        ObjectNode objectNode = createObjectNode();
        boolean value = true;

        Object result = valueInstantiator.createFromBoolean(createMockContext(objectNode), value);
        verify(valueInstantiator).createFromBoolean(eq(createMockContext(objectNode)), value);
        assertEquals(java.lang.Boolean.class, result.getClass());
    }

    @org.junit.Test
    public void testCreateFromString() throws IOException {
        ObjectNode objectNode = createObjectNode();
        String value = "true";

        Object result = valueInstantiator.createFromString(createMockContext(objectNode), value);
        verify(valueInstantiator).createFromString(eq(createMockContext(objectNode)), anyString(), any(Boolean.class));
        assertEquals(java.lang.Boolean.class, result.getClass());
        verify(valueInstantiator)._createFromStringFallbacks(eq(createMockContext(objectNode)), anyString());
    }

    private Creator creator = mock(Creator.class);

    private AnnotatedWithParams createMockCreator() {
        return new AnnotatedWithParams(creator, new AnnotatedMember(new AnnotatedMethod(creator, "test", new Class[0])));
    }

    private ObjectNode createObjectNode() {
        return createObjectNode("{}");
    }

    private Object createMockObject() {
        return createMock(Object.class);
    }

    private DeserializationContext createMockContext(ObjectNode objectNode) {
        return createMock(DeserializationContext.class);
    }
}

class Creator {
    public Object test() {
        return null;
    }
}