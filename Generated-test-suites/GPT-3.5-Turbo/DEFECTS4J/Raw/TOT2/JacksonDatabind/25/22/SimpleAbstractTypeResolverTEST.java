import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
  
  @Test
  public void testAddMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, LinkedList.class);
    assertNotNull(resolver._mappings.get(Collection.class));
  }
  
  @Test
  public void testAddMappingSelf() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    try {
      resolver.addMapping(Collection.class, Collection.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testAddMappingNonSubType() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    try {
      resolver.addMapping(List.class, HashMap.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testAddMappingNonAbstractSuperType() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    try {
      resolver.addMapping(Map.class, LinkedHashMap.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testFindTypeMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, LinkedList.class);
    assertNotNull(resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Collection.class)));
  }
  
  @Test
  public void testFindTypeMappingNotFound() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
    assertNull(resolver.findTypeMapping(null, type));
  }

  @Test
  public void testResolveAbstractType() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    assertNull(resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(Collection.class)));
  }
  
  @Test
  public void testResolveAbstractTypeNullConfig() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    assertNull(resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(Collection.class)));
  }
}