package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> raw = String.class;
        SimpleType type = SimpleType.constructUnsafe(raw);
        JavaType javaType = TypeFactory.defaultInstance().constructType(raw);
        assertEquals(type.getClass(), SimpleType.class);
        assertEquals(type.getRawClass(), raw);
        assertEquals(type.getBindings().isEmpty(), true);
        assertEquals(type.getSuperClass(), null);
        assertEquals(type.getSuperInterfaces(), new JavaType[0]);
        assertEquals(type, type._narrow(raw));
        assertEquals(type.toString(), "[simple type, class " + raw.getName() + "]");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Map() {
        Class<?> raw = HashMap.class;
        SimpleType.construct(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Collection() {
        Class<?> raw = ArrayList.class;
        SimpleType.construct(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Array() {
        Class<?> raw = String[].class;
        SimpleType.construct(raw);
    }

    @Test
    public void testNarrow() {
        Class<?> raw = String.class;
        SimpleType parentType = SimpleType.constructUnsafe(raw);
        SimpleType childType = new SimpleType(String[].class);
        assertEquals(childType, parentType._narrow(String[].class));
    }

    @Test
    public void testIsContainerType() {
        Class<?> raw = String.class;
        SimpleType type = SimpleType.constructUnsafe(raw);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> raw = String.class;
        SimpleType type = SimpleType.constructUnsafe(raw);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals(sb.toString(), "java.lang.String");
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> raw = String.class;
        SimpleType type = SimpleType.constructUnsafe(raw);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals(sb.toString(), "java.lang.String;");
    }

    @Test
    public void testToString() {
        Class<?> raw = String.class;
        SimpleType type = SimpleType.constructUnsafe(raw);
        assertEquals(type.toString(), "[simple type, class " + raw.getName() + "]");
    }

    @Test
    public void testEquals() {
        Class<?> raw1 = String.class;
        Class<?> raw2 = Integer.class;
        SimpleType type1 = SimpleType.constructUnsafe(raw1);
        SimpleType type2 = SimpleType.constructUnsafe(raw2);
        assertFalse(type1.equals(type2));

        SimpleType type3 = new SimpleType(type1);
        assertTrue(type1.equals(type3));
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> raw = String.class;
        SimpleType type = SimpleType.constructUnsafe(raw);
        Object handler = new Object();
        SimpleType newType = type.withTypeHandler(handler);
        assertEquals(newType.getBindings().getRawValueHandler(), handler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> raw = String.class;
        SimpleType type = SimpleType.constructUnsafe(raw);
        SimpleType staticType = type.withStaticTyping();
        assertTrue(staticType._asStatic);
    }

    @Test
    public void testRefine() {
        Class<?> raw = String.class;
        SimpleType type = SimpleType.constructUnsafe(raw);
        JavaType refinedType = type.refine(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> raw = List.class;
        Class<?> elementType = Integer.class;
        SimpleType type = SimpleType.constructUnsafe(raw);
        assertEquals(type.buildCanonicalName(), "[java.util.List, class java.lang.Integer]");

        SimpleType typeWithBindings = new SimpleType(type, TypeBindings.mapType(List.class, new JavaType[] { JavaType.constructUnboundedArrayType(Integer.class) }));
        assertEquals(typeWithBindings.buildCanonicalName(), "[java.util.List, class java.lang.Integer[]]");
    }
}