package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
        assertEquals(cls, type.getRawClass());
        assertFalse(type.hasSuperClass());
        assertFalse(type.hasSuperInterfaces());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType type = new SimpleType(cls, bindings, superClass, superInterfaces);
        assertEquals(cls, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertSame(superClass, type.getSuperClass());
        assertArrayEquals(superInterfaces, type.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        Class<?> cls = new ArrayList<String>().getClass();
        SimpleType.construct(cls);
    }

    @Test
    public void testCopyConstructor() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType parentType = new SimpleType(cls, bindings, superClass, superInterfaces);
        SimpleType childType = new SimpleType(parentType);
        assertEquals(parentType, childType);
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        Type type = new TypeToken<List<String>>() {}.getType();
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("java.util.List<java.lang.String>", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        JavaType elementType = TypeFactory.stringType();
        TypeBindings bindings = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null).getBindings();
        bindings.setBindings("java.util.List", List.class, elementType);
        SimpleType type = new SimpleType(cls, bindings, null, null);
        assertEquals("java.util.List<java.lang.String>", type.getCanonicalName());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType type1 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        SimpleType type2 = new SimpleType(cls2, TypeBindings.emptyBindings(), null, null);
        assertFalse(type1.equals(type2));

        SimpleType type3 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        assertTrue(type1.equals(type3));
    }

    @Test
    public void testRefine() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unsignedByteArrayType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType parentType = new SimpleType(cls, bindings, superClass, superInterfaces);
        JavaType childType = parentType.refine(Integer.class, bindings, null, null);
        assertNull(childType);
    }

    private static JavaType[] assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}