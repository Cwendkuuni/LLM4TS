package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.hasBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstruct() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.construct(raw);
        assertEquals(String.class, simpleType.getRawClass());
        assertTrue(simpleType.hasBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> raw = HashMap.class;
        SimpleType.construct(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> raw = ArrayList.class;
        SimpleType.construct(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> raw = String[].class;
        SimpleType.construct(raw);
    }

    @Test
    public void testNarrow() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.construct(raw);
        Class<?> subclass = Object.class;
        SimpleType narrowedType = simpleType.narrow(subclass);
        assertNull(narrowedType);

        raw = String[].class;
        simpleType = SimpleType.construct(raw);
        subclass = String.class;
        narrowedType = simpleType.narrow(subclass);
        assertSame(simpleType, narrowedType);

        subclass = String[].class;
        narrowedType = simpleType.narrow(subclass);
        assertSame(simpleType, narrowedType);

        subclass = Object[].class;
        narrowedType = simpleType.narrow(subclass);
        assertNull(narrowedType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> raw = String.class;
        Object handler = new Object();
        SimpleType simpleType = SimpleType.construct(raw);
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertSame(simpleType, withHandler);
        assertNull(simpleType.getTypeHandler());
        assertEquals(handler, withHandler.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> raw = String.class;
        Object handler = new Object();
        SimpleType simpleType = SimpleType.construct(raw);
        SimpleType withHandler = simpleType.withValueHandler(handler);
        assertSame(simpleType, withHandler);
        assertNull(simpleType.getValueHandler());
        assertEquals(handler, withHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.construct(raw);
        SimpleType staticType = simpleType.withStaticTyping();
        assertFalse(simpleType.isStatic());
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.construct(raw);
        Class<?> subclassRaw = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType refinedType = simpleType.refine(subclassRaw, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }

    @Test
    public void testToString() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.construct(raw);
        String expected = "[simple type, class " + raw.getName() + "]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> raw1 = String.class;
        Class<?> raw2 = Integer.class;
        SimpleType simpleType1 = SimpleType.construct(raw1);
        SimpleType simpleType2 = SimpleType.construct(raw2);
        assertFalse(simpleType1.equals(simpleType2));

        raw1 = String.class;
        raw2 = String.class;
        SimpleType simpleType3 = SimpleType.construct(raw1);
        SimpleType simpleType4 = SimpleType.construct(raw2);
        assertTrue(simpleType3.equals(simpleType4));

        raw1 = String.class;
        raw2 = String[].class;
        SimpleType simpleType5 = SimpleType.construct(raw1);
        SimpleType simpleType6 = SimpleType.construct(raw2);
        assertFalse(simpleType5.equals(simpleType6));

        raw1 = String.class;
        raw2 = String.class;
        simpleType1 = SimpleType.construct(raw1);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType refinedType = simpleType1.refine(raw2.getClass(), bindings, superClass, superInterfaces);
        assertTrue(simpleType1.equals(refinedType));
    }
}