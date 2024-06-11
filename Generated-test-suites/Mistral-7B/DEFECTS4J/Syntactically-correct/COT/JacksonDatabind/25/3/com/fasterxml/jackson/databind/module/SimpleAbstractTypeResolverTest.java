import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

 @Test
 public void testAddMapping() {
 Class<List> superType = List.class;
 Class<LinkedList> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 Class<?> mappedType = resolver._mappings.get(new ClassKey(superType));
 assertEquals(subType, mappedType);
 }

 @Test
 public void testAddMapping_SameClass() {
 Class<List> superType = List.class;
 Class<List> subType = List.class;
 resolver.addMapping(superType, subType);
 Assert.assertNull(resolver._mappings.get(new ClassKey(superType)));
 }

 @Test
 public void testAddMapping_NotSubType() {
 Class<List> superType = List.class;
 Class<Set> subType = Set.class;
 resolver.addMapping(superType, subType);
 Assert.assertNull(resolver._mappings.get(new ClassKey(superType)));
 }

 @Test
 public void testAddMapping_NonAbstractSuperType() {
 Class<List> superType = List.class;
 Class<List> subType = List.class;
 resolver.addMapping(superType, subType);
 Assert.assertFalse(Modifier.isAbstract(superType.getModifiers()));
 }

 @Test
 public void testFindTypeMapping_NullConfig() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType type = JavaType.construct();
 Assert.assertNull(resolver.findTypeMapping(null, type));
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(List.class, LinkedList.class);
 JavaType type = JavaType.construct(List.class);
 JavaType mappedType = resolver.findTypeMapping(config, type);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 @Test
 public void testFindTypeMapping_NoMapping() {
 DeserializationConfig config = new DeserializationConfig();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType type = JavaType.construct(List.class);
 JavaType mappedType = resolver.findTypeMapping(config, type);
 assertNull(mappedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMapping_InvalidMapping() {
 DeserializationConfig config = new DeserializationConfig();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(List.class, Integer.class);
 JavaType type = JavaType.construct(List.class);
 JavaType mappedType = resolver.findTypeMapping(config, type);
 fail("Unexpectedly found mapping");
 }

 @Test
 public void testResolveAbstractType_NullConfig() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType type = JavaType.construct();
 JavaType result = resolver.resolveAbstractType(null, type);
 assertNull(result);
 }

 @Test
 public void testResolveAbstractType_NullType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType type = null;
 JavaType result = resolver.resolveAbstractType(new DeserializationConfig(), type);
 assertNull(result);
 }
}