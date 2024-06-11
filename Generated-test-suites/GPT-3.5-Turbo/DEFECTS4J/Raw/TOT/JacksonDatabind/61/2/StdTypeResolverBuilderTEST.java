import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setup() {
        builder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Assert.assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        Assert.assertNull(builder._customIdResolver);
        Assert.assertEquals("class", builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializerWithNoInfo() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        JavaType baseType = config.constructType(String.class);
        Collection<NamedType> subtypes = new ArrayList<NamedType>();
        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        Assert.assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithWrapperArray() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeProperty("type");
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        JavaType baseType = config.constructType(String.class);
        Collection<NamedType> subtypes = new ArrayList<NamedType>();
        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        Assert.assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithInvalidIncludeAs() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        builder.typeProperty("type");
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        JavaType baseType = config.constructType(String.class);
        Collection<NamedType> subtypes = new ArrayList<NamedType>();
        builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializerWithNoInfo() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = config.constructType(String.class);
        Collection<NamedType> subtypes = new ArrayList<NamedType>();
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        Assert.assertNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithWrapperObject() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        builder.typeProperty("type");
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = config.constructType(String.class);
        Collection<NamedType> subtypes = new ArrayList<NamedType>();
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        Assert.assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);
    }

    @Test(expected = IllegalAccessException.class)
    public void testBuildTypeDeserializerWithInvalidIdResolver() {
        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = config.constructType(String.class);
        Collection<NamedType> subtypes = new ArrayList<NamedType>();
        builder.buildTypeDeserializer(config, baseType, subtypes);
    }

    @Test
    public void testInclusion() {
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        Assert.assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        builder.typeProperty("customType");
        Assert.assertEquals("customType", builder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNull() {
        builder.typeProperty(null);
        Assert.assertEquals("class", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        builder.defaultImpl(String.class);
        Assert.assertEquals(String.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        builder.typeIdVisibility(true);
        Assert.assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        builder.defaultImpl(String.class);
        Assert.assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        builder.typeProperty("customType");
        Assert.assertEquals("customType", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        builder.typeIdVisibility(true);
        Assert.assertTrue(builder.isTypeIdVisible());
    }
}