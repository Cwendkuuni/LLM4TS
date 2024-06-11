package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals(property, settableAnyProperty.getProperty());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);

        settableAnyProperty.fixAccess(config);

        verify(settableAnyProperty._setter).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";

        Object key = mock(Object.class);
        Object value = mock(Object.class);

        when(parser.getCurrentName()).thenReturn(propName);
        when(_keyDeserializer.deserializeKey(propName, context)).thenReturn(key);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(_valueDeserializer.deserialize(parser, context)).thenReturn(value);

        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);
        doNothing().when(settableAnyProperty).set(instance, key, value);

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);

        verify(settableAnyProperty).set(instance, key, value);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        JsonToken token = JsonToken.VALUE_STRING;
        when(parser.getCurrentToken()).thenReturn(token);

        Object value = mock(Object.class);

        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);
        when(settableAnyProperty.deserialize(parser, context)).thenReturn(value);

        assertEquals(value, settableAnyProperty.deserialize(parser, context));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object propName = "propName";
        Object value = mock(Object.class);

        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);
        doNothing().when(settableAnyProperty)._throwAsIOE(any(Exception.class), eq(propName), eq(value));

        settableAnyProperty.set(instance, propName, value);
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowAsIOE() throws IOException {
        Exception e = new IOException("test exception");
        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);

        settableAnyProperty._throwAsIOE(e, "propName", "value");
    }
}