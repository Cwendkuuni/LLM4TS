package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        Class<Collection> superType = Collection.class;
        Class<ArrayList> subType = ArrayList.class;
        resolver.addMapping(superType, subType);

        ClassKey classKey = new ClassKey(superType);
        assertEquals(subType, resolver._mappings.get(classKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SameClass() {
        Class<Collection> type = Collection.class;
        resolver.addMapping(type, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SubtypeIsNotSubclass() {
        Class<Collection> superType = Collection.class;
        Class<ArrayList> subType = ArrayList.class;
        resolver.addMapping(subType, superType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SuperTypeIsNotAbstract() {
        Class<ArrayList> superType = ArrayList.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping_MappingExists() {
        Class<Collection> superType = Collection.class;
        Class<ArrayList> subType = ArrayList.class;
        resolver.addMapping(superType, subType);

        DeserializationConfig config = null;
        JavaType type = null;

        type = new JavaType(forClass(ArrayList.class));
        JavaType result = resolver.findTypeMapping(config, type);
        assertEquals(type, result);
    }

    @Test
    public void testFindTypeMapping_MappingDoesNotExist() {
        Class<Collection> superType = Collection.class;
        Class<ArrayList> subType = ArrayList.class;
        resolver.addMapping(superType, subType);

        DeserializationConfig config = null;
        JavaType type = null;

        type = new JavaType(forClass(LinkedList.class));
        JavaType result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null;
        JavaType type = null;

        type = new JavaType(forClass(LinkedList.class));
        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }
}