package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Add a valid mapping
        resolver.addMapping(AbstractType.class, SubType.class);
        assertEquals(SubType.class, resolver._mappings.get(new ClassKey(AbstractType.class)));

        // Add a mapping from a class to itself
        try {
            resolver.addMapping(AbstractType.class, AbstractType.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }

        // Add a mapping where the subtype is not a subclass of the supertype
        try {
            resolver.addMapping(AbstractType.class, NonSubType.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class com.fasterxml.jackson.databind.module.AbstractType" +
                    " to com.fasterxml.jackson.databind.module.NonSubType, as latter is not a subtype of former", e.getMessage());
        }

        // Add a mapping where the supertype is not abstract
        try {
            resolver.addMapping(NonAbstractType.class, SubType.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class com.fasterxml.jackson.databind.module.NonAbstractType" +
                    " since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Define mappings
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        resolver.addMapping(Set.class, HashSet.class);

        // Test with a type that has a mapping
        assertEquals(resolver.findTypeMapping(config, type), LinkedList.class);

        // Test with a type that does not have a mapping
        assertNull(resolver.findTypeMapping(config, type));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test with any JavaType, since this method returns null for all types
        assertNull(resolver.resolveAbstractType(config, type));
    }
    
    // Define test classes
    abstract class AbstractType {}
    abstract class SubType extends AbstractType {}
    class NonSubType {}
    class NonAbstractType {}
}