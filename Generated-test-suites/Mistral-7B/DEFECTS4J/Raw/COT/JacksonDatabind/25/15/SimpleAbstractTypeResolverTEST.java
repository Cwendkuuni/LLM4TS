import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

 @Test
 public void testAddMapping() {
 Class<List> listClass = List.class;
 Class<LinkedList> linkedListClass = LinkedList.class;
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(listClass, linkedListClass);
 Class<?> resolvedType = resolver._mappings.get(new ClassKey(listClass));
 assertTrue(resolvedType.isAssignableFrom(linkedListClass));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameType() {
 Class<List> listClass = List.class;
 resolver.addMapping(listClass, listClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidSubType() {
 Class<List> listClass = List.class;
 Class<String> stringClass = String.class;
 resolver.addMapping(listClass, stringClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperType() {
 Class<List> listClass = List.class;
 Class<ArrayList> arrayListClass = ArrayList.class;
 resolver.addMapping(listClass, arrayListClass);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig(new AnnotationIntrospector());
 JavaType javaType = JavaType.construct(List.class, int.class);
 JavaType expectedType = JavaType.construct(LinkedList.class, int.class);
 JavaType actualType = resolver.findTypeMapping(config, javaType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testFindTypeMappingWithNullType() {
 DeserializationConfig config = new DeserializationConfig(new AnnotationIntrospector());
 JavaType actualType = resolver.findTypeMapping(config, null);
 assertNull(actualType);
 }

}