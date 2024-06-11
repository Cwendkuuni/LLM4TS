import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[]{Object.class};
 JavaType elemT = Integer.class;
 CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
 assertEquals(ArrayList.class, collectionType.getClass());
 assertEquals(Integer.class, collectionType.getComponentType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> listType = ArrayList.class;
 JavaType elemT = Integer.class;
 CollectionType collectionType = CollectionType.construct(listType, elemT);
 assertEquals(ArrayList.class, collectionType.getClass());
 assertEquals(Integer.class, collectionType.getComponentType());
 }

 @Test
 public void testWithContentType() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[]{Object.class};
 JavaType elemT = Integer.class;
 JavaType contentType = IntArrayList.class;
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
 CollectionType withContentType = collectionType.withContentType(contentType);
 assertEquals(IntArrayList.class, withContentType.getClass());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[]{Object.class};
 JavaType elemT = Integer.class;
 Object typeHandler = new IntCollectionTypeHandler();
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
 CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
 assertNotNull(withTypeHandler.getTypeHandler());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidWithContentType() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[]{Object.class};
 JavaType elemT = Integer.class;
 JavaType contentType = String.class;
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
 CollectionType withContentType = collectionType.withContentType(contentType);
 }

 @Test
 public void testWithValueHandler() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[]{Object.class};
 JavaType elemT = Integer.class;
 Object valueHandler = new IntCollectionValueHandler();
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
 CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
 assertNotNull(withValueHandler.getValueHandler());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidWithValueHandler() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[]{Object.class};
 JavaType elemT = Integer.class;
 Object valueHandler = new StringCollectionValueHandler();
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
 CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[]{Object.class};
 JavaType elemT = Integer.class;
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
 CollectionType withStaticTyping = collectionType.withStaticTyping();
 assertTrue(withStaticTyping.isStatic());
 }

 @Test
 public void testRefine() {
 Class<?> listType = ArrayList.class;
 TypeBindings bindings = TypeBindings.getDefaultInstance();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[]{Object.class};
 JavaType elemT = Integer.class;
 CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
 JavaType refinedType = collectionType.refine(LinkedList.class, bindings, superClass, superInts);
 assertEquals(LinkedList.class, refinedType.getRawClass());
 }
}