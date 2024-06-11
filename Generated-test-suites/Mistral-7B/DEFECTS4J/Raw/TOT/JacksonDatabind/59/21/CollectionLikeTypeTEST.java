import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

 // Test for construct method with Class, TypeBindings, JavaType
 @Test
 public void testConstruct() {
 Class<?> rawType = Collection.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 assertNotNull(collectionLikeType);
 assertEquals(rawType, collectionLikeType.getClass());
 assertEquals(elemT, collectionLikeType.getContentType());
 }

 // Test for construct method with Class, JavaType
 @Test
 public void testConstructDeprecated() {
 Class<?> rawType = Collection.class;
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
 assertNotNull(collectionLikeType);
 assertEquals(rawType, collectionLikeType.getClass());
 assertEquals(elemT, collectionLikeType.getContentType());
 }

 // Test for upgradeFrom method
 @Test
 public void testUpgradeFrom() {
 JavaType baseType = JavaType.constructType(ArrayList.class);
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemT);
 assertNotNull(collectionLikeType);
 assertEquals(baseType.getRawClass(), collectionLikeType.getClass().getSuperclass());
 assertEquals(elemT, collectionLikeType.getContentType());
 }

 // Test for isTrueCollectionType method
 @Test
 public void testIsTrueCollectionType() {
 Class<?> rawType = ArrayList.class;
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, null, null, null, null);
 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 // Test for isTrueCollectionType method with a non-Collection type
 @Test
 public void testIsTrueCollectionTypeFalse() {
 Class<?> rawType = String.class;
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, null, null, null, null);
 assertFalse(collectionLikeType.isTrueCollectionType());
 }

 // Test for equals method
 @Test
 public void testEquals() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 CollectionLikeType collectionLikeType3 = CollectionLikeType.construct(LinkedList.class, bindings, null, null, elemT);
 assertEquals(collectionLikeType1, collectionLikeType2);
 assertNotEquals(collectionLikeType1, collectionLikeType3);
 }

 // Test for toString method
 @Test
 public void testToString() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 String expected = "[collection-like type; class " + rawType.getName() + ", contains " + elemT + "]";
 assertEquals(expected, collectionLikeType.toString());
 }

 // Test for getErasedSignature method
 @Test
 public void testGetErasedSignature() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 StringBuilder sb = new StringBuilder();
 collectionLikeType.getErasedSignature(sb);
 String expected = "[C]";
 assertEquals(expected, sb.toString());
 }

 // Test for getGenericSignature method
 @Test
 public void testGetGenericSignature() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 StringBuilder sb = new StringBuilder();
 collectionLikeType.getGenericSignature(sb);
 String expected = "[Ljava.util.ArrayList;<0>;Ljava/lang/Number;]";
 assertEquals(expected, sb.toString());
 }

 // Test for buildCanonicalName method
 @Test
 public void testBuildCanonicalName() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 String expected = "[collection-like type; class java.util.ArrayList, contains class java.lang.Number]";
 assertEquals(expected, collectionLikeType.buildCanonicalName());
 }

 // Test for isContainerType method
 @Test
 public void testIsContainerType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, null, null, null, null);
 assertTrue(collectionLikeType.isContainerType());
 }

 // Test for isCollectionLikeType method
 @Test
 public void testIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, null, null, null, null);
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 // Test for getContentType method
 @Test
 public void testGetContentType() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 assertEquals(elemT, collectionLikeType.getContentType());
 }

 // Test for getContentValueHandler method
 @Test
 public void testGetContentValueHandler() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 assertNull(collectionLikeType.getContentValueHandler());
 }

 // Test for getContentTypeHandler method
 @Test
 public void testGetContentTypeHandler() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 assertNull(collectionLikeType.getContentTypeHandler());
 }

 // Test for hasHandlers method
 @Test
 public void testHasHandlers() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemT);
 assertFalse(collectionLikeType.hasHandlers());
 }

 // Test for refine method
 @Test
 public void testRefine() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = null;
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 CollectionLikeType refinedCollectionLikeType = collectionLikeType.refine(LinkedList.class, bindings, null, null);
 assertNotNull(refinedCollectionLikeType);
 assertEquals(LinkedList.class, refinedCollectionLikeType.getClass());
 assertEquals(elemT, refinedCollectionLikeType.getContentType());
 }

 // Test for withContentType method
 @Test
 public void testWithContentType() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = null;
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 JavaType newContentType = JavaType.constructParametrizedType(ArrayList.class, String.class);
 CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(newContentType);
 assertEquals(newContentType, newCollectionLikeType.getContentType());
 }

 // Test for withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = null;
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 Object handler = new Object() {};
 CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(handler);
 assertNotNull(newCollectionLikeType.getTypeHandler());
 assertEquals(handler, newCollectionLikeType.getTypeHandler());
 }

 // Test for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = null;
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 Object handler = new Object() {};
 CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(handler);
 assertNotNull(newCollectionLikeType.getValueHandler());
 assertEquals(handler, newCollectionLikeType.getValueHandler());
 }

 // Test for withValueHandler method
 @Test
 public void testWithValueHandler() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = null;
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 Object handler = new Object() {};
 CollectionLikeType newCollectionLikeType = collectionLikeType.withValueHandler(handler);
 assertNotNull(newCollectionLikeType.getValueHandler());
 assertEquals(handler, newCollectionLikeType.getValueHandler());
 }

 // Test for withContentValueHandler method
 @Test
 public void testWithContentValueHandler() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = null;
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 Object handler = new Object() {};
 CollectionLikeType newCollectionLikeType = collectionLikeType.withContentValueHandler(handler);
 assertNotNull(newCollectionLikeType.getValueHandler());
 assertEquals(handler, newCollectionLikeType.getValueHandler());
 }

 // Test for withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = null;
 JavaType elemT = JavaType.constructParametrizedType(ArrayList.class, Number.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemT);
 CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
 assertTrue(newCollectionLikeType.isStaticTyping());
 }
}