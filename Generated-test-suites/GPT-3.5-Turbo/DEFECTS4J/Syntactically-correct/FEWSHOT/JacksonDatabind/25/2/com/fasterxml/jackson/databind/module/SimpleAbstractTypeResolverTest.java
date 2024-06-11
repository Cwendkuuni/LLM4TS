package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {
    
    private SimpleAbstractTypeResolver resolver;
    
    @Test
    public void testAddMapping() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Number.class, Integer.class);
        resolver.addMapping(Number.class, Double.class);
        resolver.addMapping(CharSequence.class, String.class);
        
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType doubleType = TypeFactory.defaultInstance().constructType(Double.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        
        assertEquals(intType, resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Number.class)));
        assertEquals(doubleType, resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Number.class)));
        assertEquals(stringType, resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(CharSequence.class)));
    }   
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Integer.class, Integer.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddMappingNoSubtype() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Number.class, String.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddMappingNonAbstractClass() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(String.class, StringBuilder.class);
    }
    
    @Test
    public void testFindTypeMapping() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Number.class, Integer.class);
        resolver.addMapping(Number.class, Double.class);
        resolver.addMapping(CharSequence.class, String.class);
        
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType doubleType = TypeFactory.defaultInstance().constructType(Double.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        
        assertEquals(intType, resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Number.class)));
        assertEquals(doubleType, resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Number.class)));
        assertEquals(stringType, resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(CharSequence.class)));
    }
    
    @Test
    public void testFindTypeMappingNoMapping() {
        resolver = new SimpleAbstractTypeResolver();
        
        assertNull(resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Number.class)));
    }
    
    @Test
    public void testResolveAbstractType() {
        resolver = new SimpleAbstractTypeResolver();
        
        assertNull(null, resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(Number.class)));
    }

}