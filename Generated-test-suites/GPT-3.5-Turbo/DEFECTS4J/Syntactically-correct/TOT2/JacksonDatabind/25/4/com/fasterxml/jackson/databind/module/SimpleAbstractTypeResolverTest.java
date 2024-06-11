import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

  @Test
  public void testAddMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    
    try {
      resolver.addMapping(Collection.class, LinkedList.class);
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not be thrown");
    }
    
    try {
      resolver.addMapping(List.class, LinkedList.class);
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not be thrown");
    }
  }

  @Test
  public void testFindTypeMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, LinkedList.class);
    
    JavaType result = resolver.findTypeMapping(new DeserializationConfig(), TypeFactory.defaultInstance().constructType(Collection.class));
    assertNotNull(result);
    assertEquals(LinkedList.class, result.getRawClass());
  }

  @Test
  public void testResolveAbstractType() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    JavaType result = resolver.resolveAbstractType(new DeserializationConfig(), TypeFactory.defaultInstance().constructType(Collection.class));
    assertNull(result);
  }
  
  @Test
  public void testAddMappingInvalidMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    
    try {
      resolver.addMapping(Collection.class, Collection.class);
      fail("IllegalArgumentException should be thrown for adding mapping from class to itself");
    } catch (IllegalArgumentException e) {
      // expected exception
    }
    
    try {
      resolver.addMapping(ArrayList.class, LinkedList.class);
      fail("IllegalArgumentException should be thrown for adding mapping from non-superclass");
    } catch (IllegalArgumentException e) {
      // expected exception
    }
    
    try {
      resolver.addMapping(LinkedList.class, LinkedList.class);
      fail("IllegalArgumentException should be thrown for adding mapping from non-abstract superclass");
    } catch (IllegalArgumentException e) {
      // expected exception
    }
  }
}