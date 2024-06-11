import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver simpleAbstractTypeResolver;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 simpleAbstractTypeResolver = new SimpleAbstractTypeResolver();
 simpleAbstractTypeResolver.addMapping(Collection.class, LinkedList.class);
 simpleAbstractTypeResolver.addMapping(List.class, LinkedList.class);
 objectMapper = new ObjectMapper();
 objectMapper.setTypeResolver(new SimpleAbstractTypeResolver());
 }

 @Test
 public void testFindTypeMapping() {
 Class<?> src = Collection.class;
 Class<? extends Collection<?>> dst = LinkedList.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(src);
 JavaType expectedJavaType = TypeFactory.defaultInstance().constructType(dst);
 JavaType actualJavaType = simpleAbstractTypeResolver.findTypeMapping(null, javaType);
 assertEquals(expectedJavaType, actualJavaType);
 }

 @Test
 public void testFindTypeMapping_List() {
 Class<?> src = List.class;
 Class<? extends List<?>> dst = LinkedList.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(src);
 JavaType expectedJavaType = TypeFactory.defaultInstance().constructType(dst);
 JavaType actualJavaType = simpleAbstractTypeResolver.findTypeMapping(null, javaType);
 assertEquals(expectedJavaType, actualJavaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMapping_InvalidMapping() {
 Class<?> src = Collection.class;
 Class<? extends Collection<?>> dst = ArrayList.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(src);
 JavaType actualJavaType = simpleAbstractTypeResolver.findTypeMapping(null, javaType);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFindTypeMapping_Null() {
 Class<?> src = Collection.class;
 JavaType javaType = null;
 JavaType actualJavaType = simpleAbstractTypeResolver.findTypeMapping(null, javaType);
 assertNull(actualJavaType);
 }

 @Test
 public void testResolveAbstractType() {
 Class<?> src = Collection.class;
 JavaType javaType = TypeFactory.defaultInstance().constructType(src);
 JavaType actualJavaType = simpleAbstractTypeResolver.resolveAbstractType(null, javaType);
 assertNull(actualJavaType);
 }
}