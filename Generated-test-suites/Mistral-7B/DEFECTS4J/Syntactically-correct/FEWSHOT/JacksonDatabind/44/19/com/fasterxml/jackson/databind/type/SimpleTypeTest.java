package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType original = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copy = new SimpleType(original);
        assertNotNull(copy);
        assertEquals(original, copy);
        assertEquals(cls, copy.getRawClass());
        assertEquals(original.getBindings(), copy.getBindings());
        assertEquals(original.getSuperClass(), copy.getSuperClass());
        assertEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
        assertEquals(original.isStatic(), copy.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecated() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> superClass = Object.class;
        Class<?> subclass = String.class;
        SimpleType superType = new SimpleType(superClass, TypeBindings.emptyBindings(), null, null);
        SimpleType subType = new SimpleType(subclass, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowed = subType._narrow(superClass);
        assertEquals(subType, narrowed);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(simpleType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        assertNotNull(typeWithHandler);
        assertNotSame(simpleType, typeWithHandler);
        assertEquals(simpleType.getRawClass(), typeWithHandler.getRawClass());
        assertEquals(simpleType.getBindings(), typeWithHandler.getBindings());
        assertNull(simpleType.getTypeHandler());
        assertSame(handler, typeWithHandler.getTypeHandler());
        assertFalse(simpleType.isStatic());
        assertFalse(typeWithHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType staticType = simpleType.withStaticTyping();
        assertEquals(simpleType, staticType);
        assertTrue(simpleType.isStatic());
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.getDefaultInstance().constructType(List.class);
        JavaType[] superInterfaces = {Integer.TYPE};
        SimpleType simpleType = new SimpleType(rawType, bindings, null, null);
        JavaType refinedType = simpleType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String[].class;
        StringBuilder expected = new StringBuilder("java.lang.Object[]");
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String[].class;
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Map.class;
        Map<String, String> map = new HashMap<>();
        StringBuilder expected = new StringBuilder("java.util.Map<java.lang.String, java.lang.String>");
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls2, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(cls1, null, null, null); // null bindings
        assertTrue(simpleType1.equals(simpleType4));

        SimpleType simpleType5 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null); // empty bindings
        assertTrue(simpleType1.equals(simpleType5));

        Set<JavaType> bindings = new HashSet<>();
        bindings.add(new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null));
        SimpleType simpleType6 = new SimpleType(cls1, bindings, null, null);
        assertTrue(simpleType1.equals(simpleType6));
    }
}