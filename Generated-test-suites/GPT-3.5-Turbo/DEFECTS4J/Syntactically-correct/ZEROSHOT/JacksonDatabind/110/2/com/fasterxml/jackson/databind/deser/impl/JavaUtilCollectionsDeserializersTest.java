package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        // Test for COLLECTION
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, collectionType);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForMap() {
        // Test for MAP
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(null, mapType);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testConverter() {
        // Test for converter
        JavaType setType = TypeFactory.defaultInstance().constructCollectionType(Set.class, String.class);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, setType, Set.class);
        assertTrue(converter instanceof JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, converter._kind);
        assertEquals(setType, converter._inputType);
    }
}