package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdResolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdGenerator objectIdGenerator;
    private ObjectIdResolver objectIdResolver;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        objectIdGenerator = mock(ObjectIdGenerator.class);
        objectIdResolver = mock(ObjectIdResolver.class);

        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.generator).thenReturn(objectIdGenerator);
        when(objectIdReader.resolver).thenReturn(objectIdResolver);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        this.objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new StdSerializer<String>(String.class) {});
    }

    @Test
    public void testNewInstance() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
        assertNull(objectIdValueProperty.getValueDeserializer());
        assertNull(objectIdValueProperty.getMember());
        assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));

        ObjectIdValueProperty clone = new ObjectIdValueProperty(objectIdValueProperty, new StdSerializer<String>(String.class) {});
        assertSame(objectIdReader, clone._objectIdReader);
        assertSame(objectIdValueProperty.getValueDeserializer(), clone.getValueDeserializer());
    }

    @Test
    public void testNullInstance() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
        assertNull(objectIdValueProperty.getValueDeserializer());
        assertNull(objectIdValueProperty.getMember());
        assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));

        ObjectIdValueProperty clone = new ObjectIdValueProperty(null, new StdSerializer<String>(String.class) {});
        assertNull(clone);
    }

    @Test
    public void testSetterMethods() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        ObjectIdValueProperty propertyWithName = objectIdValueProperty.withName(new PropertyName("name"));
        assertSame(propertyWithName.getName(), new PropertyName("name"));
        assertSame(propertyWithName._objectIdReader, objectIdReader);

        ObjectIdValueProperty propertyWithDeserializer = objectIdValueProperty.withValueDeserializer(new StdSerializer<String>(String.class) {});
        assertSame(propertyWithDeserializer.getValueDeserializer(), new StdSerializer<String>(String.class) {});
        assertSame(propertyWithDeserializer._objectIdReader, objectIdReader);
    }

    @Test
    public void testAnnotation() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));

        ObjectIdValueProperty objectIdValuePropertyAnnotated = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return super.getAnnotation(JsonProperty.class);
            }
        };
        assertSame(new JsonProperty(new PropertyName("id")), objectIdValuePropertyAnnotated.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() throws Exception {
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        when(introspector.findMember(any(BeanPropertyDefinition.class))).thenReturn(mock(AnnotatedMember.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(objectIdValueProperty.getMember());

        ObjectIdValueProperty objectIdValuePropertyAnnotated = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            @Override
            public AnnotatedMember getMember() {
                return mock(AnnotatedMember.class);
            }
        };
        assertSame(mock(AnnotatedMember.class), objectIdValuePropertyAnnotated.getMember());
    }

    @Test
    public void testGetValue() throws Exception {
        Object instance = new Object();

        when(objectIdResolver.resolve(any())).thenReturn(new ObjectId(1L));

        objectIdValueProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        ObjectId id = objectIdValueProperty.getId();
        assertSame(new ObjectId(1L), id);

        ObjectId newId = new ObjectId(2L);
        objectIdValueProperty.set(instance, newId);
        ObjectId newIdFromProperty = objectIdValueProperty.getId();
        assertSame(newId, newIdFromProperty);
    }

    @Test
    public void testSetValue() throws Exception {
        Object instance = new Object();

        ObjectId id = new ObjectId(1L);
        objectIdValueProperty.setValue(instance, id);
        ObjectId idFromProperty = objectIdValueProperty.getId();
        assertSame(id, idFromProperty);

        ObjectId newId = new ObjectId(2L);
        objectIdValueProperty.setValue(instance, newId);
        ObjectId newIdFromProperty = objectIdValueProperty.getId();
        assertSame(newId, newIdFromProperty);

        ObjectId propertyId = new ObjectId(3L);
        ObjectIdProperty idProperty = mock(ObjectIdProperty.class);
        when(idProperty.setAndReturn(instance, anyObject())).thenReturn(instance);
        when(objectIdReader.idProperty).thenReturn(idProperty);

        objectIdValueProperty.setValue(instance, propertyId);
        ObjectId newIdFromPropertyViaIdProperty = objectIdValueProperty.getId();
        assertSame(propertyId, newIdFromPropertyViaIdProperty);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object instance = new Object();

        when(objectIdResolver.resolve(any())).thenReturn(new ObjectId(1L));

        Object id = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertSame(new ObjectId(1L), id);

        Object newId = new ObjectId(2L);
        JsonParser parser = mock(JsonParser.class);
        when(parser.getText()).thenReturn("{\"id\":\"2\"}");
        Object newIdFromDeserializer = objectIdValueProperty
                .deserializeSetAndReturn(parser, mock(DeserializationContext.class), instance);
        assertSame(newId, newIdFromDeserializer);

        ObjectIdProperty idProperty = mock(ObjectIdProperty.class);
        when(idProperty.setAndReturn(instance, anyObject())).thenReturn(instance);
        when(objectIdReader.idProperty).thenReturn(idProperty);

        JsonParser parserWithIdProperty = mock(JsonParser.class);
        when(parserWithIdProperty.getText()).thenReturn("{\"id\":\"3\", \"name\":\"name\"}");
        Object newIdFromDeserializerViaIdProperty = objectIdValueProperty
                .deserializeSetAndReturn(parserWithIdProperty, mock(DeserializationContext.class), instance);
        assertSame(new ObjectId(3L), newIdFromDeserializerViaIdProperty);

        JsonParser parserWithNullId = mock(JsonParser.class);
        Object nullId = objectIdValueProperty.deserializeSetAndReturn(parserWithNullId, mock(DeserializationContext.class), instance);
        assertNull(nullId);
    }

    @Test
    public void testSerialize() throws Exception {
        Object instance = new Object();
        ObjectId id = new ObjectId(1L);
        ObjectIdProperty idProperty = mock(ObjectIdProperty.class);
        when(idProperty.getName()).thenReturn(new PropertyName("id"));
        when(idProperty.getSerializer()).thenReturn(new StdSerializer<ObjectId>(ObjectId.class) {});

        objectIdValueProperty.setValue(instance, id);
        String json = objectIdValueProperty.serialize(instance, null);
        assertEquals("1", json);

        ObjectId newId = new ObjectId(2L);
        objectIdValueProperty.setValue(instance, newId);
        json = objectIdValueProperty.serialize(instance, null);
        assertEquals("2", json);

        when(objectIdReader.idProperty).thenReturn(idProperty);

        json = objectIdValueProperty.serialize(instance, null);
        assertEquals("\"id\"", json);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();

        when(objectIdResolver.resolve(any())).thenReturn(new ObjectId(1L));

        Object newId = objectIdValueProperty.setAndReturn(instance, new ObjectId(2L));
        assertSame(newId, new ObjectId(2L));
        ObjectId idFromProperty = objectIdValueProperty.getId();
        assertSame(newId, idFromProperty);

        ObjectIdProperty idProperty = mock(ObjectIdProperty.class);
        when(idProperty.setAndReturn(instance, anyObject())).thenReturn(instance);
        when(objectIdReader.idProperty).thenReturn(idProperty);

        newId = objectIdValueProperty.setAndReturn(instance, propertyId);
        assertSame(propertyId, newId);
        ObjectId idFromPropertyViaIdProperty = objectIdValueProperty.getId();
        assertSame(propertyId, idFromPropertyViaIdProperty);
    }
}