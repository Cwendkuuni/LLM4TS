package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        HashMap<ClassKey,Class<?>> mappings = resolver.getMappings();
        assertEquals(2, mappings.size());
        assertTrue(mappings.containsKey(new ClassKey(Collection.class)));
        assertTrue(mappings.containsKey(new ClassKey(List.class)));
        assertEquals(LinkedList.class, mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingConcreteClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType();
        type.setRawClass(Collection.class);
        JavaType result = resolver.findTypeMapping(config, type);
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getRawClass());
        assertEquals(type.getTypeBindings(), result.getTypeBindings());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType();
        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }

}