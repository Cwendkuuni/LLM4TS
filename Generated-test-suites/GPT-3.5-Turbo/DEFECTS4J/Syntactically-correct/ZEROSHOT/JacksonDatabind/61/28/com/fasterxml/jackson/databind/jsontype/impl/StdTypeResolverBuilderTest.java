package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), builder._typeProperty);
    }

    //Add more tests here

}