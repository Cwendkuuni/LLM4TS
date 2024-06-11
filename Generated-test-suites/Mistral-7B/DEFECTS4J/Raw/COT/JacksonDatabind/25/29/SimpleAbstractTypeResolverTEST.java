import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

 ClassKey key = new ClassKey(superType);
 Class<?> expectedSubType = subType;
 Class<?> actualSubType = resolver._mappings.get(key);

 assertEquals(expectedSubType, actualSubType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidArguments() {
 Class<?> superType = LinkedList.class;
 Class<? extends Collection> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 JavaType javaType = mock(JavaType.class);
 when(javaType.getRawClass()).thenReturn(Collection.class);

 Class<?> expectedSubType = LinkedList.class;
 JavaType actualType = resolver.findTypeMapping(config, javaType);

 assertEquals(expectedSubType, actualType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithNullType() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 JavaType javaType = null;

 JavaType actualType = resolver.findTypeMapping(config, javaType);

 assertNull(actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testFindTypeMappingWithNullConfig() {
 DeserializationConfig config = null;
 JavaType javaType = mock(JavaType.class);

 JavaType actualType = resolver.findTypeMapping(config, javaType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 JavaType javaType = mock(JavaType.class);
 when(javaType.getRawClass()).thenReturn(Collection.class);

 JavaType actualType = resolver.resolveAbstractType(config, javaType);

 assertNull(actualType);
 }
}