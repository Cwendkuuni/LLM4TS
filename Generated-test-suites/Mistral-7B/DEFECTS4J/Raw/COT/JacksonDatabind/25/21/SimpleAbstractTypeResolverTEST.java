import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;
 private DeserializationConfig config;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 config = mock(DeserializationConfig.class);
 }

 @Test
 public void testAddMapping() {
 Class<List> listType = List.class;
 Class<LinkedList> linkedListType = LinkedList.class;
 resolver.addMapping(listType, linkedListType);
 assertNotNull(resolver._mappings.get(new ClassKey(listType)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameTypes() {
 Class<List> listType = List.class;
 Class<LinkedList> linkedListType = LinkedList.class;
 resolver.addMapping(listType, linkedListType);
 resolver.addMapping(listType, linkedListType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidSubType() {
 Class<List> listType = List.class;
 Class<String> stringType = String.class;
 resolver.addMapping(listType, stringType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperType() {
 Class<List> listType = List.class;
 Class<ArrayList> arrayListType = ArrayList.class;
 resolver.addMapping(listType, arrayListType);
 }

 @Test
 public void testFindTypeMappingNullInput() {
 JavaType javaType = null;
 assertNull(resolver.findTypeMapping(config, javaType));
 }

 @Test
 public void testFindTypeMappingEmptyMappings() {
 resolver = new SimpleAbstractTypeResolver();
 JavaType javaType = mock(JavaType.class);
 when(javaType.getRawClass()).thenReturn(List.class);
 assertNull(resolver.findTypeMapping(config, javaType));
 }

 @Test
 public void testFindTypeMappingValidMapping() {
 Class<List> listType = List.class;
 Class<LinkedList> linkedListType = LinkedList.class;
 resolver.addMapping(listType, linkedListType);
 JavaType javaType = mock(JavaType.class);
 when(javaType.getRawClass()).thenReturn(listType);
 JavaType expectedJavaType = mock(JavaType.class);
 when(expectedJavaType.narrowBy(linkedListType)).thenReturn(expectedJavaType);
 when(config.getTypeFactory().constructType(linkedListType.getName())).thenReturn(linkedListType);
 JavaType actualJavaType = resolver.findTypeMapping(config, javaType);
 assertNotNull(actualJavaType);
 assertNotNull(actualJavaType.getRawClass());
 assertSame(expectedJavaType.getRawClass(), actualJavaType.getRawClass());
 }

 @Test
 public void testResolveAbstractTypeNullInput() {
 JavaType javaType = null;
 assertNull(resolver.resolveAbstractType(config, javaType));
 }

 @Test
 public void testResolveAbstractTypeEmptyMappings() {
 JavaType javaType = mock(JavaType.class);
 assertNull(resolver.resolveAbstractType(config, javaType));
 }

}