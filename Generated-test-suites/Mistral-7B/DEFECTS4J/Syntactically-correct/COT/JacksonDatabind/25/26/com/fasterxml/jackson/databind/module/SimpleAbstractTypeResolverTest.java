package com.fasterxml.jackson.databind.module.test;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.ClassKey;

/**
 * Test class for SimpleAbstractTypeResolver
 */
public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Test
    public void testAddMapping() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        assertEquals(2, resolver._mappings.size());
    }

    @Test
    public void testFindTypeMapping_validCase() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType javaType = JavaType.construct(ArrayList.class);
        JavaType expectedJavaType = JavaType.construct(LinkedList.class);

        JavaType actualJavaType = resolver.findTypeMapping(null, javaType);

        assertEquals(expectedJavaType, actualJavaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMapping_superTypeEqualsSubType() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType javaType = JavaType.construct(LinkedList.class);
        assertNull(resolver.findTypeMapping(null, javaType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMapping_superTypeIsNotSubType() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType javaType = JavaType.construct(ArrayList.class);
        JavaType subType = JavaType.construct(LinkedList.class);
        assertNull(resolver.findTypeMapping(null, javaType.narrowBy(subType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMapping_TypeIsNotAbstract() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType javaType = JavaType.construct(ArrayList.class);
        assertNull(resolver.findTypeMapping(null, javaType));
    }

    @Test
    public void testFindTypeMapping_NotFound() {
        resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(ArrayList.class);

        assertNull(resolver.findTypeMapping(null, javaType));
    }

    @Test
    public void testResolveAbstractType() {
        resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(ArrayList.class);

        assertNull(resolver.resolveAbstractType(null, javaType));
    }

    @Test
    public void testAddMapping_SameModifiers() {
        resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = AbstractList.class;
        Class<?> concreteType = ArrayList.class;

        resolver.addMapping(abstractType, concreteType);
        Map<ClassKey, Class<?>> mappings = resolver._mappings;

        assertEquals(1, mappings.size());
        assertTrue(Modifier.isAbstract(abstractType.getModifiers()));
        assertTrue(Modifier.isAbstract(concreteType.getModifiers()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_DifferentModifiers() {
        resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = AbstractList.class;
        Class<?> concreteType = ArrayList.class;

        resolver.addMapping(abstractType, concreteType);

        try {
            resolver.addMapping(Arrays.asList(new Class[] { abstractType }).get(0),
                    Arrays.asList(new Class[] { concreteType }).get(0));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_InvalidSuperType() {
        resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = String.class;
        Class<?> concreteType = ArrayList.class;

        resolver.addMapping(abstractType, concreteType);

        try {
            resolver.addMapping(AbstractMap.class, concreteType);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_InvalidSubType() {
        resolver = new SimpleAbstractTypeResolver();
        Class<?> abstractType = Collection.class;
        Class<?> concreteType = String.class;

        resolver.addMapping(abstractType, concreteType);

        try {
            resolver.addMapping(abstractType, Arrays.asList(new Class[] { Collection.class, List.class })
                    .get(0));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}