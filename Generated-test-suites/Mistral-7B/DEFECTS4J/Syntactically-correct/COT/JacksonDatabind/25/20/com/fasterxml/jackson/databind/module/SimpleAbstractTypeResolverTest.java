import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

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
 Class<?> superType = Collection.class;
 Class<? extends Collection<?>> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 JavaType mappedType = resolver.findTypeMapping(new DeserializationConfig(), JavaType.construct(superType, Object.class));
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with same class
 */
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSameClass() {
 Class<?> superType = Collection.class;
 Class<? extends Collection<?>> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 resolver.addMapping(superType, subType);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with incorrect super type
 */
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingIncorrectSuperType() {
 Class<?> superType = Collection.class;
 Class<? extends Number> subType = Integer.class;
 resolver.addMapping(superType, subType);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#addMapping(Class, Class)} with non-abstract super type
 */
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingNonAbstractSuperType() {
 Class<?> superType = ArrayList.class;
 Class<? extends Collection<?>> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}
 * with null config
 */
 @Test(expected = NullPointerException.class)
 public void testFindTypeMappingNullConfig() {
 resolver.findTypeMapping(null, new JavaType(Collection.class, Object.class));
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}
 * with null type
 */
 @Test(expected = NullPointerException.class)
 public void testFindTypeMappingNullType() {
 resolver.findTypeMapping(new DeserializationConfig(), null);
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#findTypeMapping(DeserializationConfig, JavaType)}
 * with no mapping found
 */
 @Test
 public void testFindTypeMappingNoMappingFound() {
 JavaType type = JavaType.construct(HashMap.class, Object.class);
 assertNull(resolver.findTypeMapping(new DeserializationConfig(), type));
 }

 /**
 * Test method for {@link SimpleAbstractTypeResolver#resolveAbstractType(DeserializationConfig, JavaType)}
 */
 @Test
 public void testResolveAbstractType() {
 assertNull(resolver.resolveAbstractType(new DeserializationConfig(), new JavaType(Collection.class, Object.class)));
 }

}