import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Test;
import java.util.Collection;
import java.util.LinkedList;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {
  
  @Test
  public void testAddMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, LinkedList.class);
    resolver.addMapping(List.class, LinkedList.class);
    
    // Assert mappings are correctly added
    assertMapping(resolver, Collection.class, LinkedList.class);
    assertMapping(resolver, List.class, LinkedList.class);
    
    // Assert mappings are not added for non-abstract type
    try {
      resolver.addMapping(LinkedList.class, LinkedList.class);
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
    
    // Assert mappings are not added for incorrect subtype
    try {
      resolver.addMapping(Collection.class, List.class);
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
    
    // Assert mappings are not added for class to itself
    try {
      resolver.addMapping(List.class, List.class);
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testFindTypeMapping() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    resolver.addMapping(Collection.class, LinkedList.class);
    resolver.addMapping(List.class, LinkedList.class);
    
    DeserializationConfig config = new DeserializationConfig();
    
    // Test mapping exists
    JavaType collectionType = config.getTypeFactory().constructType(Collection.class);
    JavaType resolvedCollectionType = resolver.findTypeMapping(config, collectionType);
    assertType(resolvedCollectionType, LinkedList.class);
    
    // Test mapping does not exist
    JavaType arrayListType = config.getTypeFactory().constructType(ArrayList.class);
    JavaType resolvedArrayListType = resolver.findTypeMapping(config, arrayListType);
    assertType(resolvedArrayListType, null);
  }
  
  @Test
  public void testResolveAbstractType() {
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    
    DeserializationConfig config = new DeserializationConfig();
    
    // Test always returns null
    JavaType collectionType = config.getTypeFactory().constructType(Collection.class);
    JavaType resolvedType = resolver.resolveAbstractType(config, collectionType);
    assertType(resolvedType, null);
  }
  
  private void assertMapping(SimpleAbstractTypeResolver resolver, Class<?> superType, Class<?> subType) {
    Class<?> resolvedType = resolver._mappings.get(new ClassKey(superType));
    assertType(resolvedType, subType);
  }
  
  private void assertType(JavaType actual, Class<?> expected) {
    if (expected == null) {
      assert(actual == null);
    } else {
      assert(actual.getRawClass() == expected);
    }
  }
}