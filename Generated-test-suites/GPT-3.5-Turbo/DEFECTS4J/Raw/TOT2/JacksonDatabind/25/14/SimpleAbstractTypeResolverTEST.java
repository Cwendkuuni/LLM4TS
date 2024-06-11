import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {
  
  @Test
  public void testAddMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    
    // Test adding a valid mapping
    resolver.addMapping(Collection.class, LinkedList.class);
    
    // Test adding a mapping from a class to itself
    try {
      resolver.addMapping(Collection.class, Collection.class);
      fail("Should throw IllegalArgumentException for adding mapping from class to itself");
    } catch (IllegalArgumentException e) {
      // expected
    }
    
    // Test adding a mapping where the subtype is not a subclass of the supertype
    try {
      resolver.addMapping(Collection.class, ArrayList.class);
      fail("Should throw IllegalArgumentException for adding mapping where subtype is not a subclass of supertype");
    } catch (IllegalArgumentException e) {
      // expected
    }
    
    // Test adding a mapping for a non-abstract class
    try {
      resolver.addMapping(ArrayList.class, LinkedList.class);
      fail("Should throw IllegalArgumentException for adding mapping for a non-abstract class");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testFindTypeMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, LinkedList.class);
    
    // Test finding type mapping that exists
    JavaType mappedType = resolver.findTypeMapping(config, TypeFactory.defaultInstance().constructType(Collection.class));
    assertNotNull(mappedType);
    assertEquals(LinkedList.class, mappedType.getRawClass());
    
    // Test finding type mapping that does not exist
    JavaType nonExistentMappedType = resolver.findTypeMapping(config, TypeFactory.defaultInstance().constructType(Map.class));
    assertNull(nonExistentMappedType);
  }
  
  @Test
  public void testResolveAbstractType() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    JavaType abstractType = TypeFactory.defaultInstance().constructType(List.class);
    
    // Test resolving abstract type to concrete type
    JavaType resolvedType = resolver.resolveAbstractType(config, abstractType);
    assertNull(resolvedType);
  }
}