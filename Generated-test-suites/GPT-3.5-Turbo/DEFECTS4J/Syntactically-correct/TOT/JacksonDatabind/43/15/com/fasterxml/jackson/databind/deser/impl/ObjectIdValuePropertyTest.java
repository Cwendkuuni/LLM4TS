
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void withName() {
        ObjectIdInfo info = new ObjectIdInfo(PropertyName.construct("object-name"), null, null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, info, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        PropertyName newName = PropertyName.construct("new-object-name");
        property = property.withName(newName);
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void withValueDeserializer() {
        ObjectIdInfo info = new ObjectIdInfo(PropertyName.construct("object-name"), null, null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, info, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonDeserializer<?> deserializer = new StdDeserializer<Object>(Object.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonParseException {
                return null;
            }
        };
        property = property.withValueDeserializer(deserializer);
        assertSame(deserializer, property.getValueDeserializer());
    }

    @Test
    public void getAnnotation() {
        ObjectIdInfo info = new ObjectIdInfo(PropertyName.construct("object-name"), null, null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, info, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(property.getAnnotation(Test.class));
    }

    @Test
    public void getMember() {
        ObjectIdInfo info = new ObjectIdInfo(PropertyName.construct("object-name"), null, null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, info, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(property.getMember());
    }

    @Test
    public void deserializeAndSet() throws IOException {
        ObjectMapper objectMapper = JsonMapper.builder().build();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("id", 1);
        node.put("name", "John Doe");

        Object instance = new Object();
        ObjectIdInfo info = new ObjectIdInfo(PropertyName.construct("object-name"), null, null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, info, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = objectMapper.treeAsTokens(node);
        DeserializationContext context = objectMapper.getDeserializationContext();
        property.deserializeAndSet(parser, context, instance);

        assertNull(property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void set() throws IOException {
        ObjectMapper objectMapper = JsonMapper.builder().build();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("id", 1);
        node.put("name", "John Doe");

        Object instance = new Object();
        ObjectIdInfo info = new ObjectIdInfo(PropertyName.construct("object-name"), null, null, null);
        ObjectIdReader objectIdReader = new ObjectIdReader(null, info, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = objectMapper.treeAsTokens(node);
        DeserializationContext context = objectMapper.getDeserializationContext();

        try {
            property.set(instance, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }
}
