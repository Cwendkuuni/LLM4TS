package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StdTypeResolverBuilderTest {
    
    private StdTypeResolverBuilder stdTypeResolverBuilder;
    private SerializationConfig serializationConfig;
    private DeserializationConfig deserializationConfig;
    private JavaType baseType;
    private Collection<NamedType> subtypes;
    private TypeIdResolver idResolver;
    
    @Before
    public void setup() {
        stdTypeResolverBuilder = new StdTypeResolverBuilder();
        serializationConfig = mock(SerializationConfig.class);
        deserializationConfig = mock(DeserializationConfig.class);
        baseType = mock(JavaType.class);
        subtypes = mock(Collection.class);
        idResolver = mock(TypeIdResolver.class);
        
        when(serializationConfig.getTypeFactory()).thenReturn(mock(ObjectMapper.class).getTypeFactory());
    }

    @Test
    public void testInit() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, idResolver);
        
        assertEquals(JsonTypeInfo.Id.CLASS, stdTypeResolverBuilder._idType);
        assertEquals(idResolver, stdTypeResolverBuilder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
        
        verify(serializationConfig, never()).getTypeFactory();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInit_NullIdType() {
        stdTypeResolverBuilder.init(null, idResolver);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        stdTypeResolverBuilder._idType = JsonTypeInfo.Id.CLASS;
        stdTypeResolverBuilder._includeAs = JsonTypeInfo.As.PROPERTY;
        stdTypeResolverBuilder._typeProperty = "type";
        stdTypeResolverBuilder._typeIdVisible = true;
        stdTypeResolverBuilder._defaultImpl = null;
        stdTypeResolverBuilder._customIdResolver = idResolver;
        
        TypeSerializer typeSerializer = stdTypeResolverBuilder.buildTypeSerializer(serializationConfig, baseType, subtypes);
        
        // assert the return type is AsPropertyTypeSerializer
        assertEquals(AsPropertyTypeSerializer.class, typeSerializer.getClass());
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        stdTypeResolverBuilder._idType = JsonTypeInfo.Id.CLASS;
        stdTypeResolverBuilder._includeAs = JsonTypeInfo.As.PROPERTY;
        stdTypeResolverBuilder._typeProperty = "type";
        stdTypeResolverBuilder._typeIdVisible = true;
        stdTypeResolverBuilder._defaultImpl = null;
        stdTypeResolverBuilder._customIdResolver = idResolver;
        
        TypeDeserializer typeDeserializer = stdTypeResolverBuilder.buildTypeDeserializer(deserializationConfig, baseType, subtypes);
        
        // assert the return type is AsPropertyTypeDeserializer
        assertEquals(AsPropertyTypeDeserializer.class, typeDeserializer.getClass());
    }
    
    @Test
    public void testInclusion() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        
        assertEquals(JsonTypeInfo.As.PROPERTY, stdTypeResolverBuilder._includeAs);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusion_NullIncludeAs() {
        stdTypeResolverBuilder.inclusion(null);
    }
    
    @Test
    public void testTypeProperty() {
        stdTypeResolverBuilder.typeProperty("customTypeProperty");
        
        assertEquals("customTypeProperty", stdTypeResolverBuilder._typeProperty);
    }
    
    @Test
    public void testTypeProperty_NullTypeProperty() {
        stdTypeResolverBuilder.typeProperty(null);
        
        assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
    }
    
    @Test
    public void testDefaultImpl() {
        stdTypeResolverBuilder.defaultImpl(String.class);
        
        assertEquals(String.class, stdTypeResolverBuilder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibility() {
        stdTypeResolverBuilder.typeIdVisibility(true);
        
        assertEquals(true, stdTypeResolverBuilder._typeIdVisible);
    }
    
    @Test
    public void testGetTypeProperty() {
        stdTypeResolverBuilder._typeProperty = "type";
        
        assertEquals("type", stdTypeResolverBuilder.getTypeProperty());
    }
    
    @Test
    public void testIsTypeIdVisible() {
        stdTypeResolverBuilder._typeIdVisible = true;
        
        assertEquals(true, stdTypeResolverBuilder.isTypeIdVisible());
    }
        
    @Test
    public void testIdResolver_IdTypeClass() {
        stdTypeResolverBuilder._idType = JsonTypeInfo.Id.CLASS;
        stdTypeResolverBuilder._customIdResolver = null;
        
        TypeIdResolver resolver = stdTypeResolverBuilder.idResolver(serializationConfig, baseType, subtypes, true, false);
        
        // assert the return type is ClassNameIdResolver
        assertEquals(ClassNameIdResolver.class, resolver.getClass());
    }
    
    @Test
    public void testIdResolver_IdTypeMinimalClass() {
        stdTypeResolverBuilder._idType = JsonTypeInfo.Id.MINIMAL_CLASS;
        stdTypeResolverBuilder._customIdResolver = null;
        
        TypeIdResolver resolver = stdTypeResolverBuilder.idResolver(serializationConfig, baseType, subtypes, true, false);
        
        // assert the return type is MinimalClassNameIdResolver
        assertEquals(MinimalClassNameIdResolver.class, resolver.getClass());
    }
    
    @Test
    public void testIdResolver_IdTypeName() {
        stdTypeResolverBuilder._idType = JsonTypeInfo.Id.NAME;
        stdTypeResolverBuilder._customIdResolver = null;
        
        TypeIdResolver resolver = stdTypeResolverBuilder.idResolver(serializationConfig, baseType, subtypes, true, false);
        
        // assert the return type is TypeNameIdResolver
        assertEquals(TypeNameIdResolver.class, resolver.getClass());
    }
    
    @Test
    public void testIdResolver_IdTypeNone() {
        stdTypeResolverBuilder._idType = JsonTypeInfo.Id.NONE;
        stdTypeResolverBuilder._customIdResolver = null;
        
        TypeIdResolver resolver = stdTypeResolverBuilder.idResolver(serializationConfig, baseType, subtypes, true, false);
        
        // assert the return type is null
        assertNull(resolver);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIdResolver_UnknownIdType() {
        stdTypeResolverBuilder._idType = JsonTypeInfo.Id.CUSTOM;
        stdTypeResolverBuilder._customIdResolver = null;
        
        stdTypeResolverBuilder.idResolver(serializationConfig, baseType, subtypes, true, false);
    }
}