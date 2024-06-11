import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstructWithValidArguments() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {Collection.class};
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

 assertTrue(collectionType instanceof CollectionType);
 assertSame(listClass, collectionType.getType());
 assertSame(bindings, collectionType.getBindings());
 assertSame(superClass, collectionType.getSuperClass());
 assertArrayEquals(superInterfaces, collectionType.getSuperInterfaces());
 assertSame(elemT, collectionType.getContentType());
 }

 @Test
 public void testConstructWithDeprecatedMethodAndValidArguments() {
 Class<List<Integer>> listClass = List.class;
 JavaType elemT = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, elemT);

 assertTrue(collectionType instanceof CollectionType);
 assertSame(listClass, collectionType.getType());
 assertNull(collectionType.getBindings()); // null by default
 assertSame(Collection.class, collectionType.getSuperClass());
 assertNull(collectionType.getSuperInterfaces());
 assertSame(elemT, collectionType.getContentType());
 }

 @Test
 public void testNarrowWithValidArguments() {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 CollectionType listType = new CollectionType(List.class, null, Collection.class, null, Integer.TYPE, null, null, false);
 CollectionType arrayListType = listType.narrow(arrayListClass);

 assertTrue(arrayListType instanceof CollectionType);
 assertSame(arrayListClass, arrayListType.getType());
 assertSame(listType.getBindings(), arrayListType.getBindings());
 assertSame(listType.getSuperClass(), arrayListType.getSuperClass());
 assertArrayEquals(listType.getSuperInterfaces(), arrayListType.getSuperInterfaces());
 assertSame(listType.getContentType(), arrayListType.getContentType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowWithInvalidSubclass() {
 Class<ArrayList<String>> arrayListClass = ArrayList.class;
 CollectionType listType = new CollectionType(List.class, null, Collection.class, null, Integer.TYPE, null, null, false);
 listType.narrow(arrayListClass);
 }

 @Test
 public void testWithContentTypeWithValidArguments() {
 JavaType stringType = String.class;
 CollectionType listType = new CollectionType(List.class, null, Collection.class, null, Integer.TYPE, null, null, false);
 CollectionType stringListType = listType.withContentType(stringType);

 assertTrue(stringListType instanceof CollectionType);
 assertSame(List.class, stringListType.getType());
 assertSame(listType.getBindings(), stringListType.getBindings());
 assertSame(listType.getSuperClass(), stringListType.getSuperClass());
 assertArrayEquals(listType.getSuperInterfaces(), stringListType.getSuperInterfaces());
 assertSame(stringType, stringListType.getContentType());
 }

 @Test
 public void testWithContentTypeHandlerWithValidArguments() {
 Object stringHandler = new Object();
 CollectionType listType = new CollectionType(List.class, null, Collection.class, null, Integer.TYPE, null, null, false);
 CollectionType stringListType = listType.withContentTypeHandler(stringHandler);

 assertTrue(stringListType instanceof CollectionType);
 assertSame(List.class, stringListType.getType());
 assertSame(listType.getBindings(), stringListType.getBindings());
 assertSame(listType.getSuperClass(), stringListType.getSuperClass());
 assertArrayEquals(listType.getSuperInterfaces(), stringListType.getSuperInterfaces());
 assertSame(listType.getContentType(), stringListType.getContentType());
 assertSame(stringHandler, stringListType.getContentTypeHandler());
 }

 @Test
 public void testWithTypeHandlerWithValidArguments() {
 Object handler = new Object();
 CollectionType listType = new CollectionType(List.class, null, Collection.class, null, Integer.TYPE, null, null, false);
 CollectionType handlerListType = listType.withTypeHandler(handler);

 assertTrue(handlerListType instanceof CollectionType);
 assertSame(List.class, handlerListType.getType());
 assertSame(listType.getBindings(), handlerListType.getBindings());
 assertSame(listType.getSuperClass(), handlerListType.getSuperClass());
 assertArrayEquals(listType.getSuperInterfaces(), handlerListType.getSuperInterfaces());
 assertSame(listType.getContentType(), handlerListType.getContentType());
 assertSame(handler, handlerListType.getTypeHandler());
 }

 @Test
 public void testWithStaticTyping() {
 CollectionType listType = new CollectionType(List.class, null, Collection.class, null, Integer.TYPE, null, null, false);
 CollectionType staticListType = listType.withStaticTyping();

 assertTrue(staticListType instanceof CollectionType);
 assertSame(List.class, staticListType.getType());
 assertSame(listType.getBindings(), staticListType.getBindings());
 assertSame(listType.getSuperClass(), staticListType.getSuperClass());
 assertArrayEquals(listType.getSuperInterfaces(), staticListType.getSuperInterfaces());
 assertSame(listType.getContentType(), staticListType.getContentType());
 assertTrue(staticListType.isStatic());
 }

 @Test
 public void testRefineWithValidArguments() {
 Class<LinkedList<Integer>> linkedListClass = LinkedList.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {Collection.class};
 CollectionType listType = new CollectionType(List.class, null, Collection.class, null, Integer.TYPE, null, null, false);
 CollectionType linkedListType = listType.refine(linkedListClass, bindings, superClass, superInterfaces);

 assertTrue(linkedListType instanceof CollectionType);
 assertSame(linkedListClass, linkedListType.getType());
 assertSame(bindings, linkedListType.getBindings());
 assertSame(superClass, linkedListType.getSuperClass());
 assertArrayEquals(superInterfaces, linkedListType.getSuperInterfaces());
 assertSame(Integer.TYPE, linkedListType.getContentType());
 }

}