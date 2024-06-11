package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsConverter;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Unit tests for {@link JavaUtilCollectionsDeserializers} class.
 */
public class JavaUtilCollectionsDeserializersTest {

    /**
     * Test method for {@link JavaUtilCollectionsDeserializers#findForCollection(DeserializationContext, JavaType)}.
     * @throws Exception if test fails
     */
    @Test
    public void testFindForCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test for singleton collections
        JavaType singletonSetType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), singletonSetType);
        assertNotNull(deserializer);
        StdDeserializer delegate = (StdDeserializer) ((StdDelegatingDeserializer) deserializer).getDelegate();
        JavaUtilCollectionsConverter converter = (JavaUtilCollectionsConverter) delegate.getConverter();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, converter.getKind());

        JavaType singletonListType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), singletonListType);
        assertNotNull(deserializer);
        delegate = (StdDeserializer) ((StdDelegatingDeserializer) deserializer).getDelegate();
        converter = (JavaUtilCollectionsConverter) delegate.getConverter();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, converter.getKind());

        JavaType singletonMapType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), singletonMapType);
        assertNotNull(deserializer);
        delegate = (StdDeserializer) ((StdDelegatingDeserializer) deserializer).getDelegate();
        converter = (JavaUtilCollectionsConverter) delegate.getConverter();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, converter.getKind());

        // Test for unmodifiable collections
        JavaType unmodifiableSetType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JavaType unmodifiableListType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableSetType);
        assertNotNull(deserializer);
        delegate = (StdDeserializer) ((StdDelegatingDeserializer) deserializer).getDelegate();
        converter = (JavaUtilCollectionsConverter) delegate.getConverter();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, converter.getKind());

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableListType);
        assertNotNull(deserializer);
        delegate = (StdDeserializer) ((StdDelegatingDeserializer) deserializer).getDelegate();
        converter = (JavaUtilCollectionsConverter) delegate.getConverter();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, converter.getKind());

        // Test for "asList"
        JavaType asListType = mapper.getTypeFactory().constructType(List.class, Integer[].class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), asListType);
        assertNotNull(deserializer);
        delegate = (StdDeserializer) ((StdDelegatingDeserializer) deserializer).getDelegate();
        converter = (JavaUtilCollectionsConverter) delegate.getConverter();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, converter.getKind());

        // Test for null input
        JavaType nullType = mapper.getTypeFactory().constructType(List.class, Object.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), nullType);
        assertNull(deserializer);
    }

    /**
     * Test method for {@link JavaUtilCollectionsDeserializers#findForMap(DeserializationContext, JavaType)}.
     * @throws Exception if test fails
     */
    @Test
    public void testFindForMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test for singleton maps
        JavaType singletonMapType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), singletonMapType);
        assertNotNull(deserializer);
        StdDeserializer delegate = (StdDeserializer) ((StdDelegatingDeserializer) deserializer).getDelegate();
        JavaUtilCollectionsConverter converter = (JavaUtilCollectionsConverter) delegate.getConverter();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, converter.getKind());

        // Test for unmodifiable maps
        JavaType unmodifiableMapType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), unmodifiableMapType);
        assertNotNull(deserializer);
        delegate = (StdDeserializer) ((StdDelegatingDeserializer) deserializer).getDelegate();
        converter = (JavaUtilCollectionsConverter) delegate.getConverter();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, converter.getKind());

        // Test for null input
        JavaType nullType = mapper.getTypeFactory().constructType(Map.class, Object.class, Object.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), nullType);
        assertNull(deserializer);
    }

    /**
     * Test method for {@link JavaUtilCollectionsConverter#convert(Object)}
     * @throws Exception if test fails
     */
    @Test
    public void testConvert() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test for singleton collections
        JavaType singletonSetType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        Set<Boolean> set = Collections.singleton(Boolean.TRUE);
        JsonDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), singletonSetType);
        Object result = mapper.readValue(mapper.writeValueAsString(set), deserializer.getType());
        assertEquals(set, result);

        JavaType singletonListType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        List<Boolean> list = Collections.singletonList(Boolean.TRUE);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), singletonListType);
        result = mapper.readValue(mapper.writeValueAsString(list), deserializer.getType());
        assertEquals(list, result);

        JavaType singletonMapType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String,String> map = Collections.singletonMap("key", "value");
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), singletonMapType);
        result = mapper.readValue(mapper.writeValueAsString(map), deserializer.getType());
        assertEquals(map, result);

        // Test for unmodifiable collections
        JavaType unmodifiableSetType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        Set<Boolean> unmodifiableSet = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableSetType);
        result = mapper.readValue(mapper.writeValueAsString(unmodifiableSet), deserializer.getType());
        assertEquals(unmodifiableSet, result);

        JavaType unmodifiableListType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        List<Boolean> unmodifiableList = Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE));
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableListType);
        result = mapper.readValue(mapper.writeValueAsString(unmodifiableList), deserializer.getType());
        assertEquals(unmodifiableList, result);

        JavaType unmodifiableMapType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String,String> unmodifiableMap = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), unmodifiableMapType);
        result = mapper.readValue(mapper.writeValueAsString(unmodifiableMap), deserializer.getType());
        assertEquals(unmodifiableMap, result);

        // Test for "asList"
        JavaType asListType = mapper.getTypeFactory().constructType(List.class, Integer[].class);
        List<Integer> asList = Arrays.asList(1, 2, 3);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), asListType);
        result = mapper.readValue(mapper.writeValueAsString(asList), deserializer.getType());
        assertEquals(asList, result);

        // Test for null input
        JavaType nullType = mapper.getTypeFactory().constructType(List.class, Object.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), nullType);
        assertNull(mapper.readValue(mapper.writeValueAsString(null), deserializer.getType()));
    }

    /**
     * Test method for {@link JavaUtilCollectionsConverter#getInputType(TypeFactory)} and {@link JavaUtilCollectionsConverter#getOutputType(TypeFactory)}.
     * @throws Exception if test fails
     */
    @Test
    public void testGetInputOutputTypes() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType inputTypeCollection = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, inputTypeCollection);
        JavaType expectedInputType = inputTypeCollection;
        JavaType expectedOutputType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaType actualInputType = converter.getInputType(mapper.getTypeFactory());
        JavaType actualOutputType = converter.getOutputType(mapper.getTypeFactory());
        assertEquals(expectedInputType, actualInputType);
        assertEquals(expectedOutputType, actualOutputType);

        JavaType inputTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsConverter mapConverter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, inputTypeMap);
        JavaType expectedMapInputType = inputTypeMap;
        JavaType expectedMapOutputType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JavaType actualMapInputType = mapConverter.getInputType(mapper.getTypeFactory());
        JavaType actualMapOutputType = mapConverter.getOutputType(mapper.getTypeFactory());
        assertEquals(expectedMapInputType, actualMapInputType);
        assertEquals(expectedMapOutputType, actualMapOutputType);
    }
}