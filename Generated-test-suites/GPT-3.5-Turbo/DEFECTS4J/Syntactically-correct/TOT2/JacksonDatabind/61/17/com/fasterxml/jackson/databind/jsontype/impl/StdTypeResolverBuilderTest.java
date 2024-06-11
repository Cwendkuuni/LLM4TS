import static org.junit.Assert.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    // Test cases for method init(JsonTypeInfo.Id idType, TypeIdResolver idRes)

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
    }

    // Test cases for method buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes)

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver());
        
        assertNotNull(builder.buildTypeSerializer(new SerializationConfig(), null, null));
    }

    // Test cases for method buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes)

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        
        assertNotNull(builder.buildTypeDeserializer(new DeserializationConfig(), null, null));
    }

    // Test cases for method inclusion(JsonTypeInfo.As includeAs)

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }

    // Test cases for method typeProperty(String typeIdPropName)

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        
        assertEquals("type", builder._typeProperty);
    }

    // Test cases for method defaultImpl(Class<?> defaultImpl)

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        
        assertEquals(String.class, builder._defaultImpl);
    }

    // Test cases for method typeIdVisibility(boolean isVisible)

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        
        assertTrue(builder._typeIdVisible);
    }
}