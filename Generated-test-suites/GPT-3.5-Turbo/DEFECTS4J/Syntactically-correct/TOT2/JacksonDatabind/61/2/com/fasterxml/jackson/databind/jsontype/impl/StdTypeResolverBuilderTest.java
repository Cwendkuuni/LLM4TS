import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(config.getTypeFactory()));

        assertEquals(JsonTypeInfo.Id.CLASS, resolverBuilder._idType);
        assertNotNull(resolverBuilder._customIdResolver);
        assertEquals("className", resolverBuilder._typeProperty);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder resolverBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();

        assertEquals(JsonTypeInfo.Id.NONE, resolverBuilder._idType);
        assertNull(resolverBuilder._customIdResolver);
        assertEquals("none", resolverBuilder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(config.getTypeFactory()));
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        resolverBuilder.typeProperty("type");

        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(config.getTypeFactory()));
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        resolverBuilder.typeProperty("type");
        resolverBuilder.defaultImpl(Void.class);

        TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);

        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, resolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("type");

        assertEquals("type", resolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.defaultImpl(Void.class);

        assertEquals(Void.class, resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeIdVisibility(true);

        assertTrue(resolverBuilder._typeIdVisible);
    }
    
}