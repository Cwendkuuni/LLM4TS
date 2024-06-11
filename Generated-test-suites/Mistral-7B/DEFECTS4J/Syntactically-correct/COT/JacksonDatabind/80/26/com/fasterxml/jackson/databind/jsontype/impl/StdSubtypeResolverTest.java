package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver stdSubtypeResolver;
    private MapperFactory mapperFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        stdSubtypeResolver = new StdSubtypeResolver();
        mapperFactory = new MapperFactory();
        mapperFactory.findAndRegisterModules();
        objectMapper = mapperFactory.getObjectMapper();
    }

    @Test
    public void testRegisterSubtypesByClasses() {
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        stdSubtypeResolver.registerSubtypes(subtype1, subtype2);
        JavaType baseTypeType = objectMapper.getTypeFactory().constructType(baseType);

        Collection<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseTypeType);

        Assert.assertEquals(2, subtypes.size());
        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(subtype1),
                new NamedType(subtype2)
        );
        Assert.assertArrayEquals(expectedSubtypes.toArray(), subtypes.toArray(new NamedType[0]));
    }

    @Test
    public void testRegisterSubtypesByName() {
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        stdSubtypeResolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector introspector = objectMapper.getConfig().getAnnotationIntrospector();
        AnnotatedClass subtype1AnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(objectMapper.getConfig(), subtype1);

        Collection<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), subtype1AnnotatedClass, null);

        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals(subtype1, subtypes.iterator().next().getType().getTypeName());
    }

    @Test
    public void testFindSubtypesByClass() {
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;
        Class<?> subtype3 = Double.class;

        stdSubtypeResolver.registerSubtypes(subtype1, subtype2, subtype3);
        JavaType baseTypeType = objectMapper.getTypeFactory().constructType(baseType);

        Collection<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseTypeType);

        Assert.assertEquals(3, subtypes.size());
        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(subtype1),
                new NamedType(subtype2),
                new NamedType(subtype3)
        );
        Assert.assertArrayEquals(expectedSubtypes.toArray(), subtypes.toArray(new NamedType[0]));
    }

    @Test
    public void testFindSubtypesByNameWithAnnotation() {
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        stdSubtypeResolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector introspector = objectMapper.getConfig().getAnnotationIntrospector();
        AnnotatedClass subtype1AnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(objectMapper.getConfig(), subtype1);
        JsonTypeInfo.Id annotation = introspector.findAnnotation(subtype1AnnotatedClass, JsonTypeInfo.Id.class);

        Collection<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), subtype1AnnotatedClass);

        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals(subtype1, subtypes.iterator().next().getType().getTypeName());
        Assert.assertNotNull(annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseTypeThrowsException() {
        JavaType baseTypeType = null;
        stdSubtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseTypeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSubtypesThrowsException() {
        JavaType baseTypeType = objectMapper.getTypeFactory().constructType(String.class);
        stdSubtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseTypeType, (NamedType[]) null);
    }
}