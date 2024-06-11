package com.fasterxml.jackson.databind.module;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    // Test addMapping() method
    
    @Test
    public void testAddMappingWithValidSuperTypeAndSubType() {
        // Arrange
        Class<Collection> superType = Collection.class;
        Class<List> subType = ArrayList.class;

        // Act
        resolver.addMapping(superType, subType);

        // Assert
        assertTrue(resolver._mappings.containsKey(new ClassKey(superType)));
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidSuperTypeAndSubType() {
        // Arrange
        Class<Collection> superType = Collection.class;
        Class<String> subType = String.class;

        // Act
        resolver.addMapping(superType, subType);

        // Should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameSuperTypeAndSubType() {
        // Arrange
        Class<Collection> type = Collection.class;

        // Act
        resolver.addMapping(type, type);

        // Should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        // Arrange
        Class<ArrayList> superType = ArrayList.class;
        Class<List> subType = LinkedList.class;

        // Act
        resolver.addMapping(superType, subType);

        // Should throw IllegalArgumentException
    }

    // Test findTypeMapping() method
    
    @Test
    public void testFindTypeMappingWithExistingMapping() {
        // Arrange
        Class<Collection> src = Collection.class;
        Class<List> dst = LinkedList.class;
        JavaType type = new JavaType();

        resolver.addMapping(src, dst);

        // Act
        JavaType result = resolver.findTypeMapping(config, type);

        // Assert
        assertNotNull(result);
        assertEquals(dst, result.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithNonExistingMapping() {
        // Arrange
        Class<Collection> src = Collection.class;
        Class<List> dst = LinkedList.class;
        JavaType type = new JavaType();

        // Act
        JavaType result = resolver.findTypeMapping(config, type);

        // Assert
        assertNull(result);
    }

    // Test resolveAbstractType() method
    
    @Test
    public void testResolveAbstractType() {
        // Arrange
        JavaType type = new JavaType();

        // Act
        JavaType result = resolver.resolveAbstractType(config, type);

        // Assert
        assertNull(result);
    }

}