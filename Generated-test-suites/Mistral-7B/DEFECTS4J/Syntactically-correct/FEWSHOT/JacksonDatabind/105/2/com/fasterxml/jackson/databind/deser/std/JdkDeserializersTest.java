package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);
    }

    @Test
    public void testFind_Null() {
        Class<?> rawType = String.class;
        String clsName = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_UnknownClass() {
        Class<?> rawType = Object.class;
        String clsName = "com.example.MyUnknownClass".intern();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_FromStringDeserializer() {
        Class<?> rawType = Integer.class;
        String clsName = FromStringDeserializer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(FromStringDeserializer.class, deserializer);
    }

    @Test
    public void testFind_CustomClass() {
        Class<?> rawType = CustomClass.class;
        String clsName = CustomClass.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_ArrayType() {
        Class<?> rawType = int[].class;
        String clsName = "[I".intern();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFind_ObjectWithNoPublicDefaultConstructor() throws JsonProcessingException {
        class TestObject implements Serializable {
            private static final long serialVersionUID = 1L;
            private final String name;

            public TestObject(String name) { this.name = name; }

            // no default constructor
        }

        Class<?> rawType = TestObject.class;
        String clsName = TestObject.class.getName();
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("name", "Test");
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

}