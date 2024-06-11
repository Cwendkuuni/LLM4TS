import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Test
 public void testAddMapping() {
 resolver = new SimpleAbstractTypeResolver();
 Class<List> list = List.class;
 Class<LinkedList> linkedList = LinkedList.class;
 resolver.addMapping(list, linkedList);
 assertNotNull(resolver._mappings);
 assertTrue(resolver._mappings.containsKey(new ClassKey(list)));
 assertEquals(linkedList, resolver._mappings.get(new ClassKey(list)));
 }

 @Test
 public void testAddMappingWithSameClass() {
 resolver = new SimpleAbstractTypeResolver();
 Class<String> string = String.class;
 resolver.addMapping(string, string);
 IllegalStateException exception = assertThrows(IllegalArgumentException.class, () -> {
 resolver.addMapping(string, string);
 });
 assertEquals("Can not add mapping from class to itself", exception.getMessage());
 }

 @Test
 public void testAddMappingWithIncompatibleTypes() {
 resolver = new SimpleAbstractTypeResolver();
 Class<List> list = List.class;
 Class<Map> map = Map.class;
 resolver.addMapping(list, map);
 IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
 resolver.addMapping(list, map);
 });
 assertEquals("Can not add mapping from class " + list.getName() + " to " + map.getName() + ", as latter is not a subtype of former", exception.getMessage());
 }

 @Test
 public void testAddMappingWithNonAbstractType() {
 resolver = new SimpleAbstractTypeResolver();
 Class<String> string = String.class;
 resolver.addMapping(string, String.class);
 IllegalStateException exception = assertThrows(IllegalArgumentException.class, () -> {
 resolver.addMapping(string, String.class);
 });
 assertEquals("Can not add mapping from class " + string.getName() + " since it is not abstract", exception.getMessage());
 }

 @Test
 public void testFindTypeMapping() {
 // Given
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(List.class, LinkedList.class);
 DeserializationConfig config = new DeserializationConfig();
 JavaType listType = JavaType.constructParametricType(LinkedList.class, Object.class);

 // When
 JavaType resolvedType = resolver.findTypeMapping(config, listType);

 // Then
 assertNotNull(resolvedType);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithEmptyMapping() {
 // Given
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 DeserializationConfig config = new DeserializationConfig();
 JavaType listType = JavaType.constructParametricType(LinkedList.class, Object.class);

 // When
 JavaType resolvedType = resolver.findTypeMapping(config, listType);

 // Then
 assertNull(resolvedType);
 }
}