import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder typeResolverBuilder;

    @Before
    public void setUp() {
        typeResolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, typeResolverBuilder._idType);
    }

    @Test
    public void testBuildTypeSerializer() {
        typeResolverBuilder.init(JsonTypeInfo.Id.NAME, null);
        assertNotNull(typeResolverBuilder.buildTypeSerializer(new SerializationConfig(), TypeFactory.defaultInstance().constructType(String.class), null));
    }

    @Test
    public void testInclusion() {
        typeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, typeResolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        typeResolverBuilder.typeProperty("myType");
        assertEquals("myType", typeResolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        typeResolverBuilder.defaultImpl(String.class);
        assertEquals(String.class, typeResolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        typeResolverBuilder.typeIdVisibility(true);
        assertEquals(true, typeResolverBuilder._typeIdVisible);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoTypeInfoBuilder() {
        typeResolverBuilder.noTypeInfoBuilder();
    }

}