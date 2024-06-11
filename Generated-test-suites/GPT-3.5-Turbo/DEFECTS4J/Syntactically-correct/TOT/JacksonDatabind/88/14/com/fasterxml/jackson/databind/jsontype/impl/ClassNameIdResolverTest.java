package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClassNameIdResolverTest {
    private ClassNameIdResolver classNameIdResolver;

    @Before
    public void setUp() {
        classNameIdResolver = new ClassNameIdResolver(null, null);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // No implementation needed for this method, so no test case
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        assertEquals("java.lang.Object", classNameIdResolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        assertEquals("java.lang.Object", classNameIdResolver.idFromValueAndType(value, Object.class));
    }

    @Test
    public void testTypeFromId() {
        DatabindContext context = null;
        String id = "java.lang.String";
        try {
            assertNotNull(classNameIdResolver.typeFromId(context, id));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdInvalid() {
        DatabindContext context = null;
        String id = "invalidType";
        try {
            assertNull(classNameIdResolver.typeFromId(context, id));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdClassNotFoundException() {
        DatabindContext context = null;
        String id = "com.example.NonExistingClass";
        try {
            assertNull(classNameIdResolver.typeFromId(context, id));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdDeserializationContext() {
        DatabindContext context = new DeserializationContext(null, null, null, null);
        String id = "com.example.NonExistingClass";
        try {
            assertNull(classNameIdResolver.typeFromId(context, id));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdDeserializationContextHandleUnknownTypeId() {
        DatabindContext context = new DeserializationContext(null, null, null, null);
        String id = "com.example.NonExistingClass";
        try {
            assertNotNull(classNameIdResolver.typeFromId(context, id));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdIllegalArgumentException() {
        DatabindContext context = null;
        String id = null;
        try {
            assertNull(classNameIdResolver.typeFromId(context, id));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdIllegalArgumentExceptionInvalidType() {
        DatabindContext context = new DeserializationContext(null, null, null, null);
        String id = null;
        try {
            assertNull(classNameIdResolver.typeFromId(context, id));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void test_idFrom() {
        Object value = new Object();
        String id = classNameIdResolver._idFrom(value, value.getClass(), null);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void test_idFromEnumSubtype() {
        EnumSubtype value = EnumSubtype.ONE;
        String id = classNameIdResolver._idFrom(value, value.getClass(), null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$EnumType", id);
    }

    @Test
    public void test_idFromEnumSubtypeSuperclass() {
        EnumSubtype value = EnumSubtypeSuperclass.ONE;
        String id = classNameIdResolver._idFrom(value, value.getClass(), null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$EnumTypeSuperclass", id);
    }

    @Test
    public void test_idFromJavaUtilClass() {
        List<String> value = new ArrayList<>();
        String id = classNameIdResolver._idFrom(value, value.getClass(), null);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFromJavaUtilEnumSet() {
        EnumSet<EnumSubtype> value = EnumSet.of(EnumSubtype.ONE);
        String id = classNameIdResolver._idFrom(value, value.getClass(), null);
        assertEquals("java.util.EnumSet", id);
    }

    @Test
    public void test_idFromJavaUtilEnumMap() {
        EnumMap<EnumSubtype, String> value = new EnumMap<>(EnumSubtype.class);
        String id = classNameIdResolver._idFrom(value, value.getClass(), null);
        assertEquals("java.util.EnumMap", id);
    }

    @Test
    public void test_idFromJavaUtilArraysList() {
        List<String> value = Arrays.asList("one", "two");
        String id = classNameIdResolver._idFrom(value, value.getClass(), null);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFromJavaUtilCollectionsSingletonList() {
        List<String> value = Collections.singletonList("one");
        String id = classNameIdResolver._idFrom(value, value.getClass(), null);
        assertEquals("java.util.ArrayList", id);
    }

    private enum EnumType {
        ONE, TWO, THREE
    }

    private static class EnumSubtype extends EnumType {}

    private static abstract class EnumTypeSuperclass extends EnumType {}

}