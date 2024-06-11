package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;


public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        HashMap<ClassKey, Class<?>> mappings = resolver.getMappings();
        assertEquals(2, mappings.size());
        assertEquals(LinkedList.class, mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Integer.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType type1 = config.constructType(Collection.class);
        JavaType type2 = config.constructType(List.class);
        JavaType type3 = config.constructType(Set.class);

        assertEquals(LinkedList.class, resolver.findTypeMapping(config, type1).getRawClass());
        assertEquals(LinkedList.class, resolver.findTypeMapping(config, type2).getRawClass());
        assertNull(resolver.findTypeMapping(config, type3));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type1 = config.constructType(Collection.class);
        JavaType type2 = config.constructType(List.class);
        JavaType type3 = config.constructType(Set.class);

        assertNull(resolver.resolveAbstractType(config, type1));
        assertNull(resolver.resolveAbstractType(config, type2));
        assertNull(resolver.resolveAbstractType(config, type3));
    }

}