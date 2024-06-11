package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testIsContainerType() {
        assertFalse(new SimpleType(String.class).isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType stringType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        stringType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType listType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        listType.getGenericSignature(sb);
        assertEquals("java.util.List", sb.toString());

        SimpleType listTypeOfInteger = new SimpleType(List.class).construct(Integer.class);
        sb.setLength(0);
        listTypeOfInteger.getGenericSignature(sb);
        assertEquals("java.util.List<java.lang.Integer>", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType stringType = new SimpleType(String.class);
        assertEquals("java.lang.String", stringType.buildCanonicalName());

        SimpleType listType = new SimpleType(List.class);
        assertEquals("java.util.List", listType.buildCanonicalName());

        SimpleType listTypeOfInteger = new SimpleType(List.class).construct(Integer.class);
        assertEquals("java.util.List<java.lang.Integer>", listTypeOfInteger.buildCanonicalName());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType stringType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, stringType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecated() {
        SimpleType stringType = SimpleType.construct(String.class);
    }

    @Test
    public void testToString() {
        SimpleType stringType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", stringType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType stringType1 = new SimpleType(String.class);
        SimpleType stringType2 = new SimpleType(String.class);
        assertTrue(stringType1.equals(stringType2));

        Object other = new Object();
        assertFalse(stringType1.equals(other));

        SimpleType differentType = new SimpleType(Integer.class);
        assertFalse(stringType1.equals(differentType));
    }

    @Test
    public void testRefine() {
        assertNull(new SimpleType(String.class).refine(Integer.class, Collections.emptyMap(), null, null));
    }

    @Test
    public void testConstructWithBindings() {
        JavaType boundType = new SimpleType(Integer.class);
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false)
                .withBindings(TypeBindings.fromMap(Collections.singletonMap("my.binding", boundType)));
        assertEquals("java.lang.String", simpleType.getRawClass().getName());
        assertEquals(1, simpleType.getBindings().size());
        assertTrue(simpleType.getBindings().containsKey("my.binding"));
        assertEquals(boundType, simpleType.getBindings().get("my.binding"));
    }

    @Test
    public void testConstructWithSuperClass() {
        JavaType superClass = new SimpleType(List.class);
        SimpleType simpleType = new SimpleType(Integer.class, null, superClass, null, null, null, false);
        assertEquals("java.lang.Integer", simpleType.getRawClass().getName());
        assertEquals(superClass, simpleType.getSuperClass());
    }

    @Test
    public void testConstructWithSuperInterfaces() {
        JavaType superInterface1 = new SimpleType(List.class);
        JavaType superInterface2 = new SimpleType(Map.class);
        SimpleType simpleType = new SimpleType(Integer.class, null, null, new JavaType[] { superInterface1, superInterface2 }, null, null, false);
        assertEquals("java.lang.Integer", simpleType.getRawClass().getName());
        assertEquals(Arrays.asList(superInterface1, superInterface2), Arrays.asList(simpleType.getSuperInterfaces()));
    }

    @Test
    public void testGetRawClass() {
        SimpleType stringType = new SimpleType(String.class);
        assertEquals(String.class, stringType.getRawClass());
    }

    @Test
    public void testGetSuperClass() {
        SimpleType listType = new SimpleType(List.class);
        assertNull(listType.getSuperClass());
    }

    @Test
    public void testGetSuperInterfaces() {
        SimpleType mapType = new SimpleType(Map.class);
        assertNull(mapType.getSuperInterfaces());
    }

    @Test
    public void testWithBindings() {
        JavaType boundType = new SimpleType(Integer.class);
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false)
                .withBindings(TypeBindings.fromMap(Collections.singletonMap("my.binding", boundType)));
        assertEquals("java.lang.String", simpleType.getRawClass().getName());
        assertEquals(1, simpleType.getBindings().size());
        assertTrue(simpleType.getBindings().containsKey("my.binding"));
        assertEquals(boundType, simpleType.getBindings().get("my.binding"));
    }

    @Test
    public void testWithSuperClass() {
        JavaType superClass = new SimpleType(List.class);
        SimpleType simpleType = new SimpleType(Integer.class, null, null, null, null, null, false)
                .withSuperClass(superClass);
        assertEquals("java.lang.Integer", simpleType.getRawClass().getName());
        assertEquals(superClass, simpleType.getSuperClass());
    }

    @Test
    public void testWithSuperInterfaces() {
        JavaType superInterface1 = new SimpleType(List.class);
        JavaType superInterface2 = new SimpleType(Map.class);
        SimpleType simpleType = new SimpleType(Integer.class, null, null, null, null, null, false)
                .withSuperInterfaces(new JavaType[] { superInterface1, superInterface2 });
        assertEquals("java.lang.Integer", simpleType.getRawClass().getName());
        assertEquals(Arrays.asList(superInterface1, superInterface2), Arrays.asList(simpleType.getSuperInterfaces()));
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false)
                .withValueHandler(valueHandler);
        assertEquals("java.lang.String", simpleType.getRawClass().getName());
        assertEquals(valueHandler, simpleType.getValueHandler());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false)
                .withTypeHandler(typeHandler);
        assertEquals("java.lang.String", simpleType.getRawClass().getName());
        assertEquals(typeHandler, simpleType.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType._asStatic);
        assertFalse(simpleType._asStatic);
    }
}