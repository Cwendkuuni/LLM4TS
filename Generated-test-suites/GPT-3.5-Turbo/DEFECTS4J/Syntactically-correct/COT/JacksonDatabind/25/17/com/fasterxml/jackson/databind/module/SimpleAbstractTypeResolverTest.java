
package com.fasterxml.jackson.databind.module;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Assert.assertNotNull(resolver.addMapping(Collection.class, LinkedList.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClasses() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNoSubtypeRelationship() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(LinkedList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        DeserializationConfig config = null;
        JavaType type = new JavaType(null, Collection.class, null, null, null, null, false, null, null);
        JavaType actual = resolver.findTypeMapping(config, type);
        JavaType expected = new JavaType(null, LinkedList.class, null, null, null, null, false, null, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = null;
        JavaType type = new JavaType(null, Collection.class, null, null, null, null, false, null, null);
        JavaType actual = resolver.resolveAbstractType(config, type);
        Assert.assertNull(actual);
    }
}

