package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNotNull(resolver);
    }

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List<String>> collectionClass = List.class.getTypeParameters()[0].newType();
        Class<LinkedList<String>> linkedListClass = LinkedList.class;

        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        resolver.addMapping(collectionClass, linkedListClass);

        Map<ClassKey, Class<?>> mappings = resolver._mappings;
        List<Map.Entry<ClassKey, Class<?>>> entries = new ArrayList<>(mappings.entrySet());
        assertEquals(3, entries.size());

        Map.Entry<ClassKey, Class<?>> entry1 = entries.get(0);
        assertEquals(new ClassKey(Collection.class), entry1.getKey());
        assertEquals(LinkedList.class, entry1.getValue());

        Map.Entry<ClassKey, Class<?>> entry2 = entries.get(1);
        assertEquals(new ClassKey(List.class), entry2.getKey());
        assertEquals(LinkedList.class, entry2.getValue());

        Map.Entry<ClassKey, Class<?>> entry3 = entries.get(2);
        ClassKey key3 = new ClassKey(collectionClass);
        assertEquals(key3, entry3.getKey());
        assertEquals(LinkedList.class, entry3.getValue());
    }

    @Test
    public void testFindTypeMapping() throws JsonMappingException {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType javaType = ClassIdType.construct(Collection.class, String.class);
        JavaType expected = javaType.narrowBy(LinkedList.class);

        JavaType actual = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingInvalidSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> collectionClass = List.class.getTypeParameters()[0].newType();
        Class<LinkedList<String>> linkedListClass = LinkedList.class;
        Class<ArrayList<String>> arrayListClass = ArrayList.class;

        resolver.addMapping(Collection.class, linkedListClass);
        resolver.addMapping(List.class, arrayListClass);
        resolver.addMapping(collectionClass, arrayListClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingInvalidSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> collectionClass = List.class.getTypeParameters()[0].newType();
        Class<LinkedList<String>> linkedListClass = LinkedList.class;

        resolver.addMapping(ArrayList.class, linkedListClass);
        resolver.addMapping(Collection.class, collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSelfMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> collectionClass = List.class.getTypeParameters()[0].newType();
        Class<LinkedList<String>> linkedListClass = LinkedList.class;

        resolver.addMapping(collectionClass, linkedListClass);
        resolver.addMapping(linkedListClass, collectionClass);
    }
}