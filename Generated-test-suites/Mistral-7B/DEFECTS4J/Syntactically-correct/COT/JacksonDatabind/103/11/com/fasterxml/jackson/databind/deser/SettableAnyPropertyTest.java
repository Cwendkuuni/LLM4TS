package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testConstructor() throws Exception {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNull(settableAnyProperty._property);
        assertNull(settableAnyProperty._setter);
        assertNull(settableAnyProperty._type);
        assertNull(settableAnyProperty._keyDeserializer);
        assertNull(settableAnyProperty._valueDeserializer);
        assertNull(settableAnyProperty._valueTypeDeserializer);
        assertFalse(settableAnyProperty._setterIsField);

        SettableAnyProperty settableAnyProperty2 = new SettableAnyProperty(property, setter, type, valueDeserializer, valueTypeDeserializer);
        assertEquals(property, settableAnyProperty2._property);
        assertNull(settableAnyProperty2._setter);
        assertEquals(type, settableAnyProperty2._type);
        assertNull(settableAnyProperty2._keyDeserializer);
        assertEquals(valueDeserializer, settableAnyProperty2._valueDeserializer);
        assertNull(settableAnyProperty2._valueTypeDeserializer);
        assertFalse(settableAnyProperty2._setterIsField);
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty settableAnyProperty2 = settableAnyProperty.withValueDeserializer(valueDeserializer);
        assertEquals(settableAnyProperty, settableAnyProperty2);
        assertEquals(valueDeserializer, settableAnyProperty2._valueDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFixAccessNull() throws Exception {
        DeserializationConfig config = mock(DeserializationConfig.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.fixAccess(config);
    }

    @Test
    public void testDeserializeAndSetNull() throws Exception {
        JsonParser parser = null;
        DeserializationContext context = null;
        Object instance = null;
        String propName = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeAndSetUnresolvedForwardReference() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        Object result = setTableAnyPropertyDeserialize(parser, context, valueDeserializer);
        assertEquals(result, valueDeserializer.deserialize(parser, context));
    }

    @Test
    public void testSetNull() throws Exception {
        Object instance = null;
        Object propName = null;
        Object value = null;
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.set(instance, propName, value);
    }

    @Test
    public void testSetValueDeserializer() throws Exception {
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.set(instance, propName, value);
        Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) _setter).getValue(instance);
        assertEquals(value, map.get(propName));
    }

    @Test(expected = IOException.class)
    public void testThrowAsIOEIllegalArgumentException() throws Exception {
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty._throwAsIOE(new IllegalArgumentException("message"), propName, value);
    }

    @Test(expected = IOException.class)
    public void testThrowAsIOEClassUtilIOException() throws Exception {
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty._throwAsIOE(new IOException("message"), propName, value);
    }

    @Test(expected = IOException.class)
    public void testThrowAsIOERte() throws Exception {
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty._throwAsIOE(new RuntimeException("message"), propName, value);
    }

    private Object setTableAnyPropertyDeserialize(JsonParser parser, DeserializationContext context, JsonDeserializer<Object> valueDeserializer) throws IOException {
        Object result;
        JsonToken token = parser.getCurrentToken();
        if (token.isNullValue()) {
            result = valueDeserializer.getNullValue(context);
        } else {
            if (valueDeserializer._valueTypeDeserializer != null) {
                result = valueDeserializer.deserializeWithType(parser, context, valueDeserializer._valueTypeDeserializer);
            } else {
                result = valueDeserializer.deserialize(parser, context);
            }
        }
        return result;
    }
}