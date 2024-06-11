package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;

/**
 * Tests for {@link StdSubtypeResolver} class.
 */
public class StdSubtypeResolverTest {

    /**
     * Test for {@link StdSubtypeResolver#registerSubtypes(NamedType...)} method.
     */
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registration with null argument
        try {
            resolver.registerSubtypes(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test registration with empty array
        resolver.registerSubtypes();

        // Test registration with valid arguments
        NamedType type1 = new NamedType(String.class, "StringSubtype1");
        NamedType type2 = new NamedType(Integer.class, "IntegerSubtype1");
        resolver.registerSubtypes(type1, type2);
        assertEquals(2, resolver.getRegisteredSubtypes().size());

        // Test registration with duplicate types
        resolver.registerSubtypes(type1, type1);
        assertEquals(2, resolver.getRegisteredSubtypes().size());
    }

    /**
     * Test for {@link StdSubtypeResolver#registerSubtypes(Class...)} method.
     */
    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registration with null argument
        try {
            resolver.registerSubtypes(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test registration with empty array
        resolver.registerSubtypes();

        // Test registration with valid arguments
        resolver.registerSubtypes(String.class, Integer.class);
        assertEquals(2, resolver.getRegisteredSubtypes().size());

        // Test registration with duplicate classes
        resolver.registerSubtypes(String.class, String.class);
        assertEquals(2, resolver.getRegisteredSubtypes().size());
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig,  AnnotatedMember, JavaType)}
     * method with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByClassNullArguments() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        resolver.collectAndResolveSubtypesByClass(config, property, baseType);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass)}
     * method with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByClassNullArguments2() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = null;
        AnnotatedClass type = null;
        resolver.collectAndResolveSubtypesByClass(config, type);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}
     * method with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdNullArguments() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}
     * method with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdNullArguments2() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = null;
        AnnotatedClass type = null;
        resolver.collectAndResolveSubtypesByTypeId(config, type);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType)}
     * method with valid arguments.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedMember property = new AnnotatedMember(Object.class.getDeclaredField("field"), new SimpleBeanPropertyDefinition("field"));
        JavaType baseType = ObjectTypeFactory.getDefaultInstance().constructType(String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        // Add check for expected number of subtypes
        // Add checks for expected subtypes based on the class source code
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass)}
     * method with valid arguments.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass2() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedClass type = new AnnotatedClass(String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type);
        // Add check for expected number of subtypes
        // Add checks for expected subtypes based on the class source code
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}
     * method with valid arguments.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedMember property = new AnnotatedMember(Object.class.getDeclaredField("field"), new SimpleBeanPropertyDefinition("field"));
        JavaType baseType = ObjectTypeFactory.getDefaultInstance().constructType(String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        // Add check for expected number of subtypes
        // Add checks for expected subtypes based on the class source code
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}
     * method with valid arguments.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId2() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedClass type = new AnnotatedClass(String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, type);
        // Add check for expected number of subtypes
        // Add checks for expected subtypes based on the class source code
    }
}