import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.Types;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testAddMapping() {
 Class<?> superType = Collection.class;
 Class<?> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithSameClass() {
 Class<?> superType = Collection.class;
 Class<?> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 resolver.addMapping(superType, subType);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNoSubTypeRelationship() {
 Class<?> superType = Collection.class;
 Class<?> subType = ArrayList.class;
 resolver.addMapping(superType, subType);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithNonAbstractSuperType() {
 Class<?> superType = ArrayList.class;
 Class<?> subType = LinkedList.class;
 resolver.addMapping(superType, subType);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testFindTypeMapping() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeFactory typeFactory = mapper.getTypeFactory();
 JavaType javaType = typeFactory.constructType(List.class);
 JavaType resolvedType = resolver.findTypeMapping(null, javaType);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithNullType() {
 JavaType resolvedType = resolver.findTypeMapping(null, null);
 assertNull(resolvedType);
 }

 @Test
 public void testResolveAbstractType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 TypeFactory typeFactory = mapper.getTypeFactory();
 JavaType javaType = typeFactory.constructType(Collection.class);
 JavaType resolvedType = resolver.resolveAbstractType(null, javaType);
 assertNull(resolvedType);
 }
}