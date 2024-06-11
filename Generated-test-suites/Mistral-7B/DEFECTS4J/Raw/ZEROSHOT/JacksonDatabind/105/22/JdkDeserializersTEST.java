package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Test class for JdkDeserializers.
 */
public class JdkDeserializersTest {

    @org.junit.Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test null input
        assertNull(JdkDeserializers.find(null, null));

        // Test invalid className
        assertNull(JdkDeserializers.find(Object.class, "java.lang.String")); ;

        // Test valid deserializer returned by FromStringDeserializer
        JsonDeserializer<UUID> deserializer = mapper.deserializationConfig()
                .findValueDeserializer(new TypeReference<UUID>() {});
        assertEquals(FromStringDeserializer.class, deserializer.getClass());
        assertNull(JdkDeserializers.find(UUID.class, FromStringDeserializer.class.getName()));

        // Test valid deserializers
        JsonDeserializer<?> uuidDeserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertEquals(UUIDDeserializer.class, uuidDeserializer.getClass());

        JsonDeserializer<?> stackTraceElementDeserializer = JdkDeserializers.find(StackTraceElement[].class,
                StackTraceElement.class.getName());
        assertEquals(StackTraceElementDeserializer.class, stackTraceElementDeserializer.getClass());

        JsonDeserializer<?> atomicBooleanDeserializer = JdkDeserializers.find(AtomicBoolean.class,
                AtomicBoolean.class.getName());
        assertEquals(AtomicBooleanDeserializer.class, atomicBooleanDeserializer.getClass());

        JsonDeserializer<?> byteBufferDeserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertEquals(ByteBufferDeserializer.class, byteBufferDeserializer.getClass());
    }
}