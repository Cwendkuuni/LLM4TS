import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.util.ClassKey;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

 @Test
 public void testAddMapping() {
 Class<String> superType = String.class;
 Class<ArrayList<String>> subType = ArrayList.class;
 resolver.addMapping(superType, subType);
 assertNotNull(resolver._mappings.get(new ClassKey(superType)));
 assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSuperType() {
 Class<String> superType = String.class;
 Class<ArrayList<String>> subType = ArrayList.class;
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(subType, superType);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSubType() {
 Class<String> superType = String.class;
 Class<ArrayList<Integer>> subType = ArrayList.class;
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(superType, subType);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSelfMapping() {
 Class<String> superType = String.class;
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(superType, superType);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testFindTypeMapping() {
 JavaType javaType = JavaType.construct(ArrayList.class, String.class);
 Class<?> src = javaType.getRawClass();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(src.getSuperclass(), ArrayList.class);
 JavaType expected = JavaType.construct(ArrayList.class, String.class);
 JavaType actual = resolver.findTypeMapping(null, javaType);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindTypeMappingNoMapping() {
 JavaType javaType = JavaType.construct(ArrayList.class, String.class);
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType actual = resolver.findTypeMapping(null, javaType);
 assertNull(actual);
 }

 @Test
 public void testResolveAbstractType() {
 JavaType javaType = JavaType.construct(ArrayList.class);
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType actual = resolver.resolveAbstractType(null, javaType);
 assertNull(actual);
 }
}