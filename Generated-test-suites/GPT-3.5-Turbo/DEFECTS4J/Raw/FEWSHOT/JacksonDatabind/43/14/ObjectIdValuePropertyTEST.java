package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

public class ObjectIdValuePropertyTest {
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }

    @Test
    public void testCopyConstructorWithDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<?>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }

    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        assertEquals(newName, property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty property = src.withName(newName);
        assertEquals(newName, property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deser = new JsonDeserializer<?>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        ObjectIdValueProperty property = src.withValueDeserializer(deser);
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Class<Annotation> acls = null;
        Annotation annotation = property.getAnnotation(acls);
        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.getMember());
    }

    // TODO: Add tests for deserializeAndSet, deserializeSetAndReturn, set, and setAndReturn methods
    
    
}