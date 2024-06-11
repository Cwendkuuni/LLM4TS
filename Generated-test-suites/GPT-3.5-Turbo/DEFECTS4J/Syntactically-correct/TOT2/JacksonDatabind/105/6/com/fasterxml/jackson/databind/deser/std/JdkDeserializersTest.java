package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use case
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);

        // Edge case - class name not in _classNames
        deserializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(deserializer);

        // Edge case - class name in _classNames but no matching deserializer
        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNull(deserializer);

        // Error scenario - rawType not handled
        deserializer = JdkDeserializers.find(String.class, StackTraceElement.class.getName());
        assertNull(deserializer);
        
        // Error scenario - clsName not in _classNames
        deserializer = JdkDeserializers.find(UUID.class, String.class.getName());
        assertNull(deserializer);
    }
}