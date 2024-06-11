import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdTypeResolverBuilderTest {
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
        
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }
    
    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
        
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder._includeAs);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }
    
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
        
        builder.typeProperty(null);
        assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Object.class);
        assertEquals(Object.class, builder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertEquals(true, builder._typeIdVisible);
        
        builder.typeIdVisibility(false);
        assertEquals(false, builder._typeIdVisible);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeProperty("type");
        
        // Test case when idType is NONE
        builder._idType = JsonTypeInfo.Id.NONE;
        assertNull(builder.buildTypeSerializer(null, null, null));
        
        // Test case when includeAs is WRAPPER_ARRAY
        builder._idType = JsonTypeInfo.Id.NAME;
        assertEquals(AsArrayTypeSerializer.class, builder.buildTypeSerializer(null, null, null).getClass());
        
        // Test case when includeAs is PROPERTY
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(AsPropertyTypeSerializer.class, builder.buildTypeSerializer(null, null, null).getClass());
        
        // Test case when includeAs is WRAPPER_OBJECT
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(AsWrapperTypeSerializer.class, builder.buildTypeSerializer(null, null, null).getClass());
        
        // Test case when includeAs is EXTERNAL_PROPERTY
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(AsExternalTypeSerializer.class, builder.buildTypeSerializer(null, null, null).getClass());
        
        // Test case when includeAs is EXISTING_PROPERTY
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertEquals(AsExistingPropertyTypeSerializer.class, builder.buildTypeSerializer(null, null, null).getClass());
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        
        // Test case when idType is NONE
        builder._idType = JsonTypeInfo.Id.NONE;
        assertNull(builder.buildTypeDeserializer(null, null, null));
        
        // Test case when includeAs is WRAPPER_ARRAY
        builder._idType = JsonTypeInfo.Id.NAME;
        assertEquals(AsArrayTypeDeserializer.class, builder.buildTypeDeserializer(null, null, null).getClass());
        
        // Test case when includeAs is PROPERTY
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(AsPropertyTypeDeserializer.class, builder.buildTypeDeserializer(null, null, null).getClass());
        
        // Test case when includeAs is WRAPPER_OBJECT
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(AsWrapperTypeDeserializer.class, builder.buildTypeDeserializer(null, null, null).getClass());
        
        // Test case when includeAs is EXTERNAL_PROPERTY
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(AsExternalTypeDeserializer.class, builder.buildTypeDeserializer(null, null, null).getClass());
        
        // Test case when includeAs is EXISTING_PROPERTY
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertEquals(AsPropertyTypeDeserializer.class, builder.buildTypeDeserializer(null, null, null).getClass());
    }
    
    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.constructType(String.class);
        
        // Test case when customIdResolver is not null
        TypeIdResolver customIdResolver = new ClassNameIdResolver(baseType, mapper.getTypeFactory());
        builder._customIdResolver = customIdResolver;
        assertEquals(customIdResolver, builder.idResolver(mapper.getDeserializationConfig(), baseType, null, true, false));
        
        builder._customIdResolver = null;
        
        // Test case when idType is CLASS
        builder._idType = JsonTypeInfo.Id.CLASS;
        assertEquals(ClassNameIdResolver.class, builder.idResolver(mapper.getDeserializationConfig(), baseType, null, true, false).getClass());
        
        // Test case when idType is MINIMAL_CLASS
        builder._idType = JsonTypeInfo.Id.MINIMAL_CLASS;
        assertEquals(MinimalClassNameIdResolver.class, builder.idResolver(mapper.getDeserializationConfig(), baseType, null, true, false).getClass());
        
        // Test case when idType is NAME
        builder._idType = JsonTypeInfo.Id.NAME;
        assertEquals(TypeNameIdResolver.class, builder.idResolver(mapper.getDeserializationConfig(), baseType, null, true, false).getClass());
        
        // Test case when idType is NONE
        builder._idType = JsonTypeInfo.Id.NONE;
        assertNull(builder.idResolver(mapper.getDeserializationConfig(), baseType, null, true, false));
        
        // Test case when idType is CUSTOM
        builder._idType = JsonTypeInfo.Id.CUSTOM;
        assertEquals(customIdResolver, builder.idResolver(mapper.getDeserializationConfig(), baseType, null, true, false));
    }
}