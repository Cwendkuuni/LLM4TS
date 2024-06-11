import static org.junit.Assert.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        
        assertEquals(JsonTypeInfo.Id.CLASS, resolverBuilder._idType);
        assertNotNull(resolverBuilder._customIdResolver);
        assertEquals("class", resolverBuilder._typeProperty);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.NAME, new ClassNameIdResolver());
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        
        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(new SerializationConfig(), TypeFactory.defaultInstance().constructType(String.class), new ArrayList<>());
        
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        resolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        
        TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(new DeserializationConfig(), TypeFactory.defaultInstance().constructType(String.class), new ArrayList<>());
        
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);
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
        resolverBuilder.typeIdVisibility(true);
        
        assertTrue(resolverBuilder._typeIdVisible);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionNull() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.inclusion(null);
    }
    
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("type");
        
        assertEquals("type", resolverBuilder._typeProperty);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTypePropertyEmpty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("");
    }
    
    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.defaultImpl(Integer.class);
        
        assertEquals(Integer.class, resolverBuilder.getDefaultImpl());
    }
    
    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("type");
        
        assertEquals("type", resolverBuilder.getTypeProperty());
    }
    
    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeIdVisibility(true);
        
        assertTrue(resolverBuilder.isTypeIdVisible());
    }
}