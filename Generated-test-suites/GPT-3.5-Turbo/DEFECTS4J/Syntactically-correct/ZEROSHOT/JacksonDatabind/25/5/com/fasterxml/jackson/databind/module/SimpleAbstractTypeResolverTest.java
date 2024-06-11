package com.fasterxml.jackson.databind.module;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Modifier;
import java.util.*;
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
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;
        try {
            resolver.addMapping(superType, subType);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
        Map<ClassKey, Class<?>> mappings = resolver._mappings;
        Class<?> mappedClass = mappings.get(new ClassKey(superType));
        Assert.assertEquals(subType, mappedClass);
    }
    
    @Test
    public void testAddMapping_sameType() {
        Class<?> superType = Collection.class;
        Class<?> subType = Collection.class;
        try {
            resolver.addMapping(superType, subType);
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testAddMapping_notAssignable() {
        Class<?> superType = List.class;
        Class<?> subType = ArrayList.class;
        try {
            resolver.addMapping(superType, subType);
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testAddMapping_notAbstract() {
        Class<?> superType = ArrayList.class;
        Class<?> subType = LinkedList.class;
        try {
            resolver.addMapping(superType, subType);
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testFindTypeMapping_mappingExists() {
        try {
            Class<?> superType = Collection.class;
            Class<?> subType = LinkedList.class;
            resolver.addMapping(superType, subType);
            
            DeserializationConfig config = new DeserializationConfig(null);
            JavaType type = JavaType.construct(Collection.class);
            JavaType result = resolver.findTypeMapping(config, type);
            Assert.assertNotNull(result);
            Assert.assertEquals(subType, result.getRawClass());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testFindTypeMapping_mappingDoesNotExist() {
        try {
            DeserializationConfig config = new DeserializationConfig(null);
            JavaType type = JavaType.construct(Collection.class);
            JavaType result = resolver.findTypeMapping(config, type);
            Assert.assertNull(result);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testResolveAbstractType() {
        try {
            DeserializationConfig config = new DeserializationConfig(null);
            JavaType type = JavaType.construct(Collection.class);
            JavaType result = resolver.resolveAbstractType(config, type);
            Assert.assertNull(result);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }   
}