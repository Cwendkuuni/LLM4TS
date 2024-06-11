package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("typeid", "com.test.ChildClass");
        node.put("value", "testValue");

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                JavaType.construct(ChildClass.class),
                new SimpleTypeIdResolver(),
                "_class",
                true,
                ChildClassImpl.class
        );

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node.toString()), new TestContext());
        assertNotNull(result);
        assertInstanceOf(ChildClassImpl.class, result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("typeid", "com.test.ChildClass");
        node.put("value", "testValue");

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                JavaType.construct(ChildClass[].class),
                new SimpleTypeIdResolver(),
                "_class",
                true,
                ChildClassImpl[].class
        );

        Object result = deserializer.deserializeTypedFromArray(objectMapper.getFactory().createParser(node.toString()), new TestContext());
        assertNotNull(result);
        assertArrayEquals(new ChildClassImpl[]{new ChildClassImpl()}, (ChildClassImpl[]) result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("typeid", "com.test.ChildClass");
        node.put("value", "testValue");

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                JavaType.construct(String.class),
                new SimpleTypeIdResolver(),
                "_class",
                true,
                String.class
        );

        Object result = deserializer.deserializeTypedFromScalar(objectMapper.getFactory().createParser(node.toString()), new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("typeid", "com.test.ChildClass");
        node.put("value", "testValue");

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                JavaType.construct(Object.class),
                new SimpleTypeIdResolver(),
                "_class",
                true,
                Object.class
        );

        Object result = deserializer.deserializeTypedFromAny(objectMapper.getFactory().createParser(node.toString()), new TestContext());
        assertInstanceOf(Object.class, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("typeid", "com.test.ChildClass");

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                JavaType.construct(ChildClass.class),
                new SimpleTypeIdResolver(),
                "_class",
                true,
                ChildClassImpl.class
        );

        Object result = deserializer._deserializeWithNativeTypeId(objectMapper.getFactory().createParser(node.toString()), new TestContext(), "com.test.ChildClass");
        assertNotNull(result);
        assertInstanceOf(ChildClassImpl.class, result);
    }
}

class TestContext extends DeserializationContext {
    @Override
    public BeanDescription getContextDescription() {
        return null;
    }

    @Override
    public DeserializationConfig getConfig() {
        return null;
    }
}

class ChildClass {
}

class ChildClassImpl {
}