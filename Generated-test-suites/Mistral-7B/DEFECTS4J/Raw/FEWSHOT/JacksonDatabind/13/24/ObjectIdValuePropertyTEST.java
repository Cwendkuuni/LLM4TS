package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.test.BaseMapTest;

public class ObjectIdValuePropertyTest extends BaseMapTest {

    private ObjectIdReader _objectIdReader;
    private ObjectIdValueProperty _property;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        _objectIdReader = mock(ObjectIdReader.class);
        when(_objectIdReader.getIdType()).thenReturn(ObjectIdReader.ObjectIdType.STRING);
        _objectIdReader.generator = mock(ObjectGenerator.class);
        _objectIdReader.resolver = mock(ObjectIdResolver.class);
        _property = new ObjectIdValueProperty(_objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(_property.getAnnotation(JsonProperty.class));
        assertNull(_property.getAnnotation(JsonDeserialize.class));
        assertNull(_property.getAnnotation(JsonValue.class));
    }

    @Test
    public void testGetMember() {
        assertNull(_property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = "idValue";
        JsonParser jp = mock(JsonParser.class);
        Object instance = new Object();
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(_objectIdReader.getIdType()).thenReturn(ObjectIdReader.ObjectIdType.STRING);
        when(_objectIdReader.idProperty).thenReturn(new StdSerializer<String>(String.class) {}.withContext(ctxt));

        when(jp.getValueAsString()).thenReturn(id);
        _property.deserializeAndSet(jp, ctxt, instance);

        ObjectIdReader idReader = ctxt.findObjectIdReader(_property);
        assertSame(_objectIdReader, idReader);
        assertSame(id, idReader.bindItem(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = "idValue";
        JsonParser jp = mock(JsonParser.class);
        Object instance = new Object();
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(_objectIdReader.getIdType()).thenReturn(ObjectIdReader.ObjectIdType.STRING);
        when(_objectIdReader.idProperty).thenReturn(new StdSerializer<String>(String.class) {}.withContext(ctxt));

        when(jp.getValueAsString()).thenReturn(id);
        Object result = _property.deserializeSetAndReturn(jp, ctxt, instance);
        assertSame(instance, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = "idValue";
        Object instance = new Object();

        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);

        when(_objectIdReader.idProperty).thenReturn(idProp);
        Object result = _property.setAndReturn(instance, id);
        assertSame(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        Object id = "idValue";
        Object instance = new Object();
        _property.set(instance, id);
    }
}