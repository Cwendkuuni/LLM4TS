package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(null, null));
        assertNull(JdkDeserializers.find(null, ""));
        assertNull(JdkDeserializers.find(String.class, ""));
        assertNull(JdkDeserializers.find(String.class, "InvalidClassName"));
        assertNull(JdkDeserializers.find(Integer.class, "java.util.UUID"));
        assertNull(JdkDeserializers.find(Character.class, "java.util.UUID"));
        assertNull(JdkDeserializers.find(Byte.class, "java.util.UUID"));
        
        assertNotNull(JdkDeserializers.find(UUID.class, "java.util.UUID"));
        assertNotNull(JdkDeserializers.find(AtomicBoolean.class, "java.util.AtomicBoolean"));
        assertNotNull(JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement"));
        assertNotNull(JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer"));
    }
}