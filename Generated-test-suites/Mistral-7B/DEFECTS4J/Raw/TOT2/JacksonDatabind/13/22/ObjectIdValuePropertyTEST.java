package com.example.junit4;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructorWithValidArguments() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(property);
    }

    @Test
    public void testConstructorWithJsonDeserializer() {
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty src = Mockito.mock(ObjectIdValueProperty.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deserializer);
        assertNotNull(property);
    }

    @Test
    public void testConstructorWithPropertyName() {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyName newName = new PropertyName("test");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, newName);
        assertEquals("test", property.getName().asString());
    }

    @Test
    public void testMethodWithName() {
        ObjectIdValueProperty property = Mockito.mock(ObjectIdValueProperty.class);
        PropertyName newName = new PropertyName("test");
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName.asString(), newProperty.getName().asString());
    }

    @Test
    public void testMethodWithValueDeserializer() {
        ObjectIdValueProperty property = Mockito.mock(ObjectIdValueProperty.class);
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = Mockito.mock(ObjectIdValueProperty.class);
        assertNull(property.getAnnotation(Mockito.any(Annotation.class).eq(Mockito.isA(Annotation.class))));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = Mockito.mock(ObjectIdValueProperty.class);
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSetAndReturn_NullId() throws IOException {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        when(jp.getValueAsObject()).thenReturn(null);

        try {
            property.deserializeAndSet(jp, ctxt, instance);
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            property.deserializeSetAndReturn(jp, ctxt, instance);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeAndSetAndReturn_InvalidId() throws IOException {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object id = new Object();
        when(jp.getValueAsObject()).thenReturn(id);

        try {
            property.deserializeAndSet(jp, ctxt, instance);
            fail();
        } catch (ObjectIdMismatchException e) {
            // expected
        }

        try {
            property.deserializeSetAndReturn(jp, ctxt, instance);
            fail();
        } catch (ObjectIdMismatchException e) {
            // expected
        }
    }

    @Test
    public void testSet_NullInstance() throws IOException {
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        try {
            property.set(null, Mockito.anyObject());
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            property.setAndReturn(null, Mockito.anyObject());
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSet_NullIdProp() throws IOException {
        Object instance = Mockito.mock(Object.class);
        ObjectIdReader objectIdReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        SettableBeanProperty idProp = Mockito.mock(SettableBeanProperty.class);

        try {
            property.setAndReturn(instance, null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            property.setAndReturn(instance, Mockito.anyObject());
            verify(idProp).setAndReturn(instance, Mockito.anyObject());
        } catch (NullPointerException e) {
            // expected
        }
    }
}