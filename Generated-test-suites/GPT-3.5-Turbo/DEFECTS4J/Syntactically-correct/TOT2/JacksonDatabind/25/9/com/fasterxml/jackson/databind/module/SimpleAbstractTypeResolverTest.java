package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        assertTrue("Mapping should be added successfully", resolver._mappings.containsKey(new ClassKey(List.class)));
    }

    @Test
    public void testAddMappingSelf() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(List.class, List.class);
            fail("Should throw IllegalArgumentException for adding mapping from class to itself");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testAddMappingNotSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("Should throw IllegalArgumentException for not adding sub class mapping");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testAddMappingConcreteClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("Should throw IllegalArgumentException for adding mapping from concrete class");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        DeserializationConfig config = new DeserializationConfig(null, null, null, null);
        JavaType type = JavaType.construct(Collection.class, null, null, null);
        JavaType resolved = resolver.findTypeMapping(config, type);
        assertEquals("Resolved JavaType should be LinkedList", LinkedList.class, resolved.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig(null, null, null, null);
        JavaType type = JavaType.construct(List.class, null, null, null);
        JavaType resolved = resolver.resolveAbstractType(config, type);
        assertNull("Resolved JavaType should be null", resolved);
    }

}