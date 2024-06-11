import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.HashMap;
import java.util.Map;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)}
 */
 @Test
 public void testAddMapping() {
 Class<Collection> collectionType = Collection.class;
 Class<LinkedList> linkedListType = LinkedList.class;
 resolver.addMapping(collectionType, linkedListType);
 JavaType javaType = new ClassType(Collection.class);
 Class<?> resolvedType = resolver.findTypeMapping(new DeserializationConfig(), javaType).getRawClass();
 assertEquals(LinkedList.class, resolvedType);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with superclass and subclass relationship
 */
 @Test
 public void testAddMappingWithSuperclassSubclassRelationship() {
 Class<LinkedList> linkedListType = LinkedList.class;
 Class<ArrayList> arrayListType = ArrayList.class;
 resolver.addMapping(LinkedList.class, arrayListType);
 JavaType javaType = new ClassType(LinkedList.class);
 Class<?> resolvedType = resolver.findTypeMapping(new DeserializationConfig(), javaType).getRawClass();
 assertTrue(ArrayList.class.isAssignableFrom(resolvedType));
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with same class
 */
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameClass() {
 Class<LinkedList> linkedListType = LinkedList.class;
 resolver.addMapping(linkedListType, linkedListType);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with non-abstract superclass
 */
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperclass() {
 Class<ArrayList> arrayListType = ArrayList.class;
 resolver.addMapping(ArrayList.class, LinkedList.class);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)} with null type
 */
 @Test(expected = NullPointerException.class)
 public void testFindTypeMappingWithNullType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = null;
 resolver.findTypeMapping(config, javaType);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)} with no mapping
 */
 @Test
 public void testFindTypeMappingWithNoMapping() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = new ClassType(HashMap.class);
 Class<?> resolvedType = resolver.findTypeMapping(config, javaType).getRawClass();
 assertNull(resolvedType);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)} with multiple mappings
 */
 @Test
 public void testFindTypeMappingWithMultipleMappings() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, ArrayList.class);
 JavaType javaType = new ClassType(List.class);
 Class<?> resolvedType = resolver.findTypeMapping(new DeserializationConfig(), javaType).getRawClass();
 assertTrue(List.class.isAssignableFrom(resolvedType));
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#resolveAbstractType(DeserializationConfig, JavaType)}
 */
 @Test
 public void testResolveAbstractType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = new ClassType(Collection.class);
 Class<?> resolvedType = resolver.resolveAbstractType(config, javaType);
 assertNull(resolvedType);
 }
}