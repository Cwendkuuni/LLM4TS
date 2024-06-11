import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

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
 Class<?> superType = Collection.class;
 Class<?> subType = LinkedList.class;
 resolver.addMapping((Class<T>) superType, (Class<? extends T>) subType);
 Class<?> mappedType = resolver._mappings.get(new ClassKey(superType));
 assertEquals(subType, mappedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSuperType() {
 Class<?> superType = LinkedList.class;
 Class<?> subType = Collection.class;
 resolver.addMapping((Class<T>) superType, (Class<? extends T>) subType);
 // This should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSubType() {
 Class<?> superType = Collection.class;
 Class<?> subType = LinkedList.class;
 resolver.addMapping((Class<T>) superType, (Class<? extends T>) ArrayList.class);
 // This should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSameType() {
 Class<?> superType = Collection.class;
 Class<?> subType = Collection.class;
 resolver.addMapping((Class<T>) superType, (Class<? extends T>) subType);
 // This should throw an exception
 }

 @Test
 public void testFindTypeMapping() {
 JavaType type = JavaType.constructType(ArrayList.class);
 JavaType mappedType = resolver.findTypeMapping(new DeserializationConfig(), type);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingNull() {
 JavaType type = null;
 JavaType mappedType = resolver.findTypeMapping(new DeserializationConfig(), type);
 assertNull(mappedType);
 }

}