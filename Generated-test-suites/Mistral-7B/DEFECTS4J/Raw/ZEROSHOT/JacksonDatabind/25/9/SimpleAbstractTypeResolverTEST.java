import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ClassKey;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType collectionType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>(){}.getType());
 JavaType expectedType = mapper.getTypeFactory().constructType(LinkedList.class);
 JavaType actualType = resolver.findTypeMapping(config, collectionType);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingWithInvalidMapping() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType intType = mapper.getTypeFactory().constructType(Integer.class);
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(intType, String.class); // Invalid mapping
 JavaType actualType = resolver.findTypeMapping(config, intType);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testFindTypeMappingWithNoMapping() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType intType = mapper.getTypeFactory().constructType(Integer.class);
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 JavaType actualType = resolver.findTypeMapping(config, intType);
 assertNull(actualType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType collectionType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>(){}.getType());
 JavaType expectedType = null;
 JavaType actualType = resolver.resolveAbstractType(config, collectionType);
 assertEquals(expectedType, actualType);
 }
}