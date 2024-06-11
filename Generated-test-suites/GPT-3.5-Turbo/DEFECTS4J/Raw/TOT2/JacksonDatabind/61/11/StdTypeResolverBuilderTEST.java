import static org.junit.Assert.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNotNull(builder._customIdResolver);
        assertEquals("class", builder._typeProperty);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNotNull(builder);
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        SerializerProvider serializerProvider = new DefaultSerializerProvider.Impl();

        // Test WRAPPER_ARRAY
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertNotNull(builder.buildTypeSerializer(new DefaultSerializerProvider.Impl(), TypeFactory.defaultInstance().constructType(String.class), Collections.emptyList()));

        // Test PROPERTY
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertNotNull(builder.buildTypeSerializer(serializerProvider, TypeFactory.defaultInstance().constructType(String.class), Collections.emptyList()));

        // Test WRAPPER_OBJECT
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertNotNull(builder.buildTypeSerializer(serializerProvider, TypeFactory.defaultInstance().constructType(String.class), Collections.emptyList()));
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        DeserializerProvider deserializerProvider = new DefaultDeserializerProvider.Impl();

        // Test WRAPPER_ARRAY
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertNotNull(builder.buildTypeDeserializer(new DefaultDeserializationContext.Impl(), TypeFactory.defaultInstance().constructType(String.class), Collections.emptyList()));

        // Test PROPERTY
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertNotNull(builder.buildTypeDeserializer(deserializerProvider, TypeFactory.defaultInstance().constructType(String.class), Collections.emptyList()));

        // Test WRAPPER_OBJECT
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertNotNull(builder.buildTypeDeserializer(deserializerProvider, TypeFactory.defaultInstance().constructType(String.class), Collections.emptyList()));
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("customTypeProperty");
        assertEquals("customTypeProperty", builder._typeProperty);
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

}