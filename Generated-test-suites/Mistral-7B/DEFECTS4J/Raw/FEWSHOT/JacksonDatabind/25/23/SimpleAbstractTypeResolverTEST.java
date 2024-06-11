package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = JavaType.constructParameticType(List.class, String.class);

        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMappingForAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = JavaType.constructRawType(ArrayList.class);

        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMappingForConcreteType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = JavaType.constructRawType(String[].class);

        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testFindTypeMappingForNonAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = JavaType.constructRawType(String.class);

        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testFindTypeMappingForUnknownType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = JavaType.constructRawType(Object.class);

        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testFindTypeMappingForNullType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = null;

        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = JavaType.constructRawType(ArrayList.class);

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeForAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = JavaType.constructRawType(List.class);

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeForConcreteType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = JavaType.constructRawType(String[].class);

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeForUnknownType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = JavaType.constructRawType(Object.class);

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeForNullType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = null;

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeWithMappings() {
        DeserializationConfig config = new DeserializationConfig();
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType type = JavaType.constructRawType(List.class);

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractTypeWithMultipleMappings() {
        DeserializationConfig config = new DeserializationConfig();
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType type = JavaType.constructRawType(List.class);

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertEquals(ArrayList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractTypeWithMultipleMappingsForAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType type = JavaType.constructRawType(List.class);

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertEquals(ArrayList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractTypeWithMultipleMappingsForConcreteType() {
        DeserializationConfig config = new DeserializationConfig();
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType type = JavaType.constructRawType(ArrayList.class);

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeWithMultipleMappingsForUnknownType() {
        DeserializationConfig config = new DeserializationConfig();
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType type = JavaType.constructRawType(Object.class);

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractTypeWithMultipleMappingsForNullType() {
        DeserializationConfig config = new DeserializationConfig();
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType type = null;

        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }
}