package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ObjectIdReferencePropertyTest {

    private ObjectMapper objectMapper;
    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        forward = Mockito.mock(SettableBeanProperty.class);
        objectIdInfo = Mockito.mock(ObjectIdInfo.class);
    }

    @Test
    public void testObjectIdReferencePropertyCreation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward, property._forward);
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertySerializationDeserialization() throws JsonProcessingException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        ObjectNode node = objectMapper.createObjectNode();
        property.serialize(node, objectMapper.findValueToSerialize(property, null));

        ObjectNode deserializedNode = objectMapper.readValue(node.toString(), ObjectNode.class);
        ObjectIdReferenceProperty deserializedProperty = objectMapper.readerForUpdating(null).readValue(deserializedNode);

        assertSame(property, deserializedProperty);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturnNull() throws JsonProcessingException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectNode node = objectMapper.createObjectNode();
        node.put("_class", "java.lang.String");

        assertNull(property.deserializeSetAndReturn(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), Mockito.mock(Object.class)));
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturnWithValue() throws JsonProcessingException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        String value = "testValue";

        ObjectNode node = objectMapper.createObjectNode();
        node.put("value", value);

        assertEquals(value, property.deserializeSetAndReturn(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), Mockito.mock(Object.class)));
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testObjectIdReferencePropertyUnresolvedForwardReference() throws JsonProcessingException {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        ObjectNode node = objectMapper.createObjectNode();

        property.deserializeSetAndReturn(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), Mockito.mock(Object.class));
    }
}