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
 Class<? extends Collection> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 Class<?> mappedSubType = resolver._mappings.get(new ClassKey(superType));
 assertEquals(subType, mappedSubType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSuperType() {
 Class<?> superType = LinkedList.class;
 Class<? extends Collection> subType = ArrayList.class;
 resolver.addMapping(superType, subType);
 // This will throw an exception since LinkedList is not a super type of ArrayList
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSubType() {
 Class<?> superType = Collection.class;
 Class<? extends Collection> subType = LinkedList.class;
 resolver.addMapping(superType, LinkedList.class);
 resolver.addMapping(superType, ArrayList.class);
 // This will throw an exception since ArrayList is not a sub type of LinkedList
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSameType() {
 Class<?> superType = Collection.class;
 Class<? extends Collection> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 resolver.addMapping(superType, subType);
 // This will throw an exception since a mapping can not be added to itself
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = new ClassType(List.class);
 Class<?> expected = LinkedList.class;
 JavaType mappedType = resolver.findTypeMapping(config, type);
 assertEquals(expected, mappedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingNull() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = new ClassType(String.class);
 JavaType mappedType = resolver.findTypeMapping(config, type);
 assertNull(mappedType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = new ClassType(Collection.class);
 JavaType abstractType = resolver.resolveAbstractType(config, type);
 assertNull(abstractType);
 }
}