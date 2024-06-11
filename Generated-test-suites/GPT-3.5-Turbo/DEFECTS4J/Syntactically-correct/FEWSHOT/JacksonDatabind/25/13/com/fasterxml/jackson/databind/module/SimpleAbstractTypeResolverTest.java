
package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, LinkedList.class);

        // Test mapping from Collection to ArrayList
        assertEquals(LinkedList.class, resolver._mappings.get(Collection.class));
        assertEquals(LinkedList.class, resolver._mappings.get(List.class));

        // Test adding mapping from class to itself
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }

        // Test adding mapping where subtype is not a subtype of supertype
        try {
            resolver.addMapping(Collection.class, Set.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to java.util.Set, as latter is not a subtype of former", e.getMessage());
        }

        // Test adding mapping where supertype is not abstract
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType type1 = config.constructType(Collection.class);
        JavaType type2 = config.constructType(List.class);
        JavaType type3 = config.constructType(ArrayList.class);
        JavaType type4 = config.constructType(LinkedList.class);

        // Test finding type mapping for Collection
        assertEquals(ArrayList.class, resolver.findTypeMapping(config, type1).getRawClass());

        // Test finding type mapping for List
        assertEquals(LinkedList.class, resolver.findTypeMapping(config, type2).getRawClass());

        // Test finding type mapping for ArrayList
        assertEquals(ArrayList.class, resolver.findTypeMapping(config, type3).getRawClass());

        // Test finding type mapping for LinkedList
        assertEquals(LinkedList.class, resolver.findTypeMapping(config, type4).getRawClass());

        // Test finding type mapping for unknown type
        assertNull(resolver.findTypeMapping(config, config.constructType(HashSet.class)));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(Collection.class);

        // Test resolving abstract type
        assertNull(resolver.resolveAbstractType(config, type));
    }
}
