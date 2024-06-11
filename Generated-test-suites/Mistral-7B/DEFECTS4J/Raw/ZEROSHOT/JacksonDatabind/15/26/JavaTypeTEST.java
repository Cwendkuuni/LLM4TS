package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    private TypeFactory typeFactory = new TypeFactory();

    @Test
    public void testJavaTypeConstruction() {
        JavaType javaType = typeFactory.constructType(String.class);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
        assertEquals("java.lang.String", javaType.getErasedSignature());
        assertEquals("java.lang.String", javaType.getGenericSignature());
    }

    @Test
    public void testJavaTypeNarrowing() {
        JavaType javaTypeString = typeFactory.constructType(String.class);
        JavaType javaTypeStringBuffer = typeFactory.constructType(StringBuffer.class);

        JavaType narrowedType = javaTypeString.narrowBy(StringBuffer.class);
        assertEquals(StringBuffer.class, narrowedType.getRawClass());
        assertEquals("java.lang.StringBuffer", narrowedType.getErasedSignature());
        assertEquals("java.lang.StringBuffer<T>", narrowedType.getGenericSignature());

        JavaType forcedNarrowedType = javaTypeString.forcedNarrowBy(StringBuffer.class);
        assertEquals(StringBuffer.class, forcedNarrowedType.getRawClass());
        assertEquals("java.lang.StringBuffer", forcedNarrowedType.getErasedSignature());
        assertEquals("java.lang.StringBuffer<T>", forcedNarrowedType.getGenericSignature());
    }

    @Test
    public void testJavaTypeWidening() {
        JavaType javaTypeString = typeFactory.constructType(String.class);
        JavaType javaTypeObject = typeFactory.constructType(Object.class);

        JavaType widenedType = javaTypeString.widenBy(Object.class);
        assertEquals(Object.class, widenedType.getRawClass());
        assertEquals("java.lang.Object", widenedType.getErasedSignature());
        assertEquals("java.lang.Object", widenedType.getGenericSignature());
    }

    @Test
    public void testJavaTypeIsStatic() {
        JavaType javaTypeString = typeFactory.constructType(String.class);
        JavaType javaTypeInteger = typeFactory.constructType(int[].class);

        assertFalse(javaTypeString.useStaticType());
        assertTrue(javaTypeInteger.useStaticType());
    }

    @Test
    public void testJavaTypeContainsTypeParameters() {
        JavaType javaTypeList = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());
        JavaType javaTypeString = typeFactory.constructType(String.class);

        assertTrue(javaTypeList.hasGenericTypes());
        assertFalse(javaTypeString.hasGenericTypes());
    }

    @Test
    public void testJavaTypeContainedType() {
        JavaType javaTypeList = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());
        JavaType javaTypeString = javaTypeList.containedType(0);

        assertEquals(String.class, javaTypeString.getRawClass());
        assertEquals("java.lang.String", javaTypeString.getErasedSignature());
        assertEquals("java.lang.String", javaTypeString.getGenericSignature());
    }

    @Test
    public void testJavaTypeContainedTypeOrUnknown() {
        JavaType javaTypeList = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());
        JavaType javaTypeString = javaTypeList.containedTypeOrUnknown(0);

        assertEquals(String.class, javaTypeString.getRawClass());
        assertEquals("java.lang.String", javaTypeString.getErasedSignature());
        assertEquals("java.lang.String", javaTypeString.getGenericSignature());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingInvalid() {
        JavaType javaTypeString = typeFactory.constructType(String.class);
        javaTypeString.narrowBy(Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningInvalid() {
        JavaType javaTypeString = typeFactory.constructType(String.class);
        javaTypeString.widenBy(StringBuffer.class);
    }
}