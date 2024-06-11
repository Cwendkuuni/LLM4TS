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
    public void testInit_NullIdType_ThrowsException() {
        try {
            resolverBuilder.init(null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("idType can not be null", e.getMessage());
        }
    }

    @Test
    public void testInit_ValidIdType_ReturnsInitializedBuilder() {
        StdTypeResolverBuilder builder = resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertSame(builder, resolverBuilder);
        assertEquals(JsonTypeInfo.Id.CLASS, resolverBuilder._idType);
        assertNull(resolverBuilder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), resolverBuilder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer_IdTypeNone_ReturnsNull() {
        resolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(resolverBuilder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer_IdTypeNone_ReturnsNull() {
        resolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(resolverBuilder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testInclusion_NullIncludeAs_ThrowsException() {
        try {
            resolverBuilder.inclusion(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("includeAs can not be null", e.getMessage());
        }
    }

    @Test
    public void testInclusion_ValidIncludeAs_ReturnsInitializedBuilder() {
        StdTypeResolverBuilder builder = resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertSame(builder, resolverBuilder);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, resolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty_NullTypeIdPropName_SetsDefaultTypeProperty() {
        StdTypeResolverBuilder builder = resolverBuilder.typeProperty(null);
        assertSame(builder, resolverBuilder);
        assertEquals(resolverBuilder._idType.getDefaultPropertyName(), resolverBuilder._typeProperty);
    }

    @Test
    public void testTypeProperty_EmptyTypeIdPropName_SetsDefaultTypeProperty() {
        StdTypeResolverBuilder builder = resolverBuilder.typeProperty("");
        assertSame(builder, resolverBuilder);
        assertEquals(resolverBuilder._idType.getDefaultPropertyName(), resolverBuilder._typeProperty);
    }

    @Test
    public void testTypeProperty_ValidTypeIdPropName_SetsTypeProperty() {
        String typeIdPropName = "typeId";
        StdTypeResolverBuilder builder = resolverBuilder.typeProperty(typeIdPropName);
        assertSame(builder, resolverBuilder);
        assertEquals(typeIdPropName, resolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl_SetsDefaultImpl() {
        Class<?> defaultImpl = String.class;
        StdTypeResolverBuilder builder = resolverBuilder.defaultImpl(defaultImpl);
        assertSame(builder, resolverBuilder);
        assertEquals(defaultImpl, resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility_SetsTypeIdVisibility() {
        boolean isVisible = true;
        StdTypeResolverBuilder builder = resolverBuilder.typeIdVisibility(isVisible);
        assertSame(builder, resolverBuilder);
        assertEquals(isVisible, resolverBuilder._typeIdVisible);
    }
}