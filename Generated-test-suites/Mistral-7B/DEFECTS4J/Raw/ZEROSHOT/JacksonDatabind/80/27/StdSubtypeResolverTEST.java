package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedType;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBaseTest.TestBase;

public class StdSubtypeResolverTest extends TestBase {

    @Test
    public void testStdSubtypeResolver() throws Exception {
        ObjectMapper mapper = getMapper();
        MapperConfig config = mapper.getConfig();
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        config.setSubtypeResolver(resolver);

        // Test registration of classes
        Class<?> parent = TestBase.class;
        Class<?> child1 = Child1.class;
        Class<?> child2 = Child2.class;

        resolver.registerSubtypes(child1, child2);
        resolver.registerSubtypes(parent, child1, child2);

        // Test serialization
        TestBean bean = new TestBean();
        bean.c1 = new Child1();
        bean.c2 = new Child2();

        byte[] bytes = mapper.writeValueAsBytes(bean);
        TestBean deserialized = mapper.readValue(bytes, TestBean.class);

        assertEquals(bean, deserialized);

        // Test deserialization by class
        JavaType baseType = mapper.getTypeFactory().constructType(TestBean.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(3, subtypes.size());
        List<Class<?>> subtypesClasses = new ArrayList<>();
        for (NamedType namedType : subtypes) {
            subtypesClasses.add(namedType.getType());
        }
        assertArrayEquals(new Class<?>[]{TestBean.class, Child1.class, Child2.class},
                subtypesClasses.toArray(new Class[0]));

        // Test deserialization by class with id
        AnnotatedType idAnnotatedType = mapper.getTypeFactory()
                .constructType(TestBean.class, Id.CLASS, "type");
        Collection<NamedType> subtypesById = resolver.collectAndResolveSubtypesByClassId(config, null, idAnnotatedType);
        assertEquals(3, subtypesById.size());
        List<Class<?>> subtypesByIdClasses = new ArrayList<>();
        for (NamedType namedType : subtypesById) {
            subtypesByIdClasses.add(namedType.getType());
        }
        assertArrayEquals(new Class<?>[]{TestBean.class, Child1.class, Child2.class},
                subtypesByIdClasses.toArray(new Class[0]));

        // Test deserialization by type id
        JavaType baseTypeWithId = mapper.getTypeFactory().constructType(TestBean.class,
                TestBean.Type.class);
        Collection<NamedType> subtypesByIdType = resolver.collectAndResolveSubtypesByTypeId(config, null,
                baseTypeWithId);
        assertEquals(3, subtypesByIdType.size());
        List<Class<?>> subtypesByIdTypeClasses = new ArrayList<>();
        for (NamedType namedType : subtypesByIdType) {
            subtypesByIdTypeClasses.add(namedType.getType());
        }
        assertArrayEquals(new Class<?>[]{TestBean.class, Child1.class, Child2.class},
                subtypesByIdTypeClasses.toArray(new Class[0]));

        // Test deserialization by type id with custom name
        JavaType baseTypeWithIdCustomName = mapper.getTypeFactory().constructType(TestBean.class,
                TestBean.Type.class, "customName");
        Collection<NamedType> subtypesByIdTypeCustomName = resolver.collectAndResolveSubtypesByTypeId(config, null,
                baseTypeWithIdCustomName);
        assertEquals(3, subtypesByIdTypeCustomName.size());
        List<Class<?>> subtypesByIdTypeCustomNameClasses = new ArrayList<>();
        for (NamedType namedType : subtypesByIdTypeCustomName) {
            subtypesByIdTypeCustomNameClasses.add(namedType.getType());
        }
        assertArrayEquals(new Class<?>[]{TestBean.class, Child1.class, Child2.class},
                subtypesByIdTypeCustomNameClasses.toArray(new Class[0]));

        // Test deserialization by type id with custom name and implicit name
        JavaType baseTypeWithIdCustomNameImplicit = mapper.getTypeFactory()
                .constructType(TestBean.class, TestBean.Type.class, "customName", TestBean.CustomType.class);
        Collection<NamedType> subtypesByIdTypeCustomNameImplicit = resolver.collectAndResolveSubtypesByTypeId(config, null,
                baseTypeWithIdCustomNameImplicit);
        assertEquals(3, subtypesByIdTypeCustomNameImplicit.size());
        List<Class<?>> subtypesByIdTypeCustomNameImplicitClasses = new ArrayList<>();
        for (NamedType namedType : subtypesByIdTypeCustomNameImplicit) {
            subtypesByIdTypeCustomNameImplicitClasses.add(namedType.getType());
        }
        assertArrayEquals(new Class<?>[]{TestBean.class, Child1.class, Child2.class},
                subtypesByIdTypeCustomNameImplicitClasses.toArray(new Class[0]));

        // Test deserialization by property
        ObjectMapper mapperWithFeature = getMapperWithFeature(MapperFeature.AUTO_DETECT_TYPES, false);
        TestBean deserializedProperty = mapperWithFeature.readValue(new String("{\"type\":\"Child1\"}"), TestBean.class);
        assertEquals(new Child1(), deserializedProperty.c1);
    }

    static class TestBase {
        public static class Child1 {
            @JsonTypeName("Child1")
            public static class CustomType {}
        }

        public static class Child2 {
            @JsonTypeName("Child2")
            public static class CustomType {}
        }

        public Child1 c1;
        public Child2 c2;

        public enum Type {
            TYPE1,
            TYPE2,
            TYPE3
        }
    }
}