package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver_addMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test addMapping with valid arguments
        Class<Collection> collectionSuperType = Collection.class;
        Class<LinkedList> collectionSubType = LinkedList.class;
        resolver.addMapping(collectionSuperType, collectionSubType);

        // Test addMapping with invalid arguments
        Class<String> stringSuperType = String.class;
        Class<StringBuilder> stringSubType = StringBuilder.class;
        resolver.addMapping(stringSuperType, stringSubType); // Throws IllegalArgumentException

        Class<Object> objectSuperType = Object.class;
        Class<Object> objectSubType = Object.class;
        resolver.addMapping(objectSuperType, objectSubType); // Throws IllegalArgumentException

        // Test addMapping with superType and subType being the same
        Class<Number> numberSuperType = Number.class;
        Class<Number> numberSubType = Number.class;
        resolver.addMapping(numberSuperType, numberSubType); // Throws IllegalArgumentException

        // Test addMapping with superType not being abstract
        Class<List> listSuperType = List.class;
        resolver.addMapping(listSuperType, LinkedList.class); // Throws IllegalArgumentException
    }

    @Test
    public void testSimpleAbstractTypeResolver_findTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test findTypeMapping with valid mapping
        Class<Collection> collectionSuperType = Collection.class;
        Class<LinkedList> collectionSubType = LinkedList.class;
        resolver.addMapping(collectionSuperType, collectionSubType);

        JavaType collectionType = TypeFactory.defaultInstance().constructType(Collection.class, Boolean.TYPE);
        JavaType mappedType = resolver.findTypeMapping(null, collectionType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        // Test findTypeMapping with invalid mapping
        JavaType invalidCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        JavaType mappedInvalidType = resolver.findTypeMapping(null, invalidCollectionType);
        assertNull(mappedInvalidType);

        // Test findTypeMapping with no mapping
        JavaType noMappingType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType mappedNoMappingType = resolver.findTypeMapping(null, noMappingType);
        assertNull(mappedNoMappingType);
    }

    @Test
    public void testSimpleAbstractTypeResolver_resolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test resolveAbstractType with valid mapping
        Class<Collection> collectionSuperType = Collection.class;
        Class<LinkedList> collectionSubType = LinkedList.class;
        resolver.addMapping(collectionSuperType, collectionSubType);

        JavaType collectionType = TypeFactory.defaultInstance().constructType(Collection.class, Boolean.TYPE);
        JavaType resolvedType = resolver.resolveAbstractType(null, collectionType);
        assertNull(resolvedType);

        // Test resolveAbstractType with invalid mapping
        JavaType invalidCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        JavaType resolvedInvalidType = resolver.resolveAbstractType(null, invalidCollectionType);
        assertNull(resolvedInvalidType);

        // Test resolveAbstractType with no mapping
        JavaType noMappingType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType resolvedNoMappingType = resolver.resolveAbstractType(null, noMappingType);
        assertNull(resolvedNoMappingType);
    }

    @Test
    public void testSimpleAbstractTypeResolver_multipleMappings() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test multiple mappings for Collection super type
        Class<Collection> collectionSuperType = Collection.class;
        Class<LinkedList> collectionSubType1 = LinkedList.class;
        Class<ArrayList> collectionSubType2 = ArrayList.class;

        resolver.addMapping(collectionSuperType, collectionSubType1);
        resolver.addMapping(collectionSuperType, collectionSubType2);

        JavaType collectionType = TypeFactory.defaultInstance().constructType(Collection.class, Boolean.TYPE);
        JavaType mappedType1 = resolver.findTypeMapping(null, collectionType);
        assertEquals(LinkedList.class, mappedType1.getRawClass());

        JavaType mappedType2 = resolver.findTypeMapping(null, collectionType);
        assertEquals(ArrayList.class, mappedType2.getRawClass());
    }
}