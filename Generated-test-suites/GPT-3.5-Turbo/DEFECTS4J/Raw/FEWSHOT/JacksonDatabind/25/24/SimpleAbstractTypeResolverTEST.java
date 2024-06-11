
package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    
    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }
    
    @Test
    public void testAddMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameClass() {
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notSubType() {
        resolver.addMapping(List.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstract() {
        resolver.addMapping(LinkedList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        JavaType type1 = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType type2 = TypeFactory.defaultInstance().constructType(List.class);
        JavaType result1 = resolver.findTypeMapping(null, type1);
        JavaType result2 = resolver.findTypeMapping(null, type2);
        assertEquals(LinkedList.class, result1.getRawClass());
        assertEquals(LinkedList.class, result2.getRawClass());
    }

    @Test
    public void testFindTypeMapping_noMapping() {
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType result = resolver.findTypeMapping(null, type);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType result = resolver.resolveAbstractType(null, type);
        assertNull(result);
    }
}
