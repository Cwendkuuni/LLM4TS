package com.fasterxml.jackson.databind;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = TypeFactory.unknownType();
        javaType = javaType.withStaticTyping();
        assertEquals(true, javaType.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        JavaType javaType = TypeFactory.unknownType();
        Class<?> subclass = String.class;
        JavaType result = javaType.narrowBy(subclass);
        assertEquals(subclass, result.getRawClass());
    }

    @Test
    public void testForcedNarrowBy() {
        JavaType javaType = TypeFactory.unknownType();
        Class<?> subclass = String.class;
        JavaType result = javaType.forcedNarrowBy(subclass);
        assertEquals(subclass, result.getRawClass());
    }

    @Test
    public void testWidenBy() {
        JavaType javaType = TypeFactory.unknownType();
        Class<?> superclass = Object.class;
        JavaType result = javaType.widenBy(superclass);
        assertEquals(superclass, result.getRawClass());
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType javaType = TypeFactory.unknownType();
        int index = 0;
        JavaType result = javaType.containedTypeOrUnknown(index);
        assertNotNull(result);
    }

    @Test
    public void testGetValueHandler() {
        JavaType javaType = TypeFactory.unknownType();
        assertEquals(null, javaType.getValueHandler());
    }

    @Test
    public void testGetTypeHandler() {
        JavaType javaType = TypeFactory.unknownType();
        assertEquals(null, javaType.getTypeHandler());
    }

    @Test
    public void testToString() {
        JavaType javaType = TypeFactory.unknownType();
        String result = javaType.toString();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = TypeFactory.unknownType();
        JavaType javaType2 = TypeFactory.unknownType();
        assertEquals(javaType1, javaType2);
    }

    @Test
    public void testHashCode() {
        JavaType javaType1 = TypeFactory.unknownType();
        JavaType javaType2 = TypeFactory.unknownType();
        assertEquals(javaType1.hashCode(), javaType2.hashCode());
    }
}