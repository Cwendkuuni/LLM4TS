package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        JsonDeserializer<?> deserializer1 = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertNotNull(deserializer1);
        
        JsonDeserializer<?> deserializer2 = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertNotNull(deserializer2);
        
        JsonDeserializer<?> deserializer3 = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        assertNotNull(deserializer3);
        
        JsonDeserializer<?> deserializer4 = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        assertNotNull(deserializer4);
        
        JsonDeserializer<?> deserializer5 = JdkDeserializers.find(HashMap.class, "java.util.HashMap");
        assertNull(deserializer5);
    }
}