package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test case 1
        Class<?> rawType1 = UUID.class;
        String clsName1 = rawType1.getName();
        JsonDeserializer<?> result1 = JdkDeserializers.find(rawType1, clsName1);
        assertNotNull(result1);
        assertTrue(result1 instanceof UUIDDeserializer);

        // Test case 2
        Class<?> rawType2 = StackTraceElement.class;
        String clsName2 = rawType2.getName();
        JsonDeserializer<?> result2 = JdkDeserializers.find(rawType2, clsName2);
        assertNotNull(result2);
        assertTrue(result2 instanceof StackTraceElementDeserializer);

        // Test case 3
        Class<?> rawType3 = AtomicBoolean.class;
        String clsName3 = rawType3.getName();
        JsonDeserializer<?> result3 = JdkDeserializers.find(rawType3, clsName3);
        assertNotNull(result3);
        assertTrue(result3 instanceof AtomicBooleanDeserializer);

        // Test case 4
        Class<?> rawType4 = ByteBuffer.class;
        String clsName4 = rawType4.getName();
        JsonDeserializer<?> result4 = JdkDeserializers.find(rawType4, clsName4);
        assertNotNull(result4);
        assertTrue(result4 instanceof ByteBufferDeserializer);
    }
}