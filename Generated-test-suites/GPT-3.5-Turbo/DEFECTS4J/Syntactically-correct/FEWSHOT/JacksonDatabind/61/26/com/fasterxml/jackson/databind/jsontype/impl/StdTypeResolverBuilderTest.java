import static org.junit.Assert.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(JavaType.class, null));
        assertEquals(builder, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, new ClassNameIdResolver(JavaType.class, null));
    }
    
    @Test
    public void testInitWithDefaultPropertyName() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(JavaType.class, null))
                                            .typeProperty("type");
        assertEquals(builder, result);
    }
    
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeProperty("type");
        assertEquals(builder, result);
    }
    
    @Test
    public void testTypePropertyWithNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeProperty(null);
        assertEquals(builder, result);
    }
    
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.defaultImpl(JavaType.class);
        assertEquals(builder, result);
    }
    
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeIdVisibility(true);
        assertEquals(builder, result);
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(builder, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeSerializer result = builder.buildTypeSerializer(new SerializationConfig(), JavaType.class, null);
        assertNull(result);
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeDeserializer result = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.class, null);
        assertNull(result);
    }
    
    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder result = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNotNull(result);
    }
}