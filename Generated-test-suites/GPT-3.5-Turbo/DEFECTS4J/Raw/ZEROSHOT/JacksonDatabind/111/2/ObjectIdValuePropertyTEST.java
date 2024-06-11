package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertSame(objectIdReader, property._objectIdReader);
        assertNull(property.getAnnotation(Test.class));
        assertNull(property.getMember());
    }

    @Test
    public void testWithNullValueProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = new JsonParser(jsonFactory);
        DeserializationContext deserializationContext = new DeserializationContext();

        ObjectIdValueProperty property = srcProperty.withNullProvider(deserializationContext);

        assertSame(srcProperty._objectIdReader, property._objectIdReader);
        assertSame(srcProperty._valueDeserializer, property._valueDeserializer);
        assertSame(deserializationContext, property._nullProvider);

        property.deserializeAndSet(jsonParser, deserializationContext, null);
        property.deserializeSetAndReturn(jsonParser, deserializationContext, null);
        assertThrows(UnsupportedOperationException.class, () -> property.set(null, null));
        assertNull(property.setAndReturn(null, null));
    }

    @Test
    public void testWithNewName() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName();

        ObjectIdValueProperty property = srcProperty.withName(newName);

        assertSame(srcProperty._objectIdReader, property._objectIdReader);
        assertEquals(newName, property.getFullName());
        assertNull(property.getAnnotation(Test.class));
        assertNull(property.getMember());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        JsonDeserializer<?> sameDeserializer = new JsonDeserializer<>();

        ObjectIdValueProperty property = srcProperty.withValueDeserializer(deserializer);
        ObjectIdValueProperty sameProperty = srcProperty.withValueDeserializer(sameDeserializer);

        assertSame(srcProperty._objectIdReader, property._objectIdReader);
        assertSame(deserializer, property._valueDeserializer);
        assertSame(deserializer, sameProperty._valueDeserializer);
        assertNull(property.getAnnotation(Test.class));
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = new JsonParser(jsonFactory);
        DeserializationContext deserializationContext = new DeserializationContext();
        Object instance = new Object();

        srcProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertTrue(deserializationContext.findObjectId(null, null, null).hasId());
        assertNull(srcProperty.setAndReturn(instance, null));
        assertThrows(UnsupportedOperationException.class, () -> srcProperty.set(instance, null));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty srcProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();

        assertThrows(UnsupportedOperationException.class, () -> srcProperty.setAndReturn(instance, value));
        assertThrows(UnsupportedOperationException.class, () -> srcProperty.set(instance, value));
    }
}