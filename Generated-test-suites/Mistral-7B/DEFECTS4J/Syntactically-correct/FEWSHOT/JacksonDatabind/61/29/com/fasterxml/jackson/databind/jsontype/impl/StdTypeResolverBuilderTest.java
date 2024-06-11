package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builderWithIdType = builder.init(idType, null);
        assertEquals(idType, builderWithIdType.getIdType());
        assertNull(builderWithIdType.getIncludeAs());
        assertNull(builderWithIdType.getTypeProperty());
        assertFalse(builderWithIdType.isTypeIdVisible());
        assertNull(builderWithIdType.getDefaultImpl());
        assertNull(builderWithIdType.getCustomIdResolver());

        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public TypeId idFromValue(Object value, JavaType baseType) {
                return null;
            }
        };
        StdTypeResolverBuilder builderWithCustomIdResolver = builder.init(null, idRes);
        assertNull(builderWithCustomIdResolver.getIdType());
        assertNull(builderWithCustomIdResolver.getIncludeAs());
        assertNull(builderWithCustomIdResolver.getTypeProperty());
        assertFalse(builderWithCustomIdResolver.isTypeIdVisible());
        assertNotNull(builderWithCustomIdResolver.getDefaultImpl());
        assertEquals(idRes, builderWithCustomIdResolver.getCustomIdResolver());
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeSerializer(null, null, null));

        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builderWithIdType = builder.init(idType, null);
        assertNull(builderWithIdType.buildTypeSerializer(null, null, null));

        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public TypeId idFromValue(Object value, JavaType baseType) {
                return null;
            }
        };
        StdTypeResolverBuilder builderWithCustomIdResolver = builder.init(null, idRes);
        assertNull(builderWithCustomIdResolver.buildTypeSerializer(null, null, null));

        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = mapper.getTypeFactory().constructValueType(Collection.class);
        StdTypeResolverBuilder builderWithIdTypeAndConfig = builderWithIdType.init(idType, idRes).init(config, baseType, subtypes);
        assertNull(builderWithIdTypeAndConfig.buildTypeSerializer(null, null, null));

        // Test with WRAPPER_ARRAY inclusion
        builderWithIdTypeAndConfig.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeSerializer serializer = builderWithIdTypeAndConfig.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        // Test with PROPERTY inclusion
        builderWithIdTypeAndConfig.typeProperty("typePropertyName");
        builderWithIdTypeAndConfig.inclusion(JsonTypeInfo.As.PROPERTY);
        serializer = builderWithIdTypeAndConfig.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeDeserializer(null, null, null));

        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builderWithIdType = builder.init(idType, null);
        assertNull(builderWithIdType.buildTypeDeserializer(null, null, null));

        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public TypeId idFromValue(Object value, JavaType baseType) {
                return null;
            }
        };
        StdTypeResolverBuilder builderWithCustomIdResolver = builder.init(null, idRes);
        assertNull(builderWithCustomIdResolver.buildTypeDeserializer(null, null, null));

        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = mapper.getTypeFactory().constructValueType(Collection.class);
        StdTypeResolverBuilder builderWithIdTypeAndConfig = builderWithIdType.init(idType, idRes).init(config, baseType, subtypes);
        assertNull(builderWithIdTypeAndConfig.buildTypeDeserializer(null, null, null));

        // Test with WRAPPER_ARRAY inclusion
        builderWithIdTypeAndConfig.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeDeserializer deserializer = builderWithIdTypeAndConfig.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);

        // Test with PROPERTY inclusion
        builderWithIdTypeAndConfig.typeProperty("typePropertyName");
        builderWithIdTypeAndConfig.inclusion(JsonTypeInfo.As.PROPERTY);
        deserializer = builderWithIdTypeAndConfig.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("typePropertyName");
        assertEquals("typePropertyName", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}