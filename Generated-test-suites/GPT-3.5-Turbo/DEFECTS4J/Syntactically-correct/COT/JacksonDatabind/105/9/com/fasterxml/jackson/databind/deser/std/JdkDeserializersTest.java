
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;
import java.util.HashSet;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test case for UUID class
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assert(deserializer != null);

        // Test case for AtomicBoolean class
        rawType = AtomicBoolean.class;
        clsName = AtomicBoolean.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assert(deserializer != null);

        // Test case for StackTraceElement class
        rawType = StackTraceElement.class;
        clsName = StackTraceElement.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assert(deserializer != null);

        // Test case for ByteBuffer class
        rawType = ByteBuffer.class;
        clsName = ByteBuffer.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assert(deserializer != null);
    }
}
