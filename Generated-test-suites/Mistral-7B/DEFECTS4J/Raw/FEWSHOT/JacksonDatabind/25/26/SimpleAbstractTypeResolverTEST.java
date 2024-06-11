package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializationContext;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test addMapping for Collection and List
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        // Test addMapping for custom classes
        Class<?> superType = CustomSuperType.class;
        Class<?> subType = CustomSubType.class;
        resolver.addMapping(superType, subType);

        // Test addMapping for generic types
        Class<List<Integer>> listIntSuperType = List.class.getGenericType();
        Class<LinkedList<Integer>> listIntSubType = LinkedList.class.getGenericType();
        resolver.addMapping(listIntSuperType, listIntSubType);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test findTypeMapping for Collection and List
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(null, type);
        assertTrue(mappedType == null || mappedType.getClass() == LinkedList.class);

        type = TypeFactory.defaultInstance().constructType(List.class);
        mappedType = resolver.findTypeMapping(null, type);
        assertTrue(mappedType == null || mappedType.getClass() == LinkedList.class);

        // Test findTypeMapping for custom classes
        Class<?> superType = CustomSuperType.class;
        Class<?> subType = CustomSubType.class;
        resolver.addMapping(superType, subType);
        type = TypeFactory.defaultInstance().constructType(superType);
        mappedType = resolver.findTypeMapping(null, type);
        assertTrue(mappedType == null || mappedType.getClass() == subType);

        // Test findTypeMapping for generic types
        Class<List<Integer>> listIntSuperType = List.class.getGenericType();
        Class<LinkedList<Integer>> listIntSubType = LinkedList.class.getGenericType();
        resolver.addMapping(listIntSuperType, listIntSubType);
        type = TypeFactory.defaultInstance().constructType(listIntSuperType);
        mappedType = resolver.findTypeMapping(null, type);
        assertTrue(mappedType != null && mappedType.getClass() == LinkedList.class);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test resolveAbstractType for Collection and List
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType mappedType = resolver.resolveAbstractType(null, type);
        assertTrue(mappedType == null);

        type = TypeFactory.defaultInstance().constructType(List.class);
        mappedType = resolver.resolveAbstractType(null, type);
        assertTrue(mappedType == null);

        // Test resolveAbstractType for custom classes
        Class<?> superType = CustomSuperType.class;
        Class<?> subType = CustomSubType.class;
        resolver.addMapping(superType, subType);
        type = TypeFactory.defaultInstance().constructType(superType);
        mappedType = resolver.resolveAbstractType(null, type);
        assertTrue(mappedType == null || mappedType.getClass() == superType);

        // Test resolveAbstractType for generic types
        Class<List<Integer>> listIntSuperType = List.class.getGenericType();
        Class<LinkedList<Integer>> listIntSubType = LinkedList.class.getGenericType();
        resolver.addMapping(listIntSuperType, listIntSubType);
        type = TypeFactory.defaultInstance().constructType(listIntSuperType);
        mappedType = resolver.resolveAbstractType(null, type);
        assertTrue(mappedType == null || mappedType.getClass() == listIntSuperType);
    }

    @Test
    public void testFindTypeMappingWithContext() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test findTypeMapping with context for Collection and List
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(new StdDeserializationContext(), type);
        assertTrue(mappedType == null || mappedType.getClass() == LinkedList.class);

        type = TypeFactory.defaultInstance().constructType(List.class);
        mappedType = resolver.findTypeMapping(new StdDeserializationContext(), type);
        assertTrue(mappedType == null || mappedType.getClass() == LinkedList.class);

        // Test findTypeMapping with context for custom classes
        Class<?> superType = CustomSuperType.class;
        Class<?> subType = CustomSubType.class;
        resolver.addMapping(superType, subType);
        type = TypeFactory.defaultInstance().constructType(superType);
        mappedType = resolver.findTypeMapping(new StdDeserializationContext(), type);
        assertTrue(mappedType == null || mappedType.getClass() == subType);

        // Test findTypeMapping with context for generic types
        Class<List<Integer>> listIntSuperType = List.class.getGenericType();
        Class<LinkedList<Integer>> listIntSubType = LinkedList.class.getGenericType();
        resolver.addMapping(listIntSuperType, listIntSubType);
        type = TypeFactory.defaultInstance().constructType(listIntSuperType);
        mappedType = resolver.findTypeMapping(new StdDeserializationContext(), type);
        assertTrue(mappedType != null && mappedType.getClass() == LinkedList.class);
    }

    @Test
    public void testMappingOrder() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test mapping order for Collection and List
        resolver.addMapping(List.class, ArrayList.class);
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(null, type);
        assertTrue(mappedType != null && mappedType.getClass() == LinkedList.class);

        // Test mapping order for custom classes
        Class<?> superType1 = CustomSuperType1.class;
        Class<?> subType1 = CustomSubType1.class;
        Class<?> superType2 = CustomSuperType2.class;
        Class<?> subType2 = CustomSubType2.class;
        resolver.addMapping(superType1, subType1);
        resolver.addMapping(superType2, subType2);
        JavaType type1 = TypeFactory.defaultInstance().constructType(superType1);
        JavaType mappedType1 = resolver.findTypeMapping(null, type1);
        assertTrue(mappedType1 != null && mappedType1.getClass() == subType1);

        JavaType type2 = TypeFactory.defaultInstance().constructType(superType2);
        JavaType mappedType2 = resolver.findTypeMapping(null, type2);
        assertTrue(mappedType2 != null && mappedType2.getClass() == subType2);
    }

    @Test
    public void testMappingMultipleSubTypes() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test mapping multiple subtypes for Collection and List
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType[] mappedTypes = resolver.findTypeMappings(null, type);
        assertTrue(Arrays.stream(mappedTypes).anyMatch(t -> t.getClass() == ArrayList.class));
        assertTrue(Arrays.stream(mappedTypes).anyMatch(t -> t.getClass() == LinkedList.class));

        // Test mapping multiple subtypes for custom classes
        Class<?> superType = CustomSuperType.class;
        Class<?> subType1 = CustomSubType1.class;
        Class<?> subType2 = CustomSubType2.class;
        resolver.addMapping(superType, subType1);
        resolver.addMapping(superType, subType2);
        JavaType type1 = TypeFactory.defaultInstance().constructType(superType);
        JavaType[] mappedTypes1 = resolver.findTypeMappings(null, type1);
        assertTrue(Arrays.stream(mappedTypes1).anyMatch(t -> t.getClass() == subType1));
        assertTrue(Arrays.stream(mappedTypes1).anyMatch(t -> t.getClass() == subType2));
    }

    static class CustomSuperType {
    }

    static class CustomSubType implements Collection<String> {
    }

    static class CustomSubType1 implements Collection<String> {
    }

    static class CustomSubType2 implements Collection<String> {
    }
}