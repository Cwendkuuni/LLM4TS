package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertSame(String.class, type.getRawClass());
        assertFalse(type.hasBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isStatic());
    }

    @Test
    public void testConstruct() {
        // Test constructing with an empty bindings map
        SimpleType type = SimpleType.construct(Integer.class);
        assertSame(Integer.class, type.getRawClass());
        assertFalse(type.hasBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isStatic());

        // Test constructing with bindings
        TypeBindings bindings = new TypeBindings();
        bindings.put(new SimpleTypeParameter("T", String.class));
        type = SimpleType.construct(Integer[].class, bindings);
        assertSame(Integer[].class, type.getRawClass());
        assertTrue(type.hasBindings());
        assertEquals(1, type.getBindings().size());
        assertSame(String.class, type.getBindings().get(0).getType());

        // Test constructing with superClass and superInterfaces
        JavaType superClass = new SimpleType(Long.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(Object.class) };
        type = SimpleType.construct(Integer.class, null, superClass, superInterfaces);
        assertSame(Integer.class, type.getRawClass());
        assertFalse(type.hasBindings());
        assertSame(superClass, type.getSuperClass());
        assertArrayEquals(superInterfaces, type.getSuperInterfaces());

        // Test constructing with valueHandler
        Object valueHandler = new Object() {};
        type = SimpleType.construct(Integer.class, null, null, null, valueHandler, null, false);
        assertSame(Integer.class, type.getRawClass());
        assertFalse(type.hasBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertSame(valueHandler, type.getValueHandler());

        // Test constructing with typeHandler
        Object typeHandler = new Object() {};
        type = SimpleType.construct(Integer.class, null, null, null, null, typeHandler, false);
        assertSame(Integer.class, type.getRawClass());
        assertFalse(type.hasBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertSame(typeHandler, type.getTypeHandler());

        // Test constructing with asStatic
        type = SimpleType.construct(Integer.class, null, null, null, null, null, true);
        assertSame(Integer.class, type.getRawClass());
        assertFalse(type.hasBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertTrue(type.isStatic());

        // Test constructing with all parameters
        JavaType superClass2 = new SimpleType(Long[].class);
        JavaType[] superInterfaces2 = new JavaType[] { new SimpleType(Object.class), new SimpleType(List.class) };
        Object valueHandler2 = new Object() {};
        Object typeHandler2 = new Object() {};
        type = SimpleType.construct(Integer.class, null, superClass2, superInterfaces2, valueHandler2, typeHandler2, true);
        assertSame(Integer.class, type.getRawClass());
        assertFalse(type.hasBindings());
        assertSame(superClass2, type.getSuperClass());
        assertArrayEquals(superInterfaces2, type.getSuperInterfaces());
        assertSame(valueHandler2, type.getValueHandler());
        assertSame(typeHandler2, type.getTypeHandler());
        assertTrue(type.isStatic());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(new SimpleType(String.class).isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType type = new SimpleType(String.class);
        type.getErasedSignature(sb);
        assertTrue(sb.toString().equals("[simple type, class java.lang.String]"));
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType type = new SimpleType(String.class);
        type.getGenericSignature(sb);
        assertTrue(sb.toString().equals("[simple type, class java.lang.String]"));

        sb = new StringBuilder();
        type = new SimpleType(Map.class);
        type.getGenericSignature(sb);
        assertTrue(sb.toString().equals("[simple type, class java.util.Map]"));

        sb = new StringBuilder();
        type = new SimpleType(List.class);
        type.getGenericSignature(sb);
        assertTrue(sb.toString().equals("[simple type, class java.util.List]"));

        sb = new StringBuilder();
        type = new SimpleType(ArrayList.class);
        type.getGenericSignature(sb);
        assertTrue(sb.toString().equals("[simple type, class java.util.ArrayList<capture#1 of ? extends java.lang.Object>]"));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType type = new SimpleType(String.class);
        assertTrue(type.buildCanonicalName().equals("java.lang.String"));

        type = new SimpleType(Map.class);
        assertTrue(type.buildCanonicalName().equals("java.util.Map<capture#1 of ?, capture#2 of ?>]"));

        type = new SimpleType(List.class);
        assertTrue(type.buildCanonicalName().equals("java.util.List<capture#1 of ? extends java.lang.Object>"));

        type = new SimpleType(ArrayList.class);
        assertTrue(type.buildCanonicalName().equals("java.util.ArrayList<capture#1 of ? extends java.lang.Object>"));

        type = new SimpleType(ArrayList.class, new TypeBindings().put(new SimpleTypeParameter("T", Integer.class)));
        assertTrue(type.buildCanonicalName().equals("java.util.ArrayList<java.lang.Integer>"));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(String.class);
        SimpleType type = new SimpleType(String[].class);
        assertNull(type.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object() {};
        SimpleType type = new SimpleType(String.class);
        SimpleType result = type.withTypeHandler(typeHandler);
        assertSame(type, result);
        assertNull(type.getTypeHandler());

        type.setValueHandler(new Object() {});
        result = type.withTypeHandler(typeHandler);
        assertSame(type, result);
        assertSame(typeHandler, type.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object() {};
        SimpleType type = new SimpleType(String.class);
        assertNull(type.withContentTypeHandler(contentTypeHandler));
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object() {};
        SimpleType type = new SimpleType(String.class);
        SimpleType result = type.withValueHandler(valueHandler);
        assertSame(type, result);
        assertNull(type.getValueHandler());

        type.setValueHandler(new Object() {});
        result = type.withValueHandler(valueHandler);
        assertSame(type, result);
        assertSame(valueHandler, type.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object() {};
        SimpleType type = new SimpleType(String.class);
        assertNull(type.withContentValueHandler(contentValueHandler));
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(String.class);
        SimpleType result = type.withStaticTyping();
        assertSame(type, result);
        assertFalse(type.isStatic());

        type.setStaticTyping(true);
        result = type.withStaticTyping();
        assertSame(type, result);
        assertTrue(type.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType original = new SimpleType(String.class);
        SimpleType refined = original.refine(Object.class, null, null, null);
        assertNull(refined);
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        assertTrue(type1.equals(type2));

        SimpleType type3 = new SimpleType(Integer.class);
        assertFalse(type1.equals(type3));

        SimpleType type4 = new SimpleType(String[].class);
        assertFalse(type1.equals(type4));

        SimpleType type5 = new SimpleType(String.class, new TypeBindings());
        assertFalse(type1.equals(type5));

        SimpleType type6 = new SimpleType(String.class, new TypeBindings().put(new SimpleTypeParameter("T", String.class)));
        assertFalse(type1.equals(type6));
    }
}