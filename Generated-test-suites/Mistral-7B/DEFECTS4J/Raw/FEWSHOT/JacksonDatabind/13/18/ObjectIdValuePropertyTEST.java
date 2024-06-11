package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtil.createParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.BasicAsStringDeserializer;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.STRING);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(new ObjectIdValueProperty.Std(null, null, null, null));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        JsonParser jp = createParser("\"foo\"");
        Object instance = new Object();

        property.deserializeAndSet(jp, new TestContext(), instance);

        ObjectIdNode idNode = (ObjectIdNode) jp.getValueAsToken();
        assertEquals("foo", idNode.getValue());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.INT);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(any(JsonParser.class), eq(new TestContext()))).thenReturn(123);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, valueDeserializer);

        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(createParser("123"), new TestContext(), instance);

        assertEquals(123, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.LONG);
        when(objectIdReader.idProperty).thenReturn(new ObjectIdValueProperty.Std(null, null, null, new BasicAsStringDeserializer.FromLongDeserializer()));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        Object id = 123;

        Object result = property.setAndReturn(instance, id);

        assertEquals(id, result);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        assertNull(property.getAnnotation(ObjectIdGenerator.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);

        assertNull(property.getMember());
    }
}