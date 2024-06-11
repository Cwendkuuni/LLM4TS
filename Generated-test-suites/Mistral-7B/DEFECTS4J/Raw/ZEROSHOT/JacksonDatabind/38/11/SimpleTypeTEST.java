package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());

        try {
            SimpleType simpleType2 = SimpleType.construct(ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        Class<String> cls = String.class;
        SimpleType stringType = new SimpleType(cls);
        SimpleType stringSubType = new SimpleType(String.class.getSuperclass());

        SimpleType narrowedType = stringType.narrow(StringSubclass.class);
        assertNull(narrowedType);

        narrowedType = stringType.narrow(String.class);
        assertEquals(stringType, narrowedType);

        narrowedType = stringType.narrow(stringSubType.getRawClass());
        assertEquals(stringSubType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        try {
            simpleType.withContentType(Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        Object handler = new Object() {
            // Empty handler
        };

        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        assertNotNull(typeWithHandler);
        assertEquals(simpleType.getRawClass(), typeWithHandler.getRawClass());
        assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        Object handler = new Object() {
            // Empty handler
        };

        try {
            simpleType.withContentTypeHandler(handler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        Object handler = new Object() {
            // Empty handler
        };

        SimpleType typeWithHandler = simpleType.withValueHandler(handler);
        assertNotNull(typeWithHandler);
        assertEquals(simpleType.getRawClass(), typeWithHandler.getRawClass());
        assertEquals(handler, typeWithHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        SimpleType staticType = simpleType.withStaticTyping();
        assertNotNull(staticType);
        assertEquals(simpleType.getRawClass(), staticType.getRawClass());
        assertTrue(staticType.isStatic());

        SimpleType nonStaticType = simpleType.withStaticTyping();
        assertNotNull(nonStaticType);
        assertEquals(simpleType.getRawClass(), nonStaticType.getRawClass());
        assertFalse(nonStaticType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        Class<StringBuilder> stringBuilderClass = StringBuffer.class;
        SimpleType stringBuilderType = new SimpleType(stringBuilderClass);

        JavaType refinedType = simpleType.refine(StringBuilder.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        assertEquals("java.lang.String", simpleType.buildCanonicalName());

        SimpleType arrayType = new SimpleType(int[].class);
        assertEquals("int[]", arrayType.buildCanonicalName());

        SimpleType listType = new SimpleType(ArrayList.class);
        TypeBindings bindings = TypeBindings.mapType(Integer.class, 0);
        SimpleType listOfIntType = new SimpleType(cls, bindings, null, null);
        assertEquals("java.util.ArrayList<java.lang.Integer>", listType.buildCanonicalName());

        SimpleType mapType = new SimpleType(HashMap.class);
        bindings = TypeBindings.mapKeyType(String.class);
        bindings = TypeBindings.mapValueType(Integer.class);
        SimpleType mapOfStringToIntType = new SimpleType(cls, bindings, null, null);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertFalse(simpleType.isContainerType());

        SimpleType arrayType = new SimpleType(int[].class);
        assertTrue(arrayType.isContainerType());

        SimpleType listType = new SimpleType(ArrayList.class);
        assertTrue(listType.isContainerType());

        SimpleType mapType = new SimpleType(HashMap.class);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        StringBuilder sb = new StringBuilder(40);
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        StringBuilder sb = new StringBuilder(40);
        simpleType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        SimpleType listType = new SimpleType(ArrayList.class);
        TypeBindings bindings = TypeBindings.mapType(Integer.class, 0);
        SimpleType listOfIntType = new SimpleType(cls, bindings, null, null);
        StringBuilder sbList = new StringBuilder(100);
        listOfIntType.getGenericSignature(sbList);
        assertEquals("java.util.ArrayList<java.lang.Integer>", sbList.toString());

        SimpleType mapType = new SimpleType(HashMap.class);
        bindings = TypeBindings.mapKeyType(String.class);
        bindings = TypeBindings.mapValueType(Integer.class);
        SimpleType mapOfStringToIntType = new SimpleType(cls, bindings, null, null);
        StringBuilder sbMap = new StringBuilder(100);
        mapOfStringToIntType.getGenericSignature(sbMap);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", sbMap.toString());
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        String toString = simpleType.toString();
        assertEquals("[simple type, class java.lang.String]", toString);
    }

    @Test
    public void testEquals() {
        Class<String> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(cls);

        assertTrue(simpleType1.equals(simpleType2));

        Object handler = new Object() {
            // Empty handler
        };

        SimpleType simpleTypeWithHandler = new SimpleType(cls, handler);
        assertFalse(simpleType1.equals(simpleTypeWithHandler));

        SimpleType simpleTypeWithDifferentClass = new SimpleType(Integer.class);
        assertFalse(simpleType1.equals(simpleTypeWithDifferentClass));
    }

    private static class StringSubclass extends String {
        // Empty class
    }

    private static class ArrayList extends AbstractList<Object> {
        // Empty class
    }

    private static class StringBuffer {
        // Empty class
    }

    private static class HashMap extends AbstractMap<String, Object> {
        // Empty class
    }
}