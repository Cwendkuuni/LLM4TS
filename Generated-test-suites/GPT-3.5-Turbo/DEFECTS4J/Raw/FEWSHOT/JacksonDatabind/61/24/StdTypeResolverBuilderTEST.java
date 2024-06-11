package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

public class StdTypeResolverBuilderTest {
    
    private StdTypeResolverBuilder builder;
    
    @Before
    public void setup() {
        builder = new StdTypeResolverBuilder();
    }
    
    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, noTypeInfoBuilder._idType);
        assertNull(noTypeInfoBuilder._typeProperty);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        builder.init(null, null);
    }
    
    @Test
    public void testInit() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
    }
    
    @Test
    public void testBuildTypeSerializerWithNoTypeInfo() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }
    
    @Test
    public void testBuildTypeSerializerWithWrapperArrayType() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertNotNull(builder.buildTypeSerializer(null, null, null));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnknownIncludeAs() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(null);
        builder.buildTypeSerializer(null, null, null);
    }
    
    @Test
    public void testBuildTypeDeserializerWithNoTypeInfo() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }
    
    @Test
    public void testBuildTypeDeserializerWithWrapperArrayType() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithUnknownIncludeAs() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(null);
        builder.buildTypeDeserializer(null, null, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        builder.inclusion(null);
    }
    
    @Test
    public void testTypePropertyWithNull() {
        builder.typeProperty(null);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test
    public void testTypePropertyWithEmpty() {
        builder.typeProperty("");
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test
    public void testTypeProperty() {
        String typeProperty = "type";
        builder.typeProperty(typeProperty);
        assertEquals(typeProperty, builder._typeProperty);
    }
    
    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;
        builder.typeIdVisibility(isVisible);
        assertEquals(isVisible, builder._typeIdVisible);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = String.class;
        builder._defaultImpl = defaultImpl;
        assertEquals(defaultImpl, builder.getDefaultImpl());
    }
    
    @Test
    public void testGetTypeProperty() {
        String typeProperty = "type";
        builder._typeProperty = typeProperty;
        assertEquals(typeProperty, builder.getTypeProperty());
    }
    
    @Test
    public void testIsTypeIdVisible() {
        boolean isVisible = true;
        builder._typeIdVisible = isVisible;
        assertEquals(isVisible, builder.isTypeIdVisible());
    }
    
    @Test
    public void testIdResolverWithCustomIdResolver() {
        TypeIdResolver customIdResolver = new CustomIdResolver();
        builder._customIdResolver = customIdResolver;
        assertEquals(customIdResolver, builder.idResolver(null, null, null, false, false));
    }
    
    @Test
    public void testIdResolverWithDefaultIdTypeAndClassName() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        TypeIdResolver idResolver = builder.idResolver(null, null, null, false, false);
        assertTrue(idResolver instanceof ClassNameIdResolver);
    }
    
    @Test
    public void testIdResolverWithDefaultIdTypeAndMinimalClassName() {
        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        TypeIdResolver idResolver = builder.idResolver(null, null, null, false, false);
        assertTrue(idResolver instanceof MinimalClassNameIdResolver);
    }
    
    @Test
    public void testIdResolverWithDefaultIdTypeAndTypeName() {
        builder.init(JsonTypeInfo.Id.NAME, null);
        TypeIdResolver idResolver = builder.idResolver(null, null, null, false, false);
        assertTrue(idResolver instanceof TypeNameIdResolver);
    }
    
    @Test
    public void testIdResolverWithDefaultIdTypeAndNone() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        TypeIdResolver idResolver = builder.idResolver(null, null, null, false, false);
        assertNull(idResolver);
    }
    
    private static class CustomIdResolver implements TypeIdResolver {

        @Override
        public void init(JavaType baseType) {
        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public String idFromBaseType() {
            return null;
        }

        @Override
        public JavaType typeFromId(String id) {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
        
    }
    
}