package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(property);
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty originalProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty copiedProperty = new ObjectIdValueProperty(originalProperty, new JsonDeserializer<>());
        assertEquals(originalProperty, copiedProperty);
    }

    @Test
    public void testObjectIdValuePropertyNameChange() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty renamedProperty = property.withName(new PropertyName("newPropertyName"));
        assertNotSame(property, renamedProperty);
        assertEquals("newPropertyName", renamedProperty.getName().asString());
    }

    @Test
    public void testObjectIdValuePropertyValueDeserializerChange() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<?> deserializer = new StdDeserializer<>();
        ObjectIdValueProperty renamedProperty = property.withValueDeserializer(deserializer);
        assertNotSame(property, renamedProperty);
        assertEquals(deserializer, renamedProperty.getValueDeserializer());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertySetNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        property.set(instance, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertySetNullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            @Override
            public SettableBeanProperty idProperty;

            @Override
            public SettableBeanProperty setAndReturn(Object instance, Object value) throws IOException {
                return this;
            }
        };
        Object instance = new Object();
        property.set(instance, idProperty);
        property.set(instance, null);
    }

    @Test
    public void testObjectIdValuePropertyDeserializeAndSet() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectParser parser = new ObjectMapper().treeParser();
        ObjectNode node = parser.readValueAsTree(
                "{ \"idPropertyName\": 123 }");
        Object instance = new Object();
        property.deserializeAndSet(parser.getRawParser(), new TestContext(), instance);
        ObjectIdNode idNode = (ObjectIdNode) node.get("idPropertyName");
        assertEquals(idNode.getValue().asText(), property.getValue().toString());
    }

    @Test
    public void testObjectIdValuePropertyDeserializeSetAndReturnNullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        assertEquals(instance, property.deserializeSetAndReturn(null, new TestContext(), instance));
    }

    @Test
    public void testObjectIdValuePropertyDeserializeSetAndReturnId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdReader idReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(idReader, new PropertyMetadata());
        Object instance = new Object();
        ObjectIdNode idNode = ObjectIdNode.valueOf("123");
        JsonParser parser = new TestParser(idNode.getValue().asText());
        assertEquals(idProperty.getValue(), property.deserializeSetAndReturn(parser, new TestContext(), instance));
    }
}

class TestContext extends DeserializationContext {}

class TestParser extends JsonParser {
    private final String id;

    public TestParser(String id) {
        this.id = id;
    }

    @Override
    public ObjectIdReader getObjectIdReader() {
        return new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
    }

    @Override
    public DeserializationContext getContext() {
        return new TestContext();
    }

    @Override
    public ObjectIdReader _findObjectIdReader(DeserializationContext ctxt, String name) {
        return new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING, null, null);
    }

    @Override
    public Object readValueAs(Class<?> valueType) throws IOException {
        return null;
    }

    @Override
    public ObjectIdNode getValueAsObjectId() throws IOException {
        return ObjectIdNode.valueOf(id);
    }
}