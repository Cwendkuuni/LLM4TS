package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        JdkDeserializers jdkDeserializers = new JdkDeserializers();

        // test for UUID class
        String clsName1 = "java.util.UUID";
        Class<?> rawType1 = UUID.class;
        assertNotNull(jdkDeserializers.find(rawType1, clsName1));

        // test for AtomicBoolean class
        String clsName2 = "java.util.concurrent.atomic.AtomicBoolean";
        Class<?> rawType2 = AtomicBoolean.class;
        assertNotNull(jdkDeserializers.find(rawType2, clsName2));

        // test for StackTraceElement class
        String clsName3 = "java.lang.StackTraceElement";
        Class<?> rawType3 = StackTraceElement.class;
        assertNotNull(jdkDeserializers.find(rawType3, clsName3));

        // test for ByteBuffer class
        String clsName4 = "java.nio.ByteBuffer";
        Class<?> rawType4 = ByteBuffer.class;
        assertNotNull(jdkDeserializers.find(rawType4, clsName4));

        // test for JSON class not present in the class list
        String clsName5 = "com.fasterxml.jackson.databind.JsonNode";
        Class<?> rawType5 = ByteBuffer.class;
        assertNull(jdkDeserializers.find(rawType5, clsName5));
    }
}