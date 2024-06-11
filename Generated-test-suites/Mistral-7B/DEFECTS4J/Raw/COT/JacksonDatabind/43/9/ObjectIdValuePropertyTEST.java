package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(property);
        assertEquals("idName", property.getName());
        assertSame(objectIdReader.getIdType(), property.getType());
        assertNull(property.getAnnotation(JsonProperty.class));
        assertNull(property.getMember());
        assertSame(objectIdReader.getDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty copiedProperty = new ObjectIdValueProperty(property, new JsonDeserializer<Object>() {});
        assertNotNull(copiedProperty);
        assertEquals(property.getName(), copiedProperty.getName());
        assertEquals(property.getType(), copiedProperty.getType());
        assertSame(property._objectIdReader, copiedProperty._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
        assertNotSame(property, newProperty);
        assertEquals("newName", newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertSame(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyWithValueDeserializerNull() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.withValueDeserializer(null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser parser = createParser("<id>1</id>");
        Object instance = new Object();
        property.deserializeAndSet(parser, new DeserializationContext(), instance);
        ObjectIdNode idNode = (ObjectIdNode) parser.readValueAsTree().path("id");
        Object id = idNode.getValue();
        Object expectedId = new ObjectIdReader.ObjectIdReaderContext(null, null, null, null).idFromString("1");
        assertEquals(expectedId, id);
        assertSame(instance, parser.readValueAsTree().path("instance"));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser parser = createParser("<id>null</id>");
        Object instance = new Object();
        property.deserializeAndSet(parser, new DeserializationContext(), instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullInstance() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.set(null, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullValue() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        property.set(instance, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNotNullInstanceNullValue() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        property.set(instance, new Object());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object expectedId = new ObjectIdReader.ObjectIdReaderContext(null, null, null, null).idFromString("123");
        Object id = property.setAndReturn(instance, expectedId).getValue();
        assertEquals(expectedId, id);
        assertEquals(instance, property.getMember().get(instance));
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader("idName", ObjectIdReader.IdType.INT, new StdSerializer<Object>(Object.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    private JsonParser createParser(String json) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(json));
    }
}