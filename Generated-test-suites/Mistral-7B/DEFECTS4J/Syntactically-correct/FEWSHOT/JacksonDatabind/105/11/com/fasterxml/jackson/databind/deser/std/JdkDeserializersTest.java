package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        assertNotNull(JdkDeserializers.find(UUID.class, UUID.class.getName()));
    }

    @Test
    public void testFind_AtomicBoolean() {
        assertNotNull(JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()));
    }

    @Test
    public void testFind_StackTraceElement() {
        assertNotNull(JdkDeserializers.find(StackTraceElement[].class, StackTraceElement[].class.getName()));
    }

    @Test
    public void testFind_ByteBuffer() {
        assertNotNull(JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()));
    }

    @Test
    public void testFind_NonExistentClass() {
        assertNull(JdkDeserializers.find(Object.class, "com.example.nonExistentClass"));
    }

    @Test
    public void testFind_PrimitiveType() {
        assertNull(JdkDeserializers.find(int.class, int.class.getName()));
    }

    @Test
    public void testFind_CollectionClass() {
        Set<Class<?>> types = new HashSet<Class<?>>(Arrays.asList(
                List.class,
                Set.class,
                Map.class
        ));

        for (Class<?> collectionType : types) {
            assertNull(JdkDeserializers.find(collectionType.getComponentType(), collectionType.getName()));
        }
    }

    @Test
    public void testFind_FromStringDeserializer() {
        assertNotNull(JdkDeserializers.find(String[].class, FromStringDeserializer.class.getName()));
    }
}