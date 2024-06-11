package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdValuePropertyTest {
    private ObjectIdValueProperty property;
    private ObjectReader reader;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        property = new ObjectIdValueProperty(null, null);
        reader = mock(ObjectReader.class);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonMapper mapper = new JsonMapper();
        StdDeserializer<?> deser = (StdDeserializer<?>) mapper.findValueDeserializer(Object.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = mock(JsonParser.class);
        Object instance = new Object();

        property.deserializeAndSet(jp, ctxt, instance);

        assertNotNull(property);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonMapper mapper = new JsonMapper();
        StdDeserializer<?> deser = (StdDeserializer<?>) mapper.findValueDeserializer(Object.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = mock(JsonParser.class);
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        Object result = property.setAndReturn(instance, value);

        assertNull(result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty idProp = null;
        property = new ObjectIdValueProperty(property, idProp);

        property.setAndReturn(instance, value);
    }

    @Test
    public void testWithNullIdProperty() throws IOException {
        PropertyName newName = new PropertyName("newName");
        property = property.withName(newName);

        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        property = property.withValueDeserializer(deser);

        assertEquals(deser, property.getValueDeserializer());
    }
}