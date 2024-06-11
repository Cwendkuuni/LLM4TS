package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        JsonParser jp = null; //initialize JsonParser object
        DeserializationContext ctxt = null; //initialize DeserializationContext object
        StringArrayDeserializer deser = new StringArrayDeserializer(); //initialize StringArrayDeserializer object
        String[] result = deser.deserialize(jp, ctxt); //call the deserialize method
        assertNull(result); //assert that the result is null
    }

    @Test
    public void testDeserializeWithType() {
        JsonParser jp = null; //initialize JsonParser object
        DeserializationContext ctxt = null; //initialize DeserializationContext object
        TypeDeserializer typeDeserializer = null; //initialize TypeDeserializer object
        StringArrayDeserializer deser = new StringArrayDeserializer(); //initialize StringArrayDeserializer object
        Object result = deser.deserializeWithType(jp, ctxt, typeDeserializer); //call the deserializeWithType method
        assertNull(result); //assert that the result is null
    }

    @Test
    public void testHandleNonArray() {
        JsonParser jp = null; //initialize JsonParser object
        DeserializationContext ctxt = null; //initialize DeserializationContext object
        StringArrayDeserializer deser = new StringArrayDeserializer(); //initialize StringArrayDeserializer object
        String[] result = deser.handleNonArray(jp, ctxt); //call the handleNonArray method
        assertNull(result); //assert that the result is null
    }

    @Test
    public void testCreateContextual() {
        DeserializationContext ctxt = null; //initialize DeserializationContext object
        BeanProperty property = null; //initialize BeanProperty object
        StringArrayDeserializer deser = new StringArrayDeserializer(); //initialize StringArrayDeserializer object
        JsonDeserializer<?> result = deser.createContextual(ctxt, property); //call the createContextual method
        assertEquals(deser, result); //assert that the result is equal to the original object
    }
}