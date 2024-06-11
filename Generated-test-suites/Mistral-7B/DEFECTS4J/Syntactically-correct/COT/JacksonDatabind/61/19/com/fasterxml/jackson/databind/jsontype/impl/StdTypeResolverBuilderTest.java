package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeFactory;

import org.junit.Test;

/**
 * Test class for StdTypeResolverBuilder
 */
public class StdTypeResolverBuilderTest {

    // Helper method to create a JsonTypeInfo.Id instance for testing
    private JsonTypeInfo.Id createIdTypeInstance() {
        return new JsonTypeInfo.Id() {
            @Override
            public String getName() {
                return "customIdType";
            }

            @Override
            public String getDefaultPropertyName() {
                return "customTypeProperty";
            }

            @Override
            public As getInclude() {
                return As.PROPERTY;
            }
        };
    }

    // Test method for init()
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        JsonTypeInfo.Id idType = createIdTypeInstance();
        TypeIdResolver idRes = TypeIdResolver.STD_INSTANCE;
        StdTypeResolverBuilder builderInit = builder.init(idType, idRes);

        // Check that init method set the correct values
        assertEquals(idType, builderInit.getIdType());
        assertEquals(idRes, builderInit.getCustomIdResolver());
        assertEquals(idType.getDefaultPropertyName(), builderInit.getTypeProperty());
        assertFalse(builderInit.isTypeIdVisible());
        assertNull(builderInit.getDefaultImpl());
    }

    // Test method for init() with null arguments
    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, TypeIdResolver.STD_INSTANCE);
    }

    // Test method for buildTypeSerializer()
    @Test
    public void testBuildTypeSerializer() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
            new SimpleType(String.class), new SimpleType(Integer.class)
        );

        TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        builder.init(Id.NONE, null);
        serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        builder.init(Id.CLASSES, null);
        serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
        assertNotNull(serializer);
    }

    // Test method for buildTypeDeserializer()
    @Test
    public void testBuildTypeDeserializer() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
            new SimpleType(String.class), new SimpleType(Integer.class)
        );

        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        builder.init(Id.NONE, null);
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        builder.init(Id.CLASSES, null);
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
    }

    // Test method for inclusion()
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);

        // Test setting inclusion to null
        builder.inclusion(null);
        assertNull(builder.getIncludeAs());

        // Test setting inclusion to WRAPPER_ARRAY
        builder.inclusion(As.WRAPPER_ARRAY);
        assertEquals(As.WRAPPER_ARRAY, builder.getIncludeAs());

        // Test setting inclusion to PROPERTY
        builder.inclusion(As.PROPERTY);
        assertEquals(As.PROPERTY, builder.getIncludeAs());

        // Test setting inclusion to WRAPPER_OBJECT
        builder.inclusion(As.WRAPPER_OBJECT);
        assertEquals(As.WRAPPER_OBJECT, builder.getIncludeAs());

        // Test setting inclusion to EXTERNAL_PROPERTY
        builder.inclusion(As.EXTERNAL_PROPERTY);
        assertEquals(As.EXTERNAL_PROPERTY, builder.getIncludeAs());
    }

    // Test method for typeProperty()
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);

        // Test setting typeProperty to null
        builder.typeProperty(null);
        assertNull(builder.getTypeProperty());

        // Test setting typeProperty to empty string
        builder.typeProperty("");
        assertNull(builder.getTypeProperty());

        // Test setting typeProperty to a valid string
        builder.typeProperty("customTypeProperty");
        assertEquals("customTypeProperty", builder.getTypeProperty());
    }

    // Test method for defaultImpl()
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);

        // Test setting defaultImpl to null
        builder.defaultImpl(null);
        assertNull(builder.getDefaultImpl());

        // Test setting defaultImpl to a class
        builder.defaultImpl(Object.class);
        assertEquals(Object.class, builder.getDefaultImpl());

        // Test setting defaultImpl to a NoClass instance
        builder.defaultImpl(NoClass.class);
        assertEquals(NoClass.class, builder.getDefaultImpl());

        // Test setting defaultImpl to a Void instance
        builder.defaultImpl(Void.class);
        assertNull(builder.getDefaultImpl());
    }

    // Test method for typeIdVisibility()
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);

        // Test setting typeIdVisibility to false
        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());

        // Test setting typeIdVisibility to true
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    // Test method for getDefaultImpl()
    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        builder.defaultImpl(String.class);

        assertEquals(String.class, builder.getDefaultImpl());
    }

    // Test method for getTypeProperty()
    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        builder.typeProperty("customTypeProperty");

        assertEquals("customTypeProperty", builder.getTypeProperty());
    }

    // Test method for isTypeIdVisible()
    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        builder.typeIdVisibility(true);

        assertTrue(builder.isTypeIdVisible());
    }

    // Test method for idResolver()
    @Test
    public void testIdResolver() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
            new SimpleType(String.class), new SimpleType(Integer.class)
        );

        // Test creating a standard id resolver with default settings
        TypeIdResolver idResolverDefault = builder.idResolver(mapper.getSerializationConfig(), baseType, subtypes, true, false);
        assertNotNull(idResolverDefault);

        // Test creating a standard id resolver with forSer = false and forDeser = true
        TypeIdResolver idResolverDeser = builder.idResolver(mapper.getDeserializationConfig(), baseType, subtypes, false, true);
        assertNotNull(idResolverDeser);

        // Test creating a custom id resolver
        TypeIdResolver customIdResolver = new TestTypeIdResolver();
        builder.defaultImpl(TestCustomClass.class);
        builder.customIdResolver(customIdResolver);
        TypeIdResolver idResolverCustom = builder.idResolver(mapper.getSerializationConfig(), baseType, subtypes, true, false);
        assertEquals(customIdResolver, idResolverCustom);
    }
}

class TestCustomClass {
    public String customField;
}

class TestTypeIdResolver extends TypeIdResolverBase {
    @Override
    public TypeId idFromValue(Object value) {
        if (value instanceof String) {
            return new SimpleType(TestCustomClass.class);
        }
        return null;
    }
}