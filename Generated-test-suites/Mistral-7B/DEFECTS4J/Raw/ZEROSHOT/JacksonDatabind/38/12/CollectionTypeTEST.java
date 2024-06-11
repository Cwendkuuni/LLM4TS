import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<Integer>> listType = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 assertEquals("collection type; class List, contains class int", collectionType.toString());
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<Integer>> listType = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listType.getRawClass(), JavaType.of(Integer.class));
 assertEquals("collection type; class List, contains class int", collectionType.toString());
 }

 @Test
 public void testRefine() {
 Class<ArrayList<Integer>> arrayListType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionType collectionType = new CollectionType(arrayListType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 CollectionType refinedType = collectionType.refine(ArrayList.class, null, null, null);
 assertEquals("collection type; class ArrayList, contains class int", refinedType.toString());
 }

 @Test
 public void testNarrow() {
 Class<LinkedList<Integer>> linkedListType = JavaType.constructCollectionType(LinkedList.class, Integer.class);
 CollectionType collectionType = new CollectionType(linkedListType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 CollectionType narrowedType = collectionType.withTypeParameters(ArrayList.class);
 assertEquals("collection type; class ArrayList, contains class int", narrowedType.toString());
 }

 @Test
 public void testWithContentType() {
 Class<List<Integer>> listType = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 JavaType contentType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 CollectionType withContentType = collectionType.withContentType(contentType);
 assertEquals("collection type; class ArrayList, contains class int", withContentType.toString());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listType = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 Object contentTypeHandler = new Object();
 CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
 assertEquals("collection type; class List, contains class int", withContentTypeHandler.toString());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listType = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 Object typeHandler = new Object();
 CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
 assertEquals("collection type; class List, contains class int", withTypeHandler.toString());
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listType = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 Object valueHandler = new Object();
 CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
 assertEquals("collection type; class List, contains class int", withValueHandler.toString());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listType = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 Object contentValueHandler = new Object();
 CollectionType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
 assertEquals("collection type; class List, contains class int", withContentValueHandler.toString());
 }

 @Test
 public void testStaticTyping() {
 Class<List<Integer>> listType = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 CollectionType withStaticTyping = collectionType.withStaticTyping();
 assertTrue(withStaticTyping.isStatic());
 }

 @Test
 public void testToString() {
 Class<List<Integer>> listType = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listType.getRawClass(), null, null, null, JavaType.of(Integer.class));
 assertEquals("collection type; class List, contains class int", collectionType.toString());
 }
}