import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ClassKey;
import com.fasterxml.jackson.databind.DeserializationConfig;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @SuppressWarnings("unchecked")
 @Test
 public void testAddMapping() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);

 Class<?> collectionType = resolver.getClass();
 Class<? extends Collection<?>> linkedListType = (Class<? extends Collection<?>>) resolver._mappings.get(new ClassKey(Collection.class));
 assertNotNull(linkedListType);
 assertTrue(Collection.class.isAssignableFrom(linkedListType));
 assertTrue(LinkedList.class.isAssignableFrom(collectionType));
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingThrowsException_whenSuperTypeIsSameAsSubType() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(String.class, String.class);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingThrowsException_whenSubTypeIsNotASubtype() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(String.class, Integer.class);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingThrowsException_whenSuperTypeIsNotAbstract() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Integer.class, List.class);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindTypeMappingReturnsMappedType() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);

 JavaType collectionType = new ClassType(Collection.class);
 JavaType linkedListType = resolver.findTypeMapping(new DeserializationConfig(new AnnotationIntrospector()), collectionType);
 assertNotNull(linkedListType);
 assertTrue(LinkedList.class.isAssignableFrom(linkedListType.getRawClass()));
 }

 @Test
 public void testFindTypeMappingReturnsNull_whenNoMappingExists() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType collectionType = new ClassType(Collection.class);
 JavaType linkedListType = resolver.findTypeMapping(new DeserializationConfig(new AnnotationIntrospector()), collectionType);
 assertNull(linkedListType);
 }

 @Test
 public void testResolveAbstractTypeReturnsNull() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType intType = new ClassType(int[].class);
 JavaType mappedType = resolver.resolveAbstractType(new DeserializationConfig(new AnnotationIntrospector()), intType);
 assertNull(mappedType);
 }

 private static class ClassType extends JavaType {
 private ClassType(Class<?> clazz) {
 super(clazz);
 }
 }

}