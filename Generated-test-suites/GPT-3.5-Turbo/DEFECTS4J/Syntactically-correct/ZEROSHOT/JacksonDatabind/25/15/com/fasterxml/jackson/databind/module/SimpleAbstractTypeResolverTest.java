
package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;
import com.fasterxml.jackson.databind.AbstractTypeResolver;
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
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType result1 = resolver.findTypeMapping(null, null);
        JavaType result2 = resolver.resolveAbstractType(null, null);

        assertNull(result1);
        assertNull(result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMappingSelf() {
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMappingNotSubType() {
        resolver.addMapping(Collection.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMappingNotAbstract() {
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

}
