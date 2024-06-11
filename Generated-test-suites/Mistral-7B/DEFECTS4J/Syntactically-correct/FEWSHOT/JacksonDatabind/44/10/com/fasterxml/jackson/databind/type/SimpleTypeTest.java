package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertTrue(simpleType.isContainerType());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType original = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
        assertEquals(original.getRawClass(), copy.getRawClass());
        assertEquals(original.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
        assertEquals(original.isContainerType(), copy.isContainerType());
        assertEquals(original.isStatic(), copy.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeprecatedConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        Class<?> baseClass = Object.class;
        Class<?> subClass1 = String.class;
        Class<?> subClass2 = Number.class;

        SimpleType baseType = new SimpleType(baseClass, TypeBindings.emptyBindings(), null, null);
        SimpleType subType1 = new SimpleType(subClass1, TypeBindings.emptyBindings(), null, null);
        SimpleType subType2 = new SimpleType(subClass2, TypeBindings.emptyBindings(), null, null);

        SimpleType narrowed1 = baseType._narrow(subClass1);
        SimpleType narrowed2 = baseType._narrow(subClass2);

        assertEquals(subType1, narrowed1);
        assertNotEquals(narrowed1, narrowed2);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType withHandler = new SimpleType(simpleType.getRawClass(), simpleType._bindings, null, null, null, handler, false);
        assertEquals(handler, withHandler._typeHandler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType._asStatic);
    }

    @Test
    public void testRefine() {
        Class<?> baseClass = Object.class;
        Class<?> subClass = String.class;

        SimpleType baseType = new SimpleType(baseClass, TypeBindings.emptyBindings(), null, null);
        SimpleType subType = new SimpleType(subClass, TypeBindings.emptyBindings(), null, null);

        JavaType refined = baseType.refine(subClass, TypeBindings.emptyBindings(), null, null);
        assertNull(refined);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        Typevariable<?> typeVariable = TypeVariable.of("E", Integer.class);
        SimpleType simpleType = new SimpleType(cls, TypeBindings.of(typeVariable), null, null);

        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.util.List<java.lang.Integer>", canonicalName);
    }
}