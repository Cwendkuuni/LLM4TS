package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInitialization() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new TypeIdResolver() {
            // Dummy implementation
        };
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        // Test with default values
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new TypeIdResolver() {
            // Dummy implementation
        };
        builder.init(idType, idRes);
        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer ts = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.of(Integer.class), null);
        assertNull(ts);

        // Test with non-default values
        builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        builder.inclusion(As.PROPERTY);
        builder.typeProperty("typeProperty");
        builder.typeIdVisibility(true);
        builder.defaultImpl(String.class);
        ts = builder.buildTypeSerializer(mapper.getConfiguration(), JavaType.of(Integer.class), null);
        assertNotNull(ts);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerUnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.buildTypeSerializer(null, null, null);
    }

    @Test
    public void testBuildTypeDeserializer() {
        // Test with default values
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new TypeIdResolver() {
            // Dummy implementation
        };
        builder.init(idType, idRes);
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer td = builder.buildTypeDeserializer(mapper.getConfiguration(), JavaType.of(Integer.class), null);
        assertNull(td);

        // Test with non-default values
        builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        builder.inclusion(As.PROPERTY);
        builder.typeProperty("typeProperty");
        builder.typeIdVisibility(true);
        builder.defaultImpl(String.class);
        td = builder.buildTypeDeserializer(mapper.getConfiguration(), JavaType.of(Integer.class), null);
        assertNotNull(td);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerUnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.buildTypeDeserializer(null, null, null);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(As.NONE, builder.getIncludeAs());
        builder.inclusion(As.PROPERTY);
        assertEquals(As.PROPERTY, builder.getIncludeAs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getTypeProperty());
        builder.typeProperty("typeProperty");
        assertEquals("typeProperty", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getDefaultImpl());
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertFalse(builder.isTypeIdVisible());
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig<?> config = new MapperConfig<>();
        JavaType baseType = JavaType.of(Integer.class);
        Collection<NamedType> subtypes = null;
        TypeIdResolver idRes = builder.idResolver(config, baseType, subtypes, true, false);
        assertNull(idRes);

        builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        idRes = builder.idResolver(config, baseType, subtypes, true, false);
        assertNotNull(idRes);
    }
}