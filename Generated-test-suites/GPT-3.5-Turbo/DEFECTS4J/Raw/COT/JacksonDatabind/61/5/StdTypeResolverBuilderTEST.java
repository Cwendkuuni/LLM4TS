import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        JsonTypeInfo.Id expectedIdType = JsonTypeInfo.Id.CLASS;
        assertEquals(expectedIdType, typeResolverBuilder._idType);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder typeResolverBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
        JsonTypeInfo.Id expectedIdType = JsonTypeInfo.Id.NONE;
        assertEquals(expectedIdType, typeResolverBuilder._idType);
    }

    @Test
    public void testBuildTypeSerializer() {
        // To-do: Implement
    }

    @Test
    public void testBuildTypeDeserializer() {
        // To-do: Implement
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        JsonTypeInfo.As expectedIncludeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        assertEquals(expectedIncludeAs, typeResolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.typeProperty("typeId");
        String expectedTypeProperty = "typeId";
        assertEquals(expectedTypeProperty, typeResolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.defaultImpl(String.class);
        Class<?> expectedDefaultImpl = String.class;
        assertEquals(expectedDefaultImpl, typeResolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.typeIdVisibility(true);
        boolean expectedTypeIdVisible = true;
        assertEquals(expectedTypeIdVisible, typeResolverBuilder._typeIdVisible);
    }
}