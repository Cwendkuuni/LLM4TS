package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

    private static ObjectIdReader mockObjectIdReader() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        return objectIdReader;
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jp = constructParser("\"123\"");
        Object instance = new Object();
        property.deserializeAndSet(jp, new DeserializationContext(), instance);

        assertNull(instance); // Id should be set on instance, not returned

        ObjectId id = mock(ReadableObjectId.class);
        when(id.getIdValue()).thenReturn("123");
        when(objectIdReader.findObjectId(anyObject(), anyObject(), anyObject()))
                .thenReturn(id);

        property.deserializeAndSet(jp, new DeserializationContext(), instance);

        assertEquals("123", instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jp = constructParser("\"123\"");
        Object instance = new Object();
        Object id = property.deserializeSetAndReturn(jp, new DeserializationContext(), instance);

        assertEquals("123", id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();

        Object id = property.setAndReturn(instance, "123");

        assertEquals("123", id);
    }

    @Test
    public void testAnnotations() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jp = mock(JsonParser.class);
        Object instance = mock(Object.class);

        Annotation annotation = mock(JsonId.class);
        when(annotation.annotationType()).thenReturn(JsonId.class);
        when(annotation.value()).thenReturn("id");

        when(instance.getClass().getDeclaredField("id")).thenReturn(new AnnotatedMember(new BeanPropertyDefinition(new PropertyName("id"), JsonType.construct(String.class), new NameTransformer(), null, null, null, null, null, null, null, null, null), annotation, null, null, null));

        property.deserializeAndSet(jp, new DeserializationContext(), instance);

        assertEquals(annotation, property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        assertEquals(null, property.getMember());
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);

        assertEquals(deserializer, newProperty.getValueDeserializer());
    }
}

class ObjectIdGenerator {
    // empty
}

class ObjectIdResolver {
    // empty
}