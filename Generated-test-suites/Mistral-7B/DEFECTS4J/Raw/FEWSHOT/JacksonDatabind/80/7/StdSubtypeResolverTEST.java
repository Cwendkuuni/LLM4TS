package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.databind.util.ReflectionUtils.newInstance;
import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        Class<?> subType1 = Integer.class;
        Class<?> subType2 = String.class;

        resolver.registerSubtypes(subType1, subType2);
        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();

        assertTrue(registeredSubtypes.size() > 0);
        assertTrue(registeredSubtypes.contains(new NamedType(subType1)));
        assertTrue(registeredSubtypes.contains(new NamedType(subType2)));
    }

    @Test
    public void testRegisterSubtypesTypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        Class<?> subType1 = Integer.class;
        Class<?> subType2 = String.class;

        resolver.registerSubtypes(subType1, subType2);
        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();

        assertEquals(registeredSubtypes.size(), 2);

        NamedType registeredSubtype1 = registeredSubtypes.iterator().next();
        assertEquals(registeredSubtype1.getType(), subType1);

        NamedType registeredSubtype2 = registeredSubtypes.iterator().next();
        assertEquals(registeredSubtype2.getType(), subType2);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_Serialization() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedMember property = new AnnotatedElement();
        JavaType baseType = Object.class.getType();

        resolver.registerSubtypes(Integer.class, String.class);
        resolver.collectAndResolveSubtypes(config, property, baseType);

        Collection<NamedType> subtypes = resolver.getRegisteredSubtypes();
        assertEquals(subtypes.size(), 2);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_Serialization_WithAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedMember property = new AnnotatedElement();
        JavaType baseType = Object.class.getType();

        resolver.registerSubtypes(Integer.class, String.class);
        @JsonTypeName("IntegerType")
        class IntegerType {}
        @JsonSubTypes({
            @JsonSubTypes.With(value = IntegerType.class, name = "IntegerType")
        })
        class MyClass {
            @JsonProperty
            private int intProp;
        }
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass.class);

        resolver.collectAndResolveSubtypes(config, property, baseType);
        Collection<NamedType> subtypes = resolver.getRegisteredSubtypes();
        assertEquals(subtypes.size(), 2);
        assertTrue(subtypes.contains(new NamedType(Integer.class, "IntegerType")));
        assertTrue(subtypes.contains(new NamedType(String.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_Deserialization() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass type = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Integer.class);

        resolver.registerSubtypes(Integer.class, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type);

        assertEquals(subtypes.size(), 2);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_Deserialization_WithAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.Builder().build();
        @JsonTypeName("IntegerType")
        class IntegerType {}
        @JsonSubTypes({
            @JsonSubTypes.With(value = IntegerType.class, name = "IntegerType")
        })
        class MyClass {
            @JsonProperty
            private int intProp;
        }
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass.class);

        resolver.registerSubtypes(Integer.class, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, annotatedType);

        assertEquals(subtypes.size(), 2);
        assertTrue(subtypes.contains(new NamedType(Integer.class, "IntegerType")));
        assertTrue(subtypes.contains(new NamedType(String.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_Serialization() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedMember property = new AnnotatedElement();
        JavaType baseType = Object.class.getType();

        resolver.registerSubtypes(Integer.class, String.class);
        resolver.collectAndResolveSubtypes(config, property, baseType);

        Collection<NamedType> subtypes = resolver.getRegisteredSubtypes();
        assertEquals(subtypes.size(), 2);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_Serialization_WithAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedMember property = new AnnotatedElement();
        JavaType baseType = Object.class.getType();

        resolver.registerSubtypes(Integer.class, String.class);
        @JsonTypeName("IntegerType")
        class IntegerType {}
        @JsonSubTypes({
            @JsonSubTypes.With(value = IntegerType.class, name = "IntegerType")
        })
        class MyClass {
            @JsonProperty
            private int intProp;
        }
        AnnotatedMember propertyAnnotated = new AnnotatedElement();
        AnnotatedMethod methodAnnotated = new AnnotatedMethod();
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass.class);
        AnnotatedMethod method = new AnnotatedMethod().withName("method").withAnnotations(newAnnotation(JsonTypeIdResolver.class));
        propertyAnnotated.setMember(methodAnnotated.withName("method").withAnnotations(newAnnotation(JsonProperty.class)));
        propertyAnnotated.setMember(new AnnotatedField().withName("intProp").withAnnotation(newAnnotation(JsonProperty.class)));
        property.setMember(propertyAnnotated);

        resolver.collectAndResolveSubtypes(config, property, baseType);
        Collection<NamedType> subtypes = resolver.getRegisteredSubtypes();
        assertEquals(subtypes.size(), 2);
        assertTrue(subtypes.contains(new NamedType(Integer.class, "IntegerType")));
        assertTrue(subtypes.contains(new NamedType(String.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_Deserialization() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass type = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Integer.class);

        resolver.registerSubtypes(Integer.class, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, type);

        assertEquals(subtypes.size(), 2);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_Deserialization_WithAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.Builder().build();
        @JsonTypeName("IntegerType")
        class IntegerType {}
        @JsonSubTypes({
            @JsonSubTypes.With(value = IntegerType.class, name = "IntegerType")
        })
        class MyClass {
            @JsonProperty
            private int intProp;
        }
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass.class);

        resolver.registerSubtypes(Integer.class, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, annotatedType);

        assertEquals(subtypes.size(), 2);
        assertTrue(subtypes.contains(new NamedType(Integer.class, "IntegerType")));
        assertTrue(subtypes.contains(new NamedType(String.class)));
    }

    private static Annotation newAnnotation(Class<? extends Annotation> annotationType) {
        try {
            return newInstance(annotationType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}