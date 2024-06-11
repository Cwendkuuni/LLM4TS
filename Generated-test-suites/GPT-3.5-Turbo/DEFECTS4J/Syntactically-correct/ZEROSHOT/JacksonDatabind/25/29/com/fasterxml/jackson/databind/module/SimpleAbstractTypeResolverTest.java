package com.fasterxml.jackson.databind.module;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {
    
    private SimpleAbstractTypeResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }
    
    @Test
    public void testAddMapping() {
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;
        
        resolver.addMapping(superType, subType);
        
        HashMap<ClassKey,Class<?>> mappings = resolver.getMappings();
        assertNotNull(mappings);
        assertTrue(mappings.containsKey(new ClassKey(superType)));
        assertEquals(subType, mappings.get(new ClassKey(superType)));
    }
    
    @Test
    public void testAddMapping_selfType() {
        Class<?> type = Collection.class;
        
        try {
            resolver.addMapping(type, type);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }
    
    @Test
    public void testAddMapping_invalidSubType() {
        Class<?> superType = Collection.class;
        Class<?> subType = String.class;
        
        try {
            resolver.addMapping(superType, subType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class " + superType.getName() + " to " + subType.getName() + ", as latter is not a subtype of former", e.getMessage());
        }
    }
    
    @Test
    public void testAddMapping_notAbstract() {
        Class<?> type = ArrayList.class;
        
        try {
            resolver.addMapping(type, LinkedList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class " + type.getName() + " since it is not abstract", e.getMessage());
        }
    }
    
    @Test
    public void testFindTypeMapping() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(Class.forName("java.util.Collection"), null, null);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertNull(result);
    }
    
    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = new JavaType(Class.forName("java.util.Collection"), null, null);
        
        JavaType result = resolver.resolveAbstractType(config, type);
        
        assertNull(result);
    }
    
}