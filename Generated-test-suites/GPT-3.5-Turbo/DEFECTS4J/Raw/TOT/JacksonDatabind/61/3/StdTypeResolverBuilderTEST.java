import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.MINIMAL_CLASS, new ClassNameIdResolver());
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, resolverBuilder._idType);
        assertNotNull(resolverBuilder._customIdResolver);
        assertEquals("myType", resolverBuilder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(null, new ClassNameIdResolver());
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver());
        resolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        resolverBuilder.typeProperty("myType");
        TypeSerializer serializer = resolverBuilder.buildTypeSerializer(null, null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver());
        resolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        resolverBuilder.typeProperty("myType");
        TypeDeserializer deserializer = resolverBuilder.buildTypeDeserializer(null, null, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, resolverBuilder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("myTypeProperty");
        assertEquals("myTypeProperty", resolverBuilder._typeProperty);
    }

    @Test
    public void testTypePropertyWithEmptyString() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("");
        assertNotNull(resolverBuilder._typeProperty);
        assertEquals("myType", resolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.defaultImpl(String.class);
        assertEquals(String.class, resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        assertTrue(resolverBuilder._typeIdVisible);
        resolverBuilder.typeIdVisibility(false);
        assertFalse(resolverBuilder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        assertNull(resolverBuilder.getDefaultImpl());
    }
}