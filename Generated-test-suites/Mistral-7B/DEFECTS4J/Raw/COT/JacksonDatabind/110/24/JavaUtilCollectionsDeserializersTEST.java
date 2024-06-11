package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;
import java.util.Collections;
import java.util.Arrays;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.*;

public class JavaUtilCollectionsDeserializersTest {

    @BeforeClass
    public static void init() {
        // Nothing to do here.
    }

    @AfterClass
    public static void cleanup() {
        // Nothing to do here.
    }

    @Test
    public void testFindForCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(List.class);

        // Test for asList
        Object result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = mapper.getTypeFactory().constructType(Collections.singletonList(String.class).getType());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for singletonList
        type = mapper.getTypeFactory().constructType(Collections.singletonList(String.class).getType());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for singletonSet
        type = mapper.getTypeFactory().constructType(Collections.singleton(String.class).getType());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for unmodifiableList
        type = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Arrays.asList("a", "b")).getType());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for unmodifiableSet
        type = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Arrays.asList("a", "b")).getType());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for unmodifiableMap
        type = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getType());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for invalid types
        type = mapper.getTypeFactory().constructType(int.class);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result);

        type = mapper.getTypeFactory().constructType(Map.class);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result);
    }

    @Test
    public void testFindForMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(Map.class);

        // Test for singletonMap
        Object result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for unmodifiableMap
        type = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getType());
        result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for invalid types
        type = mapper.getTypeFactory().constructType(List.class);
        result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(result);

        type = mapper.getTypeFactory().constructType(Set.class);
        result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(result);
    }
}