import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 assertEquals("java.util.ArrayList<java.lang.Integer>", collectionType.toString());
 }

 @Test
 public void testDeprecatedConstruct() {
 Class<?> listType = ArrayList.class;
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listType, elemT);
 assertEquals("java.util.ArrayList<java.lang.Integer>", collectionType.toString());
 }

 @Test
 public void testNarrow() {
 Class<?> listType = ArrayList.class;
 Class<?> linkedListType = LinkedList.class;
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listType, elemT);
 CollectionType narrowedType = collectionType._narrow(linkedListType);
 assertEquals("java.util.LinkedList<java.lang.Integer>", narrowedType.toString());
 }

 @Test
 public void testWithContentType() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 JavaType contentType = Long.TYPE;
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 CollectionType newCollectionType = collectionType.withContentType(contentType);
 assertEquals("java.util.ArrayList<java.lang.Long>", newCollectionType.toString());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 Object typeHandler = new Object() {};
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
 assertEquals("java.util.ArrayList<java.lang.Integer>", newCollectionType.toString());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 Object typeHandler = new Object() {};
 JavaType contentType = Long.TYPE;
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 CollectionType newCollectionType = collectionType.withContentTypeHandler(typeHandler);
 assertEquals("java.util.ArrayList<java.lang.Long>", newCollectionType.toString());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 Object valueHandler = new Object() {};
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
 assertEquals("java.util.ArrayList<java.lang.Integer>", newCollectionType.toString());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 Object valueHandler = new Object() {};
 JavaType contentType = Long.TYPE;
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 CollectionType newCollectionType = collectionType.withContentValueHandler(valueHandler);
 assertEquals("java.util.ArrayList<java.lang.Long>", newCollectionType.toString());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 CollectionType newCollectionType = collectionType.withStaticTyping();
 assertEquals("java.util.ArrayList<java.lang.Integer>", newCollectionType.toString());
 assertEquals(true, newCollectionType._asStatic);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRefineWithInvalidType() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = String.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 JavaType refinedType = collectionType.refine(LinkedList.class, bindings, superClass, superInts);
 }

 @Test
 public void testRefineWithValidType() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 JavaType refinedType = collectionType.refine(ArrayList.class, bindings, superClass, superInts);
 assertEquals("java.util.ArrayList<java.lang.Integer>", refinedType.toString());
 }
}