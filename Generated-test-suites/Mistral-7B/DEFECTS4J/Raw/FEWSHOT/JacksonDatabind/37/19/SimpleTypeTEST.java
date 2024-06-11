package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeInvalid() {
        Class<?> cls = Map.class;
        SimpleType.constructUnsafe(cls); // this should fail
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalid() {
        Class<?> cls = Map.class;
        SimpleType.construct(cls); // this should fail
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        SimpleType stringType = new SimpleType(cls);
        Class<?> stringBufferType = StringBuffer.class;

        SimpleType stringBufferTypeSimple = stringType.narrow(stringBufferType);
        assertNotNull(stringBufferTypeSimple);
        assertEquals(stringBufferType, stringBufferTypeSimple.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalid() {
        Class<?> cls = String.class;
        SimpleType stringType = new SimpleType(cls);
        Class<?> intType = int.class;

        SimpleType intTypeSimple = stringType.narrow(intType); // this should fail
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType stringType = new SimpleType(cls);
        Object handler = new Object() {};

        SimpleType stringTypeWithHandler = stringType.withTypeHandler(handler);
        assertNotNull(stringTypeWithHandler);
        assertEquals(handler, stringTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testRefine() {
        Class<?> cls = String.class;
        SimpleType stringType = new SimpleType(cls);
        Class<?> stringBufferType = StringBuffer.class;

        SimpleType refinedType = stringType.refine(StringBuffer.class, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        TypeVariable<?> typeVariable = new TypeVariable<>("T") {};
        SimpleType listType = new SimpleType(cls, Arrays.asList(typeVariable));

        String canonicalName = listType.buildCanonicalName();
        assertEquals("[java.util.List, T]", canonicalName);
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType stringType = new SimpleType(cls);

        assertFalse(stringType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType stringType = new SimpleType(cls);

        String string = stringType.toString();
        assertEquals("[simple type, class [Ljava.lang.String;]", string);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = StringBuffer.class;

        SimpleType stringType1 = new SimpleType(cls1);
        SimpleType stringBufferType1 = new SimpleType(cls2);
        SimpleType stringType2 = new SimpleType(cls1);

        // equals with same object
        assertTrue(stringType1.equals(stringType1));

        // equals with same type
        assertTrue(stringType1.equals(stringType2));

        // not equals with other type
        assertFalse(stringType1.equals(stringBufferType1));
    }
}