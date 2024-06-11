package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;

public class SettableAnyPropertyTest {
    @Test
    public void testConstructor() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;

        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);

        assertNull(sap.getProperty());
        assertFalse(sap.hasValueDeserializer());
        assertNull(sap.getType());
    }

    @Test
    public void testWithDeserializer() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;

        SettableAnyProperty sap = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        SettableAnyProperty sap2 = sap.withValueDeserializer(valueDeser);

        assertSame(valueDeser, sap2.getValueDeserializer());
    }

    @Test
    public void testDeserialize() {
        JsonToken token = JsonToken.START_OBJECT;
        JsonParser p = null;
        DeserializationContext ctxt = null;

        SettableAnyProperty sap = new SettableAnyProperty(null, null, null, null, null, null);

        assertEquals(JsonToken.END_OBJECT, sap.deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeAndSet() {
        JsonToken token = JsonToken.START_OBJECT;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        String propName = null;

        SettableAnyProperty sap = new SettableAnyProperty(null, null, null, null, null, null);

        sap.deserializeAndSet(p, ctxt, instance, propName);
    }

    @Test
    public void testSet() {
        Object instance = null;
        Object propName = null;
        Object value = null;

        SettableAnyProperty sap = new SettableAnyProperty(null, null, null, null, null, null);

        sap.set(instance, propName, value);
    }

    @Test
    public void testThrowAsIOE() {
        Exception e = new IllegalArgumentException();
        Object propName = null;
        Object value = null;

        SettableAnyProperty sap = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            sap._throwAsIOE(e, propName, value);
            fail("Expected IOException to be thrown");
        } catch (IOException ex) {
            assertEquals("Problem deserializing \"any\" property 'null' of class null (expected type: null; actual type: null) (no error message provided)", ex.getMessage());
        }
    }

    @Test
    public void testReadResolve() throws IOException {
        SettableAnyProperty sap = new SettableAnyProperty(null, null, null, null, null, null);

        assertEquals(sap, sap.readResolve());
    }
}