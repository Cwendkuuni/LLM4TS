package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithNullProvider() {
        NullValueProvider provider = new NullValueProvider() {
            public Object getNullValue(DeserializationContext ctxt) {
                return "NULL";
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null, provider);
        assertNotNull(property.withNullProvider(provider));
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonParser parser = new JsonFactory().createParser(new StringReader("{\"value\": null}"));
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);
        assertNull(property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testDeserializeAndSetWithNonNullValue() throws IOException {

        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        JsonParser parser = new JsonFactory().createParser(new StringReader("{\"value\": \"test\"}"));
        DeserializationContext context = new DeserializationContext(null, null);
        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);
        assertEquals("test", property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testSetAndReturnWithNullIdProperty() throws IOException {

        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        try {
            property.setAndReturn(instance, "test");
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturnWithNonNullIdProperty() throws IOException {

        SettableBeanProperty idProperty = new SettableBeanProperty() {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {

            }

            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                return null;
            }

            @Override
            public void set(Object instance, Object value) throws IOException {

            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return null;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        property._objectIdReader.idProperty = idProperty;
        Object instance = new Object();
        assertNull(property.setAndReturn(instance, "test"));
    }
}