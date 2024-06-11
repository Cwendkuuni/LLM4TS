package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.registerSubtypes(Foo.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals(Foo.class, subtypes.iterator().next().getType().asSubTypeOf(Object.class));
    }

    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.registerSubtypes(new NamedType(Bar.class, "bar"));
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals("bar", subtypes.iterator().next().getName());
        Assert.assertEquals(Bar.class, subtypes.iterator().next().getType().asSubTypeOf(Object.class));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.registerSubtypes(Foo.class);
        resolver.registerSubtypes(Bar.class);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        Assert.assertEquals(2, subtypes.size());
        Assert.assertTrue(subtypes.contains(new NamedType(Foo.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(Bar.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.registerSubtypes(Foo.class);
        resolver.registerSubtypes(Bar.class);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        Assert.assertEquals(2, subtypes.size());
        Assert.assertTrue(subtypes.contains(new NamedType(Foo.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(Bar.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.registerSubtypes(Foo.class);
        resolver.registerSubtypes(Bar.class);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, new JsonTypeInfo.Id(JsonTypeInfo.As.PROPERTY, "@type"));
        Assert.assertEquals(2, subtypes.size());
        Assert.assertTrue(subtypes.contains(new NamedType(Foo.class, "@foo")));
        Assert.assertTrue(subtypes.contains(new NamedType(Bar.class, "@bar")));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithExplicitTypeRegistration() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.registerSubtypes(Foo.class);
        resolver.registerSubtypes(Bar.class);
        resolver.registerSubtypes(Baz.class);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        Assert.assertEquals(3, subtypes.size());
        Assert.assertTrue(subtypes.contains(new NamedType(Foo.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(Bar.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(Baz.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithNullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.collectAndResolveSubtypesByTypeId(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithNullMapperConfig() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.collectAndResolveSubtypesByTypeId(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithNullAnnotatedMember() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.collectAndResolveSubtypesByTypeId(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithNullJavaType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.collectAndResolveSubtypesByTypeId(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCollectAndResolveSubtypesByClassWithNullMapperConfig() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.collectAndResolveSubtypesByClass(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCollectAndResolveSubtypesByClassWithNullAnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.collectAndResolveSubtypesByClass(null, null, null);
    }

    static class Foo {}

    static class Bar {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
        String foo = "foo";
    }

    static class Baz {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
        String baz = "baz";
    }
}