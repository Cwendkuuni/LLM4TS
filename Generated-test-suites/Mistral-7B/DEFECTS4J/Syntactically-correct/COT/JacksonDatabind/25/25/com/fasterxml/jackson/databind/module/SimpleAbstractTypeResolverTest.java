import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;

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
 Class<?> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 assertNotNull(resolver._mappings);
 assertEquals(1, resolver._mappings.size());
 assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameType() {
 Class<?> superType = LinkedList.class;
 Class<?> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 fail("Should have thrown an exception");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithWrongSubType() {
 Class<?> superType = Collection.class;
 Class<?> subType = HashMap.class;
 resolver.addMapping(superType, subType);
 fail("Should have thrown an exception");
 }

 @Test
 public void testAddMappingWithAbstractSuperType() {
 Class<?> superType = AbstractList.class;
 Class<?> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 assertNotNull(resolver._mappings);
 assertEquals(1, resolver._mappings.size());
 assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
 }

 @Test
 public void testFindTypeMapping() {
 Class<?> collectionType = Collection.class;
 JavaType type = SimpleType.construct(collectionType, Collections.emptyList());
 JavaType expected = SimpleType.construct(LinkedList.class, Collections.emptyList());
 JavaType actual = resolver.findTypeMapping(null, type);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindTypeMappingWithNullType() {
 JavaType expected = null;
 JavaType actual = resolver.findTypeMapping(null, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testResolveAbstractType() {
 JavaType type = SimpleType.construct(Collection.class, Collections.emptyList());
 JavaType actual = resolver.resolveAbstractType(null, type);
 assertNull(actual);
 }
}