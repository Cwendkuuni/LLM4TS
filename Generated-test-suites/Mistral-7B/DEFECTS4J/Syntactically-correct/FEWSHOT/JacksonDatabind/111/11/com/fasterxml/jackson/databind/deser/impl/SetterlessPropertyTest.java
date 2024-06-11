package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class SetterlessPropertyTest {
    private static final SetterlessProperty TEST_PROPERTY = new SetterlessProperty(
            mock(BeanPropertyDefinition.class), mock(JavaType.class), null, mock(Annotations.class),
            mock(AnnotatedMethod.class));
    private static final Object TEST_INSTANCE = mock(Object.class);
    private static final JsonDeserializer<Object> TEST_DESERIALIZER = mock(JsonDeserializer.class);
    private static final NullValueProvider TEST_NULL_PROVIDER = mock(NullValueProvider.class);
    private static final Object TEST_VALUE = mock(Object.class);
    private static final Method MOCK_GETTER = mock(Method.class);

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAnnotation() {
        when(TEST_ANNOTATED.getAnnotation(JsonProperty.class)).thenReturn(mock(JsonProperty.class));
        assertSame(mock(JsonProperty.class), TEST_PROPERTY.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        when(TEST_ANNOTATED.getMember()).thenReturn(mock(AnnotatedMember.class));
        assertSame(mock(AnnotatedMember.class), TEST_PROPERTY.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(MOCK_GETTER.invoke(TEST_INSTANCE, (Object[]) null)).thenReturn(TEST_VALUE);
        when(TEST_VALUE_DESERIALIZER.deserialize(mock(JsonParser.class), mock(DeserializationContext.class), TEST_VALUE)).thenReturn(TEST_VALUE);

        TEST_PROPERTY.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), TEST_INSTANCE);

        assertSame(TEST_VALUE, BeanUtil.findValue(TEST_INSTANCE, TEST_PROPERTY.getName()));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(MOCK_GETTER.invoke(TEST_INSTANCE, (Object[]) null)).thenReturn(TEST_VALUE);
        when(TEST_VALUE_DESERIALIZER.deserialize(mock(JsonParser.class), mock(DeserializationContext.class), TEST_VALUE)).thenReturn(TEST_VALUE);

        Object result = TEST_PROPERTY.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), TEST_INSTANCE);

        assertSame(TEST_INSTANCE, result);
        assertSame(TEST_VALUE, BeanUtil.findValue(TEST_INSTANCE, TEST_PROPERTY.getName()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        TEST_PROPERTY.set(TEST_INSTANCE, TEST_VALUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        TEST_PROPERTY.setAndReturn(TEST_INSTANCE, TEST_VALUE);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty result = TEST_PROPERTY.withName(newName);

        assertSame(TEST_PROPERTY, result);
        assertSame(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Object> deser = spy(TEST_DESERIALIZER);
        SettableBeanProperty result = TEST_PROPERTY.withValueDeserializer(deser);

        assertSame(TEST_PROPERTY, result);
        assertSame(deser, result._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = spy(TEST_NULL_PROVIDER);
        SettableBeanProperty result = TEST_PROPERTY.withNullProvider(nva);

        assertSame(TEST_PROPERTY, result);
        assertSame(nva, result._nullProvider);
    }

    @Test
    public void testFixAccess() {
        TEST_PROPERTY.fixAccess(mock(DeserializationConfig.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvokeGetter() throws IOException {
        when(MOCK_GETTER.invoke(TEST_INSTANCE, (Object[]) null)).thenThrow(new IOException());
        TEST_PROPERTY.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), TEST_INSTANCE);
    }

    @Test
    public void testGetValueDeserializer() {
        assertNull(TEST_PROPERTY.getValueDeserializer());
    }

    @Test
    public void testWithValueDeserializer_null() {
        JsonDeserializer<Object> deser = null;
        SettableBeanProperty result = TEST_PROPERTY.withValueDeserializer(deser);

        assertSame(TEST_PROPERTY, result);
        assertNull(result._valueDeserializer);
    }

    @Test
    public void testWithValueDeserializer_deserializer() {
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        SettableBeanProperty result = TEST_PROPERTY.withValueDeserializer(deser);

        assertSame(TEST_PROPERTY, result);
        assertSame(deser, result._valueDeserializer);
    }

    @Test
    public void testWithName_null() {
        PropertyName newName = null;
        SettableBeanProperty result = TEST_PROPERTY.withName(newName);

        assertSame(TEST_PROPERTY, result);
        assertSame(TEST_PROPERTY.getName(), result.getName());
    }

    @Test
    public void testWithName_PropertyName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty result = TEST_PROPERTY.withName(newName);

        assertSame(TEST_PROPERTY, result);
        assertSame(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer_deserializer_NullValueProvider() {
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty result = TEST_PROPERTY.withValueDeserializer(deser).withNullProvider(nva);

        assertSame(TEST_PROPERTY, result);
        assertSame(deser, result._valueDeserializer);
        assertSame(nva, result._nullProvider);
    }
}

class TestValueDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return TEST_VALUE;
    }
}

class TestNullValueProvider implements NullValueProvider {
    @Override
    public Object getNullValue(DeserializationContext ctxt) throws IOException {
        return null;
    }
}