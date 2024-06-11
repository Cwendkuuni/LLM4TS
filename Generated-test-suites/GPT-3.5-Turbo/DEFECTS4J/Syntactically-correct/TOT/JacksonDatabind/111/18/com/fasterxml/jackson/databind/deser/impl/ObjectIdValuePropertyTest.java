
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        // Test case: null input
        Object instance = new Object();
        assertNull(property.deserializeAndSet(null, null, instance));
        
        // Test case: VALUE_NULL token
        instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        assertNull(property.deserializeAndSet(jsonParser, null, instance));
        
        // Test case: id is deserialized and bound to instance
        instance = new Object();
        jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        JsonDeserializer deserializer = Mockito.mock(JsonDeserializer.class);
        Mockito.when(deserializer.deserialize(jsonParser, null)).thenReturn("testId");
        property = new ObjectIdValueProperty(objectIdReader, deserializer, metadata);
        assertNotNull(property.deserializeAndSet(jsonParser, null, instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        // Test case: idProperty is null
        Object instance = new Object();
        try {
            property.setAndReturn(instance, "testId");
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        
        // Test case: idProperty is not null
        SettableBeanProperty idProperty = Mockito.mock(SettableBeanProperty.class);
        Mockito.when(idProperty.setAndReturn(instance, "testId")).thenReturn(instance);
        objectIdReader = new ObjectIdReader(null, idProperty, null);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertSame(instance, property.setAndReturn(instance, "testId"));
    }
}
