import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

 @Test
 public void testAddMapping() {
 Class<Integer> intType = Integer.class;
 Class<ArrayList<Integer>> intListType = ArrayList.class.generateType(intType);
 resolver.addMapping(Collection.class, ArrayList.class);
 resolver.addMapping(List.class, ArrayList.class);
 JavaType intListTypeResolved = resolver.findTypeMapping(null, JavaType.construct(intListType));
 Assert.assertTrue(intListTypeResolved.getRawClass() == ArrayList.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingSelf() {
 Class<Integer> intType = Integer.class;
 resolver.addMapping(intType, intType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSubtype() {
 Class<Integer> intType = Integer.class;
 Class<String> strType = String.class;
 resolver.addMapping(intType, strType);
 }

 @Test
 public void testAddMappingAbstractTypes() {
 Class<Collection<Integer>> collectionType = Collection.class.generateType(intType);
 Class<ArrayList<Integer>> listType = ArrayList.class.getType();
 resolver.addMapping(collectionType, listType);
 JavaType collectionTypeResolved = resolver.findTypeMapping(null, JavaType.construct(collectionType));
 Assert.assertTrue(collectionTypeResolved.getRawClass() == ArrayList.class);
 }

 @Test
 public void testNullType() {
 JavaType nullType = null;
 JavaType nullTypeResolved = resolver.findTypeMapping(null, nullType);
 Assert.assertNull(nullTypeResolved);
 }

 @Test
 public void testFindTypeMappingNonAbstractType() {
 Class<Integer> intType = Integer.class;
 JavaType intTypeResolved = resolver.findTypeMapping(null, JavaType.construct(intType));
 Assert.assertNull(intTypeResolved);
 }

 @Test
 public void testResolveAbstractType() {
 Class<Collection<Integer>> collectionType = Collection.class.generateType(intType);
 JavaType collectionTypeResolved = resolver.resolveAbstractType(null, JavaType.construct(collectionType));
 Assert.assertNull(collectionTypeResolved);
 }
}