import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.ClassIdMapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct_ValidArguments() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType elemType = String.class;
 JavaType superClass = List.class;
 JavaType[] superInterfaces = new JavaType[] {Collection.class};
 CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemType);
 assertNotNull(collectionType);
 assertSame(List.class, collectionType.getRawClass());
 assertSame(bindings, collectionType.getTypeBindings());
 assertSame(superClass, collectionType.getSuperClass());
 assertArrayEquals(new JavaType[] {superInterfaces[0]}, collectionType.getSuperInterfaces());
 assertSame(elemType, collectionType.getElementType());
 }

 @Test
 public void testConstruct_InvalidSuperClass() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType elemType = String.class;
 JavaType superClass = Object.class; // This is not a valid superclass for a CollectionType
 JavaType[] superInterfaces = new JavaType[] {Collection.class};
 CollectionType collectionType = null; // Expect null as a result of the invalid constructor call
 try {
 collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemType);
 } catch (IllegalArgumentException e) {
 collectionType = e;
 }
 assertNotNull(collectionType); // The IllegalArgumentException should have been thrown, so we have a non-null result
 assertSame(IllegalArgumentException.class, collectionType.getClass());
 }

 @Test
 public void testWithContentType_ValidArguments() {
 CollectionType collectionType = new CollectionType(ArrayList.class, null, List.class, new JavaType[0], String.class);
 JavaType contentType = Integer.class;
 CollectionType newCollectionType = collectionType.withContentType(contentType);
 assertNotNull(newCollectionType);
 assertSame(ArrayList.class, newCollectionType.getRawClass());
 assertSame(new TypeBindings(), newCollectionType.getTypeBindings());
 assertSame(List.class, newCollectionType.getSuperClass());
 assertArrayEquals(new JavaType[] {Collection.class}, newCollectionType.getSuperInterfaces());
 assertSame(contentType, newCollectionType.getElementType());
 }

 @Test
 public void testWithTypeHandler_ValidArguments() {
 CollectionType collectionType = new CollectionType(ArrayList.class, null, List.class, new JavaType[0], String.class);
 TypeHandler typeHandler = mock(TypeHandler.class);
 CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
 assertNotNull(newCollectionType);
 assertSame(ArrayList.class, newCollectionType.getRawClass());
 assertSame(new TypeBindings(), newCollectionType.getTypeBindings());
 assertSame(List.class, newCollectionType.getSuperClass());
 assertArrayEquals(new JavaType[] {Collection.class}, newCollectionType.getSuperInterfaces());
 assertSame(typeHandler, newCollectionType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler_ValidArguments() {
 CollectionType collectionType = new CollectionType(ArrayList.class, null, List.class, new JavaType[0], String.class);
 ContentTypeHandler contentTypeHandler = mock(ContentTypeHandler.class);
 CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);
 assertNotNull(newCollectionType);
 assertSame(ArrayList.class, newCollectionType.getRawClass());
 assertSame(new TypeBindings(), newCollectionType.getTypeBindings());
 assertSame(List.class, newCollectionType.getSuperClass());
 assertArrayEquals(new JavaType[] {Collection.class}, newCollectionType.getSuperInterfaces());
 assertSame(contentTypeHandler, newCollectionType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler_ValidArguments() {
 CollectionType collectionType = new CollectionType(ArrayList.class, null, List.class, new JavaType[0], String.class);
 ValueHandler valueHandler = mock(ValueHandler.class);
 CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
 assertNotNull(newCollectionType);
 assertSame(ArrayList.class, newCollectionType.getRawClass());
 assertSame(new TypeBindings(), newCollectionType.getTypeBindings());
 assertSame(List.class, newCollectionType.getSuperClass());
 assertArrayEquals(new JavaType[] {Collection.class}, newCollectionType.getSuperInterfaces());
 assertSame(valueHandler, newCollectionType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler_ValidArguments() {
 CollectionType collectionType = new CollectionType(ArrayList.class, null, List.class, new JavaType[0], String.class);
 ContentValueHandler contentValueHandler = mock(ContentValueHandler.class);
 CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
 assertNotNull(newCollectionType);
 assertSame(ArrayList.class, newCollectionType.getRawClass());
 assertSame(new TypeBindings(), newCollectionType.getTypeBindings());
 assertSame(List.class, newCollectionType.getSuperClass());
 assertArrayEquals(new JavaType[] {Collection.class}, newCollectionType.getSuperInterfaces());
 assertSame(contentValueHandler, newCollectionType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping_ValidArguments() {
 CollectionType collectionType = new CollectionType(ArrayList.class, null, List.class, new JavaType[0], String.class, false);
 CollectionType newCollectionType = collectionType.withStaticTyping();
 assertNotNull(newCollectionType);
 assertSame(ArrayList.class, newCollectionType.getRawClass());
 assertSame(new TypeBindings(), newCollectionType.getTypeBindings());
 assertSame(List.class, newCollectionType.getSuperClass());
 assertArrayEquals(new JavaType[] {Collection.class}, newCollectionType.getSuperInterfaces());
 assertSame(String.class, newCollectionType.getElementType());
 assertTrue(newCollectionType.isStaticTyping());
 }

 @Test
 public void testRefine_ValidArguments() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType elemType = String.class;
 JavaType superClass = List.class;
 JavaType[] superInterfaces = new JavaType[] {Collection.class};
 CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemType);
 JavaType refinedType = IntegerType.class; // A refined type that implements Collection and List interfaces
 CollectionType refinedCollectionType = collectionType.refine(rawType, bindings, refinedType.getRawClass(), refinedType.getSuperInterfaces());
 assertNotNull(refinedCollectionType);
 assertSame(IntegerType.class, refinedCollectionType.getRawClass());
 assertSame(bindings, refinedCollectionType.getTypeBindings());
 assertSame(List.class, refinedCollectionType.getSuperClass());
 assertArrayEquals(new JavaType[] {Collection.class, List.class}, refinedCollectionType.getSuperInterfaces());
 assertSame(elemType, refinedCollectionType.getElementType());
 }
}