package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.TestUtils.assertCollectionEquals;
import static com.fasterxml.jackson.core.util.TestUtils.assertNull;
import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdSubtypeResolverTest {

    private static final MapperConfig<?> config = new MapperConfig.Builder().build();
    static {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        SubtypeResolver sr = new StdSubtypeResolver();
        sr.registerSubtypes(new NamedType(Foo.class, "FOO"), new NamedType(Bar.class, "BAR"),
                new NamedType(Baz.class, "BAZ"));
        config.setSubtypeResolver(sr);
    }

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver sr = (StdSubtypeResolver) config.getSubtypeResolver();
        assertEquals(3, sr.getRegisteredSubtypes().size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_UsingProperty() {
        AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
        JavaType baseType = JavaType.construct(String.class);
        Collection<NamedType> result = sr.collectAndResolveSubtypesByClass(config, property, baseType);
        assertCollectionEquals(Arrays.asList(new NamedType(Foo.class, "FOO"), new NamedType(Bar.class, "BAR"),
                new NamedType(Baz.class, "BAZ")), result);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_UsingClass() {
        AnnotatedClass type = Mockito.mock(AnnotatedClass.class);
        Collection<NamedType> result = sr.collectAndResolveSubtypesByClass(config, type);
        assertCollectionEquals(Arrays.asList(new NamedType(Foo.class, "FOO"), new NamedType(Bar.class, "BAR"),
                new NamedType(Baz.class, "BAZ")), result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_UsingProperty() {
        AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
        JavaType baseType = JavaType.construct(String.class);
        Collection<NamedType> result = sr.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertCollectionEquals(Arrays.asList(new NamedType(Foo.class, "FOO"), new NamedType(Bar.class, "BAR"),
                new NamedType(Baz.class, "BAZ")), result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_UsingClass() {
        AnnotatedClass type = Mockito.mock(AnnotatedClass.class);
        Collection<NamedType> result = sr.collectAndResolveSubtypesByTypeId(config, type);
        assertCollectionEquals(Arrays.asList(new NamedType(Foo.class, "FOO"), new NamedType(Bar.class, "BAR"),
                new NamedType(Baz.class, "BAZ")), result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_UsingExplicitRegistration() {
        AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
        JavaType baseType = JavaType.construct(String.class);
        Collection<NamedType> result = sr.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertCollectionEquals(Arrays.asList(new NamedType(Foo.class, "FOO"), new NamedType(Bar.class, "BAR"),
                new NamedType(Baz.class, "BAZ"), new NamedType(BazWithId.class, "BAZ_ID")), result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_UsingAnnotation() {
        AnnotatedClass type = Mockito.mock(AnnotatedClass.class);
        Collection<NamedType> result = sr.collectAndResolveSubtypesByTypeId(config, type);
        assertCollectionEquals(Arrays.asList(new NamedType(Foo.class, "FOO"), new NamedType(Bar.class, "BAR"),
                new NamedType(Baz.class, "BAZ"), new NamedType(BazWithAnnotation.class, "BAZ_ANN")), result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_UsingBaseTypeRegistration() {
        AnnotatedClass type = Mockito.mock(AnnotatedClass.class);
        Collection<NamedType> result = sr.collectAndResolveSubtypesByTypeId(config, type);
        assertCollectionEquals(Arrays.asList(new NamedType(Foo.class, "FOO"), new NamedType(Bar.class, "BAR"),
                new NamedType(Baz.class, "BAZ"), new NamedType(BazWithAnnotation.class, "BAZ_ANN"),
                new NamedType(BazWithId.class, "BAZ_ID")), result);
    }

    @Test
    public void testNullInput() {
        Collection<NamedType> result = sr.collectAndResolveSubtypesByClass(config, null);
        assertNull(result);

        result = sr.collectAndResolveSubtypesByTypeId(config, null);
        assertNull(result);
    }

    private static class Foo {
        @JsonTypeName("FOO")
        static class Inner {
        }
    }

    private static class Bar {
        @JsonTypeName("BAR")
        static class Inner {
        }
    }

    private static class Baz {
        @JsonTypeName("BAZ")
        static class Inner {
        }

        @JsonTypeName("BAZ_ID")
        static class WithId {
        }

        @JsonTypeName("BAZ_ANN")
        @JsonTypeName("BAZ_ANNOTATION")
        static class WithAnnotation {
        }
    }

    private static class BazWithId extends Baz {
    }

    private static class BazWithAnnotation extends Baz {
        @JsonTypeName("BAZ_ANN")
        static class Inner {
        }
    }
}