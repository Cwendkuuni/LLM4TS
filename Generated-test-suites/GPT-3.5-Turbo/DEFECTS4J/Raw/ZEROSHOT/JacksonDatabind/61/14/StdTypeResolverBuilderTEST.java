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
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNotNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        builder.init(null, new ClassNameIdResolver(null, null));
    }

    @Test
    public void testBuildTypeSerializer() {
        assertNull(builder.buildTypeSerializer(null, null, null));
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        assertNotNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer() {
        assertNull(builder.buildTypeDeserializer(null, null, null));
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testInclusion() {
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNull() {
        builder.typeProperty(null);
        assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        builder.defaultImpl(Integer.class);
        assertEquals(Integer.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }
}