import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 config.setBaseTypeResolver(resolver);

 JavaType type1 = mapper.getTypeFactory().constructType(LinkedList.class);
 JavaType type2 = mapper.getTypeFactory().constructType(ArrayList.class);
 JavaType superType = mapper.getTypeFactory().constructType(Collection.class);
 JavaType expectedType = mapper.getTypeFactory().constructType(LinkedList.class);

 JavaType actualType = resolver.findTypeMapping(config, superType);
 assertEquals(expectedType, actualType);

 actualType = resolver.findTypeMapping(config, type2);
 assertNull(actualType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 config.setBaseTypeResolver(resolver);

 JavaType type = mapper.getTypeFactory().constructType(Collection.class);
 assertNull(resolver.resolveAbstractType(config, type));
 }
}