package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JavaType javaTypeSingletonListInteger = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType javaTypeSingletonSetString = TypeFactory.defaultInstance().constructType(Set.class, String.class);
        JavaType javaTypeUnmodifiableListInteger = TypeFactory.defaultInstance().constructType(Collection.class, Integer.class);
        JavaType javaTypeUnmodifiableSetString = TypeFactory.defaultInstance().constructType(Collection.class, String.class);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeSingletonListInteger);
        assertNotNull(deserializer);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeSingletonSetString);
        assertNotNull(deserializer);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeUnmodifiableListInteger);
        assertNotNull(deserializer);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeUnmodifiableSetString);
        assertNotNull(deserializer);

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, TypeFactory.defaultInstance().constructType(List.class, String[].class));
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JavaType javaTypeSingletonMapStringString = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaType javaTypeUnmodifiableMapStringString = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeSingletonMapStringString);
        assertNotNull(deserializer);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeUnmodifiableMapStringString);
        assertNotNull(deserializer);

        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, TypeFactory.defaultInstance().constructType(Map.class, Number.class, String.class));
        assertNull(deserializer);

        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, new TypeReference<Map<String, ObjectNode>>() {}.getType());
        assertNotNull(deserializer);
    }

    @Test
    public void testConverter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JavaType javaTypeSingletonSetString = TypeFactory.defaultInstance().constructType(Set.class, String.class);
        JavaType javaTypeSingletonListInteger = TypeFactory.defaultInstance().constructType(List.class, Integer.class);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeSingletonListInteger);
        StdDelegatingDeserializer delegatingDeserializer = (StdDelegatingDeserializer) deserializer;
        JavaUtilCollectionsConverter converter = (JavaUtilCollectionsConverter) delegatingDeserializer.getDelegate();

        Object input = new ArrayList<Object>(Arrays.asList("foo", "bar"));
        Object output = converter.convert(input);
        assertEquals(input, output);
        assertEquals(List.class, output.getClass());

        input = new HashSet<String>(Arrays.asList("foo", "bar"));
        output = converter.convert(input);
        assertEquals(input, output);
        assertEquals(Set.class, output.getClass());
    }
}