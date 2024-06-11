package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseMapTest;

import org.junit.Test;

public class CreatorPropertyTest extends BaseMapTest {

    // Test helper class for simulation of CreatorProperty with mocked behavior
    static class TestCreatorProperty extends CreatorProperty {
        TestCreatorProperty(String name, JavaType type, int index, Object injectableValueId) {
            super(name, type, null, null, null, null, index, injectableValueId, null);
        }

        @Override
        public void setFallbackSetter(SettableBeanProperty fallbackSetter) {
            super.setFallbackSetter(fallbackSetter);
        }

        @Override
        public Object findInjectableValue(DeserializationContext context, Object beanInstance)
                throws JsonMappingException {
            return "injected value";
        }

        @Override
        public void inject(DeserializationContext context, Object beanInstance)
                throws IOException {
            super.set(beanInstance, "injected value");
        }
    }

    @Test
    public void testBasicFunctionality() throws IOException, JsonProcessingException {
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructValueType(String.class);
        CreatorProperty cp = new TestCreatorProperty("testProperty", type, 0, null);
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        String json = "{\"testProperty\": \"testValue\"}";
        Object beanInstance = new Object();

        mapper.readValue(new ByteArrayInputStream(json.getBytes()), CreatorProperty.class, beanInstance);

        assertEquals("testValue", cp.get(beanInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingSetter() throws IOException {
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructValueType(String.class);
        CreatorProperty cp = new TestCreatorProperty("testProperty", type, 0, null);

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        String json = "{\"testProperty\": \"testValue\"}";

        mapper.readValue(new ByteArrayInputStream(json.getBytes()), CreatorProperty.class, null);
    }

    @Test
    public void testInjectValue() throws IOException, JsonProcessingException {
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructValueType(String.class);
        CreatorProperty cp = new TestCreatorProperty("testProperty", type, 0, "injectValueId");
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object beanInstance = new Object();

        mapper.readValue(new ByteArrayInputStream("{\"testProperty\": 1}".getBytes()), CreatorProperty.class, beanInstance);
        cp.inject(mapper.getDeserializationContext(), beanInstance);

        assertEquals("injected value", cp.get(beanInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingInjectableValueId() throws IOException {
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructValueType(String.class);
        CreatorProperty cp = new TestCreatorProperty("testProperty", type, 0, null);

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object beanInstance = new Object();

        mapper.readValue(new ByteArrayInputStream("{}".getBytes()), CreatorProperty.class, beanInstance);
    }

    @Test
    public void testEdgeCases() throws IOException, JsonProcessingException {
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructValueType(String.class);
        CreatorProperty cp = new TestCreatorProperty("testProperty", type, 0, null);
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object beanInstance = new Object();

        mapper.readValue(new ByteArrayInputStream("{\"testProperty\": null}".getBytes()), CreatorProperty.class, beanInstance);
        assertEquals(null, cp.get(beanInstance));

        mapper.readValue(new ByteArrayInputStream("{\"testProperty\": 123}".getBytes()), CreatorProperty.class, beanInstance);
        assertEquals(123, cp.get(beanInstance));

        mapper.readValue(new ByteArrayInputStream("{\"testProperty\": \"testValue\"}".getBytes()), CreatorProperty.class, beanInstance);
        assertEquals("testValue", cp.get(beanInstance));

        mapper.readValue(new ByteArrayInputStream("{\"testProperty\": []}".getBytes()), CreatorProperty.class, beanInstance);
        assertNotNull(cp.get(beanInstance));
        assertTrue(cp.get(beanInstance) instanceof Object[]);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidInjectValueId() throws IOException {
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructValueType(String.class);
        CreatorProperty cp = new TestCreatorProperty("testProperty", type, 0, "invalidInjectValueId");

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object beanInstance = new Object();

        mapper.readValue(new ByteArrayInputStream("{\"testProperty\": 1}".getBytes()), CreatorProperty.class, beanInstance);
    }

    @Test
    public void testExceptionHandling() throws IOException, JsonProcessingException {
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructValueType(String.class);
        CreatorProperty cp = new TestCreatorProperty("testProperty", type, 0, null);
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        Object beanInstance = new Object();

        // Test null pointer exception
        mapper.readValue(null, CreatorProperty.class, beanInstance);
        assertNull(cp.get(beanInstance));

        // Test IllegalArgumentException
        JavaType typeWithInvalidAnnotations = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructValueType(String.class, new TypeDeserializer() {
                    @Override
                    public Object deserializeAtEmpty(JsonParser p, DeserializationContext ctxt)
                            throws IOException, JsonProcessingException {
                        throw new IllegalArgumentException("Test IllegalArgumentException");
                    }
                });
        CreatorProperty cpWithInvalidAnnotations = new TestCreatorProperty("testProperty", typeWithInvalidAnnotations, 0, null);
        mapper.readValue(new ByteArrayInputStream("{\"testProperty\": \"testValue\"}".getBytes()), CreatorProperty.class, beanInstance);
        assertNull(cpWithInvalidAnnotations.get(beanInstance));
    }
}