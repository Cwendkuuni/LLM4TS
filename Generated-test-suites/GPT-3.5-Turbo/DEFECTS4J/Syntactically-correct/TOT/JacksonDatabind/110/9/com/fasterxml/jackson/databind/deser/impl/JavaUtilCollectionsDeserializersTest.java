package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUtilCollectionsDeserializersTest {

    private DeserializationContext ctxt;
    private JavaType type;
    
    @Before
    public void setup() {
        ctxt = new DeserializationContext();
        type = TypeFactory.defaultInstance().constructType(String.class);
    }

    @Test
    public void testFindForCollection_CollectionType_List_AsArraysList() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructCollectionLikeType(List.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, ((StdDelegatingDeserializer<?>) deserializer).getDelegatee().getKind());
    }

    @Test
    public void testFindForCollection_CollectionType_List_AsSingletonList() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructCollectionLikeType(List.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, ((StdDelegatingDeserializer<?>) deserializer).getDelegatee().getKind());
    }

    @Test
    public void testFindForCollection_CollectionType_Set_AsSingletonSet() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructCollectionLikeType(Set.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, ((StdDelegatingDeserializer<?>) deserializer).getDelegatee().getKind());
    }

    @Test
    public void testFindForCollection_CollectionType_List_AsUnmodifiableList() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructCollectionLikeType(List.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, ((StdDelegatingDeserializer<?>) deserializer).getDelegatee().getKind());
    }

    @Test
    public void testFindForCollection_CollectionType_Set_AsUnmodifiableSet() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructCollectionLikeType(Set.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, ((StdDelegatingDeserializer<?>) deserializer).getDelegatee().getKind());
    }

    @Test
    public void testFindForCollection_CollectionType_NotSupported() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_MapType_Map_AsSingletonMap() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, ((StdDelegatingDeserializer<?>) deserializer).getDelegatee().getKind());
    }

    @Test
    public void testFindForMap_MapType_Map_AsUnmodifiableMap() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, ((StdDelegatingDeserializer<?>) deserializer).getDelegatee().getKind());
    }

    @Test
    public void testFindForMap_MapType_NotSupported() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructCollectionLikeType(Set.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testConvert_SingletonSet() {
        Set<String> set = Collections.singleton("value");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type);
        Object convertedValue = converter.convert(set);
        assertTrue(convertedValue instanceof Set);
        Set<?> convertedSet = (Set<?>) convertedValue;
        assertEquals(1, convertedSet.size());
        assertEquals("value", convertedSet.iterator().next());
    }

    @Test
    public void testConvert_SingletonList() {
        List<String> list = Collections.singletonList("value");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, type);
        Object convertedValue = converter.convert(list);
        assertTrue(convertedValue instanceof List);
        List<?> convertedList = (List<?>) convertedValue;
        assertEquals(1, convertedList.size());
        assertEquals("value", convertedList.get(0));
    }

    @Test
    public void testConvert_SingletonMap() {
        Map<String, String> map = Collections.singletonMap("key", "value");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, type);
        Object convertedValue = converter.convert(map);
        assertTrue(convertedValue instanceof Map);
        Map<?, ?> convertedMap = (Map<?, ?>) convertedValue;
        assertEquals(1, convertedMap.size());
        assertTrue(convertedMap.containsKey("key"));
        assertEquals("value", convertedMap.get("key"));
    }

    @Test
    public void testConvert_UnmodifiableSet() {
        Set<String> set = Collections.unmodifiableSet(Collections.singleton("value"));
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, type);
        Object convertedValue = converter.convert(set);
        assertTrue(convertedValue instanceof Set);
        Set<?> convertedSet = (Set<?>) convertedValue;
        assertEquals(1, convertedSet.size());
        assertEquals("value", convertedSet.iterator().next());
    }

    @Test
    public void testConvert_UnmodifiableList() {
        List<String> list = Collections.unmodifiableList(Collections.singletonList("value"));
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, type);
        Object convertedValue = converter.convert(list);
        assertTrue(convertedValue instanceof List);
        List<?> convertedList = (List<?>) convertedValue;
        assertEquals(1, convertedList.size());
        assertEquals("value", convertedList.get(0));
    }

    @Test
    public void testConvert_UnmodifiableMap() {
        Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, type);
        Object convertedValue = converter.convert(map);
        assertTrue(convertedValue instanceof Map);
        Map<?, ?> convertedMap = (Map<?, ?>) convertedValue;
        assertEquals(1, convertedMap.size());
        assertTrue(convertedMap.containsKey("key"));
        assertEquals("value", convertedMap.get("key"));
    }

    @Test
    public void testConvert_AsList() {
        List<String> list = Arrays.asList("value1", "value2");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, type);
        Object convertedValue = converter.convert(list);
        assertTrue(convertedValue instanceof List);
        List<?> convertedList = (List<?>) convertedValue;
        assertEquals(2, convertedList.size());
        assertEquals("value1", convertedList.get(0));
        assertEquals("value2", convertedList.get(1));
    }

    @Test
    public void testGetInputType() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, type);
        JavaType inputType = converter.getInputType(TypeFactory.defaultInstance());
        assertEquals(type, inputType);
    }

    @Test
    public void testGetOutputType() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, type);
        JavaType outputType = converter.getOutputType(TypeFactory.defaultInstance());
        assertEquals(type, outputType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSingleton_Failure() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type);
        converter._checkSingleton(2);
    }

    @Test
    public void testCheckSingleton_Success() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type);
        converter._checkSingleton(1);
    }
}