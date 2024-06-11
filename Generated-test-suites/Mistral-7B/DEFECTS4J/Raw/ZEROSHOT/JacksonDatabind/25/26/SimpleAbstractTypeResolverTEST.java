import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;
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
 JavaType javaType = JavaType.construct(Collection.class, JavaType.construct(Integer.class));
 JavaType expected = JavaType.construct(LinkedList.class, JavaType.construct(Integer.class));
 JavaType actual = resolver.findTypeMapping(config, javaType);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindTypeMapping_null() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = null;
 JavaType actual = resolver.findTypeMapping(config, javaType);
 assertNull(actual);
 }

 @Test
 public void testFindTypeMapping_wrongType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = JavaType.construct(String.class);
 JavaType actual = resolver.findTypeMapping(config, javaType);
 assertNull(actual);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = JavaType.construct(Collection.class, JavaType.construct(Integer.class));
 JavaType expected = null;
 JavaType actual = resolver.resolveAbstractType(config, javaType);
 assertEquals(expected, actual);
 }
}