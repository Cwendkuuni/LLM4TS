
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyMetadata;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata propertyMetadata;
    private JsonDeserializer<?> valueDeserializer;

    @Before
    public void setUp() {
        objectIdReader = new ObjectIdReader(null, null);
        propertyMetadata = new PropertyMetadata(null, null, null, null);
        valueDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testConstructor() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);

        assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
        assertNull(objectIdValueProperty.getAnnotation(null));
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testCopyConstructorWithDeserializer() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(src, valueDeserializer);

        assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
        assertNull(objectIdValueProperty.getAnnotation(null));
        assertNull(objectIdValueProperty.getMember());
        assertSame(valueDeserializer, objectIdValueProperty._valueDeserializer);
    }

    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(src, newName);

        assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
        assertNull(objectIdValueProperty.getAnnotation(null));
        assertNull(objectIdValueProperty.getMember());
        assertEquals("newName", objectIdValueProperty.getName().getSimple());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);

        assertSame(objectIdReader, newObjectIdValueProperty._objectIdReader);
        assertNull(newObjectIdValueProperty.getAnnotation(null));
        assertNull(newObjectIdValueProperty.getMember());
        assertEquals("newName", newObjectIdValueProperty.getName().getSimple());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(valueDeserializer);

        assertSame(objectIdReader, newObjectIdValueProperty._objectIdReader);
        assertNull(newObjectIdValueProperty.getAnnotation(null));
        assertNull(newObjectIdValueProperty.getMember());
        assertSame(valueDeserializer, newObjectIdValueProperty._valueDeserializer);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullId() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        JsonParser p = new ObjectMapper().getFactory().createParser("null");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object instance = new Object();

        assertNull(objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testDeserializeSetAndReturnWithNonNullId() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        JsonParser p = new ObjectMapper().getFactory().createParser("1");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object instance = new Object();

        assertNull(objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNoIdProperty() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Object instance = new Object();
        Object value = new Object();

        objectIdValueProperty.setAndReturn(instance, value);
    }
}
