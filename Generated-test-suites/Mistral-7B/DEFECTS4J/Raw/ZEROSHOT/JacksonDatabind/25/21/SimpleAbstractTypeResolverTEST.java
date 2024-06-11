import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassKey;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testAddMapping() {
 Class<? extends Collection<?>> collectionType =LinkedList.class;
 Class<Collection<?>> collectionSuperType = Collection.class;
 resolver.addMapping(collectionSuperType, collectionType);
 assertNotNull(resolver._mappings);
 assertNotNull(resolver._mappings.get(new ClassKey(collectionSuperType)));
 assertEquals(collectionType, resolver._mappings.get(new ClassKey(collectionSuperType)));
 }

 @Test
 public void testFindTypeMapping() throws Exception {
 JavaType javaType = resolver.findTypeMapping(null, JavaType.construct(Collection.class, Integer.class));
 assertNotNull(javaType);
 JavaType actualType = JavaType.construct(LinkedList.class, Integer.class);
 JavaType expectedType = JavaType.construct(LinkedList.class, Integer.class);
 assertEquals(actualType, javaType);
 assertEquals(expectedType, javaType);
 }

 @Test
 public void testFindTypeMappingWhenNullMapping() throws Exception {
 JavaType javaType = resolver.findTypeMapping(null, JavaType.construct(Collection.class, Integer.class));
 assertNull(javaType);
 }

 @Test
 public void testResolveAbstractType() throws Exception {
 JavaType javaType = resolver.resolveAbstractType(null, JavaType.construct(Collection.class));
 assertNull(javaType);
 }
}