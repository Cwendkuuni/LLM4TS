package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final ClassNameIdResolver resolver = new ClassNameIdResolver(null, new TypeFactory());

    @Test
    public void testIdFromValue() {
        Object value;
        String expectedId;
        try {
            value = new Object();
            expectedId = resolver.idFromValue(value);
            assertEquals("java.lang.Object", expectedId);
        } catch (Exception e) {
            throw new TestCaseFailureException("Unexpected exception: " + e.getMessage());
        }

        value = new Enum[] { Enum.valueOf(String.class, "VALUE") };
        expectedId = resolver.idFromValue(value);
        assertEquals("java.lang.Enum[]", expectedId);

        value = new ArrayList<>() {{ add(1); }};
        expectedId = resolver.idFromValue(value);
        assertEquals("java.util.ArrayList", expectedId);

        value = new HashMap<>() {{ put(1, "value"); }};
        expectedId = resolver.idFromValue(value);
        assertEquals("java.util.HashMap", expectedId);

        value = new EnumSet<Enum>() {{ add(Enum.valueOf(String.class, "VALUE")); }};
        expectedId = resolver.idFromValue(value);
        assertEquals("java.util.EnumSet", expectedId);

        value = new EnumMap<Enum, String>() {{ put(Enum.valueOf(String.class, "KEY"), "value"); }};
        expectedId = resolver.idFromValue(value);
        assertEquals("java.util.EnumMap", expectedId);

        value = Arrays.asList(new Object[] { new Object() });
        expectedId = resolver.idFromValue(value);
        assertEquals("java.util.ArrayList", expectedId);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value;
        Class<?> type;
        String expectedId;
        try {
            value = new Object();
            type = Object.class;
            expectedId = resolver.idFromValueAndType(value, type);
            assertEquals("java.lang.Object", expectedId);
        } catch (Exception e) {
            throw new TestCaseFailureException("Unexpected exception: " + e.getMessage());
        }

        value = new Enum[] { Enum.valueOf(String.class, "VALUE") };
        type = Enum[].class;
        expectedId = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.Enum[]", expectedId);

        value = new ArrayList<>() {{ add(1); }};
        type = ArrayList.class;
        expectedId = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.ArrayList", expectedId);

        value = new HashMap<>() {{ put(1, "value"); }};
        type = HashMap.class;
        expectedId = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.HashMap", expectedId);

        value = new EnumSet<Enum>() {{ add(Enum.valueOf(String.class, "VALUE")); }};
        type = EnumSet.class;
        expectedId = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.EnumSet", expectedId);

        value = new EnumMap<Enum, String>() {{ put(Enum.valueOf(String.class, "KEY"), "value"); }};
        type = EnumMap.class;
        expectedId = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.EnumMap", expectedId);

        value = Arrays.asList(new Object[] { new Object() });
        type = List.class;
        expectedId = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.ArrayList", expectedId);
    }

    @Test
    public void testTypeFromId() {
        String id;
        JavaType expectedType;
        try {
            id = "java.lang.Object";
            expectedType = resolver.typeFromId(null, id);
            assertEquals(Object.class, expectedType.getRawClass());
        } catch (Exception e) {
            throw new TestCaseFailureException("Unexpected exception: " + e.getMessage());
        }

        id = "java.lang.Enum[]";
        try {
            expectedType = resolver.typeFromId(null, id);
            assertEquals(Enum[].class, expectedType.getRawClass());
        } catch (IOException e) {
            throw new TestCaseFailureException("Unexpected exception: " + e.getMessage());
        }

        id = "java.util.ArrayList";
        try {
            expectedType = resolver.typeFromId(null, id);
            assertEquals(ArrayList.class, expectedType.getRawClass());
        } catch (IOException e) {
            throw new TestCaseFailureException("Unexpected exception: " + e.getMessage());
        }

        id = "java.util.HashMap";
        try {
            expectedType = resolver.typeFromId(null, id);
            assertEquals(HashMap.class, expectedType.getRawClass());
        } catch (IOException e) {
            throw new TestCaseFailureException("Unexpected exception: " + e.getMessage());
        }

        id = "java.util.EnumSet";
        try {
            expectedType = resolver.typeFromId(null, id);
            assertEquals(EnumSet.class, expectedType.getRawClass());
        } catch (IOException e) {
            throw new TestCaseFailureException("Unexpected exception: " + e.getMessage());
        }

        id = "java.util.EnumMap";
        try {
            expectedType = resolver.typeFromId(null, id);
            assertEquals(EnumMap.class, expectedType.getRawClass());
        } catch (IOException e) {
            throw new TestCaseFailureException("Unexpected exception: " + e.getMessage());
        }

        id = "java.util.ArrayList<java.lang.String>";
        try {
            expectedType = resolver.typeFromId(null, id);
            assertEquals(ArrayList.class, expectedType.getRawClass());
            assertEquals(String.class, expectedType.getContentType());
        } catch (IOException e) {
            throw new TestCaseFailureException("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testIdFromValueAndUnknownClass() {
        Object value = new Object() {{
            // A dummy class that is not present in the classpath
            // to test the deserialization context handling
            static class UnknownClass {}
            this.clazz = UnknownClass.class;
        }};
        String id;
        try {
            id = resolver.idFromValue(value);
            fail("Expected an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}