package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * ClassNameIdResolverTest
 */
public class ClassNameIdResolverTest {

    private TypeIdResolver resolver;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        resolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
    }

    /**
     * Test method for {@link ClassNameIdResolver#ClassNameIdResolver(JavaType, TypeFactory)} .
     */
    @Test
    public void testClassNameIdResolver() {
        assertNotNull(resolver);
    }

    /**
     * Test method for {@link ClassNameIdResolver#getMechanism()}.
     */
    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    /**
     * Test method for {@link ClassNameIdResolver#registerSubtype(Class, String)}.
     */
    @Test
    public void testRegisterSubtype() {
        // not applicable as it is not used with class name-based resolver
    }

    /**
     * Test method for {@link ClassNameIdResolver#idFromValue(Object)}.
     */
    @Test
    public void testIdFromValue() throws Exception {
        Object obj = new Object();
        String id = resolver.idFromValue(obj);
        assertNotNull(id);
        assertEquals(obj.getClass().getName(), id);
    }

    /**
     * Test method for {@link ClassNameIdResolver#idFromValueAndType(Object, Class)}.
     */
    @Test
    public void testIdFromValueAndType() throws Exception {
        Object obj = new Object();
        String id = resolver.idFromValueAndType(obj, obj.getClass());
        assertNotNull(id);
        assertEquals(obj.getClass().getName(), id);
    }

    /**
     * Test method for {@link ClassNameIdResolver#typeFromId(DatabindContext, String)}.
     */
    @Test
    public void testTypeFromId() throws Exception {
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(null, id);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    /**
     * Test method for {@link ClassNameIdResolver#_typeFromId(String, DatabindContext)}.
     */
    @Test
    public void testTypeFromIdInternal() throws Exception {
        String id = "java.util.ArrayList";
        JavaType javaType = resolver._typeFromId(id, null);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());

        id = "java.util.EnumSet";
        JavaType javaTypeEnumSet = resolver._typeFromId(id, null);
        assertNotNull(javaTypeEnumSet);
        assertEquals(EnumSet.class, javaTypeEnumSet.getRawClass());

        id = "java.util.EnumMap";
        JavaType javaTypeEnumMap = resolver._typeFromId(id, null);
        assertNotNull(javaTypeEnumMap);
        assertEquals(EnumMap.class, javaTypeEnumMap.getRawClass());

        id = "java.util.Arrays$ArrayList";
        JavaType javaTypeArrayList = resolver._typeFromId(id, null);
        assertNotNull(javaTypeArrayList);
        assertEquals(ArrayList.class, javaTypeArrayList.getRawClass());
    }

    /**
     * Test method for {@link ClassNameIdResolver#_idFrom(Object, Class, TypeFactory)}.
     */
    @Test
    public void testIdFromObjectAndClass() throws Exception {
        Object obj = new Object();
        String id = resolver._idFrom(obj, obj.getClass(), null);
        assertNotNull(id);
        assertEquals(obj.getClass().getName(), id);

        Object enumSet = EnumSet.noneOf(Apple.class);
        String idEnumSet = resolver._idFrom(enumSet, enumSet.getClass(), null);
        assertNotNull(idEnumSet);
        assertEquals(EnumSet.class.getName() + "<Apple>", idEnumSet);

        Object enumMap = new EnumMap<>(Apple.class) {{ put(Apple.GREEN, new Object()); }};
        String idEnumMap = resolver._idFrom(enumMap, enumMap.getClass(), null);
        assertNotNull(idEnumMap);
        assertEquals(EnumMap.class.getName() + "<Apple; java.lang.Object>", idEnumMap);
    }

    /**
     * Test method for {@link ClassNameIdResolver#getDescForKnownTypeIds()}.
     */
    @Test
    public void testGetDescForKnownTypeIds() {
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    /**
     * Test method for handling {@code ClassNotFoundException} when trying to
     * resolve type by name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithClassNotFoundException() throws Exception {
        String id = "com.non.existing.package.NonExistingClass";
        resolver.typeFromId(null, id);
    }

    /**
     * Test method for handling {@code IOException} when trying to resolve type by name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithIOException() throws Exception {
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver$TestIOException";
        resolver.typeFromId(null, id);
    }

    /**
     * Test method for handling {@code IllegalArgumentException} when invalid type id is provided.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidTypeId() throws Exception {
        String id = "invalidTypeId";
        resolver.typeFromId(null, id);
    }

    /**
     * Test method for handling unknown types when trying to deserialize.
     */
    @Test
    public void testUnknownType() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\": \"test\"}";
        ObjectNode node = objectMapper.readValue(json, ObjectNode.class);
        node.put("typeId", "com.non.existing.package.NonExistingClass");

        try {
            Object result = objectMapper.readValue(node, String.class);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test method for handling custom deserializer when trying to deserialize.
     */
    @Test
    public void testCustomDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = new StubDeserializer();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerDeserializer(String.class, customDeserializer);

        String json = "{\"name\": \"test\"}";
        ObjectNode node = objectMapper.readValue(json, ObjectNode.class);
        node.put("typeId", String.class.getName());

        String result = objectMapper.readValue(node, String.class);
        assertEquals("Custom deserializer was called", "test", result);
    }

    /**
     * Test method for handling {@code ClassCastException} when trying to deserialize.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithClassCastException() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\": \"test\"}";
        ObjectNode node = objectMapper.readValue(json, ObjectNode.class);
        node.put("typeId", "java.util.ArrayList<java.lang.Object>");

        try {
            Object result = objectMapper.readValue(node, Integer.class);
            fail("Should have thrown exception");
        } catch (IOException e) {
            // expected
            assertEquals("Can not instantiate value of type [simple type, class java.util.ArrayList], " +
                         "not an actual java type (note: Java classes represent concrete classes, " +
                         "not abstract types): [simple type, class java.util.ArrayList<java.lang.Object>]",
                         e.getMessage());
        }
    }

    private static class StubDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "Custom deserializer was called";
        }
    }

    private enum Apple {
        GREEN,
        RED
    }
}