package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;
    private ObjectIdReader objectIdReader;
    private JsonDeserializer<Object> valueDeserializer;
    private JsonDeserializer<Object> idDeserializer;
    private NullValueProvider nullValueProvider;

    @Mock
    private AnnotatedMember annotatedMember;
    @Mock
    private JsonParser jsonParser;
    @Mock
    private DeserializationContext deserializationContext;
    @Mock
    private Object instance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn("objectId");
        when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));

        valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("value");

        idDeserializer = mock(JsonDeserializer.class);
        when(idDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("id");

        nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return null;
            }
        };

        property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer) {
            @Override
            public AnnotatedMember getMember() {
                return annotatedMember;
            }
        };
        property = new ObjectIdValueProperty(property, idDeserializer, nullValueProvider);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        when(deserializationContext.findObjectId(anyString(), eq(objectIdReader.generator),
                eq(objectIdReader.resolver))).thenReturn(mock(ReadableObjectId.class));

        property.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(instance).setId("id");
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        when(deserializationContext.findObjectId(anyString(), eq(objectIdReader.generator),
                eq(objectIdReader.resolver))).thenReturn(mock(ReadableObjectId.class));

        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertSame("id", result);
    }

    @Test
    public void testSet() throws Exception {
        when(deserializationContext.findObjectId(anyString(), eq(objectIdReader.generator),
                eq(objectIdReader.resolver))).thenReturn(mock(ReadableObjectId.class));

        property.set(instance, "id");

        verify(instance).setId("id");
    }

    @Test
    public void testSetAndReturn() throws Exception {
        when(deserializationContext.findObjectId(anyString(), eq(objectIdReader.generator),
                eq(objectIdReader.resolver))).thenReturn(mock(ReadableObjectId.class));

        Object result = property.setAndReturn(instance, "id");

        assertSame("id", result);
    }

    @Test
    public void testWithName() throws Exception {
        String newName = "newName";
        SettableBeanProperty result = property.withName(PropertyName.construct(newName));

        assertSame(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);
        SettableBeanProperty result = property.withValueDeserializer(newValueDeserializer);

        assertSame(newValueDeserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() throws Exception {
        NullValueProvider newNullValueProvider = mock(NullValueProvider.class);
        SettableBeanProperty result = property.withNullProvider(newNullValueProvider);

        assertSame(newNullValueProvider, result._nullProvider);
    }
}