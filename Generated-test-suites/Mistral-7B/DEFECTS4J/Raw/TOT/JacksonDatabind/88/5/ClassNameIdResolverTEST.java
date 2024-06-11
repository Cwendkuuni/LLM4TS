package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for {@link ClassNameIdResolver}.
 */
public class ClassNameIdResolverTest {

    private ObjectMapper objectMapper;
    private TypeFactory typeFactory;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        typeFactory = objectMapper.getTypeFactory();
    }

    /**
     * Test for {@link ClassNameIdResolver#idFromValue(Object, Class)}.
     */
    @Test
    public void testIdFromValue() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);
        Object value = "hello world";

        String id = resolver.idFromValue(value);
        assertEquals("java.lang.String", id);

        value = new Object();
        id = resolver.idFromValue(value);
        assertNull(id);

        value = new ArrayList<>();
        id = resolver.idFromValue(value);
        assertEquals("java.util.ArrayList", id);

        value = new HashMap<>();
        id = resolver.idFromValue(value);
        assertEquals("java.util.HashMap", id);

        value = new EnumSet.AllEnumSet(EnumSet.of(Color.RED, Color.GREEN, Color.BLUE));
        id = resolver.idFromValue(value);
        assertEquals("java.util.EnumSet", id);

        value = new EnumMap<>(Map.of(Color.RED, "red", Color.GREEN, "green", Color.BLUE, "blue"));
        id = resolver.idFromValue(value);
        assertEquals("java.util.EnumMap", id);
    }

    /**
     * Test for {@link ClassNameIdResolver#idFromValueAndType(Object, Class)}.
     */
    @Test
    public void testIdFromValueAndType() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);
        Object value = new Object();
        Class<?> type = Integer.class;

        String id = resolver.idFromValueAndType(value, type);
        assertNull(id);

        value = "hello world";
        id = resolver.idFromValueAndType(value, String.class);
        assertEquals("java.lang.String", id);

        value = new ArrayList<>();
        id = resolver.idFromValueAndType(value, List.class);
        assertEquals("java.util.ArrayList", id);

        value = new HashMap<>();
        id = resolver.idFromValueAndType(value, Map.class);
        assertEquals("java.util.HashMap", id);

        value = new EnumSet.AllEnumSet(EnumSet.of(Color.RED, Color.GREEN, Color.BLUE));
        id = resolver.idFromValueAndType(value, Set.class);
        assertEquals("java.util.EnumSet", id);

        value = new EnumMap<>(Map.of(Color.RED, "red", Color.GREEN, "green", Color.BLUE, "blue"));
        id = resolver.idFromValueAndType(value, Map.class);
        assertEquals("java.util.EnumMap", id);
    }

    /**
     * Test for {@link ClassNameIdResolver#typeFromId(DatabindContext, String)}.
     */
    @Test
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);
        String id = "java.lang.String";

        JavaType javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), id);
        assertEquals(String.class, javaType.getRawClass());

        id = "java.util.ArrayList";
        javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), id);
        assertEquals(ArrayList.class, javaType.getRawClass());

        id = "java.util.HashMap";
        javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), id);
        assertEquals(HashMap.class, javaType.getRawClass());

        id = "java.util.EnumSet";
        javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), id);
        assertEquals(EnumSet.class, javaType.getRawClass());

        id = "java.util.EnumMap";
        javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), id);
        assertEquals(EnumMap.class, javaType.getRawClass());

        id = "java.util.ArrayList<java.lang.Integer>";
        javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), id);
        assertEquals(TypeReference.getType(List.class, Integer.class), javaType);
    }

    /**
     * Test for {@link ClassNameIdResolver#_idFrom(Object, Class, TypeFactory)}.
     */
    @Test
    public void test_idFrom() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);
        Object value = new Object();
        Class<?> cls = Integer.class;

        String id = resolver._idFrom(value, cls, typeFactory);
        assertNull(id);

        value = "hello world";
        id = resolver._idFrom(value, String.class, typeFactory);
        assertEquals("java.lang.String", id);

        value = new ArrayList<>();
        id = resolver._idFrom(value, List.class, typeFactory);
        assertEquals("java.util.ArrayList", id);

        value = new HashMap<>();
        id = resolver._idFrom(value, Map.class, typeFactory);
        assertEquals("java.util.HashMap", id);

        value = new EnumSet.AllEnumSet(EnumSet.of(Color.RED, Color.GREEN, Color.BLUE));
        id = resolver._idFrom(value, Set.class, typeFactory);
        assertEquals("java.util.EnumSet", id);

        value = new EnumMap<>(Map.of(Color.RED, "red", Color.GREEN, "green", Color.BLUE, "blue"));
        id = resolver._idFrom(value, Map.class, typeFactory);
        assertEquals("java.util.EnumMap", id);
    }

    /**
     * Test for various edge cases.
     */
    @Test
    public void testEdgeCases() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);

        String id;
        try {
            id = resolver.idFromValue(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            id = resolver.idFromValue("null");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            id = resolver.idFromValueAndType(null, String.class);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            id = resolver.idFromValueAndType("null", String.class);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            id = resolver.typeFromId(objectMapper.getDeserializationContext(), null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            id = resolver.typeFromId(objectMapper.getDeserializationContext(), "");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            id = resolver.typeFromId(null, "java.util.ArrayList");
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test for various error scenarios.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testErrorScenarios() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);

        resolver.idFromValue(null);
        fail();

        resolver.idFromValue("null");

        resolver.idFromValueAndType(null, String.class);
        fail();

        resolver.idFromValueAndType("null", String.class);

        resolver.typeFromId(objectMapper.getDeserializationContext(), null);

        resolver.typeFromId(objectMapper.getDeserializationContext(), "");

        resolver.typeFromId(null, "java.util.ArrayList");

        // The following test is not an error scenario per se, but just a sanity check
        resolver.typeFromId(objectMapper.getDeserializationContext(), "java.util.ArrayList<java.lang.String>");

        // The following test is not an error scenario per se, but just a sanity check
        resolver.idFromValueAndType(new Object(), String.class);
    }
}