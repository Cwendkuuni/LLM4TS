package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals(intClass, intType.getRawClass());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());
        assertFalse(intType.isStatic());
        assertEquals(intType.hashCode(), intType.hashCode());
        assertNotNull(intType.getBindings());
        assertEquals(intType.getClass(), SimpleType.class);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intTypeCopy = new SimpleType(intType);
        assertEquals(intType, intTypeCopy);
        assertEquals(intType.hashCode(), intTypeCopy.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodDeprecatedWarning() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = SimpleType.construct(intClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeMethodWarning() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = SimpleType.constructUnsafe(intClass);
    }

    @Test
    public void testNarrowMethod() {
        Class<Integer> intClass = Integer.class;
        Class<Long> longClass = Long.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType longType = intType.narrow(longClass);
        assertNotSame(intType, longType);
        assertEquals(longClass, longType.getRawClass());
        assertNull(longType.getSuperClass());
        assertNull(longType.getSuperInterfaces());
        assertFalse(longType.isStatic());
        assertEquals(longType.hashCode(), longType.hashCode());
        assertNotNull(longType.getBindings());
        assertEquals(longType.getClass(), SimpleType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithWrongSubclass() {
        Class<Integer> intClass = Integer.class;
        Class<String> stringClass = String.class;
        SimpleType intType = new SimpleType(intClass);
        intType.narrow(stringClass);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<Integer> intClass = Integer.class;
        Object intHandler = new Object() {};
        SimpleType intType = new SimpleType(intClass);
        SimpleType intTypeWithHandler = intType.withTypeHandler(intHandler);
        assertNotSame(intType, intTypeWithHandler);
        assertEquals(intHandler, intTypeWithHandler.getTypeHandler());
        assertEquals(intType.hashCode(), intTypeWithHandler.hashCode());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<Integer> intClass = Integer.class;
        Object intHandler = new Object() {};
        SimpleType intType = new SimpleType(intClass);
        try {
            intType.withContentTypeHandler(intHandler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<Integer> intClass = Integer.class;
        Object intHandler = new Object() {};
        SimpleType intType = new SimpleType(intClass);
        SimpleType intTypeWithHandler = intType.withValueHandler(intHandler);
        assertNotSame(intType, intTypeWithHandler);
        assertEquals(intHandler, intTypeWithHandler.getValueHandler());
        assertEquals(intType.hashCode(), intTypeWithHandler.hashCode());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<Integer> intClass = Integer.class;
        Object intHandler = new Object() {};
        SimpleType intType = new SimpleType(intClass);
        try {
            intType.withContentValueHandler(intHandler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intTypeWithStaticTyping = intType.withStaticTyping();
        assertNotSame(intType, intTypeWithStaticTyping);
        assertTrue(intTypeWithStaticTyping.isStatic());
        assertEquals(intType.hashCode(), intTypeWithStaticTyping.hashCode());
    }

    @Test
    public void testRefineMethod() {
        Class<Integer> intClass = Integer.class;
        Class<List<Integer>> listIntClass = new ArrayList<Integer>().getClass();
        SimpleType intType = new SimpleType(intClass);
        JavaType javaType = new SimpleType(listIntClass);
        assertNull(intType.refine(intClass, TypeBindings.emptyBindings(), null, null));
        assertNotNull(javaType.refine(intClass, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals(intType.buildCanonicalName(), "[simple type, class " + intClass.getName() + ']');
    }

    @Test
    public void testIsContainerTypeMethod() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertFalse(intType.isContainerType());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        StringBuilder sb = new StringBuilder();
        intType.getErasedSignature(sb);
        assertEquals("[I", sb.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        StringBuilder sb = new StringBuilder();
        intType.getGenericSignature(sb);
        assertEquals("[I]", sb.toString());
    }

    @Test
    public void testEqualsMethod() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType1 = new SimpleType(intClass);
        SimpleType intType2 = new SimpleType(intClass);
        SimpleType intType3 = new SimpleType(String.class);
        assertTrue(intType1.equals(intType1));
        assertTrue(intType1.equals(intType2));
        assertFalse(intType1.equals(intType3));
    }

    @Test
    public void testHashCodeMethod() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType1 = new SimpleType(intClass);
        SimpleType intType2 = new SimpleType(intClass);
        SimpleType intType3 = new SimpleType(String.class);
        assertEquals(intType1.hashCode(), intType1.hashCode());
        assertEquals(intType1.hashCode(), intType2.hashCode());
        assertNotEquals(intType1.hashCode(), intType3.hashCode());
    }

    @Test
    public void testToStringMethod() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        String expected = "[simple type, class " + intClass.getName() + ']';
        assertEquals(expected, intType.toString());
    }
}