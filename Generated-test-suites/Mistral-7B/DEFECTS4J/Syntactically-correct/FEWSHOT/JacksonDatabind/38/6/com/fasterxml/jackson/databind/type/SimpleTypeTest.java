package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        final Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType3 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, true);
        SimpleType simpleType4 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false, 123);

        assertEquals(cls, simpleType1.getRawClass());
        assertEquals(cls, simpleType2.getRawClass());
        assertEquals(cls, simpleType3.getRawClass());
        assertEquals(cls, simpleType4.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType1._bindings);
        assertEquals(TypeBindings.emptyBindings(), simpleType2._bindings);
        assertNull(simpleType1._superClass);
        assertNull(simpleType2._superClass);
        assertNull(simpleType3._superClass);
        assertNull(simpleType4._superClass);
        assertArrayEquals(new JavaType[0], simpleType1._superInterfaces);
        assertArrayEquals(new JavaType[0], simpleType2._superInterfaces);
        assertArrayEquals(new JavaType[0], simpleType3._superInterfaces);
        assertArrayEquals(new JavaType[0], simpleType4._superInterfaces);
        assertEquals(false, simpleType1._asStatic);
        assertEquals(false, simpleType2._asStatic);
        assertEquals(false, simpleType3._asStatic);
        assertEquals(false, simpleType4._asStatic);
        assertEquals(123, simpleType4.extraHashCode());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        final Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(simpleType1);

        assertEquals(cls, simpleType1.getRawClass());
        assertEquals(cls, simpleType2.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType1._bindings);
        assertEquals(TypeBindings.emptyBindings(), simpleType2._bindings);
        assertNull(simpleType1._superClass);
        assertNull(simpleType2._superClass);
        assertArrayEquals(new JavaType[0], simpleType1._superInterfaces);
        assertArrayEquals(new JavaType[0], simpleType2._superInterfaces);
        assertEquals(false, simpleType1._asStatic);
        assertEquals(false, simpleType2._asStatic);
    }

    @Test
    public void testConstructUnsafe() {
        final Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);

        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType._bindings);
        assertNull(simpleType._superClass);
        assertArrayEquals(new JavaType[0], simpleType._superInterfaces);
        assertEquals(false, simpleType._asStatic);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeprecatedConstruct() {
        final Class<?> cls = String.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        final Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        final Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        final Class<?> cls = String[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        final Class<?> baseClass = String.class;
        final Class<?> subClass = Integer.class;

        SimpleType simpleType = new SimpleType(baseClass, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowType = simpleType._narrow(subClass);

        assertEquals(subClass, narrowType.getRawClass());
        assertEquals(simpleType._bindings, narrowType._bindings);
        assertNull(narrowType._superClass);
        assertArrayEquals(simpleType._superInterfaces, narrowType._superInterfaces);
        assertEquals(false, narrowType._asStatic);
    }
}