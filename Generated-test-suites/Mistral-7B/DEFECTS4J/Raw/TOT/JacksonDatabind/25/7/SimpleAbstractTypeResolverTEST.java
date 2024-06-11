package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SimpleAbstractTypeResolverTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
                resolver.addMapping(Collection.class, LinkedList.class);
                resolver.addMapping(List.class, LinkedList.class);
                context.addBean(AbstractTypeResolver.StdResolver.class, resolver);
            }
        });
    }

    @Test
    public void testCollectionMapping() throws Exception {
        SimpleAbstractTypeResolver resolver = objectMapper.getFactory().getInjectableValue(AbstractTypeResolver.StdResolver.class);

        JavaType collectionType = new TypeReference<List<String>>(){}.getType();
        JavaType linkedListType = new TypeReference<LinkedList<String>>(){}.getType();

        DeserializationConfig config = new DeserializationConfig();
        JavaType resolvedType = resolver.findTypeMapping(config, collectionType);

        assertEquals(linkedListType, resolvedType);
    }

    @Test
    public void testListMapping() throws Exception {
        SimpleAbstractTypeResolver resolver = objectMapper.getFactory().getInjectableValue(AbstractTypeResolver.StdResolver.class);

        JavaType listType = new TypeReference<List<Integer>>(){}.getType();
        JavaType linkedListType = new TypeReference<LinkedList<Integer>>(){}.getType();

        DeserializationConfig config = new DeserializationConfig();
        JavaType resolvedType = resolver.findTypeMapping(config, listType);

        assertEquals(linkedListType, resolvedType);
    }

    @Test
    public void testInvalidMapping() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        try {
            resolver.addMapping(String.class, Integer.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.String to java.lang.Integer, as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testMappingWithSameClass() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        try {
            resolver.addMapping(Integer.class, Integer.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.Integer to java.lang.Integer", e.getMessage());
        }
    }

    @Test
    public void testMappingWithNonAbstractClass() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        try {
            resolver.addMapping(List.class, ArrayList.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.List to class java.util.ArrayList, since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testMappingWithEmptyList() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(List.class, Collections.emptyList().getClass());

        JavaType listType = new TypeReference<List<String>>(){}.getType();
        JavaType emptyListType = new TypeReference<List<String>>(){}.getType();

        DeserializationConfig config = new DeserializationConfig();
        JavaType resolvedType = resolver.findTypeMapping(config, listType);

        assertEquals(emptyListType, resolvedType);
    }

    @Test
    public void testMappingWithNullList() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        try {
            resolver.addMapping(List.class, null);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testMappingWithArray() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        try {
            resolver.addMapping(int[].class, int[].class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class [I to [I, as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testMappingWithGenericType() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List<String>> listClass = List.class.getTypeParameters()[0].getType();
        Class<LinkedList<String>> linkedListClass = LinkedList.class.getTypeParameters()[0].getType();

        resolver.addMapping(listClass, linkedListClass);

        JavaType listType = new TypeReference<List<String>>(){}.getType();
        JavaType linkedListType = new TypeReference<LinkedList<String>>(){}.getType();

        DeserializationConfig config = new DeserializationConfig();
        JavaType resolvedType = resolver.findTypeMapping(config, listType);

        assertEquals(linkedListType, resolvedType);
    }

    @Test
    public void testMappingWithArrayOfGenericType() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List<String>> listClass = List.class.getTypeParameters()[0].getType();
        Class<LinkedList<String>> linkedListClass = LinkedList.class.getTypeParameters()[0].getType();

        Class<List<List<String>>> listOfListClass = List.class.getGenericSuperclass();
        Type listOfListType = new TypeReference<List<List<String>>>(){}
                .getType();

        resolver.addMapping(listOfListClass, new TypeReference<List<LinkedList<String>>>(){}
                .getType());

        JavaType listOfListType = new TypeReference<List<List<String>>>(){}
                .getType();
        JavaType listOfLinkedListType = new TypeReference<List<LinkedList<String>>>(){}
                .getType();

        DeserializationConfig config = new DeserializationConfig();
        JavaType resolvedType = resolver.findTypeMapping(config, listOfListType);

        assertEquals(listOfLinkedListType, resolvedType);
    }
}