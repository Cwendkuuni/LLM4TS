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
        resolver.addMapping(Set.class, HashSet.class);
        
        assertEquals(ArrayList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
        assertEquals(HashSet.class, resolver._mappings.get(new ClassKey(Set.class)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_subTypeNotASubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, HashMap.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, LinkedList.class);
        resolver.addMapping(Set.class, HashSet.class);
        
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        JavaType type1 = Mockito.mock(JavaType.class);
        JavaType type2 = Mockito.mock(JavaType.class);
        JavaType type3 = Mockito.mock(JavaType.class);
        JavaType type4 = Mockito.mock(JavaType.class);
        
        Mockito.when(type1.getRawClass()).thenReturn(Collection.class);
        Mockito.when(type2.getRawClass()).thenReturn(List.class);
        Mockito.when(type3.getRawClass()).thenReturn(Set.class);
        Mockito.when(type4.getRawClass()).thenReturn(Map.class);
        
        assertEquals(ArrayList.class, resolver.findTypeMapping(config, type1).getRawClass());
        assertEquals(LinkedList.class, resolver.findTypeMapping(config, type2).getRawClass());
        assertEquals(HashSet.class, resolver.findTypeMapping(config, type3).getRawClass());
        assertNull(resolver.findTypeMapping(config, type4));
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNull(resolver.resolveAbstractType(null, null));
    }
}