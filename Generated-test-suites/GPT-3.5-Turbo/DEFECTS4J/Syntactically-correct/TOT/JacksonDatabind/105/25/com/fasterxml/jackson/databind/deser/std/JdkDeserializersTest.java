package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_validClassAndClassname_returnsDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
    }

    @Test
    public void testFind_invalidClassAndClassname_returnsNull() {
        Class<?> rawType = ArrayList.class;
        String clsName = "java.util.ArrayList";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFind_validRawTypeAndInvalidClassname_returnsNull() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.lang.AtomicBooleanInvalid";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFind_validClassAndValidClassnameWithNoDeserializer_returnsNull() {
        Class<?> rawType = Boolean.class;
        String clsName = "java.lang.Boolean";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }

    @Test
    public void testFind_validClassAndNullClassname_returnsNull() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = null;
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }
}