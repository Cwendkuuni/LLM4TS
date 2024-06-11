package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

@SuppressWarnings("unchecked")
public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;
    private MapperConfig mapperConfig;

    @BeforeClass
    public void setup() {
        mapperConfig = new MapperFactory().getConfig();
        objectMapper = mapperConfig.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
    }

    @Test
    public void testRegistrationAndCollectionByClass() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Register subtypes by class
        subtypeResolver.registerSubtypes(MySubType1.class, MySubType2.class);

        JavaType baseType = objectMapper.getTypeFactory().constructType(MyBaseClass.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, null, baseType);

        Assert.assertEquals(subtypes.size(), 2);
        Assert.assertTrue(subtypes.contains(new NamedType(MySubType1.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(MySubType2.class)));
    }

    @Test
    public void testRegistrationAndCollectionByAnnotation() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Register subtypes by annotation
        MyAnnotation annotation = new MyAnnotation();
        MyAnnotation.SubType1 subType1 = new MyAnnotation.SubType1();
        MyAnnotation.SubType2 subType2 = new MyAnnotation.SubType2();
        subType1.setAnnotation(annotation);
        subType2.setAnnotation(annotation);
        AnnotatedClass annotatedClass1 = AnnotatedClassResolver. resolveWithoutSuperTypes(mapperConfig, MyAnnotation.SubType1.class);
        AnnotatedClass annotatedClass2 = AnnotatedClassResolver. resolveWithoutSuperTypes(mapperConfig, MyAnnotation.SubType2.class);
        subtypeResolver.registerSubtypes(annotatedClass1, annotatedClass2);

        MyBaseClass baseClass = new MyBaseClass();
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(mapperConfig, null, baseClass.getClass());

        Assert.assertEquals(subtypes.size(), 2);
        Assert.assertTrue(subtypes.contains(new NamedType(MyAnnotation.SubType1.class)));
        Assert.assertTrue(subtypes.contains(new NamedType(MyAnnotation.SubType2.class)));
    }

    @Test
    public void testEmptyCollection() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        JavaType baseType = objectMapper.getTypeFactory().constructType(MyBaseClass.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, null, baseType);

        Assert.assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testRegistrationAndCollectionByClassWithBaseType() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Register subtypes with base type
        subtypeResolver.registerSubtypes(MySubType1.class);

        JavaType baseType = objectMapper.getTypeFactory().constructType(MyBaseClass.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, null, baseType);

        Assert.assertEquals(subtypes.size(), 1);
        Assert.assertTrue(subtypes.contains(new NamedType(MySubType1.class)));
    }

    @Test
    public void testCollectionWithAbstractBaseType() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        JavaType baseType = objectMapper.getTypeFactory().constructType(MyAbstractBaseClass.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, null, baseType);

        Assert.assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectionWithEmptyRegisteredSubtypes() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Empty registered subtypes
        subtypeResolver.registerSubtypes();

        JavaType baseType = objectMapper.getTypeFactory().constructType(MyBaseClass.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, null, baseType);

        Assert.assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectionWithNullBaseType() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Null base type
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, null, null);

        Assert.assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectionWithNullProperty() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        JavaType baseType = objectMapper.getTypeFactory().constructType(MyBaseClass.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, null, baseType);

        Assert.assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testSerializationWithRegisteredSubtypes() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(MySubType1.class, MySubType2.class);

        // Serialize object with registered subtypes
        MyBaseClass baseClass = new MyBaseClass();
        ObjectNode rootNode = objectMapper.valueToTree(baseClass);

        ArrayNode subtypesNode = rootNode.path("_class");
        Assert.assertEquals(subtypesNode.size(), 2);
        Assert.assertEquals(subtypesNode.get(0).asText(), MySubType1.class.getName());
        Assert.assertEquals(subtypesNode.get(1).asText(), MySubType2.class.getName());

        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println(jsonString);
    }

    @Test
    public void testSerializationWithAnnotatedSubtypes() throws IOException {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        MyAnnotation annotation = new MyAnnotation();
        MyAnnotation.SubType1 subType1 = new MyAnnotation.SubType1();
        MyAnnotation.SubType2 subType2 = new MyAnnotation.SubType2();
        subType1.setAnnotation(annotation);
        subType2.setAnnotation(annotation);
        subtypeResolver.registerSubtypes(AnnotatedClassResolver. resolveWithoutSuperTypes(mapperConfig, MyAnnotation.SubType1.class),
                AnnotatedClassResolver. resolveWithoutSuperTypes(mapperConfig, MyAnnotation.SubType2.class));

        MyBaseClass baseClass = new MyBaseClass();
        ObjectNode rootNode = objectMapper.valueToTree(baseClass);

        ArrayNode subtypesNode = rootNode.path("_class");
        Assert.assertEquals(subtypesNode.size(), 2);
        Assert.assertEquals(subtypesNode.get(0).asText(), MyAnnotation.SubType1.class.getName());
        Assert.assertEquals(subtypesNode.get(1).asText(), MyAnnotation.SubType2.class.getName());

        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println(jsonString);
    }

    static class MyBaseClass {
    }

    static class MyAbstractBaseClass extends MyBaseClass {
    }

    static class MySubType1 extends MyBaseClass {
    }

    static class MySubType2 extends MyBaseClass {
    }

    static class MyAnnotation {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
        static class SubType1 {
        }

        static class SubType2 {
        }
    }
}