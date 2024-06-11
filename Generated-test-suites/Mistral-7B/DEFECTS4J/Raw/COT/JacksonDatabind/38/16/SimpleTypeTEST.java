package com.example.junit; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
        assertNotNull(type);
        assertEquals(cls, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        assertNotNull(type);
        assertEquals(cls, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidType() {
        Class<?> cls = Map.class;
        SimpleType type = SimpleType.construct(cls);
        assertFalse(true); // this line is unreachable
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());
        sb.setLength(0);
        SimpleType listType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        type = listType.with(JavaType.of(String.class));
        type.getGenericSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(type.isContainerType());

        cls = ArrayList.class;
        type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertTrue(type.isContainerType());
    }

    @Test
    public void testRefine() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType superClass = typeFactory.constructType(Integer[].class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType subType = baseType.refine(String[].class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertNull(subType);

        cls = Integer.class;
        baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        JavaType subClass = typeFactory.constructType(String.class);
        SimpleType result = baseType.refine(cls, TypeBindings.emptyBindings(), null, new JavaType[] { subClass });
        assertNotNull(result);
        assertEquals(cls, result.getRawClass());
        assertEquals(subClass, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType result = baseType.withTypeHandler(handler);
        assertSame(baseType, result);
        result = baseType.withTypeHandler(handler);
        assertSame(baseType, result);

        cls = Integer.class;
        baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        result = baseType.withTypeHandler(handler);
        assertNotNull(result);
        assertEquals(cls, result.getRawClass());
        assertEquals(handler, result.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidTypeHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        baseType.withTypeHandler(int.class);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType result = baseType.withContentTypeHandler(handler);
        assertSame(baseType, result);
        result = baseType.withContentTypeHandler(handler);
        assertSame(baseType, result);

        cls = Integer.class;
        baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        JavaType contentType = JavaType.of(String.class);
        result = baseType.withContentTypeHandler(handler);
        assertNotNull(result);
        assertEquals(cls, result.getRawClass());
        assertEquals(contentType, result.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidContentTypeHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        baseType.withContentTypeHandler(int.class);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType result = baseType.withValueHandler(handler);
        assertSame(baseType, result);
        result = baseType.withValueHandler(handler);
        assertSame(baseType, result);

        cls = Integer.class;
        baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        result = baseType.withValueHandler(handler);
        assertNotNull(result);
        assertEquals(cls, result.getRawClass());
        assertEquals(handler, result.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidValueHandler() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        baseType.withValueHandler(int.class);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType result = baseType.withStaticTyping();
        assertSame(baseType, result);
        result = baseType.withStaticTyping();
        assertSame(baseType, result);

        cls = Integer.class;
        baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        result = baseType.withStaticTyping();
        assertNotNull(result);
        assertEquals(cls, result.getRawClass());
        assertTrue(result.isStatic());
    }

    @Test
    public void testConstructNullClass() {
        assertThrows(NullPointerException.class, () -> new SimpleType(null, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testConstructEmptyBindings() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls, new TypeBindings(), null, null);
        assertNotNull(type);
        assertEquals(cls, type.getRawClass());
        assertTrue(type.getBindings().isEmpty());
    }

    @Test
    public void testConstructSuperClass() {
        Class<?> cls = String.class;
        Class<?> superClass = Object.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), JavaType.of(superClass), null);
        assertNotNull(type);
        assertEquals(cls, type.getRawClass());
        assertEquals(JavaType.of(superClass), type.getSuperClass());
    }

    @Test
    public void testConstructSuperInterfaces() {
        Class<?> cls = String.class;
        Class<?>[] superInterfaces = { List.class };
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, JavaType.arrayFromClass(superInterfaces));
        assertNotNull(type);
        assertEquals(cls, type.getRawClass());
        assertArrayEquals(superInterfaces, type.getSuperInterfaces());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals("[simple type, class " + cls.getName() + "]", type.toString());
    }
}