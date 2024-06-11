import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

   @Test
   public void testAddMapping() {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      Class<Collection> superType = Collection.class;
      Class<LinkedList> subType = LinkedList.class;
      resolver.addMapping(superType, subType);

      assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMappingSameClass() {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      Class<Collection> superType = Collection.class;
      resolver.addMapping(superType, superType);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMappingSubtypeMismatch() {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      Class<Collection> superType = Collection.class;
      Class<LinkedList> subType = LinkedList.class;
      resolver.addMapping(superType, subType);
      resolver.addMapping(subType, superType);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMappingNotAbstract() {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      Class<LinkedList> superType = LinkedList.class;
      Class<ArrayList> subType = ArrayList.class;
      resolver.addMapping(superType, subType);
   }
  
   @Test
   public void testFindTypeMapping() {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      resolver.addMapping(Collection.class, LinkedList.class);
      JavaType type = new JavaType(Collection.class);
      DeserializationConfig config = new DeserializationConfig();
      JavaType resolvedType = resolver.findTypeMapping(config, type);
      assertEquals(LinkedList.class, resolvedType.getRawClass());
   }

   @Test
   public void testFindTypeMappingNoMapping() {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      JavaType type = new JavaType(List.class);
      DeserializationConfig config = new DeserializationConfig();
      JavaType resolvedType = resolver.findTypeMapping(config, type);
      assertNull(resolvedType);
   }

   @Test
   public void testResolveAbstractType() {
      SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
      JavaType type = new JavaType(Collection.class);
      DeserializationConfig config = new DeserializationConfig();
      JavaType resolvedType = resolver.findTypeMapping(config, type);
      assertNull(resolvedType);
   }
}