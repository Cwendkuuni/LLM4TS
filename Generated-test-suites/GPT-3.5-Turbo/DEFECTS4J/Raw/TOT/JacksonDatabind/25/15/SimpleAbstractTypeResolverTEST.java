package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping_typicalCase() {
        // Arrange
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;
        
        // Act
        SimpleAbstractTypeResolver result = resolver.addMapping(superType, subType);
        
        // Assert
        assertThat(result, sameInstance(resolver));
        assertThat(resolver._mappings, hasEntry(new ClassKey(superType), subType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameClass() {
        // Arrange
        Class<LinkedList> type = LinkedList.class;
        
        // Act
        resolver.addMapping(type, type);
        
        // Assert
        // Expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_subtypeNotAssignable() {
        // Arrange
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;
        
        // Act
        resolver.addMapping(subType, superType);
        
        // Assert
        // Expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_superTypeNotAbstract() {
        // Arrange
        Class<LinkedList> superType = LinkedList.class;
        Class<Collection> subType = Collection.class;
        
        // Act
        resolver.addMapping(superType, subType);
        
        // Assert
        // Expected exception
    }

    @Test
    public void testFindTypeMapping_mappingExists() {
        // Arrange
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        JavaType type = JavaType.forClass(superType);
        
        // Act
        JavaType result = resolver.findTypeMapping(null, type);
        
        // Assert
        assertThat(result.getRawClass(), equalTo(subType));
    }

    @Test
    public void testFindTypeMapping_mappingDoesNotExist() {
        // Arrange
        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;
        JavaType type = JavaType.forClass(superType);
        
        // Act
        JavaType result = resolver.findTypeMapping(null, type);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        // Arrange
        JavaType type = JavaType.forClass(LinkedList.class);
        
        // Act
        JavaType result = resolver.resolveAbstractType(null, type);
        
        // Assert
        assertNull(result);
    }
}