package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionSingletonSet() {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer<>(), new TypeFactory());
        JavaType type = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionSingletonList() {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer<>(), new TypeFactory());
        JavaType type = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionSingletonMap() {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer<>(), new TypeFactory());
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer<>(), new TypeFactory());
        JavaType type = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE)));
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionUnmodifiableList() {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer<>(), new TypeFactory());
        JavaType type = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, Arrays.asList(Boolean.TRUE));
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer<>(), new TypeFactory());
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, new HashMap<String, String>() {{ put("key", "value"); }});
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionAsList() {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer<>(), new TypeFactory());
        JavaType type = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMapSingletonMap() {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer<>(), new TypeFactory());
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForMapUnmodifiableMap() {
        DeserializationContext ctxt = new DeserializationContext(new StandardDeserializer<>(), new TypeFactory());
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, new HashMap<String, String>() {{ put("key", "value"); }});
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }
}