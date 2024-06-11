package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import static com.fasterxml.jackson.databind. JaneObjectMapperFactory.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

/**
 * Test class for {@link StdTypeResolverBuilder}.
 */
public class StdTypeResolverBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = createObjectMapper();
    }

    @Test
    public void testConstructor() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder);
    }

    @Test
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(null, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("idType can not be null", e.getMessage());
        }
    }

    @Test
    public void testInitWithNullIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(JsonTypeInfo.Id.CLASS, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("idRes can not be null", e.getMessage());
        }
    }

    @Test
    public void testBuildTypeSerializerWithNullConfig() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(JsonTypeInfo.Id.CLASS, null);
            Object typeSerializer = builder.buildTypeSerializer(null, null, null);
            assertNull(typeSerializer);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBuildTypeSerializerWithValidConfig() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        ObjectMapper mapper = createObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        Collection<NamedType> namedTypes = Arrays.asList(new NamedType("TestType", Integer.class));
        Object typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), javaType, namedTypes);
        assertNotNull(typeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnknownIncludeType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.include(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        ObjectMapper mapper = createObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        Collection<NamedType> namedTypes = Arrays.asList(new NamedType("TestType", Integer.class));
        Object typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), javaType, namedTypes);
    }

    @Test
    public void testBuildTypeDeserializerWithNullConfig() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(JsonTypeInfo.Id.CLASS, null);
            Object typeDeserializer = builder.buildTypeDeserializer(null, null, null);
            assertNull(typeDeserializer);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBuildTypeDeserializerWithValidConfig() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        ObjectMapper mapper = createObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        Collection<NamedType> namedTypes = Arrays.asList(new NamedType("TestType", Integer.class));
        Object typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), javaType, namedTypes);
        assertNotNull(typeDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithUnknownIncludeType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.include(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        ObjectMapper mapper = createObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        Collection<NamedType> namedTypes = Arrays.asList(new NamedType("TestType", Integer.class));
        Object typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), javaType, namedTypes);
    }

    @Test
    public void testInclude() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
        builder.inclusion(includeAs);
        assertEquals(includeAs, builder.getIncludeAs());
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        String typeProperty = "testProperty";
        builder.typeProperty(typeProperty);
        assertEquals(typeProperty, builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Class<?> defaultImpl = Integer.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.defaultImpl(Integer.class);
        assertEquals(Integer.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty("testProperty");
        assertEquals("testProperty", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}