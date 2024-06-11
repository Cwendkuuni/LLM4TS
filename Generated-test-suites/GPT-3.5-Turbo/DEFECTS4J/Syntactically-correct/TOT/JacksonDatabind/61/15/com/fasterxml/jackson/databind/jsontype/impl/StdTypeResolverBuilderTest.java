package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

@RunWith(Parameterized.class)
public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder resolverBuilder;
    private JsonTypeInfo.Id idType;
    private JsonTypeInfo.As includeAs;
    private String typeProperty;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;
    private TypeIdResolver customIdResolver;
    private MockConfig mockConfig;
    private JavaType baseType;
    private Collection<NamedType> subtypes;

    public StdTypeResolverBuilderTest(JsonTypeInfo.Id idType, JsonTypeInfo.As includeAs, String typeProperty,
            boolean typeIdVisible, Class<?> defaultImpl, TypeIdResolver customIdResolver) {
        this.idType = idType;
        this.includeAs = includeAs;
        this.typeProperty = typeProperty;
        this.typeIdVisible = typeIdVisible;
        this.defaultImpl = defaultImpl;
        this.customIdResolver = customIdResolver;
    }

    @Before
    public void setup() {
        mockConfig = new MockConfig();
        baseType = mockConfig.getTypeFactory().constructType(Object.class);
        subtypes = new ArrayList<>();
        resolverBuilder = new StdTypeResolverBuilder().init(idType, customIdResolver);
        resolverBuilder.inclusion(includeAs).typeProperty(typeProperty).defaultImpl(defaultImpl)
                .typeIdVisibility(typeIdVisible);
    }

    @Test
    public void testBuildTypeSerializer() {
        TypeResolverBuilder<StdTypeResolverBuilder> builder = resolverBuilder;
        TypeSerializer serializer = builder.buildTypeSerializer(mockConfig, baseType, subtypes);

        assertNotNull(serializer);

        // Additional test cases, specific to each inclusion type
        if (includeAs == JsonTypeInfo.As.WRAPPER_ARRAY) {
            assertTrue(serializer instanceof AsArrayTypeSerializer);
        } else if (includeAs == JsonTypeInfo.As.PROPERTY) {
            assertTrue(serializer instanceof AsPropertyTypeSerializer);
        } else if (includeAs == JsonTypeInfo.As.WRAPPER_OBJECT) {
            assertTrue(serializer instanceof AsWrapperTypeSerializer);
        } else if (includeAs == JsonTypeInfo.As.EXTERNAL_PROPERTY) {
            assertTrue(serializer instanceof AsExternalTypeSerializer);
        } else if (includeAs == JsonTypeInfo.As.EXISTING_PROPERTY) {
            assertTrue(serializer instanceof AsExistingPropertyTypeSerializer);
        }
    }

    @Test
    public void testBuildTypeDeserializer() {
        TypeResolverBuilder<StdTypeResolverBuilder> builder = resolverBuilder;
        TypeDeserializer deserializer = builder.buildTypeDeserializer(mockConfig, baseType, subtypes);

        assertNotNull(deserializer);

        // Additional test cases, specific to each inclusion type
        if (includeAs == JsonTypeInfo.As.WRAPPER_ARRAY) {
            assertTrue(deserializer instanceof AsArrayTypeDeserializer);
        } else if (includeAs == JsonTypeInfo.As.PROPERTY || includeAs == JsonTypeInfo.As.EXISTING_PROPERTY) {
            assertTrue(deserializer instanceof AsPropertyTypeDeserializer);
        } else if (includeAs == JsonTypeInfo.As.WRAPPER_OBJECT) {
            assertTrue(deserializer instanceof AsWrapperTypeDeserializer);
        } else if (includeAs == JsonTypeInfo.As.EXTERNAL_PROPERTY) {
            assertTrue(deserializer instanceof AsExternalTypeDeserializer);
        }
    }

    @Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> data = new ArrayList<>();

        // Test case 1: WRAPPER_ARRAY inclusion, default type property name, type id visible, custom id resolver
        data.add(new Object[] { JsonTypeInfo.Id.CLASS, JsonTypeInfo.As.WRAPPER_ARRAY, null, true, null, new CustomResolver() });

        // Test case 2: PROPERTY inclusion, specific type property name, type id not visible, default implementation class
        data.add(new Object[] { JsonTypeInfo.Id.CLASS, JsonTypeInfo.As.PROPERTY, "type", false, ArrayList.class, null });

        // Test case 3: WRAPPER_OBJECT inclusion, default type property name, type id visible, minimal class name id resolver
        data.add(new Object[] { JsonTypeInfo.Id.MINIMAL_CLASS, JsonTypeInfo.As.WRAPPER_OBJECT, null, true, null,
                new MinimalClassNameIdResolver() });

        return data;
    }
}

class MockConfig extends MapperConfig<MockConfig> {
    public MockConfig() {
        super(null, null, null, null, null, null, null, null);
    }
}

class CustomResolver implements TypeIdResolver {
    @Override public void init(JavaType baseType) { }
    @Override public String idFromValue(Object value) { return null; }
    @Override public String idFromValueAndType(Object value, Class<?> suggestedType) { return null; }
    @Override public JavaType typeFromId(String id) { return null; }
    @Override public String idFromBaseType() { return null; }
    @Override public String getDescForKnownTypeIds() { return null; }
    @Override public JsonTypeInfo.Id getMechanism() { return null; }
}