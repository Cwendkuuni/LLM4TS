import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollectionTypeTest {

 private JavaType elemType;
 private CollectionType collectionType;
 private JavaType superClass;
 private JavaType[] superInterfaces;

 @Test
 public void testConstructor() {
 Class<?> rawType = mock(Class.class);
 TypeBindings bindings = mock(TypeBindings.class);
 elemType = mock(JavaType.class);
 superClass = mock(JavaType.class);
 superInterfaces = new JavaType[0];

 collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemType);

 Assert.assertEquals(rawType, collectionType.getRawClass());
 Assert.assertSame(bindings, collectionType.getTypeBindings());
 Assert.assertSame(superClass, collectionType.getSuperClass());
 Assert.assertArrayEquals(superInterfaces, collectionType.getSuperInterfaces());
 Assert.assertSame(elemType, collectionType.getContentType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> rawType = mock(Class.class);
 JavaType elemType = mock(JavaType.class);

 collectionType = new CollectionType(rawType, elemType);

 Assert.assertEquals(rawType, collectionType.getRawClass());
 Assert.assertNull(collectionType.getTypeBindings());
 Assert.assertSame(CollectionType._bogusSuperClass(rawType), collectionType.getSuperClass());
 Assert.assertNull(collectionType.getSuperInterfaces());
 Assert.assertSame(elemType, collectionType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructNullBindings() {
 Class<?> rawType = mock(Class.class);
 JavaType elemType = mock(JavaType.class);
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInterfaces = new JavaType[0];

 collectionType = new CollectionType(rawType, null, superClass, superInterfaces, elemType);
 }

 @Test
 public void testNarrow() {
 Class<?> subclass = mock(Class.class);
 CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, elemType);

 when(collectionType._narrow(subclass)).thenReturn(mock(CollectionType.class));

 JavaType actual = collectionType.narrow(subclass);

 Assert.assertSame(mock(CollectionType.class), actual);
 }

 @Test
 public void testWithContentType() {
 JavaType contentType = mock(JavaType.class);
 CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, elemType);

 JavaType actual = collectionType.withContentType(contentType);

 Assert.assertNotSame(collectionType, actual);
 Assert.assertSame(contentType, actual.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Object typeHandler = mock(Object.class);
 CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, elemType);

 CollectionType actual = collectionType.withTypeHandler(typeHandler);

 Assert.assertNotSame(collectionType, actual);
 }

 @Test
 public void testWithContentTypeHandler() {
 Object contentTypeHandler = mock(Object.class);
 Object typeHandler = mock(Object.class);
 CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, elemType);

 JavaType elemTypeWithContentTypeHandler = new SimpleType(contentTypeHandler.getClass());

 CollectionType actual = collectionType.withContentTypeHandler(contentTypeHandler);

 Assert.assertNotSame(collectionType, actual);
 Assert.assertSame(elemTypeWithContentTypeHandler, actual.getContentType());
 }

 @Test
 public void testWithValueHandler() {
 Object valueHandler = mock(Object.class);
 CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, elemType);

 CollectionType actual = collectionType.withValueHandler(valueHandler);

 Assert.assertNotSame(collectionType, actual);
 }

 @Test
 public void testWithContentValueHandler() {
 Object contentValueHandler = mock(Object.class);
 Object valueHandler = mock(Object.class);
 CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, elemType);

 JavaType elemTypeWithContentValueHandler = new SimpleType(contentValueHandler.getClass());

 CollectionType actual = collectionType.withContentValueHandler(contentValueHandler);

 Assert.assertNotSame(collectionType, actual);
 Assert.assertSame(elemTypeWithContentValueHandler, actual.getContentType());
 }

 @Test
 public void testWithStaticTyping() {
 CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, elemType);

 CollectionType actual = collectionType.withStaticTyping();

 Assert.assertNotSame(collectionType, actual);
 Assert.assertTrue(actual.isStaticTyping());
 }

 @Test
 public void testRefine() {
 Class<?> rawType = mock(Class.class);
 TypeBindings bindings = mock(TypeBindings.class);
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInterfaces = new JavaType[0];

 CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, elemType);

 JavaType actual = collectionType.refine(rawType, bindings, superClass, superInterfaces);

 Assert.assertNotSame(collectionType, actual);
 Assert.assertSame(rawType, actual.getRawClass());
 Assert.assertSame(bindings, actual.getTypeBindings());
 Assert.assertSame(superClass, actual.getSuperClass());
 Assert.assertArrayEquals(superInterfaces, actual.getSuperInterfaces());
 Assert.assertSame(elemType, actual.getContentType());
 }

 @Test
 public void testToString() {
 Class<?> rawType = mock(Class.class);
 TypeBindings bindings = mock(TypeBindings.class);
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemType = mock(JavaType.class);

 CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemType);

 String actual = collectionType.toString();

 String expected = "[collection type; class " + rawType.getName() + ", contains " + elemType + "]";
 Assert.assertEquals(expected, actual);
 }
}