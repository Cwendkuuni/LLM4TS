package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = Integer.TYPE;
        SimpleType type = SimpleType.constructUnsafe(cls);
        assertEquals(cls, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrowWithSameClass() {
        Class<?> cls = Integer.TYPE;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType narrowType = baseType.narrow(cls);
        assertEquals(baseType, narrowType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithSubclass() {
        Class<?> baseClass = Integer.TYPE;
        Class<?> subclass = Integer.class;
        SimpleType baseType = new SimpleType(baseClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType narrowType = baseType.narrow(subclass);
        // This should not get here but...
        assertNotNull(narrowType);
        assertEquals(baseType.getClass(), narrowType.getClass());
        assertEquals(subclass, narrowType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = Integer.TYPE;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        JavaType contentType = TypeFactory.unknownType();
        assertNull(type.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = Integer.TYPE;
        Object handler = new Object();
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType newType = type.withTypeHandler(handler);
        assertEquals(type.getClass(), newType.getClass());
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = Integer.TYPE;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType staticType = type.withStaticTyping();
        assertEquals(type.getClass(), staticType.getClass());
        assertTrue(staticType.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = Integer[].class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals("[Ljava.lang.Integer;", type.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = Integer.TYPE;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = Integer.TYPE;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        StringBuilder expected = new StringBuilder("I");
        StringBuilder result = type.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Integer[].class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        StringBuilder expected = new StringBuilder("[L");
        expected.append(Integer.TYPE.getCanonicalName());
        expected.append(';');
        StringBuilder result = type.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetRawClass() {
        Class<?> cls = Integer.TYPE;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals(cls, type.getRawClass());
    }

    @Test
    public void testGetSuperClass() {
        Class<?> cls = Integer.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertNull(type.getSuperClass());
    }

    @Test
    public void testGetSuperInterfaces() {
        Class<?> cls = Number.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        Class<?>[] expected = new Class[] { Number.class };
        Class<?>[] result = type.getSuperInterfaces();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testRefine() {
        Class<?> cls = Integer[].class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertNull(type.refine(cls, TypeBindings.emptyBindings(), null, null));
    }

    private static void assertArrayEquals(Object expected, Object result) {
        if (expected == result) {
            return;
        }
        if (expected.getClass() != result.getClass()) {
            throw new AssertionError("Expected array of class " + expected.getClass()
                    + " but got array of class " + result.getClass());
        }
        Object[] expectedArray = (Object[]) expected;
        Object[] resultArray = (Object[]) result;
        if (expectedArray.length != resultArray.length) {
            throw new AssertionError("Expected array of length " + expectedArray.length
                    + " but got array of length " + resultArray.length);
        }
        for (int i = 0; i < expectedArray.length; i++) {
            if (!expectedArray[i].equals(resultArray[i])) {
                throw new AssertionError("Expected: " + expectedArray[i]
                        + " but got: " + resultArray[i]);
            }
        }
    }
}