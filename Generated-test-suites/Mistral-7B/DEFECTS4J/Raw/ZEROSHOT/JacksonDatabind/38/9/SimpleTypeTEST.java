package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertNotNull(simpleType1);

        // Test constructor with bindings, superClass and superInterfaces
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(simpleType1, simpleType2);

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType3);

        // Test pass-through constructor used by ReferenceType
        SimpleType simpleType4 = new SimpleType(SimpleType.class, TypeBindings.emptyBindings(), null, null, 0, null, false);
        assertEquals(SimpleType.class, simpleType4.getRawClass());

        // Test unsafe constructor
        SimpleType simpleType5 = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType5.getRawClass());

        // Test deprecated constructor
        SimpleType simpleType6 = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType6.getRawClass());
    }

    @Test
    public void testNarrowing() {
        SimpleType simpleType = new SimpleType(String.class);

        // Test narrowing with same class
        SimpleType narrowedType1 = simpleType.narrow(String.class);
        assertEquals(simpleType, narrowedType1);

        // Test narrowing with subclass
        SimpleType subclass = new SimpleType(Integer.class);
        SimpleType narrowedType2 = simpleType.narrow(subclass.getRawClass());
        assertNull(narrowedType2);

        // Test narrowing with class not related
        SimpleType unrelatedClass = new SimpleType(List.class);
        SimpleType narrowedType3 = simpleType.narrow(unrelatedClass.getRawClass());
        assertNull(narrowedType3);
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        Object handler = new Object();
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        assertNotNull(typeWithHandler);
        assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        Object handler = new Object();
        SimpleType typeWithContentTypeHandler = simpleType.withContentTypeHandler(handler);
        assertNull(typeWithContentTypeHandler);
        assertEquals(null, simpleType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        Object handler = new Object();
        SimpleType typeWithValueHandler = simpleType.withValueHandler(handler);
        assertNotNull(typeWithValueHandler);
        assertEquals(handler, typeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        Object handler = new Object();
        SimpleType typeWithContentValueHandler = simpleType.withContentValueHandler(handler);
        assertNull(typeWithContentValueHandler);
        assertEquals(null, simpleType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("[Ljava.lang.String;", canonicalName);

        SimpleType simpleTypeWithGenerics = new SimpleType(List.class);
        canonicalName = simpleTypeWithGenerics.buildCanonicalName();
        assertEquals("java.util.List", canonicalName);
        canonicalName = simpleTypeWithGenerics.buildCanonicalName(new StringBuilder());
        assertEquals("java.util.List<>", canonicalName);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());

        SimpleType listType = new SimpleType(List.class);
        sb.setLength(0);
        listType.getGenericSignature(sb);
        assertEquals("java.util.List<T>", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String toString = simpleType.toString();
        String expected = "[simple type, class " + SimpleType.class.getName() + "]";
        assertEquals(expected, toString);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}