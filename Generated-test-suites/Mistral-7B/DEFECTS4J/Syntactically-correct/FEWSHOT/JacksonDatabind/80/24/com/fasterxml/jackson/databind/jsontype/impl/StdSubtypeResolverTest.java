package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.databind.util.TestUtil.constructMapper;
import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NamedType;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(String.class, "foo.bar.FooString.class", "foo.bar.FooString2.class");

        JavaType baseType = JavaType.constructType(int[].class);
        MapperConfig<?> config = new MapperConfig<>();
        config.setFeature(MapperFeature.AUTO_DETECT_TYPES, false);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class, "foo.bar.FooString")));
        assertTrue(subtypes.contains(new NamedType(String.class, "foo.bar.FooString2")));
    }

    @Test
    public void testRegisterSubtypesByAnnotation() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Foo fooAnnotation = new Foo();
        fooAnnotation.value().add(String.class);
        fooAnnotation.value().add(Integer.class);

        Class<Bar> barClass = Bar.class;
        resolver.registerSubtypes(barClass, new Annotation[] { fooAnnotation });

        JavaType baseType = JavaType.constructType(int[].class);
        MapperConfig<?> config = new MapperConfig<>();
        config.setFeature(MapperFeature.AUTO_DETECT_TYPES, false);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class, null)));
        assertTrue(subtypes.contains(new NamedType(Integer.class, null)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(String.class, "foo.bar.FooString.class", "foo.bar.FooString2.class");

        MapperConfig<?> config = new MapperConfig<>();
        config.setFeature(MapperFeature.AUTO_DETECT_TYPES, false);
        Map<String, Class<?>> subtypesByName = new LinkedHashMap<>();
        subtypesByName.put("String1", String1.class);
        subtypesByName.put("String2", String2.class);
        AnnotatedClass stringClass = AnnotatedClass.construct(String.class, config);
        resolver.collectAndResolveSubtypesByClass(config, stringClass, null);

        Set<Class<?>> handledTypes = new HashSet<>();
        Map<String, NamedType> namedTypesByName = new LinkedHashMap<>();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, JavaType.constructType(String.class));
        assertEquals(4, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class, null)));
        assertTrue(subtypes.contains(new NamedType(String1.class, null)));
        assertTrue(subtypes.contains(new NamedType(String2.class, null)));
        assertTrue(subtypes.contains(new NamedType(String.class, "foo.bar.FooString")));
        assertTrue(subtypes.contains(new NamedType(String.class, "foo.bar.FooString2")));

        assertEquals(2, handledTypes.size());
        assertTrue(handledTypes.contains(String1.class));
        assertTrue(handledTypes.contains(String2.class));

        assertEquals(4, namedTypesByName.size());
        assertEquals(new NamedType(String1.class, null), namedTypesByName.get("String1"));
        assertEquals(new NamedType(String2.class, null), namedTypesByName.get("String2"));
        assertEquals(new NamedType(String.class, "foo.bar.FooString"), namedTypesByName.get("foo.bar.FooString"));
        assertEquals(new NamedType(String.class, "foo.bar.FooString2"), namedTypesByName.get("foo.bar.FooString2"));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(String.class, "foo.bar.FooString.class", "foo.bar.FooString2.class");

        MapperConfig<?> config = new MapperConfig<>();
        config.setFeature(MapperFeature.AUTO_DETECT_TYPES, false);
        Map<String, Class<?>> subtypesByName = new LinkedHashMap<>();
        subtypesByName.put("String1", String1.class);
        subtypesByName.put("String2", String2.class);
        AnnotatedClass stringClass = AnnotatedClass.construct(String.class, config);
        resolver.collectAndResolveSubtypesByClassId(config, stringClass, null);

        Set<Class<?>> handledTypes = new HashSet<>();
        Map<String, NamedType> namedTypesByName = new LinkedHashMap<>();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, JavaType.constructType(String.class));
        assertEquals(4, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class, null)));
        assertTrue(subtypes.contains(new NamedType(String1.class, null)));
        assertTrue(subtypes.contains(new NamedType(String2.class, null)));
        assertTrue(subtypes.contains(new NamedType(String.class, "foo.bar.FooString")));
        assertTrue(subtypes.contains(new NamedType(String.class, "foo.bar.FooString2")));

        assertEquals(2, handledTypes.size());
        assertTrue(handledTypes.contains(String1.class));
        assertTrue(handledTypes.contains(String2.class));

        assertEquals(4, namedTypesByName.size());
        assertEquals(new NamedType(String1.class, null), namedTypesByName.get("String1"));
        assertEquals(new NamedType(String2.class, null), namedTypesByName.get("String2"));
        assertEquals(new NamedType(String.class, "foo.bar.FooString"), namedTypesByName.get("foo.bar.FooString"));
        assertEquals(new NamedType(String.class, "foo.bar.FooString2"), namedTypesByName.get("foo.bar.FooString2"));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(String.class, "foo.bar.FooString.class", "foo.bar.FooString2.class");

        MapperConfig<?> config = new MapperConfig<>();
        config.setFeature(MapperFeature.AUTO_DETECT_TYPES, false);
        Map<String, Class<?>> subtypesByName = new LinkedHashMap<>();
        subtypesByName.put("String1", String1.class);
        subtypesByName.put("String2", String2.class);
        AnnotatedClass stringClass = AnnotatedClass.construct(String.class, config);
        resolver.collectAndResolveSubtypesByTypeId(config, stringClass, null);

        Set<Class<?>> handledTypes = new HashSet<>();
        Map<String, NamedType> namedTypesByName = new LinkedHashMap<>();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, JavaType.constructType(String.class));
        assertEquals(4, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class, null)));
        assertTrue(subtypes.contains(new NamedType(String1.class, null)));
        assertTrue(subtypes.contains(new NamedType(String2.class, null)));
        assertTrue(subtypes.contains(new NamedType(String.class, "foo.bar.FooString")));
        assertTrue(subtypes.contains(new NamedType(String.class, "foo.bar.FooString2")));

        assertEquals(2, handledTypes.size());
        assertTrue(handledTypes.contains(String1.class));
        assertTrue(handledTypes.contains(String2.class));

        assertEquals(4, namedTypesByName.size());
        assertEquals(new NamedType(String1.class, null), namedTypesByName.get("String1"));
        assertEquals(new NamedType(String2.class, null), namedTypesByName.get("String2"));
        assertEquals(new NamedType(String.class, "foo.bar.FooString"), namedTypesByName.get("foo.bar.FooString"));
        assertEquals(new NamedType(String.class, "foo.bar.FooString2"), namedTypesByName.get("foo.bar.FooString2"));
    }

    private static class Foo {
        @JsonTypeInfo(as = As.PROPERTY, use = Id.CLASS, include = As.EXISTING_PROPERTY)
        public List<Class<?>> value() {
            return new ArrayList<>();
        }
    }

    private static class String1 {
        // Empty
    }

    private static class String2 {
        // Empty
    }

}