
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        
        // Check if idType is set correctly
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        
        // Check if customIdResolver is set correctly
        assertEquals(null, builder._customIdResolver);
        
        // Check if typeProperty is set correctly
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        
        SerializationConfig config = new SerializationConfig(StdTypeResolverBuilder.class, null, null);
        JavaType baseType = new JavaType(Integer.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        // Check if serializer is null for idType NONE
        assertEquals(null, serializer);
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        
        DeserializationConfig config = new DeserializationConfig(StdTypeResolverBuilder.class, null, null);
        JavaType baseType = new JavaType(Integer.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        // Check if deserializer is null for idType NONE
        assertEquals(null, deserializer);
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        
        // Check if includeAs is set correctly
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }
    
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        
        // Check if typeProperty is set correctly
        assertEquals("type", builder._typeProperty);
    }
    
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        
        // Check if defaultImpl is set correctly
        assertEquals(String.class, builder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        
        // Check if typeIdVisible is set correctly
        assertEquals(true, builder._typeIdVisible);
    }
}
