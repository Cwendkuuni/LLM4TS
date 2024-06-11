package com.fasterxml.jackson.databind.module;

import java.lang.reflect.Modifier;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.AbstractTypeResolver;
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
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        
        HashMap<ClassKey,Class<?>> mappings = resolver._mappings;
        assertNotNull(mappings);
        assertEquals(1, mappings.size());
        assertTrue(mappings.containsKey(new ClassKey(superType)));
        assertEquals(subType, mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameTypes() {
        Class<Collection> type = Collection.class;
        resolver.addMapping(type, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidSubType() {
        Class<List> superType = List.class;
        Class<ArrayList> subType = ArrayList.class;
        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        Class<ArrayList> superType = ArrayList.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping() {
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);

        DeserializationConfig config = null; // mock or instantiate DeserializationConfig
        JavaType type = config.constructType(superType);

        JavaType result = resolver.findTypeMapping(config, type);
        assertNotNull(result);
        assertEquals(subType, result.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithNoMapping() {
        DeserializationConfig config = null; // mock or instantiate DeserializationConfig
        JavaType type = config.constructType(Collection.class);

        JavaType result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null; // mock or instantiate DeserializationConfig
        JavaType type = config.constructType(Collection.class);

        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }

}