import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StdSubtypeResolverTest {
  
  private StdSubtypeResolver subtypeResolver;
  
  @Before
  public void setUp() {
    subtypeResolver = new StdSubtypeResolver();
  }
  
  @Test
  public void testRegisterSubtypes() {
    NamedType type1 = new NamedType(SubtypeClass1.class);
    NamedType type2 = new NamedType(SubtypeClass2.class);
    subtypeResolver.registerSubtypes(type1, type2);
    assertEquals(2, subtypeResolver._registeredSubtypes.size());
    assertTrue(subtypeResolver._registeredSubtypes.contains(type1));
    assertTrue(subtypeResolver._registeredSubtypes.contains(type2));
  }
  
  @Test
  public void testRegisterSubtypesWithClassisArray() {
    Class<?>[] types = {SubtypeClass1.class, SubtypeClass2.class};
    subtypeResolver.registerSubtypes(types);
    assertEquals(2, subtypeResolver._registeredSubtypes.size());
    assertTrue(subtypeResolver._registeredSubtypes.contains(new NamedType(SubtypeClass1.class)));
    assertTrue(subtypeResolver._registeredSubtypes.contains(new NamedType(SubtypeClass2.class)));
  }
  
  @Test
  public void testCollectAndResolveSubtypesByClassWithBaseTypeNull() {
    // Arrange
    MapperConfig config = null;
    AnnotatedMember property = null;
    JavaType baseType = null;
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
    
    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testCollectAndResolveSubtypesByClassWithRegisteredSubtypes() {
    // Arrange
    NamedType subtype1 = new NamedType(SubtypeClass1.class);
    NamedType subtype2 = new NamedType(SubtypeClass2.class);
    subtypeResolver.registerSubtypes(subtype1, subtype2);
    
    MapperConfig config = null;
    AnnotatedMember property = null;
    JavaType baseType = new JavaType(BaseClass.class);
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
    
    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains(subtype1));
    assertTrue(result.contains(subtype2));
  }
  
  @Test
  public void testCollectAndResolveSubtypesByClassWithAnnotatedSubtypes() {
    // Arrange
    NamedType subtype1 = new NamedType(SubtypeClass1.class);
    NamedType subtype2 = new NamedType(SubtypeClass2.class);
    
    MapperConfig config = null;
    AnnotatedMember property = null;
    JavaType baseType = new JavaType(BaseClass.class);
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
    
    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testCollectAndResolveSubtypesByClassWithAnnotatedBaseType() {
    // Arrange
    NamedType subtype1 = new NamedType(SubtypeClass1.class);
    NamedType subtype2 = new NamedType(SubtypeClass2.class);
    
    MapperConfig config = null;
    AnnotatedClass type = null;
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
    
    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testCollectAndResolveSubtypesByClassWithRegisteredAndAnnotatedSubtypes() {
    // Arrange
    NamedType subtype1 = new NamedType(SubtypeClass1.class);
    NamedType subtype2 = new NamedType(SubtypeClass2.class);
    subtypeResolver.registerSubtypes(subtype1, subtype2);
    
    MapperConfig config = null;
    AnnotatedClass type = null;
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
    
    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains(subtype1));
    assertTrue(result.contains(subtype2));
  }
  
  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithBaseTypeNull() {
    // Arrange
    MapperConfig config = null;
    AnnotatedMember property = null;
    JavaType baseType = null;
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
    
    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithRegisteredSubtypes() {
    // Arrange
    NamedType subtype1 = new NamedType(SubtypeClass1.class);
    NamedType subtype2 = new NamedType(SubtypeClass2.class);
    subtypeResolver.registerSubtypes(subtype1, subtype2);
    
    MapperConfig config = null;
    AnnotatedMember property = null;
    JavaType baseType = new JavaType(BaseClass.class);
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
    
    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains(subtype1));
    assertTrue(result.contains(subtype2));
  }
  
  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedSubtypes() {
    // Arrange
    NamedType subtype1 = new NamedType(SubtypeClass1.class);
    NamedType subtype2 = new NamedType(SubtypeClass2.class);
    
    MapperConfig config = null;
    AnnotatedMember property = null;
    JavaType baseType = new JavaType(BaseClass.class);
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
    
    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedBaseType() {
    // Arrange
    NamedType subtype1 = new NamedType(SubtypeClass1.class);
    NamedType subtype2 = new NamedType(SubtypeClass2.class);
    
    MapperConfig config = null;
    AnnotatedClass type = null;
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);
    
    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testCollectAndResolveSubtypesByTypeIdWithRegisteredAndAnnotatedSubtypes() {
    // Arrange
    NamedType subtype1 = new NamedType(SubtypeClass1.class);
    NamedType subtype2 = new NamedType(SubtypeClass2.class);
    subtypeResolver.registerSubtypes(subtype1, subtype2);
    
    MapperConfig config = null;
    AnnotatedClass type = null;
    
    // Act
    Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);
    
    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains(subtype1));
    assertTrue(result.contains(subtype2));
  }
  
  private static class BaseClass {}
  private static class SubtypeClass1 extends BaseClass {}
  private static class SubtypeClass2 extends BaseClass {}
}