
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.SerializationFeature;

public class StdTypeResolverBuilderTest {
    private StdTypeResolverBuilder resolverBuilder;
    
    @Before
    public void setUp() {
        resolverBuilder = new StdTypeResolverBuilder();
    }
  
    @Test
    public void testInit() {
        TypeIdResolver mockedIdResolver = mock(TypeIdResolver.class); 
        StdTypeResolverBuilder result = resolverBuilder.init(JsonTypeInfo.Id.DEDUCTION, mockedIdResolver);
      
        assertEquals(JsonTypeInfo.Id.DEDUCTION, result._idType);
        assertEquals(mockedIdResolver, result._customIdResolver);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        resolverBuilder.init(null, mock(TypeIdResolver.class));
    }
    
    @Test
    public void testBuildTypeSerializerForNoneIdType() {
        resolverBuilder._idType = JsonTypeInfo.Id.NONE;
        SerializationConfig mockedConfig = mock(SerializationConfig.class);
        JavaType mockedBaseType = mock(JavaType.class);
        Collection<NamedType> mockedSubtypes = mock(Collection.class);
        
        assertNull(resolverBuilder.buildTypeSerializer(mockedConfig, mockedBaseType, mockedSubtypes));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerForUnknownIncludeAs() {
        resolverBuilder._idType = JsonTypeInfo.Id.CLASS;
        resolverBuilder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;
        SerializationConfig mockedConfig = mock(SerializationConfig.class);
        JavaType mockedBaseType = mock(JavaType.class);
        Collection<NamedType> mockedSubtypes = mock(Collection.class);
        
        resolverBuilder.buildTypeSerializer(mockedConfig, mockedBaseType, mockedSubtypes);
    }
    
    // Other test methods for all other methods in StdTypeResolverBuilder class
    // ...
}
