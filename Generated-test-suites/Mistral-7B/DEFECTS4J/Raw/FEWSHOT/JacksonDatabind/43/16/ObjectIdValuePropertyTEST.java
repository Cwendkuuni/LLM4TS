package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtil.convertObjectIdToValue;
import static com.fasterxml.jackson.core.util.TestUtil.createObjectIdReader;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectIdNode;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReader(ObjectIdReader.Default.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        JsonParser p = createParser();
        Object instance = new Object();

        ObjectIdNode idNode = p.readValueAs(ObjectIdNode.class);
        property.deserializeAndSet(p, new TestContext(), instance);
        assertEquals(convertObjectIdToValue(idNode), property.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReader(ObjectIdReader.Default.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        JsonParser p = createParser();
        Object instance = new Object();
        Object id = property.deserializeSetAndReturn(p, new TestContext(), instance);
        assertEquals(convertObjectIdToValue(p.getValueAsToken()), id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = createObjectIdReader(ObjectIdReader.Default.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        Object id = new Object();

        Object newId = property.setAndReturn(instance, id);
        assertSame(newId, id);
        assertEquals(id, property.get(instance));
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        assertNull(property.getMember());
    }

    private JsonParser createParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{}");
        return p;
    }

    private static class TestContext extends DeserializationContext {
        private static final long serialVersionUID = 1L;

        @Override
        public ObjectIdReader getObjectIdReader(DeserializerProvider dp) {
            return null;
        }

        @Override
        public TypeDeserializer getObjectIdValueDeserializer(DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer valueDeserializer) {
            return null;
        }

        @Override
        public ObjectMapper getConfig() {
            return null;
        }
    }
}