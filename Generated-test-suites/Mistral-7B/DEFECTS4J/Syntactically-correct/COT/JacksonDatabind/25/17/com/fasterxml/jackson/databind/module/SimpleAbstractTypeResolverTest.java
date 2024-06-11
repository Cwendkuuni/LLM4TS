import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

@RunWith(MockitoJUnitRunner.class)
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
 Class<?> mappedType = resolver._mappings.get(key);
 assertNotNull(mappedType);
 assertEquals(subType, mappedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameType() {
 Class<?> superType = Collection.class;
 Class<? extends Collection> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 resolver.addMapping(superType, subType);
 fail("Should have thrown exception");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidSubType() {
 Class<?> superType = Collection.class;
 Class<? extends Collection> subType = ArrayList.class;
 resolver.addMapping(superType, subType);
 fail("Should have thrown exception");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperType() {
 Class<?> superType = LinkedList.class;
 Class<? extends Collection> subType = ArrayList.class;
 resolver.addMapping(superType, subType);
 fail("Should have thrown exception");
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 JavaType javaType = mock(JavaType.class);
 when(javaType.getRawClass()).thenReturn(Collection.class);

 Class<?> src = Collection.class;
 Class<?> dst = LinkedList.class;

 when(resolver.findTypeMapping(config, javaType)).thenReturn(javaType);

 JavaType result = resolver.findTypeMapping(config, javaType);
 assertSame(javaType, result);
 }

 @Test
 public void testFindTypeMappingWithNullType() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 JavaType javaType = null;
 JavaType result = resolver.findTypeMapping(config, javaType);
 assertNull(result);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = mock(DeserializationConfig.class);
 JavaType javaType = mock(JavaType.class);
 when(javaType.getRawClass()).thenReturn(Collection.class);

 when(resolver.resolveAbstractType(config, javaType)).thenReturn(null);

 JavaType result = resolver.resolveAbstractType(config, javaType);
 assertNull(result);
 }
}