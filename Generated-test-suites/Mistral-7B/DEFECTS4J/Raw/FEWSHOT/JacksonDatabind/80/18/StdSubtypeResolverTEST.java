package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClassResolver;

public class StdSubtypeResolverTest {

    private static final MapperConfig<?> config = new MapperConfig.Builder().build();
    private static final AnnotationIntrospector ai = new JacksonAnnotationIntrospector(config);
    private static final ClassIntrospector classIntrospector = new ClassIntrospector(config);

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<? extends Object> baseType = Object.class;
        Class<Subclass1> subclass1 = Subclass1.class;
        Class<Subclass2> subclass2 = Subclass2.class;

        resolver.registerSubtypes(baseType, Subclass1.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertNotNull(subtypes);
        assertTrue(subtypes.size() > 0);
        assertTrue(subtypes.contains(new NamedType(subclass1)));
        assertFalse(subtypes.contains(new NamedType(subclass2)));

        resolver.registerSubtypes(baseType, Subclass1.class, Subclass2.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(subtypes.size() > 0);
        assertTrue(subtypes.contains(new NamedType(subclass1)));
        assertTrue(subtypes.contains(new NamedType(subclass2)));
    }

    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<? extends Object> baseType = Object.class;

        resolver.registerSubtypes(new ClassNameFilter("Subclass1"));
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertNotNull(subtypes);
        assertTrue(subtypes.size() > 0);
        // Use reflection to check if the registered type is Subclass1
        assertTrue(subtypes.get(0).getType().isAssignableFrom(Subclass1.class));

        resolver.registerSubtypes(new ClassNameFilter("Subclass2"));
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(subtypes.size() > 0);
        assertTrue(subtypes.get(1).getType().isAssignableFrom(Subclass2.class));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<? extends Object> baseType = Object.class;
        Class<Subclass1> subclass1 = Subclass1.class;
        Class<Subclass2> subclass2 = Subclass2.class;

        // Register Subclass1 and Subclass2
        resolver.registerSubtypes(baseType, subclass1, subclass2);

        // Test with null property
        List<NamedType> subtypes1 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertNotNull(subtypes1);
        assertTrue(subtypes1.size() > 0);
        assertTrue(subtypes1.contains(new NamedType(subclass1)));
        assertTrue(subtypes1.contains(new NamedType(subclass2)));

        // Test with a property with no annotations
        AnnotatedClass ac1 = AnnotatedClassResolver.resolve(config, Subclass1.class);
        AnnotatedMember property1 = ClassIntrospector.findProperty(ac1, "property");
        List<NamedType> subtypes2 = resolver.collectAndResolveSubtypesByClass(config, property1, baseType);
        assertNotNull(subtypes2);
        assertTrue(subtypes2.size() > 0);
        assertTrue(subtypes2.contains(new NamedType(subclass1)));
        assertTrue(subtypes2.contains(new NamedType(subclass2)));

        // Test with a property with @JsonTypeInfo
        AnnotatedClass ac2 = AnnotatedClassResolver.resolve(config, Subclass1WithAnnotation.class);
        AnnotatedMember property2 = ClassIntrospector.findProperty(ac2, "property");
        List<NamedType> subtypes3 = resolver.collectAndResolveSubtypesByClass(config, property2, baseType);
        assertNotNull(subtypes3);
        assertTrue(subtypes3.size() > 0);
        assertTrue(subtypes3.contains(new NamedType(Subclass1WithAnnotation.class)));
        assertFalse(subtypes3.contains(new NamedType(Subclass1.class)));
        assertFalse(subtypes3.contains(new NamedType(Subclass2.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<? extends Object> baseType = Object.class;
        Class<Subclass1> subclass1 = Subclass1.class;
        Class<Subclass2> subclass2 = Subclass2.class;

        // Register Subclass1 and Subclass2
        resolver.registerSubtypes(baseType, subclass1, subclass2);

        // Test with null property
        List<NamedType> subtypes1 = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertNotNull(subtypes1);
        assertTrue(subtypes1.size() > 0);
        assertTrue(subtypes1.containsAll(Arrays.asList(new NamedType(subclass1), new NamedType(subclass2))));

        // Test with a property with no annotations
        AnnotatedClass ac1 = AnnotatedClassResolver.resolve(config, Subclass1.class);
        AnnotatedMember property1 = ClassIntrospector.findProperty(ac1, "property");
        List<NamedType> subtypes2 = resolver.collectAndResolveSubtypesByTypeId(config, property1, baseType);
        assertNotNull(subtypes2);
        assertTrue(subtypes2.size() > 0);
        assertTrue(subtypes2.containsAll(Arrays.asList(new NamedType(subclass1), new NamedType(subclass2))));

        // Test with a property with @JsonTypeInfo
        AnnotatedClass ac2 = AnnotatedClassResolver.resolve(config, Subclass1WithAnnotation.class);
        AnnotatedMember property2 = ClassIntrospector.findProperty(ac2, "property");
        List<NamedType> subtypes3 = resolver.collectAndResolveSubtypesByTypeId(config, property2, baseType);
        assertNotNull(subtypes3);
        assertTrue(subtypes3.size() > 0);
        assertTrue(subtypes3.contains(new NamedType(Subclass1WithAnnotation.class)));
        assertFalse(subtypes3.contains(new NamedType(Subclass1.class)));
        assertFalse(subtypes3.contains(new NamedType(Subclass2.class)));
    }

    private static class Subclass1 {
        String property;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    private static class Subclass1WithAnnotation {
        String property;
        String type;
    }

    private static class Subclass2 {
        String property;
    }

    private static class ClassNameFilter implements Predicate<Class<?>> {
        private final String className;

        public ClassNameFilter(String className) {
            this.className = className;
        }

        @Override
        public boolean test(Class<?> clazz) {
            return className.equals(clazz.getSimpleName());
        }
    }
}