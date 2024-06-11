package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    private static final int TYPE_SINGLETON_SET = 1;
    private static final int TYPE_SINGLETON_LIST = 2;
    private static final int TYPE_SINGLETON_MAP = 3;
    private static final int TYPE_UNMODIFIABLE_SET = 4;
    private static final int TYPE_UNMODIFIABLE_LIST = 5;
    private static final int TYPE_UNMODIFIABLE_MAP = 6;
    public static final int TYPE_AS_LIST = 7;

    private JavaUtilCollectionsDeserializers deserializers;

    @Before
    public void setUp() {
        deserializers = new JavaUtilCollectionsDeserializers();
    }

    @Test
    public void testFindForCollection_WhenTypeIsSingletonSet_ThenReturnStdDelegatingDeserializerWithConverterOfTypeSingletonSet() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForCollection(context, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(deserializers.converter(TYPE_SINGLETON_SET, type, Set.class), ((StdDelegatingDeserializer) result).getDelegatee());
    }

    @Test
    public void testFindForCollection_WhenTypeIsSingletonList_ThenReturnStdDelegatingDeserializerWithConverterOfTypeSingletonList() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForCollection(context, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(deserializers.converter(TYPE_SINGLETON_LIST, type, List.class), ((StdDelegatingDeserializer) result).getDelegatee());
    }

    @Test
    public void testFindForCollection_WhenTypeIsSingletonSet_ThenReturnStdDelegatingDeserializerWithConverterOfTypeSingletonMap() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForCollection(context, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(deserializers.converter(TYPE_SINGLETON_MAP, type, Map.class), ((StdDelegatingDeserializer) result).getDelegatee());
    }

    @Test
    public void testFindForCollection_WhenTypeIsUnmodifiableSet_ThenReturnStdDelegatingDeserializerWithConverterOfTypeUnmodifiableSet() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForCollection(context, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(deserializers.converter(TYPE_UNMODIFIABLE_SET, type, Set.class), ((StdDelegatingDeserializer) result).getDelegatee());
    }

    @Test
    public void testFindForCollection_WhenTypeIsUnmodifiableList_ThenReturnStdDelegatingDeserializerWithConverterOfTypeUnmodifiableList() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForCollection(context, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(deserializers.converter(TYPE_UNMODIFIABLE_LIST, type, List.class), ((StdDelegatingDeserializer) result).getDelegatee());
    }

    @Test
    public void testFindForMap_WhenTypeIsSingletonMap_ThenReturnStdDelegatingDeserializerWithConverterOfTypeSingletonMap() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForMap(context, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(deserializers.converter(TYPE_SINGLETON_MAP, type, Map.class), ((StdDelegatingDeserializer) result).getDelegatee());
    }

    @Test
    public void testFindForMap_WhenTypeIsUnmodifiableMap_ThenReturnStdDelegatingDeserializerWithConverterOfTypeUnmodifiableMap() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForMap(context, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(deserializers.converter(TYPE_UNMODIFIABLE_MAP, type, Map.class), ((StdDelegatingDeserializer) result).getDelegatee());
    }

    @Test
    public void testFindForCollection_WhenTypeIsNotSpecialCollection_ReturnNull() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForCollection(context, type);

        assertNull(result);
    }

    @Test
    public void testFindForMap_WhenTypeIsNotSpecialMap_ReturnNull() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForMap(context, type);

        assertNull(result);
    }
}