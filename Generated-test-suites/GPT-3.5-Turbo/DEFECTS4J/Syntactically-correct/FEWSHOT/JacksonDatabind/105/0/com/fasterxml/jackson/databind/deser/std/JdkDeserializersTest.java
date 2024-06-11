package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(Object.class, "java.lang.Object"));
        assertNotNull(JdkDeserializers.find(UUID.class, "java.util.UUID"));
        assertNotNull(JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean"));
        assertNotNull(JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement"));
        assertNotNull(JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer"));
    }
}