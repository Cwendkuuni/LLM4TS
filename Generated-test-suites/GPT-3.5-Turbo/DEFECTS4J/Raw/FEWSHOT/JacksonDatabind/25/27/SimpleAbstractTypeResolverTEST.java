package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        resolver.addMapping(Map.class, HashMap.class);
        resolver.addMapping(Collection.class, ArrayList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(Map.class)));
        assertNotNull(resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(HashMap.class, resolver._mappings.get(new ClassKey(Map.class)));
        assertEquals(ArrayList.class, resolver._mappings.get(new ClassKey(Collection.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SameClass() {
        resolver.addMapping(ArrayList.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SubTypeNotSubclass() {
        resolver.addMapping(Collection.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SuperTypeNotAbstract() {
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(Map.class, HashMap.class);
        resolver.addMapping(Collection.class, ArrayList.class);

        assertNull(resolver.findTypeMapping(null, null));

        assertNull(resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Integer.class)));

        assertEquals(TypeFactory.defaultInstance().constructType(HashMap.class),
                resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Map.class)));

        assertEquals(TypeFactory.defaultInstance().constructType(ArrayList.class),
                resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Collection.class)));

        assertEquals(TypeFactory.defaultInstance().constructType(ArrayList.class),
                resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructCollectionLikeType(List.class, String.class)));
    }

    @Test
    public void testResolveAbstractType() {
        assertNull(resolver.resolveAbstractType(null, null));
    }
}