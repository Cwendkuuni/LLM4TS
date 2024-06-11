package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdTypeResolver;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StdSubtypeResolverTest {

    @Test
    public void testRegistrationAndCollectionByClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Register some subtypes by class
        subtypeResolver.registerSubtypes(Person.class, PersonWithAge.class);
        subtypeResolver.registerSubtypes(Person.class, PersonWithGender.class);

        // Check that subtypes are collected correctly by class
        MapperConfig<?> config = mapper.getConfiguration();
        AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Person.class);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, baseType);

        Set<Class<?>> expected = new HashSet<>(Arrays.asList(
                PersonWithAge.class, PersonWithGender.class
        ));
        Set<Class<?>> actual = new HashSet<>();
        for (NamedType namedType : subtypes) {
            actual.add(namedType.getType());
        }
        Assert.assertEquals(expected, actual);

        // Check that subtypes are collected correctly by annotated member
        AnnotatedMember property = AnnotatedMember.construct(Person.class, "name");
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, null);
        Assert.assertEquals(0, subtypes.size());

        // Check that subtypes are collected correctly by type id
        AnnotatedClass baseTypeById = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Person.class);
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseTypeById);
        Assert.assertEquals(expected, new HashSet<>(subtypes));
    }

    @Test
    public void testRegistrationAndCollectionByTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Register some subtypes by class
        subtypeResolver.registerSubtypes(Person.class, PersonWithAge.class);
        subtypeResolver.registerSubtypes(Person.class, PersonWithGender.class);

        // Register some subtypes by type id
        subtypeResolver.registerSubtypes(Person.class, PersonWithAge.class, "age");
        subtypeResolver.registerSubtypes(Person.class, PersonWithGender.class, "gender");

        // Check that subtypes are collected correctly by class
        MapperConfig<?> config = mapper.getConfiguration();
        AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Person.class);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, baseType);

        Set<Class<?>> expected = new HashSet<>(Arrays.asList(
                PersonWithAge.class, PersonWithGender.class, PersonWithAge.class, PersonWithGender.class
        ));
        Set<Class<?>> actual = new HashSet<>();
        for (NamedType namedType : subtypes) {
            actual.add(namedType.getType());
        }
        Assert.assertEquals(expected, actual);

        // Check that subtypes are collected correctly by annotated member
        AnnotatedMember property = AnnotatedMember.construct(Person.class, "name");
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, null);
        Assert.assertEquals(0, subtypes.size());

        // Check that subtypes are collected correctly by type id
        AnnotatedClass baseTypeById = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Person.class);
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseTypeById);
        Assert.assertEquals(expected, new HashSet<>(subtypes));
    }

    @Test
    public void testEmptyRegistration() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Register empty set of subtypes
        subtypeResolver.registerSubtypes();

        // Check that no subtypes are collected by class
        MapperConfig<?> config = mapper.getConfiguration();
        AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Person.class);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, baseType);
        Assert.assertEquals(0, subtypes.size());

        // Check that no subtypes are collected by annotated member
        AnnotatedMember property = AnnotatedMember.construct(Person.class, "name");
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, null);
        Assert.assertEquals(0, subtypes.size());

        // Check that no subtypes are collected by type id
        AnnotatedClass baseTypeById = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Person.class);
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseTypeById);
        Assert.assertEquals(0, subtypes.size());
    }

    @Test
    public void testPrecedenceOfTypeIdOverClasses() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Register some subtypes by class
        subtypeResolver.registerSubtypes(Person.class, PersonWithAge.class);
        subtypeResolver.registerSubtypes(Person.class, PersonWithGender.class);

        // Register some subtypes by type id (should take precedence over classes)
        subtypeResolver.registerSubtypes(Person.class, PersonWithAge.class, "age");
        subtypeResolver.registerSubtypes(Person.class, PersonWithGender.class, "gender");

        // Check that subtypes are collected correctly by class
        MapperConfig<?> config = mapper.getConfiguration();
        AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Person.class);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, baseType);

        Set<Class<?>> expected = new HashSet<>(Arrays.asList(
                PersonWithAge.class, PersonWithGender.class // registered by class
        ), Collections.singleton(PersonWithAge.class)); // registered by type id
        Set<Class<?>> actual = new HashSet<>();
        for (NamedType namedType : subtypes) {
            actual.add(namedType.getType());
        }
        Assert.assertEquals(expected, actual);

        // Check that subtypes are collected correctly by annotated member
        AnnotatedMember property = AnnotatedMember.construct(Person.class, "name");
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, null);
        Assert.assertEquals(0, subtypes.size());

        // Check that subtypes are collected correctly by type id
        AnnotatedClass baseTypeById = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Person.class);
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseTypeById);
        Assert.assertEquals(Collections.singleton(PersonWithAge.class), subtypes);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    static class PersonWithAge extends Person {
        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @JsonProperty("type")
        public String getType() {
            return "age";
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    static class PersonWithGender extends Person {
        private String gender;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        @JsonProperty("type")
        public String getType() {
            return "gender";
        }
    }
}