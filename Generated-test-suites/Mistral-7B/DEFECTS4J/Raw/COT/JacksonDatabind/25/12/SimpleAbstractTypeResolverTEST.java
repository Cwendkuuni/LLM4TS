import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.type.ClassKey;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

 @Test
 public void testAddMapping() {
  resolver.addMapping(Collection.class, LinkedList.class);
  resolver.addMapping(List.class, LinkedList.class);
  assertEquals(2, resolver._mappings.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSameType() {
  resolver.addMapping(Collection.class, Collection.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWrongRelation() {
  resolver.addMapping(Collection.class, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingNonAbstractType() {
  resolver.addMapping(ArrayList.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
  resolver.addMapping(Collection.class, LinkedList.class);
  resolver.addMapping(List.class, LinkedList.class);
  JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
  JavaType expectedJavaType = new ClassType(LinkedList.class);
  JavaType actualJavaType = resolver.findTypeMapping(null, javaType);
  assertEquals(expectedJavaType, actualJavaType);
 }

 @Test
 public void testFindTypeMappingNullType() {
  JavaType actualJavaType = resolver.findTypeMapping(null, null);
  assertNull(actualJavaType);
 }

 @Test
 public void testResolveAbstractType() {
  JavaType actualJavaType = resolver.resolveAbstractType(null, null);
  assertNull(actualJavaType);
 }
}