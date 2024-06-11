
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type1 = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JsonDeserializer<?> deserializer1 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type1);
        assertNotNull(deserializer1);

        JavaType type2 = TypeFactory.defaultInstance().constructType(LinkedList.class);
        JsonDeserializer<?> deserializer2 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type2);
        assertNotNull(deserializer2);

        JavaType type3 = TypeFactory.defaultInstance().constructType(HashSet.class);
        JsonDeserializer<?> deserializer3 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type3);
        assertNotNull(deserializer3);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type1 = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class);
        JsonDeserializer<?> deserializer1 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type1);
        assertNotNull(deserializer1);

        JavaType type2 = TypeFactory.defaultInstance().constructMapType(TreeMap.class, String.class, Integer.class);
        JsonDeserializer<?> deserializer2 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type2);
        assertNotNull(deserializer2);
    }

    @Test
    public void testConverter() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        Class<?> rawSuper = List.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, concreteType, rawSuper);
        assertNotNull(converter);
    }

    @Test
    public void testJavaUtilCollectionsConverter() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, null);

        List<Integer> list = new ArrayList<>();
        list.add(1);

        Object result = converter.convert(list);
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(1, ((List) result).size());
    }

    @Test
    public void testJavaUtilCollectionsConverterInputType() {
        JavaType inputType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);

        TypeFactory typeFactory = TypeFactory.defaultInstance();

        JavaType result = converter.getInputType(typeFactory);
        assertNotNull(result);
        assertEquals(inputType, result);
    }

    @Test
    public void testJavaUtilCollectionsConverterOutputType() {
        JavaType inputType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);

        TypeFactory typeFactory = TypeFactory.defaultInstance();

        JavaType result = converter.getOutputType(typeFactory);
        assertNotNull(result);
        assertEquals(inputType, result);
    }

}
