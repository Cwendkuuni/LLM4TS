package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder stdTypeResolverBuilder;

    @Before
    public void setUp() {
        stdTypeResolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void init_shouldSetIdTypeAndCustomIdResolver() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, stdTypeResolverBuilder._idType);
        assertNull(stdTypeResolverBuilder._customIdResolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void init_withNullIdType_shouldThrowException() {
        stdTypeResolverBuilder.init(null, null);
    }

    @Test
    public void buildTypeSerializer_withNoneIdType_shouldReturnNull() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(stdTypeResolverBuilder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void buildTypeDeserializer_withNoneIdType_shouldReturnNull() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(stdTypeResolverBuilder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void inclusion_shouldSetIncludeAs() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, stdTypeResolverBuilder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void inclusion_withNullIncludeAs_shouldThrowException() {
        stdTypeResolverBuilder.inclusion(null);
    }

    @Test
    public void typeProperty_shouldSetTypeProperty() {
        stdTypeResolverBuilder.typeProperty("type");
        assertEquals("type", stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void typeProperty_withNull_shouldSetDefaultProperty() {
        stdTypeResolverBuilder.typeProperty(null);
        assertNotNull(stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void defaultImpl_shouldSetDefaultImpl() {
        stdTypeResolverBuilder.defaultImpl(StdTypeResolverBuilder.class);
        assertEquals(StdTypeResolverBuilder.class, stdTypeResolverBuilder._defaultImpl);
    }

    @Test
    public void typeIdVisibility_shouldSetTypeIdVisible() {
        stdTypeResolverBuilder.typeIdVisibility(true);
        assertTrue(stdTypeResolverBuilder._typeIdVisible);
    }
}