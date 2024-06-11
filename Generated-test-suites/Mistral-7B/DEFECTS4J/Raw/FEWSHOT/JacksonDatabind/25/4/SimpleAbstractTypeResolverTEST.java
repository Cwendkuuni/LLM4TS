package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNotNull(resolver);
        assertEquals(0, resolver._mappings.size());
    }

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> collectionClass = Collection.class;
        Class<?> linkedListClass = LinkedList.class;
        resolver.addMapping(collectionClass, linkedListClass);
        resolver.addMapping(Collection.class, linkedListClass);
        resolver.addMapping(List.class, linkedListClass);
        assertEquals(3, resolver._mappings.size());
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(collectionClass)));
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> collectionClass = Collection.class;
        Class<?> linkedListClass = LinkedList.class;
        resolver.addMapping(collectionClass, linkedListClass);

        JavaType javaType = null;
        DeserializationConfig config = new DeserializationConfig();

        javaType = JavaType.construct(Collection.class, Collection.class);
        assertNull(resolver.findTypeMapping(config, javaType));

        javaType = JavaType.construct(LinkedList.class, LinkedList.class);
        assertNotNull(resolver.findTypeMapping(config, javaType));

        javaType = JavaType.construct(Collection.class, Collection.class);
        assertNotNull(resolver.findTypeMapping(config, javaType));

        javaType = JavaType.construct(List.class, LinkedList.class);
        assertNotNull(resolver.findTypeMapping(config, javaType));

        javaType = JavaType.construct(Collection.class, ArrayList.class);
        assertNull(resolver.findTypeMapping(config, javaType));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNull(resolver.resolveAbstractType(new DeserializationConfig(), JavaType.construct(Collection.class, Collection.class)));
    }
}