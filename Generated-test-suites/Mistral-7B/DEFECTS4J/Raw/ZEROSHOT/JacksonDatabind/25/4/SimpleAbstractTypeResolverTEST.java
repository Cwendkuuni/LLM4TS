import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

 @Test
 public void testSimpleAbstractTypeResolver() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 // Test case for adding a mapping for Collection and List to LinkedList
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);

 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
 JavaType expectedType = new TypeReference<LinkedList<Integer>>(){}.getType();

 JavaType actualType = resolver.findTypeMapping(config, javaType);
 assertNotNull(actualType);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameClass() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(Collection.class, LinkedList.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidSubType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Integer.class, LinkedList.class);
 }
}