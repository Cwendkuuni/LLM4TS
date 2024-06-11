import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;
 private DeserializationConfig config;
 private ObjectMapper mapper;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 config = new DeserializationConfig();
 mapper = new ObjectMapper(config);
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 config.setTypeResolver(resolver);
 }

 @Test
 public void testFindTypeMapping() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>() {}.getType());
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }

 @Test
 public void testResolveAbstractType() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(Collection.class);
 JavaType mappedType = resolver.resolveAbstractType(config, javaType);
 assertNull(mappedType);
 }

 @Test
 public void testMappingAbstractClass() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(AbstractList.class);
 resolver.addMapping(AbstractList.class, ArrayList.class);
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertEquals(ArrayList.class, mappedType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMapping() throws Exception {
 // invalid mapping as subType is not a subclass of superType
 resolver.addMapping(Number.class, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSelfMapping() throws Exception {
 // invalid mapping as superType is equal to subType
 resolver.addMapping(List.class, List.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonAbstractMapping() throws Exception {
 // invalid mapping as superType is not abstract
 resolver.addMapping(String.class, LinkedList.class);
 }
}