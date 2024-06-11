package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.*;
import java.util.Map;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {
    private SettableAnyProperty settableAnyProperty;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;
    private KeyDeserializer keyDeserializer;
    private DeserializationContext ctxt;
    private JsonParser p;
    private BeanProperty property;
    private AnnotatedMember setter;
    private JavaType type;

    @Before
    public void setup() {
        property = mock(BeanProperty.class);
        setter = mock(AnnotatedMember.class);
        type = mock(JavaType.class);
        keyDeserializer = mock(KeyDeserializer.class);
        valueDeserializer = mock(JsonDeserializer.class);
        valueTypeDeserializer = mock(TypeDeserializer.class);
        ctxt = mock(DeserializationContext.class);
        p = mock(JsonParser.class);
        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        String propName = "propName";
        String key = "key";
        Object expectedValue = new Object();

        when(keyDeserializer.deserializeKey(propName, ctxt)).thenReturn(key);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(expectedValue);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(keyDeserializer).deserializeKey(propName, ctxt);
        verify(valueDeserializer).deserialize(p, ctxt);
        verify(setter).callOnWith(instance, key, expectedValue);
    }

    @Test
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        Object instance = new Object();
        String propName = "propName";
        String key = "key";

        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        AnySetterReferring referring = mock(AnySetterReferring.class);
        ReadableObjectId.ReferringContainer referringContainer = mock(ReadableObjectId.ReferringContainer.class);

        when(keyDeserializer.deserializeKey(propName, ctxt)).thenReturn(key);

        when(reference.getRoid()).thenReturn(referringContainer);
        when(referringContainer.hasId(key)).thenReturn(true);
        when(referringContainer.get(key)).thenReturn(referring);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(reference).getRoid();
        verify(referringContainer).hasId(key);
        verify(referringContainer).get(key);
        verify(referring).handleResolvedForwardReference(key, null);
        verifyNoMoreInteractions(p);
        verifyZeroInteractions(keyDeserializer, valueDeserializer, setter);
    }

    @Test
    public void testDeserialize() throws IOException {
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        settableAnyProperty.deserialize(p, ctxt);

        verify(p).getCurrentToken();
        verify(valueDeserializer).getNullValue(ctxt);
        verifyZeroInteractions(valueTypeDeserializer);
    }

    @Test
    public void testDeserializeWithValueTypeDeserializer() throws IOException {
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);

        settableAnyProperty = settableAnyProperty.withValueDeserializer(valueDeserializer);

        settableAnyProperty.deserialize(p, ctxt);

        verify(p).getCurrentToken();
        verify(valueDeserializer).deserializeWithType(p, ctxt, valueTypeDeserializer);
        verifyNoMoreInteractions(valueDeserializer);
        verifyZeroInteractions(valueTypeDeserializer);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        String propName = "propName";
        Object value = new Object();

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(setterIsField()).thenReturn(false);
        when(setter instanceof AnnotatedMethod).thenReturn(true);
        when(setter).thenReturn(annotatedMethod);

        settableAnyProperty.set(instance, propName, value);

        verify(annotatedMethod).callOnWith(instance, propName, value);
        verifyZeroInteractions(valueDeserializer);
        verifyZeroInteractions(keyDeserializer);
    }

    @Test
    public void testSetWithNullValue() throws IOException {
        Object instance = new Object();
        String propName = "propName";
        Object value = null;

        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(setterIsField()).thenReturn(false);
        when(setter instanceof AnnotatedMethod).thenReturn(true);
        when(setter).thenReturn(annotatedMethod);

        settableAnyProperty.set(instance, propName, value);

        verify(annotatedMethod).callOnWith(instance, propName, value);
        verifyZeroInteractions(valueDeserializer);
        verifyZeroInteractions(keyDeserializer);
    }

    @Test
    public void testSetWithSetterIsField() throws IOException {
        Object instance = new Object();
        String propName = "propName";
        Object value = new Object();

        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(setterIsField()).thenReturn(true);
        when(setter instanceof AnnotatedField).thenReturn(true);
        when(setter).thenReturn(annotatedField);

        settableAnyProperty.set(instance, propName, value);

        verifyZeroInteractions(valueDeserializer);
        verifyNoMoreInteractions(keyDeserializer);
        verifyZeroInteractions(annotatedField);
    }

    private Boolean setterIsField() {
        return settableAnyProperty._setterIsField;
    }
}