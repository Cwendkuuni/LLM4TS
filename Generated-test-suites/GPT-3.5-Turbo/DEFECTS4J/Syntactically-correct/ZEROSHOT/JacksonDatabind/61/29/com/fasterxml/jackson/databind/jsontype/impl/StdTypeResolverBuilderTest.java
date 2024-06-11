import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder resolverBuilder;

    @Before
    public void setUp() {
        resolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertSame(JsonTypeInfo.Id.CLASS, resolverBuilder._idType);
        assertNull(resolverBuilder._customIdResolver);
        assertEquals("class", resolverBuilder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        resolverBuilder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        assertNull(resolverBuilder.buildTypeSerializer(null, null, null));
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(resolverBuilder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer() {
        assertNull(resolverBuilder.buildTypeDeserializer(null, null, null));
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(resolverBuilder.buildTypeDeserializer(null, null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        resolverBuilder.inclusion(null);
    }

    @Test
    public void testInclusion() {
        resolverBuilder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertSame(JsonTypeInfo.As.EXTERNAL_PROPERTY, resolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        resolverBuilder.typeProperty("type");
        assertEquals("type", resolverBuilder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNull() {
        resolverBuilder.typeProperty(null);
        assertEquals("class", resolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        assertNull(resolverBuilder._defaultImpl);
        resolverBuilder.defaultImpl(Integer.class);
        assertSame(Integer.class, resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        assertFalse(resolverBuilder._typeIdVisible);
        resolverBuilder.typeIdVisibility(true);
        assertTrue(resolverBuilder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(resolverBuilder.getDefaultImpl());
        resolverBuilder.defaultImpl(Integer.class);
        assertSame(Integer.class, resolverBuilder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        assertNull(resolverBuilder.getTypeProperty());
        resolverBuilder.typeProperty("type");
        assertEquals("type", resolverBuilder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        assertFalse(resolverBuilder.isTypeIdVisible());
        resolverBuilder.typeIdVisibility(true);
        assertTrue(resolverBuilder.isTypeIdVisible());
    }
}