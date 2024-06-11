package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Unit tests for {@link StdSubtypeResolver}.
 */
public class StdSubtypeResolverTest extends TestBase {

    private static final String TEST_BASE_PACKAGE = "test.package.";
    private static final String TEST_BASE_CLASS = "Base";
    private static final String TEST_SUBTYPE_CLASS1 = "Subtype1";
    private static final String TEST_SUBTYPE_CLASS2 = "Subtype2";
    private static final String TEST_SUBTYPE_CLASS3 = "Subtype3";

    private ObjectMapper mapper;
    private AnnotationIntrospector introspector;
    private StdSubtypeResolver subtypeResolver;

    @BeforeClass
    public void beforeClass() throws Exception {
        MapperFactory factory = mapperFactory();
        mapper = factory.build();
        mapper.enable(MapperFeature.AUTO_DETECT_TYPES);
        introspector = mapper.getAnnotationIntrospector();
        subtypeResolver = (StdSubtypeResolver) mapper.getTypeFactory().getDefaultImpl(SubtypeResolver.class);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_registeredSubtypes() throws Exception {
        // GIVEN: register some subtypes
        Class<?> baseType = ClassUtil.findClass(TEST_BASE_PACKAGE + TEST_BASE_CLASS);
        Class<?> subtype1 = ClassUtil.findClass(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS1);
        Class<?> subtype2 = ClassUtil.findClass(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS2);

        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        // WHEN: collect and resolve subtypes by class
        JavaType baseTypeJavaType = mapper.constructType(baseType);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeJavaType);

        // THEN: verify that subtypes are correctly resolved
        Assert.assertEquals(subtypes.size(), 2, "Wrong number of subtypes found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype1)), "Subtype1 not found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype2)), "Subtype2 not found");
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_registeredSubtypes_serialization() throws Exception {
        // GIVEN: register some subtypes
        Class<?> baseType = ClassUtil.findClass(TEST_BASE_PACKAGE + TEST_BASE_CLASS);
        Class<?> subtype1 = ClassUtil.findClass(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS1);
        Class<?> subtype2 = ClassUtil.findClass(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS2);

        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        // WHEN: collect and resolve subtypes by class for serialization
        JavaType baseTypeJavaType = mapper.constructType(baseType);
        Object instance = mapper.readValue(mapper.writeValueAsString(new Object()), baseTypeJavaType);

        // THEN: verify that subtypes are correctly resolved during serialization
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeJavaType);

        Assert.assertEquals(subtypes.size(), 2, "Wrong number of subtypes found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype1)), "Subtype1 not found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype2)), "Subtype2 not found");
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_annotatedSubtypes() throws Exception {
        // GIVEN: define a base class with annotations
        Class<?> baseType = ClassUtil.forName(TEST_BASE_PACKAGE + TEST_BASE_CLASS);
        JavaType baseTypeJavaType = mapper.constructType(baseType);

        // GIVEN: define subtypes with annotations
        Class<?> subtype1 = ClassUtil.forName(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS1);
        Class<?> subtype2 = ClassUtil.forName(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS2);
        Class<?> subtype3 = ClassUtil.forName(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS3);

        // WHEN: collect and resolve subtypes
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, baseTypeJavaType);

        // THEN: verify that subtypes are correctly resolved
        Assert.assertEquals(subtypes.size(), 3, "Wrong number of subtypes found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype1)), "Subtype1 not found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype2)), "Subtype2 not found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype3)), "Subtype3 not found");
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_registeredSubtypes() throws Exception {
        // GIVEN: register some subtypes
        Class<?> baseType = ClassUtil.findClass(TEST_BASE_PACKAGE + TEST_BASE_CLASS);
        Class<?> subtype1 = ClassUtil.findClass(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS1);
        Class<?> subtype2 = ClassUtil.findClass(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS2);

        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        // WHEN: collect and resolve subtypes by type id
        JavaType baseTypeJavaType = mapper.constructType(baseType);
        Type idResolver = mapper.getTypeFactory().constructType(TypeIdResolver.class);
        TypeIdResolver typeIdResolver = mapper.valueToValue(idResolver);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, baseTypeJavaType, typeIdResolver);

        // THEN: verify that subtypes are correctly resolved
        Assert.assertEquals(subtypes.size(), 2, "Wrong number of subtypes found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype1)), "Subtype1 not found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype2)), "Subtype2 not found");
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_annotatedSubtypes() throws Exception {
        // GIVEN: define a base class with no type id annotations
        Class<?> baseType = ClassUtil.forName(TEST_BASE_PACKAGE + TEST_BASE_CLASS);

        // GIVEN: define subtypes with type id annotations
        Class<?> subtype1 = ClassUtil.forName(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS1);
        Class<?> subtype2 = ClassUtil.forName(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS2);
        Class<?> subtype3 = ClassUtil.forName(TEST_BASE_PACKAGE + TEST_SUBTYPE_CLASS3);

        // WHEN: collect and resolve subtypes by type id
        JavaType baseTypeJavaType = mapper.constructType(baseType);
        TypeIdResolver typeIdResolver = mapper.valueToValue(mapper.getPolymorphicTypeIdResolver());
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, baseTypeJavaType, typeIdResolver);

        // THEN: verify that subtypes are correctly resolved
        Assert.assertEquals(subtypes.size(), 3, "Wrong number of subtypes found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype1)), "Subtype1 not found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype2)), "Subtype2 not found");
        Assert.assertTrue(subtypes.contains(new NamedType(subtype3)), "Subtype3 not found");
    }
}