import org.junit.*;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

   @Test
   public void testAddMapping() {
       SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
       resolver.addMapping(ArrayList.class, LinkedList.class);
       resolver.addMapping(Set.class, HashSet.class);
       
       // Test case 1: Mapping from super type to specific subtype is added successfully
       assertTrue(resolver._mappings.containsKey(new ClassKey(ArrayList.class)));
       assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(ArrayList.class)));
       
       // Test case 2: Mapping from super type to specific subtype is added successfully
       assertTrue(resolver._mappings.containsKey(new ClassKey(Set.class)));
       assertEquals(HashSet.class, resolver._mappings.get(new ClassKey(Set.class)));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMapping_sameClass() {
       SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
       resolver.addMapping(ArrayList.class, ArrayList.class);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testAddMapping_subTypeNotCompatible() {
       SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
       resolver.addMapping(Collection.class, LinkedList.class);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testAddMapping_superTypeNotAbstract() {
       SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
       resolver.addMapping(ArrayList.class, LinkedList.class);
   }

   @Test
   public void testFindTypeMapping() {
       SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
       resolver.addMapping(List.class, ArrayList.class);
       
       DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
       JavaType type = Mockito.mock(JavaType.class);
       
       // Test case 1: Mapping exists for the supplied raw class
       Mockito.when(type.getRawClass()).thenReturn(List.class);
       JavaType resultType = resolver.findTypeMapping(config, type);
       assertEquals(ArrayList.class, resultType.getRawClass());
       
       // Test case 2: Mapping does not exist for the supplied raw class
       Mockito.when(type.getRawClass()).thenReturn(String.class);
       JavaType resultType2 = resolver.findTypeMapping(config, type);
       assertNull(resultType2);
   }
   
   @Test
   public void testResolveAbstractType() {
       SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
       
       DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
       JavaType type = Mockito.mock(JavaType.class);
       
       // Test case 1: Always returns null
       JavaType resultType = resolver.resolveAbstractType(config, type);
       assertNull(resultType);
   }

}