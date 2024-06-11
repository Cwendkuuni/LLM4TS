
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import java.io.IOException;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata propertyMetadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        Object instance = new Object();
        
        try {
            objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        } catch (Exception e) {
            fail("Error should not be thrown");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata propertyMetadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        Object instance = new Object();
        
        try {
            Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Error should not be thrown");
        }
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata propertyMetadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Object instance = new Object();
        Object value = new Object();
        
        try {
            objectIdValueProperty.set(instance, value);
        } catch (Exception e) {
            fail("Error should not be thrown");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnNullIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata propertyMetadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Object instance = new Object();
        Object value = new Object();
        
        objectIdValueProperty.setAndReturn(instance, value);
    }
}
