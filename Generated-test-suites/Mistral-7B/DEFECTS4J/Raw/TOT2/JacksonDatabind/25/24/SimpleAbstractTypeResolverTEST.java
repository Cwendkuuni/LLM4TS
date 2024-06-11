package com.example.test;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> listClass = List.class.getTypeParameters()[0].getRawType();
        Class<LinkedList<String>> linkedListClass = LinkedList.class;

        resolver.addMapping(List.class, LinkedList.class);
        JavaType javaType = resolver.findTypeMapping(null, new JavaType(List.class));

        assertEquals(LinkedList.class, javaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> listClass = List.class.getTypeParameters()[0].getRawType();

        resolver.addMapping(List.class, List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> listClass = List.class.getTypeParameters()[0].getRawType();

        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMappingNoMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> listClass = List.class.getTypeParameters()[0].getRawType();

        JavaType javaType = resolver.findTypeMapping(null, new JavaType(List.class));

        assertNull(javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMappingIncorrectSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> listClass = List.class.getTypeParameters()[0].getRawType();

        resolver.addMapping(List.class, ArrayList.class);
        JavaType javaType = resolver.findTypeMapping(null, new JavaType(List.class));

        assertNull(javaType);
    }

    @Test
    public void testResolveAbstractTypeNullType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        assertNull(resolver.resolveAbstractType(null, null));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List<String>> listClass = List.class.getTypeParameters()[0].getRawType();

        assertNull(resolver.resolveAbstractType(null, new JavaType(List.class)));
    }
}