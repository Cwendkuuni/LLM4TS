package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(Long.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(objectIdReader.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        JsonParser jp = constructParser("{\"id\": 123}");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(jp, ctxt, instance);

        assertNotNull(instance);
        Object id = ctxt.findValueHolderForId("123");
        assertNotNull(id);
        assertEquals(id.getValue(), instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(Long.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(objectIdReader.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        JsonParser jp = constructParser("{\"id\": 123}");
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);

        assertNotNull(result);
        assertEquals(instance, result);

        Object id = ctxt.findValueHolderForId("123");
        assertNotNull(id);
        assertEquals(id.getValue(), instance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(Long.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        Object id = 123L;

        Object result = property.setAndReturn(instance, id);

        assertNotNull(result);
        assertEquals(instance, result);

        ObjectNode node = objectMapper.createObjectNode();
        node.put("id", id);
        JsonParser jp = constructParser(node.toString());
        DeserializationContext ctxt = mock(DeserializationContext.class);

        property.deserializeAndSet(jp, ctxt, instance);

        assertNotNull(instance);
        Object idFromContext = ctxt.findValueHolderForId(id.toString());
        assertNotNull(idFromContext);
        assertEquals(id, idFromContext.getValue());
    }

    @Test
    public void testNullId() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(Long.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(objectIdReader.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(null, mock(DeserializationContext.class), instance);

        assertNull(result);
        assertNull(instance);
    }
}