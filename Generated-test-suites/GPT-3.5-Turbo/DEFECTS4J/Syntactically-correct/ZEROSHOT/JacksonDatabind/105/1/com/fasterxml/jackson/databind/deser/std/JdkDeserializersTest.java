
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test UUID class
        JsonDeserializer<?> uuidDeserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        Assert.assertNotNull(uuidDeserializer);
        Assert.assertTrue(uuidDeserializer instanceof UUIDDeserializer);

        // Test StackTraceElement class
        JsonDeserializer<?> stackTraceElementDeserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        Assert.assertNotNull(stackTraceElementDeserializer);
        Assert.assertTrue(stackTraceElementDeserializer instanceof StackTraceElementDeserializer);

        // Test AtomicBoolean class
        JsonDeserializer<?> atomicBooleanDeserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        Assert.assertNotNull(atomicBooleanDeserializer);
        Assert.assertTrue(atomicBooleanDeserializer instanceof AtomicBooleanDeserializer);

        // Test ByteBuffer class
        JsonDeserializer<?> byteBufferDeserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        Assert.assertNotNull(byteBufferDeserializer);
        Assert.assertTrue(byteBufferDeserializer instanceof ByteBufferDeserializer);

        // Test other classes
        JsonDeserializer<?> otherDeserializer = JdkDeserializers.find(ArrayList.class, ArrayList.class.getName());
        Assert.assertNull(otherDeserializer);
        
        otherDeserializer = JdkDeserializers.find(String.class, String.class.getName());
        Assert.assertNull(otherDeserializer);
    }
}
