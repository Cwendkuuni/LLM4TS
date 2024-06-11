
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNotNull(builder._customIdResolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, new ClassNameIdResolver());
    }

    @Test
    public void testBuildTypeSerializerWithNoTypeInfo() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerWithWrapperArray() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(serializer instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(serializer instanceof AsPropertyTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithWrapperObject() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(serializer instanceof AsWrapperTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(serializer instanceof AsExternalTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithExistingProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(serializer instanceof AsExistingPropertyTypeSerializer);
    }

    // Repeat the similar tests for buildTypeDeserializer...

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
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
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getDeserializationConfig();
        TypeIdResolver resolver = builder.idResolver(config, null, null, true, false);
        assertNotNull(resolver);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithoutCustomResolverAndNoIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getDeserializationConfig();
        builder.idResolver(config, null, null, true, false);
    }
    
}
