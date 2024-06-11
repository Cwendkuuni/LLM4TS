package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyConstruction() {
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertNull(settableAnyProperty.getValueTypeDeserializer());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertFalse(settableAnyProperty.isSetterField());

        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);

        assertEquals(property, settableAnyPropertyWithValueDeserializer.getProperty());
        assertEquals(setter, settableAnyPropertyWithValueDeserializer.getSetter());
        assertEquals(type, settableAnyPropertyWithValueDeserializer.getType());
        assertEquals(valueDeserializer, settableAnyPropertyWithValueDeserializer.getValueDeserializer());
        assertNull(settableAnyPropertyWithValueDeserializer.getValueTypeDeserializer());
        assertNull(settableAnyPropertyWithValueDeserializer.getKeyDeserializer());
        assertFalse(settableAnyPropertyWithValueDeserializer.isSetterField());
    }

    @Test
    public void testSettableAnyPropertyFixAccess() {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        SettableAnyProperty settableAnyProperty = Mockito.spy(new SettableAnyProperty(null, setter, null, null, null, null));

        settableAnyProperty.fixAccess(config);
        Mockito.verify(setter).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"key\":\"value\"}");
        Object instance = new Object();
        String propName = "key";
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        settableAnyProperty.deserializeAndSet(jsonParser, Mockito.mock(DeserializationContext.class), instance, propName);

        Mockito.verify(setter, Mockito.times(1)).callOnWith(instance, propName, "value");
    }

    @Test
    public void testSettableAnyPropertyDeserialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"value\":\"value\"}");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        SettableAnyProperty settableAnyProperty = Mockito.spy(new SettableAnyProperty(null, null, null, null, null, null));

        Object value = settableAnyProperty.deserialize(jsonParser, ctxt);

        JsonToken jsonToken = jsonParser.getCurrentToken();
        assertEquals(JsonToken.VALUE_STRING, jsonToken);
        assertEquals("value", value);
    }

    @Test
    public void testSettableAnyPropertyWithValueTypeDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"value\":123}");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        SettableAnyProperty settableAnyProperty = Mockito.spy(new SettableAnyProperty(null, null, null, null, null, Mockito.mock(TypeDeserializer.class)));

        Object value = settableAnyProperty.deserialize(jsonParser, ctxt);

        JsonToken jsonToken = jsonParser.getCurrentToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, jsonToken);
        assertEquals(123, value);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyWithUnresolvedForwardReference() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"key\":null}");
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        SettableAnyProperty settableAnyProperty = Mockito.spy(new SettableAnyProperty(null, null, null, null, null, null));

        settableAnyProperty.deserializeAndSet(jsonParser, ctxt, new Object(), "key");

        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        Mockito.when(ctxt.getConfig()).thenReturn(Mockito.mock(DeserializationConfig.class));

        settableAnyProperty.deserializeAndSet(jsonParser, ctxt, new Object(), "key");
    }
}