package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class JdkDeserializersTest {

    @org.junit.Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(UUIDDeserializer.class, deserializer);

        rawType = AtomicBoolean.class;
        clsName = AtomicBoolean.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);

        rawType = StackTraceElement.class;
        clsName = StackTraceElement.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, deserializer);

        rawType = ByteBuffer.class;
        clsName = ByteBuffer.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertInstanceOf(ByteBufferDeserializer.class, deserializer);

        rawType = String.class; // invalid type
        clsName = "InvalidType"; // invalid class name
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testFindWithFromStringDeserializer() {
        Class<?> rawType = Integer.class;
        String clsName = Integer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);

        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> deserializerFromString = mapper.findValueDeserializer(new TypeReference<Integer>() {});
        assertNotNull(deserializerFromString);
        assertInstanceOf(FromStringDeserializer.class, deserializerFromString.getClass());

        deserializer = JdkDeserializers.find(rawType, FromStringDeserializer.class.getName());
        assertNotNull(deserializer);
        assertSame(deserializerFromString, deserializer);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindWithNullRawType() {
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(null, clsName);
        fail("Expected IllegalArgumentException");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindWithNullClassName() {
        Class<?> rawType = UUID.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, null);
        fail("Expected IllegalArgumentException");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFindWithUnknownClassName() {
        Class<?> rawType = UUID.class;
        String clsName = "UnknownType";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        fail("Expected IllegalArgumentException");
    }
}