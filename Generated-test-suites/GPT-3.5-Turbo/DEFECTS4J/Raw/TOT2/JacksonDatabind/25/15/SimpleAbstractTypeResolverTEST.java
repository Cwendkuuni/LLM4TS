package com.fasterxml.jackson.databind.module;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(Collection.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingError() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class); // This should pass
        resolver.addMapping(Collection.class, LinkedList.class); // This should throw IllegalArgumentException
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertNotNull(resolver.findTypeMapping(config, type));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertNull(resolver.resolveAbstractType(config, type));
    }
}