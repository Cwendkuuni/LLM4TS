package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdReader;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(property);
    }

    @Test
    public void testObjectIdValuePropertyCopyConstructor() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty copy = new ObjectIdValueProperty(property, new StdSerializer<Object>(Object.class) {});
        assertEquals(property, copy);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyDeprecatedConstructor() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyName("name"));
        property.setAndReturn(null, null);
        ObjectIdValueProperty copy = new ObjectIdValueProperty(property, new StdSerializer<Object>(Object.class) {});
        fail("Should not reach here");
    }

    @Test
    public void testObjectIdValuePropertyWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty renamed = property.withName(new PropertyName("newName"));
        assertNotSame(property, renamed);
        assertEquals(renamed.getName(), "newName");
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        JsonDeserializer<Object> deserializer = new StdDeserializer<Object>(Object.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty deserialized = property.withValueDeserializer(deserializer);
        assertEquals(deserialized.getValueDeserializer(), deserializer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertyGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.getAnnotation(Mockito.mock(MockAnnotation.class));
        fail("Should not reach here");
    }

    @Test
    public void testObjectIdValuePropertyGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testObjectIdValuePropertyDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        JsonDeserializer<Object> deserializer = new StdDeserializer<Object>(Object.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = ObjectIdGenerator.instance.generateId();
        JsonParser parser = new JsonTestParser();
        Object instance = new Object();
        property.deserializeAndSet(parser, new TestContext(), instance);
        Object deserializedId = parser.getValueAsToken().getValueAsString();
        ObjectIdNode idNode = new ObjectIdNode(DeserializationTest.mkTextualValue(id.toString()));
        assertEquals(deserializedId, idNode.getValueAsString());
        // Test that ObjectIdValueProperty sets the id property
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), null, deserializer);
        idProperty.set(instance, id);
        assertEquals(id, idProperty.getValueProperty(instance).getValue());
    }

    @Test
    public void testObjectIdValuePropertyDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        JsonDeserializer<Object> deserializer = new StdDeserializer<Object>(Object.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = ObjectIdGenerator.instance.generateId();
        JsonParser parser = new JsonTestParser();
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(parser, new TestContext(), instance);
        Object deserializedId = parser.getValueAsToken().getValueAsString();
        ObjectIdNode idNode = new ObjectIdNode(DeserializationTest.mkTextualValue(id.toString()));
        assertEquals(deserializedId, idNode.getValueAsString());
        assertEquals(instance, result);
    }

    @Test
    public void testObjectIdValuePropertySet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), null, new StdSerializer<Object>(Object.class) {});
        Object id = ObjectIdGenerator.instance.generateId();
        Object instance = new Object();
        property.setAndReturn(instance, id);
        Object deserializedId = property.getValueProperty(instance).getValue();
        assertEquals(id, deserializedId);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertySetNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), null, new StdSerializer<Object>(Object.class) {});
        property.setAndReturn(null, null);
        fail("Should not reach here");
    }

    @Test
    public void testObjectIdValuePropertySetNullId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        JsonDeserializer<Object> deserializer = new StdDeserializer<Object>(Object.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        property.setAndReturn(instance, null);
        assertEquals(instance, property.getValueProperty(instance).getValue());
    }

    @Test
    public void testObjectIdValuePropertySetId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), null, new StdSerializer<Object>(Object.class) {});
        Object id = ObjectIdGenerator.instance.generateId();
        Object instance = new Object();
        property.setAndReturn(instance, id);
        assertEquals(id, property.getValueProperty(instance).getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertySetEmptyId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), null, new StdSerializer<Object>(Object.class) {});
        property.setAndReturn(new Object(), "");
        fail("Should not reach here");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdValuePropertySetLongId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), null, new StdSerializer<Object>(Object.class) {});
        property.setAndReturn(new Object(), 123L);
        fail("Should not reach here");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdValuePropertySetInvalidId() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), null, new StdSerializer<Object>(Object.class) {});
        property.setAndReturn(new Object(), new Object());
        fail("Should not reach here");
    }

    @Test
    public void testObjectIdValuePropertyValueProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        JsonDeserializer<Object> deserializer = new StdDeserializer<Object>(Object.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = ObjectIdGenerator.instance.generateId();
        Object instance = new Object();
        property.setAndReturn(instance, id);
        ObjectIdValueProperty valueProperty = property.getValueProperty(instance);
        assertEquals(property, valueProperty);
        assertEquals(id, valueProperty.getValue());
    }

    @Test
    public void testObjectIdValuePropertyValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(new ObjectIdGenerator(), new ObjectIdResolver());
        JsonDeserializer<Object> deserializer = new StdDeserializer<Object>(Object.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), null, deserializer);
        assertEquals(deserializer, property.getValueDeserializer());
    }

}

class TestContext extends DeserializationContext {
    @Override
    public ObjectIdGenerator getObjectIdGenerator() {
        return ObjectIdGenerator.instance;
    }

    @Override
    public ObjectIdReader getObjectIdReader() {
        return new ObjectIdReader(ObjectIdGenerator.instance, new ObjectIdResolver() {
            @Override
            public ObjectId idFromString(String id) {
                return ObjectIdGenerator.instance.parseId(id);
            }

            @Override
            public ObjectId idFromBytes(byte[] idBytes) {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Override
    public Converter<?> findValueConverter(Class<?> fromClass, Class<?> toClass) {
        return null;
    }
}