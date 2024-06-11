package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolverIsSerializable() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // check serialization
        assertNotNull(resolver);
    }

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // add mapping for Collection to ArrayList
        Class<?> collectionType = Collection.class;
        Class<?> listType = ArrayList.class;
        resolver.addMapping(collectionType, listType);

        // add mapping for List to ArrayList
        Class<?> listInterface = List.class;
        resolver.addMapping(listInterface, listType);

        Map<ClassKey, Class<?>> mappings = resolver._mappings;
        assertNotNull(mappings);
        assertEquals(2, mappings.size());

        // check mappings
        ClassKey collectionKey = new ClassKey(collectionType);
        assertNotNull(mappings.get(collectionKey));
        assertEquals(listType, mappings.get(collectionKey));

        ClassKey listKey = new ClassKey(listInterface);
        assertNotNull(mappings.get(listKey));
        assertEquals(listType, mappings.get(listKey));

        // add mapping for AbstractClass to ConcreteClass
        Class<?> abstractClassType = AbstractClass.class;
        Class<?> concreteClassType = ConcreteClass.class;
        resolver.addMapping(abstractClassType, concreteClassType);

        // check mappings
        assertNotNull(mappings.get(new ClassKey(abstractClassType)));
        assertEquals(concreteClassType, mappings.get(new ClassKey(abstractClassType)));
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // add mapping for Collection to ArrayList
        Class<?> collectionType = Collection.class;
        Class<?> listType = ArrayList.class;
        resolver.addMapping(collectionType, listType);

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new ClassType(Collection.class);

        JavaType mappedJavaType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedJavaType);
        assertEquals(listType, mappedJavaType.getRawClass());
    }

    @Test
    public void testFindTypeMappingForAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // add mapping for AbstractClass to ConcreteClass
        Class<?> abstractClassType = AbstractClass.class;
        Class<?> concreteClassType = ConcreteClass.class;
        resolver.addMapping(abstractClassType, concreteClassType);

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new ClassType(abstractClassType);

        JavaType mappedJavaType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedJavaType);
        assertEquals(concreteClassType, mappedJavaType.getRawClass());
    }

    @Test
    public void testFindTypeMappingForConcreteType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new ClassType(String.class);

        JavaType mappedJavaType = resolver.findTypeMapping(config, javaType);
        assertNull(mappedJavaType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new ClassType(AbstractClass.class);

        JavaType resolvedJavaType = resolver.resolveAbstractType(config, javaType);
        assertNull(resolvedJavaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidArguments() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // add mapping for Collection to ArrayList with same type
        Class<?> collectionType = Collection.class;
        Class<?> listType = ArrayList.class;
        resolver.addMapping(collectionType, listType);
        resolver.addMapping(collectionType, listType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidSuperSubTypeRelationship() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // add mapping for Collection to String
        Class<?> collectionType = Collection.class;
        Class<?> stringType = String.class;
        resolver.addMapping(collectionType, stringType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // add mapping for Collection to String
        Class<?> collectionType = Collection.class;
        Class<?> stringType = String.class;
        resolver.addMapping(collectionType, stringType);
    }

    @Test
    public void testFindTypeMappingWithMultipleMappings() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // add mapping for Collection to ArrayList
        Class<?> collectionType = Collection.class;
        Class<?> listType = ArrayList.class;
        resolver.addMapping(collectionType, listType);

        // add mapping for Collection to LinkedList
        Class<?> collectionTypeLinkedList = Collection.class;
        Class<?> linkedListType = LinkedList.class;
        resolver.addMapping(collectionTypeLinkedList, linkedListType);

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new ClassType(Collection.class);

        JavaType mappedJavaType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedJavaType);

        Class<?> mappedClass = mappedJavaType.getRawClass();
        assertEquals(Arrays.asList(ArrayList.class, LinkedList.class).get(0), mappedClass);
    }

    @Test
    public void testFindTypeMappingWithGenericType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // add mapping for Collection<String> to ArrayList<String>
        Class<?> collectionType = Collection.class;
        Class<?> listType = ArrayList.class;
        Class<?> stringType = String.class;
        JavaType genericType = new TypeFactory().constructType(Collection.class, stringType);
        resolver.addMapping(genericType, new TypeFactory().constructType(ArrayList.class, stringType));

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new ClassType(Collection.class);
        JavaType mappedJavaType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedJavaType);

        Class<?> mappedClass = mappedJavaType.getRawClass();
        assertEquals(ArrayList.class, mappedClass);
    }

    @Test
    public void testFindTypeMappingWithGenericTypeAndSpecificType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // add mapping for Collection<String> to ArrayList<String>
        Class<?> collectionType = Collection.class;
        Class<?> listType = ArrayList.class;
        Class<?> stringType = String.class;
        JavaType genericType = new TypeFactory().constructType(Collection.class, stringType);
        resolver.addMapping(genericType, new TypeFactory().constructType(ArrayList.class, stringType));

        // add mapping for ArrayList<String> to LinkedList<String>
        Class<?> listInterface = List.class;
        Class<?> linkedListType = LinkedList.class;
        JavaType listGenericType = new TypeFactory().constructType(List.class, stringType);
        resolver.addMapping(listGenericType, new TypeFactory().constructType(LinkedList.class, stringType));

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new ClassType(Collection.class);

        JavaType mappedJavaType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedJavaType);

        Class<?> mappedClass = mappedJavaType.getRawClass();
        assertEquals(LinkedList.class, mappedClass);
    }

    private static class AbstractClass {
        // abstract class
    }

    private static class ConcreteClass extends AbstractClass {
        // concrete class
    }

    private static class ClassType extends Class<?> {
        public ClassType(Class<?> clazz) {
            super(clazz.getName());
        }
    }

    private static class TypeFactory {
        public JavaType constructType(Class<?> type, Class<?>... typeArguments) {
            return new TypeFactory().constructType(type, new ArrayList<>(Arrays.asList(typeArguments)));
        }

        public JavaType constructType(Class<?> type) {
            return new TypeFactory().constructType(type, Collections.emptyList());
        }

        public JavaType constructType(Class<?> type, List<Class<?>> typeArguments) {
            return new TypeFactory().constructType(type, typeArguments.toArray(new Class[0]));
        }
    }

    private static final class TypeFactory {
        // Inner static class for TypeFactory, to avoid having to create an instance
        // every time TypeFactory is used
    }
}