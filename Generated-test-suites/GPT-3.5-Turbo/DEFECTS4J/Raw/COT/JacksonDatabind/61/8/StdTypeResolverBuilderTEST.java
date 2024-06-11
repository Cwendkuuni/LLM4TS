
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {
  
  @Test
  public void testInit() {
    StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
    resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
    assertEquals(JsonTypeInfo.Id.CLASS, resolverBuilder._idType);
    assertNull(resolverBuilder._customIdResolver);
    assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), resolverBuilder._typeProperty);
  }
  
  @Test
  public void testBuildTypeSerializer() {
    // TODO: Add test cases for buildTypeSerializer method
  }
  
  @Test
  public void testBuildTypeDeserializer() {
    // TODO: Add test cases for buildTypeDeserializer method
  }
  
  @Test
  public void testInclusion() {
    StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
    resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
    assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, resolverBuilder._includeAs);
  }
  
  @Test
  public void testTypeProperty() {
    StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
    resolverBuilder.typeProperty("type");
    assertEquals("type", resolverBuilder._typeProperty);
  }
  
  @Test
  public void testDefaultImpl() {
    StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
    resolverBuilder.defaultImpl(Object.class);
    assertEquals(Object.class, resolverBuilder._defaultImpl);
  }
  
  @Test
  public void testTypeIdVisibility() {
    StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
    resolverBuilder.typeIdVisibility(true);
    assertTrue(resolverBuilder._typeIdVisible);
  }
}
