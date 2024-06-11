package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        objectMapper = new ObjectMapper(new JsonFactory());
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        objectMapper = null;
    }

    @Before
    public void setUp() throws Exception {
        objectMapper.registerModule(new TestIdModule());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("{\"id\": 123}");
        ObjectIdReader objectIdReader = new ObjectIdReader(null, new PropertyName("id"), null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        DummyObject dummyObject = new DummyObject();
        
        property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), dummyObject);
        
        assertEquals(123, dummyObject.getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("{\"id\": 456}");
        ObjectIdReader objectIdReader = new ObjectIdReader(null, new PropertyName("id"), null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        DummyObject dummyObject = new DummyObject();
        
        Object returned = property.deserializeSetAndReturn(parser, objectMapper.getDeserializationContext(), dummyObject);
        
        assertEquals(456, dummyObject.getId());
        assertSame(dummyObject, returned);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("{\"id\": 789}");
        ObjectIdReader objectIdReader = new ObjectIdReader(null, new PropertyName("id"), null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        DummyObject dummyObject = new DummyObject();
        
        Object returned = property.setAndReturn(dummyObject, 789);
        
        assertEquals(789, dummyObject.getId());
        assertSame(dummyObject, returned);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWhenIdPropertyIsNull() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser("{\"id\": 789}");
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        DummyObject dummyObject = new DummyObject();
        
        property.setAndReturn(dummyObject, 789);
    }

    private static class DummyObject {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    private static class TestIdModule extends Module {
        @Override
        public String getModuleName() {
            return "TestIdModule";
        }

        @Override
        public void setupModule(SetupContext context) {
            context.addBeanDeserializerModifier(new TestDeserializerModifier());
        }
    }

    private static class TestDeserializerModifier extends BeanDeserializerModifier {
        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config,
                BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
            if (beanDesc.getBeanClass() == DummyObject.class) {
                return new TestDeserializer(deserializer);
            }
            return deserializer;
        }
    }

    private static class TestDeserializer extends JsonDeserializer<Object> {
        private JsonDeserializer<?> deserializer;

        public TestDeserializer(JsonDeserializer<?> deserializer) {
            this.deserializer = deserializer;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Object value = deserializer.deserialize(p, ctxt);
            if (value instanceof Integer) {
                return ((Integer) value) * 2;
            }
            return value;
        }
    }

}