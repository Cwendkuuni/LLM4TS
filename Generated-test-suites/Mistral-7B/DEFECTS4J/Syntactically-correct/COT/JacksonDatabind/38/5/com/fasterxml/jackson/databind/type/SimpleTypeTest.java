package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<String[]> cls = String[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<Map<String, String>> cls = Map.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<List<String>> cls = List.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<String[]> cls = String[].class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<Map<String, String>> cls = Map.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<List<String>> cls = List.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test
    public void testCopyConstructor() {
        Class<String> cls = String.class;
        SimpleType original = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copied = new SimpleType(original);
        assertEquals(original, copied);
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType stringBuilderType = new SimpleType(stringBuilderClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = stringType.narrow(StringBuilder.class);
        assertEquals(stringBuilderType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithDifferentClass() {
        Class<String> stringClass = String.class;
        Class<List<String>> listClass = List.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = stringType.narrow(listClass);
    }

    @Test
    public void testWithContentType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType javaType = new TypeFactory().constructType(String.class, String[].class);
        JavaType withContentType = simpleType.withContentType(javaType);
        assertNotNull(withContentType);
        assertEquals(javaType, withContentType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeWithNull() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        simpleType.withContentType(null);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object() {};
        JavaType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotNull(withTypeHandler);
        assertEquals(simpleType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object() {};
        JavaType withContentTypeHandler = simpleType.withContentTypeHandler(handler);
        assertNotNull(withContentTypeHandler);
        assertEquals(simpleType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(handler, withContentTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object() {};
        JavaType withValueHandler = simpleType.withValueHandler(handler);
        assertNotNull(withValueHandler);
        assertEquals(simpleType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(handler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType withStaticTyping = simpleType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertEquals(simpleType.getRawClass(), withStaticTyping.getRawClass());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        JavaType refinedType = simpleType.refine(stringBuilderClass, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        String canonicalName = simpleType.buildCanonicalName();
        String expected = "[java.util.List, java.lang.String]";
        assertEquals(expected, canonicalName);
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

}