package com.fasterxml.jackson.databind.module;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    
    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
    }
    
    @Test
    public void testAddMapping_typical() {
        // Typical use case: adding a mapping from an abstract type to a concrete subtype
        resolver.addMapping(AbstractType.class, ConcreteType.class);
        // Verify that the mapping was added successfully
        assertEquals(ConcreteType.class, resolver._mappings.get(new ClassKey(AbstractType.class)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameClass() {
        // Error scenario: adding a mapping from a class to itself, which is not allowed
        resolver.addMapping(AbstractType.class, AbstractType.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_subtypeNotSubclass() {
        // Error scenario: adding a mapping where the subtype is not a subclass of the supertype
        resolver.addMapping(Collection.class, Integer.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstract() {
        // Error scenario: adding a mapping where the supertype is not abstract
        resolver.addMapping(ConcreteType.class, SubType.class);
    }
    
    @Test
    public void testFindTypeMapping_mappingExists() {
        // Typical use case: finding a mapping for a given JavaType when a mapping exists
        resolver.addMapping(AbstractType.class, ConcreteType.class);
        JavaType result = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(AbstractType.class));
        assertEquals(TypeFactory.defaultInstance().constructType(ConcreteType.class), result);
    }
    
    @Test
    public void testFindTypeMapping_mappingDoesNotExist() {
        // Typical use case: finding a mapping for a given JavaType when no mapping exists
        JavaType result = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(AbstractType.class));
        assertNull(result);
    }
    
    @Test
    public void testResolveAbstractType() {
        // Typical use case: resolving an abstract type using the resolver, which always returns null
        JavaType result = resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(AbstractType.class));
        assertNull(result);
    }
    
    private abstract class AbstractType {}
    
    private class ConcreteType extends AbstractType {}
    
    private class SubType extends ConcreteType {}
}