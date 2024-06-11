import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;
 private DeserializationConfig config;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 config = Mockito.spy(new DeserializationConfig());
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)}
 */
 @Test
 public void testAddMapping() {
 Class<List<Integer>> listClass = List.class;
 Class<LinkedList<Integer>> linkedListClass = LinkedList.class;
 resolver.addMapping(listClass, linkedListClass);
 Class<?> mappedType = resolver._mappings.get(new ClassKey(listClass));
 assertEquals(linkedListClass, mappedType);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with self assignment
 */
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSelfAssignment() {
 Class<List<Integer>> listClass = List.class;
 Class<List<Integer>> listClassSame = List.class;
 resolver.addMapping(listClass, listClassSame);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with incompatible types
 */
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingIncompatibleTypes() {
 Class<List<Integer>> listClass = List.class;
 Class<String> stringClass = String.class;
 resolver.addMapping(listClass, stringClass);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with non-abstract class
 */
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingNonAbstractClass() {
 Class<List<Integer>> listClass = List.class;
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 resolver.addMapping(listClass, arrayListClass);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}
 */
 @Test
 public void testFindTypeMapping() {
 Class<List<Integer>> listClass = List.class;
 Class<LinkedList<Integer>> linkedListClass = LinkedList.class;
 resolver.addMapping(listClass, linkedListClass);
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)} with null input
 */
 @Test
 public void testFindTypeMappingNullInput() {
 JavaType javaType = null;
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertNull(mappedType);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#resolveAbstractType(DeserializationConfig, JavaType)}
 */
 @Test
 public void testResolveAbstractType() {
 JavaType javaType = null;
 JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
 assertNull(resolvedType);
 }
}