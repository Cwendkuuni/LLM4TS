package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

// Test find method for UUID class
@Test
public void testFindUUID() {
    JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
    assertNotNull(deserializer);
    assertTrue(deserializer instanceof UUIDDeserializer);
}

// Test find method for StackTraceElement class
@Test
public void testFindStackTraceElement() {
    JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
    assertNotNull(deserializer);
    assertTrue(deserializer instanceof StackTraceElementDeserializer);
}

// Test find method for AtomicBoolean class
@Test
public void testFindAtomicBoolean() {
    JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
    assertNotNull(deserializer);
    assertTrue(deserializer instanceof AtomicBooleanDeserializer);
}

// Test find method for ByteBuffer class
@Test
public void testFindByteBuffer() {
    JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
    assertNotNull(deserializer);
    assertTrue(deserializer instanceof ByteBufferDeserializer);
}

// Test find method for invalid class
@Test
public void testFindInvalid() {
    JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "java.lang.String");
    assertNull(deserializer);
}

}