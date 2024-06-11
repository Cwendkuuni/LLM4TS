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
    public void testSettableAnyPropertyCreation() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty settableAnyProperty2 = settableAnyProperty.withValueDeserializer(new JsonDeserializer<>());

        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertEquals(valueTypeDeserializer, settableAnyProperty.getValueTypeDeserializer());
        assertTrue(settableAnyProperty instanceof Serializable);

        assertEquals(property, settableAnyProperty2.getProperty());
        assertEquals(setter, settableAnyProperty2.getSetter());
        assertEquals(type, settableAnyProperty2.getType());
        assertEquals(valueDeserializer, settableAnyProperty2.getValueDeserializer());
        assertEquals(valueTypeDeserializer, settableAnyProperty2.getValueTypeDeserializer());
        assertEquals(valueDeserializer, settableAnyProperty2.getValueDeserializer());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, setter, null, null, null, null);
        settableAnyProperty.fixAccess(config);
        verify(setter).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = mock(Object.class);
        Object propName = "propName";
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<Object>() {};
        TypeDeserializer valueTypeDeserializer = new BaseJsonTypeDeserializer() {};
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, valueDeserializer, valueTypeDeserializer);

        Object value = mock(Object.class);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(value);

        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        String key = "key";
        when(keyDeserializer.deserializeKey(eq(propName), eq(ctxt))).thenReturn(key);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        AnnotatedMethod setter = mock(AnnotatedMethod.class);
        AnnotatedField field = mock(AnnotatedField.class);

        if (instance instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) instance;
            assertEquals(key, map.keySet().iterator().next());
            assertEquals(value, map.get(key));
        } else {
            Object fieldValue = ((AnnotatedMethod) setter).callOnWith(instance, propName, value);
            assertEquals(value, fieldValue);
        }
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<Object>() {};
        TypeDeserializer valueTypeDeserializer = new BaseJsonTypeDeserializer() {};
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, valueDeserializer, valueTypeDeserializer);

        Object value = mock(Object.class);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(value);

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        JsonToken jsonToken = settableAnyProperty.deserialize(p, ctxt);
        assertEquals(JsonToken.VALUE_STRING, jsonToken);
        verify(valueDeserializer).deserialize(p, ctxt);

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        jsonToken = settableAnyProperty.deserialize(p, ctxt);
        assertEquals(JsonToken.VALUE_NUMBER_INT, jsonToken);
        verify(valueDeserializer).deserialize(p, ctxt);

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_FLOAT);
        jsonToken = settableAnyProperty.deserialize(p, ctxt);
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, jsonToken);
        verify(valueDeserializer).deserialize(p, ctxt);

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_EMBEDDED_ARRAY);
        jsonToken = settableAnyProperty.deserialize(p, ctxt);
        assertEquals(JsonToken.VALUE_EMBEDDED_ARRAY, jsonToken);
        verify(valueDeserializer).deserialize(p, ctxt);

        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        jsonToken = settableAnyProperty.deserialize(p, ctxt);
        assertEquals(JsonToken.START_OBJECT, jsonToken);
        verify(valueDeserializer).deserializeWithType(p, ctxt, valueTypeDeserializer);

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        jsonToken = settableAnyProperty.deserialize(p, ctxt);
        assertEquals(JsonToken.VALUE_NULL, jsonToken);
        verifyNoMoreInteractions(valueDeserializer);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object propName = "propName";
        Object value = new Object() {};

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        AnnotatedField field = mock(AnnotatedField.class);
        Map<Object, Object> map = mock(Map.class);
        when(field.getValue(instance)).thenReturn(map);

        settableAnyProperty.set(instance, propName, value);

        assertEquals(value, map.get(propName));

        AnnotatedMethod setter = mock(AnnotatedMethod.class);
        when(setter.callOnWith(instance, propName, value)).thenReturn(value);

        settableAnyProperty = new SettableAnyProperty(null, setter, null, null, null, null);
        settableAnyProperty.set(instance, propName, value);

        assertEquals(value, setter.invoke(instance, propName));
    }

    @Test
    public void testThrowAsIOE() {
        Object instance = new Object() {};
        Object propName = "propName";
        Object value = new Object() {};
        Exception e = new Exception("Exception message");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        settableAnyProperty._throwAsIOE(e, propName, value);

        fail("Expected JsonMappingException to be thrown");
    }
}