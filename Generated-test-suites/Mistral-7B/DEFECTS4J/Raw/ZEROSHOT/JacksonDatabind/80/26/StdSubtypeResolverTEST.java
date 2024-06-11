package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testStdSubtypeResolver() throws Exception {
        MapperConfig config = new MapperFactoryBuilder().configure(StdSubtypeResolver.class, false)
                .build();
        ObjectMapper mapper = config.builder().build();

        // Test registration of subtypes by class
        StdSubtypeResolver resolver = config.getDefaultInstance().getSubtypeResolver();
        Class<?> superclass = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Number.class;
        Class<?> subtype3 = Integer.class;
        Class<?> subtype4 = Integer.class;

        resolver.registerSubtypes(subtype1);
        resolver.registerSubtypes(subtype2);
        resolver.registerSubtypes(subtype3);
        resolver.registerSubtypes(subtype4);

        JavaType baseType = mapper.getTypeFactory().constructType(superclass);
        List<NamedType> collected = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(4, collected.size());
        assertTrue(collected.contains(new NamedType(subtype1)));
        assertTrue(collected.contains(new NamedType(subtype2)));
        assertTrue(collected.contains(new NamedType(subtype3)));
        assertTrue(collected.contains(new NamedType(subtype4)));

        // Test registration of subtypes by annotation
        @JsonTypeName("test")
        static class TestClass1 {}
        @JsonTypeName("test2")
        static class TestClass2 {}
        @JsonTypeName("test3")
        static class TestClass3 {}

        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(TestClass1.class);
        resolver.registerSubtypes(TestClass2.class);
        resolver.registerSubtypes(TestClass3.class);

        baseType = mapper.getTypeFactory().constructType(Object.class);
        collected = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(3, collected.size());
        assertTrue(collected.contains(new NamedType(TestClass1.class, "test")));
        assertTrue(collected.contains(new NamedType(TestClass2.class, "test2")));
        assertTrue(collected.contains(new NamedType(TestClass3.class, "test3")));

        // Test resolution by class (serialization)
        AnnotatedMember property = new AnnotatedMethod() {
            @Override
            public AnnotatedClass getAnnotationMembers() {
                return null;
            }

            @Override
            public AnnotatedClass getDeclaringClass() {
                return null;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }

            @Override
            public JavaType getGenericType() {
                return null;
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public AnnotatedClass getParameterType() {
                return null;
            }

            @Override
            public Class<?> getRawType() {
                return Object.class;
            }

            @Override
            public boolean isAnnotation() {
                return false;
            }

            @Override
            public boolean isConstructor() {
                return false;
            }

            @Override
            public boolean isMethod() {
                return true;
            }

            @Override
            public AnnotatedMember withResolved(AnnotatedClass resolved) {
                return null;
            }
        };
        JavaType baseType1 = mapper.getTypeFactory().constructType(String.class);
        collected = resolver.collectAndResolveSubtypesByClass(config, property, baseType1);
        assertEquals(1, collected.size());
        assertTrue(collected.contains(new NamedType(String.class, null)));

        JavaType baseType2 = mapper.getTypeFactory().constructType(Number.class);
        collected = resolver.collectAndResolveSubtypesByClass(config, property, baseType2);
        assertEquals(3, collected.size());
        assertTrue(collected.contains(new NamedType(Integer.class, null)));
        assertTrue(collected.contains(new NamedType(Long.class, null)));
        assertTrue(collected.contains(new NamedType(Double.class, null)));

        JavaType baseType3 = mapper.getTypeFactory().constructType(Object.class);
        collected = resolver.collectAndResolveSubtypesByClass(config, property, baseType3);
        assertEquals(7, collected.size());
        assertTrue(collected.contains(new NamedType(String.class, null)));
        assertTrue(collected.contains(new NamedType(Integer.class, null)));
        assertTrue(collected.contains(new NamedType(Long.class, null)));
        assertTrue(collected.contains(new NamedType(Double.class, null)));
        assertTrue(collected.contains(new NamedType(TestClass1.class, "test")));
        assertTrue(collected.contains(new NamedType(TestClass2.class, "test2")));
        assertTrue(collected.contains(new NamedType(TestClass3.class, "test3")));

        // Test resolution by class (deserialization)
        AnnotatedClass baseClass = new AnnotatedClass() {
            @Override
            public AnnotatedClass getAnnotationMembers() {
                return null;
            }

            @Override
            public AnnotatedClass getDeclaringClass() {
                return null;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }

            @Override
            public JavaType getGenericType() {
                return null;
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public AnnotatedMember findMember(String name) {
                return null;
            }

            @Override
            public AnnotatedClass getSuperClass() {
                return null;
            }

            @Override
            public List<AnnotatedClass> getInterfaces() {
                return Collections.emptyList();
            }

            @Override
            public Class<?> getRawType() {
                return Object.class;
            }

            @Override
            public boolean isAnnotation() {
                return false;
            }

            @Override
            public boolean isArray() {
                return false;
            }

            @Override
            public boolean isEnum() {
                return false;
            }

            @Override
            public boolean isInterface() {
                return false;
            }

            @Override
            public boolean isLocalClass() {
                return false;
            }

            @Override
            public boolean isMemberClass() {
                return false;
            }

            @Override
            public boolean isSynthetic() {
                return false;
            }

            @Override
            public String toString() {
                return "baseClass";
            }
        };
        collected = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType3);
        assertEquals(7, collected.size());
        assertTrue(collected.contains(new NamedType(String.class, null)));
        assertTrue(collected.contains(new NamedType(Integer.class, null)));
        assertTrue(collected.contains(new NamedType(Long.class, null)));
        assertTrue(collected.contains(new NamedType(Double.class, null)));
        assertTrue(collected.contains(new NamedType(TestClass1.class, "test")));
        assertTrue(collected.contains(new NamedType(TestClass2.class, "test2")));
        assertTrue(collected.contains(new NamedType(TestClass3.class, "test3")));

        // Test resolution by class (deserialization) with explicit type id
        AnnotatedClass baseClass2 = new AnnotatedClass() {
            @Override
            public AnnotatedClass getAnnotationMembers() {
                return null;
            }

            @Override
            public AnnotatedClass getDeclaringClass() {
                return null;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }

            @Override
            public JavaType getGenericType() {
                return null;
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public AnnotatedMember findMember(String name) {
                return null;
            }

            @Override
            public AnnotatedClass getSuperClass() {
                return null;
            }

            @Override
            public List<AnnotatedClass> getInterfaces() {
                return Collections.emptyList();
            }

            @Override
            public Class<?> getRawType() {
                return Object.class;
            }

            @Override
            public boolean isAnnotation() {
                return false;
            }

            @Override
            public boolean isArray() {
                return false;
            }

            @Override
            public boolean isEnum() {
                return false;
            }

            @Override
            public boolean isInterface() {
                return false;
            }

            @Override
            public boolean isLocalClass() {
                return false;
            }

            @Override
            public boolean isMemberClass() {
                return false;
            }

            @Override
            public boolean isSynthetic() {
                return false;
            }

            @Override
            public String toString() {
                return "baseClass2";
            }

            @Override
            public JavaType getAnnotationType() {
                return null;
            }

            @Override
            public JavaType getType() {
                return mapper.getTypeFactory().constructType(Object.class);
            }

            @Override
            public java.lang.reflect.AnnotatedElement getMember() {
                return null;
            }
        };
        collected = resolver.collectAndResolveSubtypesByTypeId(config, property, baseClass2);
        assertEquals(7, collected.size());
        assertTrue(collected.contains(new NamedType(String.class, null)));
        assertTrue(collected.contains(new NamedType(Integer.class, null)));
        assertTrue(collected.contains(new NamedType(Long.class, null)));
        assertTrue(collected.contains(new NamedType(Double.class, null)));
        assertTrue(collected.contains(new NamedType(TestClass1.class, "test")));
        assertTrue(collected.contains(new NamedType(TestClass2.class, "test2")));
        assertTrue(collected.contains(new NamedType(TestClass3.class, "test3")));

        // Test registration of subtypes by type id
        MapperConfig config2 = new MapperFactoryBuilder().configure(StdSubtypeResolver.class, false)
                .build();
        resolver = config2.getDefaultInstance().getSubtypeResolver();
        Set<NamedType> registeredSubtypes = new LinkedHashSet<>();
        Map<String,NamedType> registeredTypesById = new LinkedHashMap<>();

        resolver.registerSubtypes(new NamedType(subtype1, "sub1"));
        resolver.registerSubtypes(new NamedType(subtype2, "sub2"));
        resolver.registerSubtypes(new NamedType(subtype3, "sub3"));
        resolver.registerSubtypes(new NamedType(subtype4, "sub4"));

        ClassType superType = mapper.getTypeFactory().constructType(Object.class);
        collected = resolver.collectAndResolveSubtypesByTypeId(config2, property, superType);
        assertEquals(4, collected.size());
        Set<JavaType> foundTypes = new HashSet<>();
        for (NamedType type : collected) {
            foundTypes.add(mapper.getTypeFactory().constructType(type.getType()));
        }
        assertTrue(foundTypes.contains(mapper.getTypeFactory().constructType(subtype1)));
        assertTrue(foundTypes.contains(mapper.getTypeFactory().constructType(subtype2)));
        assertTrue(foundTypes.contains(mapper.getTypeFactory().constructType(subtype3)));
        assertTrue(foundTypes.contains(mapper.getTypeFactory().constructType(subtype4)));

        // Test registration of subtypes by class name
        resolver = new StdSubtypeResolver();
        Map<String,Class<?>> registeredClassesById = new LinkedHashMap<>();
        registeredClassesById.put("sub1", subtype1);
        registeredClassesById.put("sub2", subtype2);
        registeredClassesById.put("sub3", subtype3);
        registeredClassesById.put("sub4", subtype4);

        resolver.registerSubtypesByName(registeredClassesById.values().toArray(new Class[0]));

        collected = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType3);
        assertEquals(7, collected.size());
        assertTrue(collected.contains(new NamedType(String.class, null)));
        assertTrue(collected.contains(new NamedType(Integer.class, null)));
        assertTrue(collected.contains(new NamedType(Long.class, null)));
        assertTrue(collected.contains(new NamedType(Double.class, null)));
        assertTrue(collected.contains(new NamedType(TestClass1.class, "test")));
        assertTrue(collected.contains(new NamedType(TestClass2.class, "test2")));
        assertTrue(collected.contains(new NamedType(TestClass3.class, "test3")));
    }
}