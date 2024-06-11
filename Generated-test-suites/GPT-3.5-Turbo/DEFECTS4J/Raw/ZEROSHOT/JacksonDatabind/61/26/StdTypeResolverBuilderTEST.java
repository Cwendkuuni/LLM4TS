import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {
    
    private StdTypeResolverBuilder builder;
    
    @Before
    public void setup() {
        builder = new StdTypeResolverBuilder();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdTypeThrowsException() {
        builder.init(null, null);
    }
    
    @Test
    public void testInitSetsIdTypeAndTypeProperty() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertEquals("class", builder._typeProperty);   
    }
    
    @Test
    public void testInitSetsDefaultImplNull() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder._defaultImpl);
    }
    
    @Test
    public void testNoTypeInfoBuilderReturnsNewStdTypeResolverBuilder() {
        StdTypeResolverBuilder result = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNotNull(result);
        assertTrue(result instanceof StdTypeResolverBuilder);
    }
    
    @Test
    public void testBuildTypeSerializerReturnsNullWhenIdTypeIsNone() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }
    
    @Test
    public void testBuildTypeSerializerReturnsAsArrayTypeSerializerWhenIncludeAsIsWrapperArray() {
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerReturnsAsPropertyTypeSerializerWhenIncludeAsIsProperty() {
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsPropertyTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializerReturnsAsWrapperTypeSerializerWhenIncludeAsIsWrapperObject() {
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsWrapperTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializerReturnsAsExternalTypeSerializerWhenIncludeAsIsExternalProperty() {
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsExternalTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializerReturnsAsExistingPropertyTypeSerializerWhenIncludeAsIsExistingProperty() {
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsExistingPropertyTypeSerializer);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerThrowsExceptionWhenIncludeAsIsUnknown() {
        builder.inclusion(null);
        builder.buildTypeSerializer(null, null, null);
    }
    
    @Test
    public void testBuildTypeDeserializerReturnsNullWhenIdTypeIsNone() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }
    
    @Test
    public void testBuildTypeDeserializerReturnsAsArrayTypeDeserializerWhenIncludeAsIsWrapperArray() {
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertTrue(builder.buildTypeDeserializer(null, null, null) instanceof AsArrayTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializerReturnsAsPropertyTypeDeserializerWhenIncludeAsIsProperty() {
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertTrue(builder.buildTypeDeserializer(null, null, null) instanceof AsPropertyTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializerReturnsAsWrapperTypeDeserializerWhenIncludeAsIsWrapperObject() {
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertTrue(builder.buildTypeDeserializer(null, null, null) instanceof AsWrapperTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializerReturnsAsExternalTypeDeserializerWhenIncludeAsIsExternalProperty() {
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertTrue(builder.buildTypeDeserializer(null, null, null) instanceof AsExternalTypeDeserializer);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerThrowsExceptionWhenIncludeAsIsUnknown() {
        builder.inclusion(null);
        builder.buildTypeDeserializer(null, null, null);
    }
    
    @Test
    public void testInclusionSetsIncludeAs() {
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder._includeAs);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAsThrowsException() {
        builder.inclusion(null);
    }
    
    @Test
    public void testTypePropertySetsTypeProperty() {
        builder.typeProperty("testProperty");
        assertEquals("testProperty", builder._typeProperty);
    }
    
    @Test
    public void testTypePropertySetsDefaultToIdTypeDefaultPropertyName() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty(null);
        assertEquals("class", builder._typeProperty);
    }
    
    @Test
    public void testDefaultImplSetsDefaultImpl() {
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibilitySetsTypeIdVisibleToTrue() {
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }
    
    @Test
    public void testTypeIdVisibilitySetsTypeIdVisibleToFalse() {
        builder.typeIdVisibility(false);
        assertFalse(builder._typeIdVisible);
    }
    
    @Test
    public void testGetDefaultImplReturnsDefaultImpl() {
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }
    
    @Test
    public void testGetTypePropertyReturnsTypeProperty() {
        builder.typeProperty("testProperty");
        assertEquals("testProperty", builder.getTypeProperty());
    }
    
    @Test
    public void testIsTypeIdVisibleReturnsTypeIdVisible() {
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
        
        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }
    
    @Test
    public void testIdResolverReturnsCustomIdResolverWhenCustomIdResolverIsNotNull() {
        TypeIdResolver customIdResolver = new ClassNameIdResolver(null, null);
        builder._customIdResolver = customIdResolver;
        assertSame(customIdResolver, builder.idResolver(null, null, null, false, false));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIdResolverThrowsExceptionWhenIdTypeIsNull() {
        builder.idResolver(null, null, null, false, false);
    }
    
    @Test
    public void testIdResolverReturnsClassNameIdResolverWhenIdTypeIsClass() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertTrue(builder.idResolver(null, null, null, false, false) instanceof ClassNameIdResolver);
    }
    
    @Test
    public void testIdResolverReturnsMinimalClassNameIdResolverWhenIdTypeIsMinimalClass() {
        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        assertTrue(builder.idResolver(null, null, null, false, false) instanceof MinimalClassNameIdResolver);
    }
    
    @Test
    public void testIdResolverReturnsTypeNameIdResolverWhenIdTypeIsName() {
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertTrue(builder.idResolver(null, null, null, false, false) instanceof TypeNameIdResolver);
    }
    
    @Test
    public void testIdResolverReturnsNullWhenIdTypeIsNone() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.idResolver(null, null, null, false, false));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIdResolverThrowsExceptionWhenIdTypeIsCustom() {
        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        builder.idResolver(null, null, null, false, false);
    }
}