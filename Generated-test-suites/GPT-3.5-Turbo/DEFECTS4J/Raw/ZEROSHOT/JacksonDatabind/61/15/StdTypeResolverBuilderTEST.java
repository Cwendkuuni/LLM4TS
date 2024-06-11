import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setUp() {
        builder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(builder._idType, JsonTypeInfo.Id.NAME);
        assertNull(builder._customIdResolver);
        assertEquals(builder._typeProperty, JsonTypeInfo.Id.NAME.getDefaultPropertyName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;

        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        TypeSerializer serializer = builder
                .buildTypeSerializer(null, null, null);
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;

        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithNoneIdType() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        TypeDeserializer deserializer = builder
                .buildTypeDeserializer(null, null, null);
        assertNull(deserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder result = builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(builder, result);
        assertEquals(builder._includeAs, JsonTypeInfo.As.WRAPPER_ARRAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder result = builder.typeProperty("testProperty");
        assertEquals(builder, result);
        assertEquals(builder._typeProperty, "testProperty");
    }

    @Test
    public void testTypePropertyWithDefault() {
        StdTypeResolverBuilder result = builder.typeProperty(null);
        assertEquals(builder, result);
        assertEquals(builder._typeProperty, JsonTypeInfo.Id.NONE.getDefaultPropertyName());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder result = builder.defaultImpl(String.class);
        assertEquals(builder, result);
        assertEquals(builder._defaultImpl, String.class);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder result = builder.typeIdVisibility(true);
        assertEquals(builder, result);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(builder.getDefaultImpl());
        builder.defaultImpl(Integer.class);
        assertEquals(builder.getDefaultImpl(), Integer.class);
    }

    @Test
    public void testGetTypeProperty() {
        assertNull(builder.getTypeProperty());
        builder.typeProperty("testProperty");
        assertEquals(builder.getTypeProperty(), "testProperty");
    }

    @Test
    public void testIsTypeIdVisible() {
        assertFalse(builder.isTypeIdVisible());
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;

        TypeIdResolver resolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertNull(resolver);

        builder.init(JsonTypeInfo.Id.NAME, null);
        resolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertNotNull(resolver);

        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        resolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertNotNull(resolver);
    }
}