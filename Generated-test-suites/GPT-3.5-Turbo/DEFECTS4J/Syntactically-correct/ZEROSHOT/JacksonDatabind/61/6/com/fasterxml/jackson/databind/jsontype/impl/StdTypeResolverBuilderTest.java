package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testStdTypeResolverBuilder_noTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNotNull(builder);
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStdTypeResolverBuilder_initNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }
    
    @Test
    public void testStdTypeResolverBuilder_init() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test
    public void testStdTypeResolverBuilder_buildTypeSerializer() {
        // TODO: Write test cases for buildTypeSerializer()
    }
    
    @Test
    public void testStdTypeResolverBuilder_buildTypeDeserializer() {
        // TODO: Write test cases for buildTypeDeserializer()
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStdTypeResolverBuilder_inclusionNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }
    
    @Test
    public void testStdTypeResolverBuilder_inclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertNotNull(builder._includeAs);
    }
    
    @Test
    public void testStdTypeResolverBuilder_typePropertyNull() {
        // TODO: Write test cases for typeProperty() with null argument
    }
    
    @Test
    public void testStdTypeResolverBuilder_typePropertyEmpty() {
        // TODO: Write test cases for typeProperty() with empty argument
    }
    
    @Test
    public void testStdTypeResolverBuilder_typeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertNotNull(builder._typeProperty);
    }
    
    @Test
    public void testStdTypeResolverBuilder_defaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertNotNull(builder._defaultImpl);
    }
    
    @Test
    public void testStdTypeResolverBuilder_typeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }
    
    @Test(expected=IllegalStateException.class)
    public void testStdTypeResolverBuilder_noIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.buildTypeSerializer(null, null, null);
    }
}