package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.BaseConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdGenerator;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.test.BaseTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest extends BaseTest {

    private ObjectIdReader _objectIdReader;
    private JsonDeserializer<ObjectIdNode> _idDeserializer;
    private SettableBeanProperty _idProperty;
    private ObjectIdValueProperty _propertyUnderTest;

    @Before
    public void setup() throws Exception {
        _objectIdReader = mock(ObjectIdReader.class);
        _idDeserializer = new StdSerializer<ObjectIdNode>(ObjectIdNode.class) {};
        _idProperty = mock(SettableBeanProperty.class);

        when(_objectIdReader.getIdType()).thenReturn(String.class);
        when(_objectIdReader.propertyName).thenReturn("name");
        when(_objectIdReader.idProperty).thenReturn(_idProperty);
        when(_objectIdReader.generator).thenReturn(new ObjectIdGenerator() {
            @Override public ObjectIdNode generateId(String id) {
                return new ObjectIdNode(id);
            }
        });
        when(_objectIdReader.resolver).thenReturn(noOpResolver());

        _propertyUnderTest = new ObjectIdValueProperty(_objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(_propertyUnderTest.getAnnotation(ObjectIdAnnotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(_propertyUnderTest.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = new Object();
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(p.nextValue()).thenReturn(id);

        _propertyUnderTest.deserializeAndSet(p, ctxt, instance);

        verify(ctxt).findObjectId(eq(id), eq(_objectIdReader.generator), eq(_objectIdReader.resolver));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = new Object();
        ObjectIdNode expectedIdNode = new ObjectIdNode("id");
        Object result = new Object();

        when(_objectIdReader.idProperty).thenReturn(_idProperty);

        ObjectIdNode actualIdNode = (ObjectIdNode) _propertyUnderTest.deserializeSetAndReturn(
                mock(JsonParser.class), mock(DeserializationContext.class), instance);

        verify(idDeserializer).deserialize(eq(mock(JsonParser.class)), eq(mock(DeserializationContext.class)));
        verify(idDeserializer).getContentAsTyped(eq(ObjectIdNode.class));
        verify(_idProperty).setAndReturn(instance, eq(expectedIdNode.getTextValue()));
        assertEquals(result, actualIdNode);
    }

    @Test
    public void testSet() throws IOException {
        Object id = new Object();
        Object instance = new Object();

        _propertyUnderTest.set(instance, id);

        verify(_idProperty).setAndReturn(instance, eq(id));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = new Object();
        Object result = new Object();

        when(_idProperty.setAndReturn(anyObject(), anyObject())).thenReturn(result);

        Object actualResult = _propertyUnderTest.setAndReturn(new Object(), id);

        assertEquals(result, actualResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedSet() throws IOException {
        _propertyUnderTest.set(new Object(), null);
    }
}