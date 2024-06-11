import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>() {}.getType());
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);
 assertNotNull(resolvedType);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithAbstractType() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType javaType = mapper.getTypeFactory().constructType(LinkedList.class);
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);
 assertNull(resolvedType);
 }

 @Test
 public void testFindTypeMappingWithInvalidMapping() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 SimpleAbstractTypeResolver resolver2 = new SimpleAbstractTypeResolver();
 resolver2.addMapping(String.class, Integer.class);
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 JavaType resolvedType = resolver2.findTypeMapping(config, javaType);
 assertNull(resolvedType);
 }

 @Test
 public void testFindTypeMappingWithCircularMapping() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 SimpleAbstractTypeResolver resolver2 = new SimpleAbstractTypeResolver();
 resolver2.addMapping(List.class, Integer[].class);
 resolver2.addMapping(Integer[].class, List.class);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class);
 JavaType resolvedType = resolver2.findTypeMapping(config, javaType);
 assertNull(resolvedType);
 }

 @Test
 public void testResolveAbstractType() throws Exception {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType javaType = mapper.getTypeFactory().constructType(LinkedList.class);
 JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
 assertNull(resolvedType);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidSuperType() throws Exception {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(String.class, LinkedList.class);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidSubType() throws Exception {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(LinkedList.class, String.class);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidMapping() throws Exception {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Integer.class, Integer.class);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddMappingWithSameType() throws Exception {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(LinkedList.class, LinkedList.class);
 }
}