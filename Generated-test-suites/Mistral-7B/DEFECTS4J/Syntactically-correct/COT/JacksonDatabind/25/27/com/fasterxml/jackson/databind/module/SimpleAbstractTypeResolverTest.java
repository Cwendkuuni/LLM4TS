import org.junit.Test;
import org.junit. Assert.*;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

import static com.fasterxml.jackson.databind.JavaType.constructType;

 public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testAddMapping() {
 Class<?> expectedCollectionType = LinkedList.class;
 Class<?> actualCollectionType = resolver.addMapping(Collection.class, ArrayList.class)._mappings.get(new ClassKey(Collection.class)).getClass();
 Assert.assertEquals(expectedCollectionType, actualCollectionType);
 }

 @Test
 public void testFindTypeMappingWithCollectionType() {
 JavaType javaType = constructType(List.class);
 Class<?> expectedType = LinkedList.class;
 JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), javaType);
 Assert.assertEquals(expectedType, actualType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithNonCollectionType() {
 JavaType javaType = constructType(String.class);
 JavaType actualType = resolver.findTypeMapping(new DeserializationConfig(new DeserializationConfig.Builder()), javaType);
 Assert.assertNull(actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidArguments() {
 resolver.addMapping(String.class, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidSuperSubTypeRelationship() {
 resolver.addMapping(String.class, LinkedList.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperType() {
 resolver.addMapping(ArrayList.class, LinkedList.class);
 }

 @Test
 public void testResolveAbstractType() {
 JavaType javaType = constructType(AbstractClass.class);
 JavaType actualType = resolver.resolveAbstractType(new DeserializationConfig(new DeserializationConfig.Builder()), javaType);
 Assert.assertNull(actualType);
 }
 }

 class AbstractClass {
 }