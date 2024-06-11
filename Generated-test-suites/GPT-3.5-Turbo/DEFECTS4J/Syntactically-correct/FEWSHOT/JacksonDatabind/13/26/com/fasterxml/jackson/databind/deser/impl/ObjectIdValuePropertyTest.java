package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdValuePropertyTest {
    
    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;

    @Before
    public void setUp() {
        objectIdReader = new ObjectIdReader();
        metadata = new PropertyMetadata();
    }

    @Test
    public void testObjectIdValueProperty() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertSame(objectIdReader, property._objectIdReader);
        assertNull(property.getAnnotation(null));
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        property.deserializeAndSet(jp, ctxt, new Object());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNotNull(property.deserializeSetAndReturn(jp, ctxt, new Object()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        property.setAndReturn(new Object(), new Object());
    }
}