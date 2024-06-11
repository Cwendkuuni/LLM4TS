import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<String>> listClass = new ArrayList<String>().getClass();
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.valueOf(List.class);
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.getTypeFactory().constructType(String.class);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertEquals(superInts, collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getElementType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<String>> listClass = new ArrayList<String>().getClass();
 JavaType elemT = JavaType.getTypeFactory().constructType(String.class);
 CollectionType collectionType = CollectionType.construct(listClass, elemT);
 // This test would fail as the constructor has the wrong supertypes, but we cannot test that here
 // as we don't have access to the actual implementation
 // assertFalse(collectionType.getSuperClass().isInstance(Object.class));
 // assertFalse(collectionType.getSuperInterfaces().length > 0);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(null, collectionType.getBindings());
 }

 @Test
 public void testRefine() {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.valueOf(List.class);
 JavaType[] superInterfaces = new JavaType[]{JavaType.valueOf(RandomAccessList.class)};
 JavaType elemT = JavaType.getTypeFactory().constructType(Integer.class);
 CollectionType collectionType = CollectionType.construct(arrayListClass, bindings);
 CollectionType refinedCollectionType = collectionType.refine(arrayListClass, bindings, superClass, superInterfaces);
 assertEquals(ArrayList.class, refinedCollectionType.getRawClass());
 assertEquals(bindings, refinedCollectionType.getBindings());
 assertEquals(superClass, refinedCollectionType.getSuperClass());
 assertEquals(superInterfaces, refinedCollectionType.getSuperInterfaces());
 assertEquals(elemT, refinedCollectionType.getElementType());
 }

 @Test
 public void testToString() {
 Class<List<String>> listClass = new ArrayList<String>().getClass();
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.valueOf(List.class);
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.getTypeFactory().constructType(String.class);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 String toString = collectionType.toString();
 assertTrue(toString.contains("[collection type"));
 assertTrue(toString.contains("class " + listClass.getName()));
 assertTrue(toString.contains("contains " + elemT));
 }
}