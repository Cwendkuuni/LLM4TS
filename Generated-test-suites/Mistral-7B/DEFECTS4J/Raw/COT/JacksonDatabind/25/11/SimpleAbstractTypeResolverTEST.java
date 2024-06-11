import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testAddMapping() {
 Class<?> superType = Collection.class;
 Class<? extends Collection> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSuperTypeSubtypeError() {
 Class<?> superType = LinkedList.class;
 Class<? extends Collection> subType = ArrayList.class;
 resolver.addMapping(superType, subType);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSameClass() {
 Class<?> superType = Collection.class;
 Class<? extends Collection> subType = Collection.class;
 resolver.addMapping(superType, subType);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testAddMappingForAbstractClass() {
 Class<?> superType = AbstractList.class;
 Class<? extends AbstractList> subType = ArrayList.class;
 resolver.addMapping(superType, subType);
 assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingForNonAbstractClass() {
 Class<?> superType = ArrayList.class;
 Class<? extends ArrayList> subType = ArrayList.class;
 resolver.addMapping(superType, subType);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testFindTypeMappingNullType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = null;
 assertNull(resolver.findTypeMapping(config, type));
 }

 @Test(expected = NullPointerException.class)
 public void testFindTypeMappingNullMappings() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = new ClassType(Integer.class);
 assertNull(resolver.findTypeMapping(config, type));
 }

 @Test
 public void testFindTypeMapping() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = new ClassType(Collection.class);
 Class<?> expected = LinkedList.class;
 JavaType actual = resolver.findTypeMapping(config, type);
 assertNotNull(actual);
 assertEquals(expected, actual.getRawClass());
 }

 @Test
 public void testFindTypeMappingTwoMappings() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = new ClassType(Collection.class);
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, ArrayList.class);
 Class<?> expected = ArrayList.class; // This would depend on the order of the mappings
 JavaType actual = resolver.findTypeMapping(config, type);
 assertEquals(expected, actual.getRawClass());
 }

 @Test
 public void testResolveAbstractTypeNullType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = null;
 assertNull(resolver.resolveAbstractType(config, type));
 }

 @Test
 public void testResolveAbstractTypeNullMappings() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = new ClassType(Integer.class);
 assertNull(resolver.resolveAbstractType(config, type));
 }

}