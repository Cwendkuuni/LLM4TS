import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = JavaType.construct(Collection.class, int.class);
 JavaType resolvedType = resolver.findTypeMapping(config, type);
 assertEquals(LinkedList.class, resolvedType.getRawClass());

 type = JavaType.construct(List.class, int.class);
 resolvedType = resolver.findTypeMapping(config, type);
 assertEquals(LinkedList.class, resolvedType.getRawClass());

 type = JavaType.construct(ArrayList.class); // concrete class
 assertNull(resolver.findTypeMapping(config, type));
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = JavaType.construct(Collection.class, int.class);
 JavaType resolvedType = resolver.resolveAbstractType(config, type);
 assertNull(resolvedType);
 }

 @Test
 public void testFindTypeMappingWithClassIdType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType type = JavaType.construct(ClassIdType.construct(Collection.class), int.class);
 JavaType resolvedType = resolver.findTypeMapping(config, type);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(List.class, LinkedList.class);
 resolver.addMapping(List.class, ArrayList.class); // same type, but different implementation
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonSubType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, ArrayList.class); // Collection is not a subtype of ArrayList
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(ArrayList.class, LinkedList.class); // ArrayList is not abstract
 }
}