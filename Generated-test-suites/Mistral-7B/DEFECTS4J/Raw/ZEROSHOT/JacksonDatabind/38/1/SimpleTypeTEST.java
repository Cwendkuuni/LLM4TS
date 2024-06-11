package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeResolverBuilder;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertNotNull(simpleType1);

        // Test constructor with bindings, superClass and superInterfaces
        JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), javaType, new JavaType[0]);
        assertNotNull(simpleType2);

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertNotNull(simpleType3);
        assertEquals(simpleType1.getClass(), simpleType3.getClass());

        // Test constructor with additional parameters
        SimpleType simpleType4 = new SimpleType(String.class, TypeBindings.emptyBindings(), javaType, new JavaType[0], null, null, false);
        assertNotNull(simpleType4);
        assertEquals(simpleType1.getClass(), simpleType4.getClass());

        // Test Unsafe constructor
        SimpleType simpleType5 = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(simpleType5);

        // Test constructor that should not be used by application code
        try {
            SimpleType simpleType6 = SimpleType.construct(Integer.class);
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNarrowing() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(Integer.class);

        try {
            JavaType narrowedType = simpleType1._narrow(Integer.class);
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            // expected
        }

        JavaType narrowedType2 = simpleType2._narrow(String.class);
        assertEquals(String.class, narrowedType2.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(TypeFactory.defaultInstance().constructType(int[].class));
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(handler, simpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        try {
            simpleType.withContentTypeHandler(handler);
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType simpleTypeWithHandler = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(handler, simpleTypeWithHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        try {
            simpleType.withContentValueHandler(handler);
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticTypingSimpleType = simpleType.withStaticTyping();
        assertNotSame(simpleType, staticTypingSimpleType);
        assertTrue(staticTypingSimpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class);
        assertEquals("[Ljava.lang.String;", simpleType.buildCanonicalName());

        SimpleType simpleTypeWithGenericType = new SimpleType(new SimpleType(String.class), TypeBindings.emptyBindings(), null, null);
        assertEquals("[Lcom.fasterxml.jackson.databind.type.SimpleType;", simpleTypeWithGenericType.buildCanonicalName());

        SimpleType simpleTypeWithBindings = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertEquals("[I", simpleTypeWithBindings.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder();
        expected.append("[Ljava.lang.String;");
        assertEquals(expected.toString(), simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder expected = new StringBuilder("[L");
        expected.append(TypeFactory.defaultInstance().constructType(String.class).getGenericSignature(new StringBuilder()).toString());
        expected.append(';');
        assertEquals(expected.toString(), simpleType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("[simple type, class ");
        expected.append(simpleType.buildCanonicalName()).append(']');
        assertEquals(expected.toString(), simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertFalse(simpleType1.equals(simpleType3));
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
    }
}