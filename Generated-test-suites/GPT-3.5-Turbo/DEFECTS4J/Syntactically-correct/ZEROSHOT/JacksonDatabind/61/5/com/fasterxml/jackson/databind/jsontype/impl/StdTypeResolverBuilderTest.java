package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {
    
    private StdTypeResolverBuilder typeResolverBuilder;
    private JsonTypeInfo.Id idType;
    private JsonTypeInfo.As includeAs;
    private String typeProperty;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;
    private TypeIdResolver customIdResolver;
    
    @Before
    public void setUp() {
        typeResolverBuilder = new StdTypeResolverBuilder();
        idType = JsonTypeInfo.Id.DEFAULT;
        includeAs = JsonTypeInfo.As.PROPERTY;
        typeProperty = "type";
        typeIdVisible = false;
        defaultImpl = String.class;
        customIdResolver = null;
    }
    
    @Test
    public void testInit() {
        typeResolverBuilder.init(idType, customIdResolver);
        Assert.assertEquals(idType, typeResolverBuilder._idType);
        Assert.assertEquals(customIdResolver, typeResolverBuilder._customIdResolver);
        Assert.assertEquals(idType.getDefaultPropertyName(), typeResolverBuilder._typeProperty);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        typeResolverBuilder.init(null, customIdResolver);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        typeResolverBuilder._idType = JsonTypeInfo.Id.NONE;
        Assert.assertNull(typeResolverBuilder.buildTypeSerializer(null, null, null));
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        typeResolverBuilder._idType = JsonTypeInfo.Id.NONE;
        Assert.assertNull(typeResolverBuilder.buildTypeDeserializer(null, null, null));
    }
    
    @Test
    public void testInclusion() {
        typeResolverBuilder.inclusion(includeAs);
        Assert.assertEquals(includeAs, typeResolverBuilder._includeAs);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        typeResolverBuilder.inclusion(null);
    }
    
    @Test
    public void testTypeProperty() {
        typeResolverBuilder.typeProperty(typeProperty);
        Assert.assertEquals(typeProperty, typeResolverBuilder._typeProperty);
    }
    
    @Test
    public void testTypePropertyWithNullPropName() {
        typeResolverBuilder.typeProperty(null);
        Assert.assertEquals(idType.getDefaultPropertyName(), typeResolverBuilder._typeProperty);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTypePropertyWithEmptyPropName() {
        typeResolverBuilder.typeProperty("");
    }
    
    @Test
    public void testDefaultImpl() {
        typeResolverBuilder.defaultImpl(defaultImpl);
        Assert.assertEquals(defaultImpl, typeResolverBuilder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibility() {
        typeResolverBuilder.typeIdVisibility(typeIdVisible);
        Assert.assertEquals(typeIdVisible, typeResolverBuilder._typeIdVisible);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Assert.assertEquals(defaultImpl, typeResolverBuilder.getDefaultImpl());
    }
    
    @Test
    public void testGetTypeProperty() {
        Assert.assertEquals(typeProperty, typeResolverBuilder.getTypeProperty());
    }
    
    @Test
    public void testIsTypeIdVisible() {
        Assert.assertEquals(typeIdVisible, typeResolverBuilder.isTypeIdVisible());
    }
    
    @Test
    public void testIdResolver() {
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        boolean forSer = true;
        boolean forDeser = true;
        TypeIdResolver idResolver = typeResolverBuilder.idResolver(config, baseType, subtypes, forSer, forDeser);
        Assert.assertNull(idResolver);
    }
}