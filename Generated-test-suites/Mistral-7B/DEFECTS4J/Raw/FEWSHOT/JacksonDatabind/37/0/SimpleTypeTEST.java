package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals(0, simpleType.getHashCode());
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = new TypeBindings();
        bindings.put(String.class, new SimpleType(String[].class));

        SimpleType simpleType = new SimpleType(stringClass, bindings, null, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals(1, simpleType.getHashCode());
    }

    @Test
    public void testSimpleTypeConstructionWithSuperClassAndBindings() {
        Class<String> stringClass = String.class;
        Class<List<Integer>> listClass = List.class.getGenericType();
        TypeBindings bindings = new TypeBindings();
        bindings.put(List.class, new SimpleType(String[].class));

        JavaType superClass = new SimpleType(listClass);
        SimpleType simpleType = new SimpleType(stringClass, bindings, superClass, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertSame(superClass, simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals(1, simpleType.getHashCode());
    }

    @Test
    public void testSimpleTypeConstructionWithSuperClassAndInterfacesAndBindings() {
        Class<String> stringClass = String.class;
        Class<List<Integer>> listClass = List.class.getGenericType();
        Class<Map<String, Integer>> mapClass = Map.class.getGenericType();
        TypeBindings bindings = new TypeBindings();
        bindings.put(List.class, new SimpleType(String[].class));
        bindings.put(Map.class, new SimpleType(HashMap.class));

        JavaType superClass = new SimpleType(listClass);
        JavaType[] superInterfaces = new SimpleType[1];
        superInterfaces[0] = new SimpleType(mapClass);

        SimpleType simpleType = new SimpleType(stringClass, bindings, superClass, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertSame(superClass, simpleType.getSuperClass());
        assertSame(superInterfaces[0], simpleType.getSuperInterfaces()[0]);
        assertFalse(simpleType.isStatic());
        assertEquals(2, simpleType.getHashCode());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = new TypeBindings();
        bindings.put(String.class, new SimpleType(String[].class));

        SimpleType simpleType1 = new SimpleType(stringClass, bindings, null, null);
        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals(0, simpleType.getHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        String[] stringArray = new String[0];
        SimpleType simpleType = SimpleType.constructUnsafe(stringArray.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct() {
        Class<String> stringClass = String.class;
        SimpleType.construct(stringClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        Class<Map<String, String>> mapClass = Map.class.getGenericType();
        SimpleType.construct(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        Class<List<String>> listClass = List.class.getGenericType();
        SimpleType.construct(listClass);
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuffer.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringBuilderClass);

        JavaType narrowedType = simpleType1.narrow(StringBuilder.class);
        assertNull(narrowedType);

        narrowedType = simpleType2.narrow(String.class);
        assertEquals(simpleType1, narrowedType);
    }

    @Test
    public void testNarrowWithSuperType() {
        Class<String> stringClass = String.class;
        Class<StringBuffer> stringBufferClass = StringBuffer.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringBufferClass);
        SimpleType simpleType3 = new SimpleType(stringBuilderClass);

        JavaType narrowedType = simpleType1.narrow(StringBuffer.class);
        assertNull(narrowedType);

        narrowedType = simpleType2.narrow(StringBuilder.class);
        assertEquals(simpleType3, narrowedType);

        narrowedType = simpleType3.narrow(StringBuffer.class);
        assertNull(narrowedType);

        narrowedType = simpleType3.narrow(String.class);
        assertEquals(simpleType1, narrowedType);
    }

    @Test
    public void testSuperClassAndInterfaces() {
        Class<String> stringClass = String.class;
        Class<List<Integer>> listClass = List.class.getGenericType();
        Class<Map<String, Integer>> mapClass = Map.class.getGenericType();

        JavaType superClass = new SimpleType(listClass);
        JavaType[] superInterfaces = new SimpleType[1];
        superInterfaces[0] = new SimpleType(mapClass);

        SimpleType simpleType = new SimpleType(stringClass, null, superClass, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertSame(superClass, simpleType.getSuperClass());
        assertSame(superInterfaces[0], simpleType.getSuperInterfaces()[0]);
        assertFalse(simpleType.isStatic());
        assertEquals(3, simpleType.getHashCode());
    }

    @Test
    public void testHashCode() {
        Class<String> stringClass = String.class;
        TypeBindings bindings1 = new TypeBindings();
        TypeBindings bindings2 = new TypeBindings();

        SimpleType simpleType1 = new SimpleType(stringClass, bindings1, null, null);
        SimpleType simpleType2 = new SimpleType(stringClass, bindings2, null, null);

        assertEquals(simpleType1.hashCode(), simpleType1.hashCode());
        assertEquals(simpleType1.hashCode(), simpleType1.hashCode());
        assertEquals(simpleType1.hashCode(), simpleType1.hashCode());

        assertNotEquals(simpleType1.hashCode(), simpleType2.hashCode());
        assertNotEquals(simpleType1.hashCode(), new Object().hashCode());
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        TypeBindings bindings1 = new TypeBindings();
        TypeBindings bindings2 = new TypeBindings();

        SimpleType simpleType1 = new SimpleType(stringClass, bindings1, null, null);
        SimpleType simpleType2 = new SimpleType(stringClass, bindings2, null, null);
        SimpleType simpleType3 = new SimpleType(String[].class, null, null, null);
        Object object = new Object();

        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType1);

        assertNotEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
        assertNotEquals(simpleType1, object);

        TypeBindings bindings3 = new TypeBindings(bindings1);
        SimpleType simpleType4 = new SimpleType(stringClass, bindings3, null, null);
        assertEquals(simpleType1, simpleType4);
    }

    @Test
    public void testGetCanonicalName() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = new TypeBindings();

        SimpleType simpleType = new SimpleType(stringClass, bindings, null, null);
        assertEquals(stringClass.getName(), simpleType.getCanonicalName());

        Class<List<String>> listClass = List.class.getGenericType();
        bindings.put(String.class, simpleType);
        bindings.put(List.class, new SimpleType(String[].class));

        simpleType = new SimpleType(stringClass, bindings, null, null);
        assertEquals("[Ljava.lang.String;", simpleType.getCanonicalName());

        bindings.clear();
        bindings.put(List.class, new SimpleType(ArrayList.class));
        simpleType = new SimpleType(listClass, bindings, null, null);
        assertEquals("java.util.ArrayList", simpleType.getCanonicalName());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = new TypeBindings();

        SimpleType simpleType = new SimpleType(stringClass, bindings, null, null);
        assertEquals("[simple type, class " + stringClass.getName() + "]", simpleType.toString());

        bindings.put(String.class, new SimpleType(String[].class));
        simpleType = new SimpleType(stringClass, bindings, null, null);
        assertEquals("[simple type, class " + stringClass.getName() + "<:Ljava.lang.String;>]", simpleType.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder expected = new StringBuilder(stringClass.getName());
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<String>> listClass = List.class.getGenericType();
        SimpleType simpleType = new SimpleType(listClass);
        StringBuilder expected = new StringBuilder("<List<String>>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        TypeBindings bindings = new TypeBindings();
        bindings.put(String.class, new SimpleType(String[].class));
        bindings.put(List.class, simpleType);
        simpleType = new SimpleType(listClass, bindings, null, null);
        expected = new StringBuilder("<String>[]");
        actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = new SimpleType(String[].class);
        SimpleType simpleType = new SimpleType(String.class, null, null, null);

        try {
            simpleType.withContentType(javaType);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null);

        SimpleType simpleType2 = simpleType.withTypeHandler(handler);
        assertEquals(simpleType, simpleType2);
        assertNull(simpleType.getTypeHandler());
        assertSame(handler, simpleType2.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null);

        try {
            simpleType.withContentTypeHandler(handler);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null);

        SimpleType simpleType2 = simpleType.withValueHandler(handler);
        assertEquals(simpleType, simpleType2);
        assertNull(simpleType.getValueHandler());
        assertSame(handler, simpleType2.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null);
        SimpleType simpleTypeStatic = simpleType.withStaticTyping();
        assertFalse(simpleType.isStatic());
        assertTrue(simpleTypeStatic.isStatic());
    }

    @Test
    public void testRefine() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;

        JavaType refinedType = simpleType.refine(StringBuilder.class, null, null, null);
        assertNull(refinedType);
    }
}