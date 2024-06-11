import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 }

 @Test
 public void testAddMapping() {
 Class<List<String>> listClass = List.class.getTypeParameters()[0].getType();
 Class<LinkedList<String>> linkedListClass = LinkedList.class;
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, linkedListClass);
 resolver.addMapping(listClass, linkedListClass);

 Set<Class<?>> expectedMappings = new HashSet<>(Arrays.asList(LinkedList.class, List.class, Collection.class));
 Set<Class<?>> actualMappings = new HashSet<>(resolver._mappings.values());
 assertEquals(expectedMappings, actualMappings);
 }

 @Test
 public void testFindTypeMapping() {
 Class<List<String>> listClass = List.class.getTypeParameters()[0].getType();
 Class<LinkedList<String>> linkedListClass = LinkedList.class;
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, linkedListClass);
 resolver.addMapping(listClass, linkedListClass);

 JavaType javaType = Mockito.mock(JavaType.class);
 Mockito.when(javaType.getRawClass()).thenReturn(listClass);
 JavaType result = resolver.findTypeMapping(Mockito.mock(DeserializationConfig.class), javaType);
 assertNotNull(result);
 assertEquals(linkedListClass, result.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithNoMapping() {
 JavaType javaType = Mockito.mock(JavaType.class);
 Mockito.when(javaType.getRawClass()).thenReturn(Integer.TYPE);
 JavaType result = resolver.findTypeMapping(Mockito.mock(DeserializationConfig.class), javaType);
 assertNull(result);
 }

 @Test
 public void testResolveAbstractType() {
 JavaType javaType = Mockito.mock(JavaType.class);
 JavaType result = resolver.resolveAbstractType(Mockito.mock(DeserializationConfig.class), javaType);
 assertNull(result);
 }
}