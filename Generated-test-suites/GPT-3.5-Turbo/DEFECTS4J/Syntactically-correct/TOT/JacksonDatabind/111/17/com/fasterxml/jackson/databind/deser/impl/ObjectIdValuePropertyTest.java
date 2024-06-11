
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet_NullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null);
        PropertyMetadata metadata = new PropertyMetadata(null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("null");
        DeserializationContext context = new DefaultDeserializationContext(new ObjectMapper(), factory);

        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);

        assertNull(property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testDeserializeAndSet_NonNullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null);
        PropertyMetadata metadata = new PropertyMetadata(null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("\"123\"");
        DeserializationContext context = new DefaultDeserializationContext(new ObjectMapper(), factory);

        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);

        assertEquals("123", property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testSetAndReturn_NullIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null);
        PropertyMetadata metadata = new PropertyMetadata(null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        try {
            property.setAndReturn(new Object(), "123");
            fail("Expected UnsupportedOperationException is not thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("Should not call set() on ObjectIdProperty"));
        }
    }
}
