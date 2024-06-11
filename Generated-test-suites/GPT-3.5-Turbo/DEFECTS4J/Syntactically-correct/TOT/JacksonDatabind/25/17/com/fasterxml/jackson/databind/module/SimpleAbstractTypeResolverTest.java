import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

  @Test
  public void addMapping_ShouldThrowException_WhenSuperTypeIsSameAsSubType() {
    try {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      resolver.addMapping(Collection.class, Collection.class);
      fail("Expected IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not add mapping from class to itself", e.getMessage());
    }
  }

  @Test
  public void addMapping_ShouldThrowException_WhenSubTypeIsNotSubtypeOfSuperType() {
    try {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      resolver.addMapping(Collection.class, HashSet.class);
      fail("Expected IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not add mapping from class java.util.Collection to java.util.HashSet, as latter is not a subtype of former", e.getMessage());
    }
  }

  @Test
  public void addMapping_ShouldThrowException_WhenSuperTypeIsNotAbstract() {
    try {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      resolver.addMapping(List.class, LinkedList.class);
      fail("Expected IllegalArgumentException was not thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not add mapping from class java.util.List since it is not abstract", e.getMessage());
    }
  }

  @Test
  public void addMapping_ShouldAddMapping_WhenSuperTypeAndSubTypeAreValid() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Set.class, HashSet.class);
    assertEquals(1, resolver.getMappings().size());
    assertTrue(resolver.getMappings().containsKey(new ClassKey(Set.class)));
    assertEquals(HashSet.class, resolver.getMappings().get(new ClassKey(Set.class)));
  }

  @Test
  public void findTypeMapping_ShouldReturnNull_WhenMappingDoesNotExist() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    assertNull(resolver.findTypeMapping(null, null));
  }

  @Test
  public void findTypeMapping_ShouldReturnDestinationType_WhenMappingExists() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, Set.class);
    DeserializationConfig config = null;
    JavaType type = new JavaType();
    type.setRawClass(Collection.class);
    JavaType result = resolver.findTypeMapping(config, type);
    assertNotNull(result);
    assertEquals(Set.class, result.getRawClass());
  }

  @Test
  public void resolveAbstractType_ShouldReturnNull_WhenCalled() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    DeserializationConfig config = null;
    JavaType type = new JavaType();
    assertNull(resolver.resolveAbstractType(config, type));
  }

}