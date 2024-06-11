import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializerBinding;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstructWithValidArguments() {
 Class<?> rawType = List.class;
 TypeBindings bindings = null; // not used in this test case
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = IntegerType.getInstance();
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(elemT, collectionType.getElementType());
 assertFalse(collectionType.isStatic());
 }

 @Test
 public void testConstructWithDeprecatedArguments() {
 Class<?> rawType = List.class;
 JavaType elemT = IntegerType.getInstance();
 CollectionType collectionType = CollectionType.construct(rawType, elemT);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(elemT, collectionType.getElementType());
 assertFalse(collectionType.isStatic());
 }

 @Test
 public void testWithContentType() {
 Class<?> rawType = List.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = IntegerType.getInstance();
 JavaType contentType = LongType.getInstance();
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 JavaType result = collectionType.withContentType(contentType);
 assertEquals(collectionType.getRawClass(), result.getRawClass());
 assertEquals(contentType, result.getElementType());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> rawType = List.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = IntegerType.getInstance();
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 Object contentTypeHandler = new Object() {}; // any handler can be used here for testing
 JavaType result = collectionType.withContentTypeHandler(contentTypeHandler);
 assertEquals(collectionType.getRawClass(), result.getRawClass());
 assertEquals(elemT, result.getElementType());
 assertNull(result.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> rawType = List.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = IntegerType.getInstance();
 Object valueHandler = new Object() {}; // any handler can be used here for testing
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 JavaType result = collectionType.withValueHandler(valueHandler);
 assertEquals(collectionType.getRawClass(), result.getRawClass());
 assertEquals(elemT, result.getElementType());
 assertNull(result.getValueHandler());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> rawType = List.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = IntegerType.getInstance();
 Object typeHandler = new Object() {}; // any handler can be used here for testing
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 JavaType result = collectionType.withTypeHandler(typeHandler);
 assertEquals(collectionType.getRawClass(), result.getRawClass());
 assertEquals(elemT, result.getElementType());
 assertSame(typeHandler, result.getTypeHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> rawType = List.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = IntegerType.getInstance();
 Object contentValueHandler = new Object() {}; // any handler can be used here for testing
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 JavaType result = collectionType.withContentValueHandler(contentValueHandler);
 assertEquals(collectionType.getRawClass(), result.getRawClass());
 assertEquals(elemT, result.getElementType());
 assertNull(result.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> rawType = List.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = IntegerType.getInstance();
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 JavaType result = collectionType.withStaticTyping();
 assertEquals(collectionType.getRawClass(), result.getRawClass());
 assertEquals(elemT, result.getElementType());
 assertTrue(result.isStatic());
 }

 @Test
 public void testRefine() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = null;
 JavaType superClass = ArrayList.class;
 JavaType[] superInterfaces = new JavaType[] {Collection.class};
 JavaType elemT = IntegerType.getInstance();
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 JavaType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(ArrayList.class, result.getRawClass());
 assertEquals(elemT, result.getElementType());
 assertFalse(result.isStatic());
 }

 @Test
 public void testToString() {
 Class<?> rawType = List.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = IntegerType.getInstance();
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 String toStringResult = collectionType.toString();
 String expectedString = "[collection type; class " + List.class.getName() + ", contains " + elemT + "]";
 assertEquals(expectedString, toStringResult);
 }

 @Test
 public void testConstructWithInvalidArguments() {
 Class<?> rawType = null;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = IntegerType.getInstance();
 try {
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testConstructWithInvalidElementType() {
 Class<?> rawType = List.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = Object.class;
 try {
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNarrowWithInvalidSubclass() {
 CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, IntegerType.getInstance());
 try {
 JavaType narrowResult = collectionType._narrow(LinkedList.class);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }
}