import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;

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
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(superType, subType);

 ClassKey key = new ClassKey(superType);
 Class<?> mappedType = resolver._mappings.get(key);
 assertEquals(subType, mappedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSuperSubRelation() {
 Class<?> superType = Collection.class;
 Class<? extends Collection> subType = ArrayList.class;
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(superType, subType);

 failure(new IllegalArgumentException("Can not add mapping from class " + superType.getName()
 + " to " + subType.getName() + ", as latter is not a subtype of former"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSameType() {
 Class<?> superType = Collection.class;
 Class<? extends Collection> subType = LinkedList.class;
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(superType, subType);
 resolver.addMapping(superType, LinkedList.class);

 failure(new IllegalArgumentException("Can not add mapping from class to itself"));
 }

 @Test
 public void testAddMappingAbstractType() {
 Class<?> superType = AbstractList.class;
 Class<? extends AbstractList> subType = LinkedList.class;
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(superType, subType);

 ClassKey key = new ClassKey(superType);
 Class<?> mappedType = resolver._mappings.get(key);
 assertEquals(subType, mappedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingNonAbstractSuperType() {
 Class<?> superType = LinkedList.class;
 Class<? extends LinkedList> subType = ArrayList.class;
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(superType, subType);

 failure(new IllegalArgumentException("Can not add mapping from class " + superType.getName()
 + " since it is not abstract"));
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = config.constructType(ArrayList.class);
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertNotNull(mappedType);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingNull() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = null;
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertNull(mappedType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = config.constructType(ArrayList.class);
 JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
 assertNull(resolvedType);
 }

 private void failure(Throwable throwable) {
 throw new AssertionError("Unexpected exception", throwable);
 }

}