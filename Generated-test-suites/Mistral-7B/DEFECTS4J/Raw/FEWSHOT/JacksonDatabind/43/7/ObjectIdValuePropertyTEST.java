package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
    private AnnotatedMember annotatedMember = mock(AnnotatedMember.class);

    @SuppressWarnings("unchecked")
    @Test
    public void testConstruction() {
        ObjectIdType idType = mock(ObjectIdType.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, idType, new PropertyMetadata(), deserializer);
        assertSame(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty result = new ObjectIdValueProperty(src, newName);
        assertSame(src, result.getBackingField());
        assertSame(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty result = new ObjectIdValueProperty(src, deserializer);
        assertSame(src, result.getBackingField());
        assertSame(deserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNameAndValueDeserializer() {
        ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
        PropertyName newName = mock(PropertyName.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty result = new ObjectIdValueProperty(src, newName, deserializer);
        assertSame(src, result.getBackingField());
        assertSame(newName, result.getName());
        assertSame(deserializer, result.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        when(annotatedMember.getAnnotation(JsonId.class)).thenReturn(new JsonId());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return (A) annotatedMember.getAnnotation(acls);
            }
        };
        JsonId jsonId = new JsonId();
        assertSame(jsonId, property.getAnnotation(JsonId.class));
        assertNull(property.getAnnotation(NullAware.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            @Override
            public AnnotatedMember getMember() {
                return annotatedMember;
            }
        };
        assertSame(annotatedMember, property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = "idValue";
        Object instance = new Object();
        StdSerializer<Object> valueDeserializer = mock(StdSerializer.class);
        when(objectIdReader.idType()).thenReturn(ObjectIdType.INT_ID);
        when(objectIdReader.generator()).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver()).thenReturn(mock(ObjectIdResolver.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                assertEquals("idValue", id);
                super.deserializeAndSet(p, ctxt, instance);
            }
        };
        when(valueDeserializer.deserialize(eq(p), eq(ctxt))).thenReturn(id);
        property.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertSame(instance, property.getBackingInstance());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = "idValue";
        Object instance = new Object();
        StdSerializer<Object> valueDeserializer = mock(StdSerializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer) {
            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                assertEquals("idValue", id);
                return id;
            }
        };
        when(valueDeserializer.deserialize(eq(p), eq(ctxt))).thenReturn(id);
        assertEquals(id, property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance));
    }

    @Test
    public void testSet() throws IOException {
        Object id = "idValue";
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            @Override
            public void set(Object instance, Object value) throws IOException {
                assertEquals(instance, instance);
                assertEquals("idValue", value);
                super.set(instance, value);
            }
        };
        property.set(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNoIdPropertyShouldFail() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.set(mock(Object.class), mock(Object.class));
    }
}