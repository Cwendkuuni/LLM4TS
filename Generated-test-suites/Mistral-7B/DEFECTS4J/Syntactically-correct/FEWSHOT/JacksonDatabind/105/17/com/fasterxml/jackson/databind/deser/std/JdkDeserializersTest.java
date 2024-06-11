package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*; // Import all the deserializers
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFindNull() {
        assertNull(JdkDeserializers.find(null, null));
        assertNull(JdkDeserializers.find(Object.class, null));
    }

    @Test
    public void testFindPrimitive() {
        assertNull(JdkDeserializers.find(int.class, null));
        assertNull(JdkDeserializers.find(boolean.class, null));
        // Add other primitives if necessary
    }

    @Test
    public void testFindClass() {
        assertNotNull(JdkDeserializers.find(UUID.class, UUID.class.getName()));
        assertNotNull(JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()));
        assertNotNull(JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName()));
        assertNotNull(JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()));

        // Test for FromStringDeserializer
        assertNotNull(JdkDeserializers.find(String.class, "java.lang.String"));
        // Add other classes if necessary
    }

    @Test
    public void testFindClassWithWrongName() {
        assertNull(JdkDeserializers.find(UUID.class, "java.lang.String.class"));
        // Add other classes if necessary
    }

    @Test
    public void testFindList() {
        TypeReference<List<UUID>> typeRef = new TypeReference<List<UUID>>() {};
        JsonDeserializer<List<UUID>> deserializer = JdkDeserializers.find(typeRef.getType(), "java.util.List");
        assertNotNull(deserializer);
        assertNotNull(deserializer instanceof CollectionValueDeserializer);
    }

    @Test
    public void testFindMap() {
        TypeReference<Map<String, UUID>> typeRef = new TypeReference<Map<String, UUID>>() {};
        JsonDeserializer<Map<String, UUID>> deserializer = JdkDeserializers.find(typeRef.getType(), "java.util.Map");
        assertNotNull(deserializer);
        assertNotNull(deserializer instanceof MapDeserializer);
    }
}