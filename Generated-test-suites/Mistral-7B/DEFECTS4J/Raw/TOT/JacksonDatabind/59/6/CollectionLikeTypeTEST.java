import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;

public class CollectionLikeTypeTest {

 @Test
 public void testConstruct() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class));
 assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType javaType = JavaType.constructRawListType(String.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
 assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
 }

 @Test
 public void testGetContentType() {
 JavaType javaType = JavaType.constructRawListType(String.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertEquals(JavaType.constructRawListType(String.class), collectionLikeType.getContentType());
 }

 @Test
 public void testIsCollectionLikeType() {
 JavaType javaType = JavaType.constructRawListType(String.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testIsContainerType() {
 JavaType javaType = JavaType.constructRawListType(String.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testIsTrueCollectionType() {
 JavaType javaType = java.util.ArrayList.class;
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testGetErasedSignature() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertEquals("[Ljava.lang.Object;", collectionLikeType.getErasedSignature().toString());
 }

 @Test
 public void testGetGenericSignature() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertEquals("java.util.ArrayList<Ljava.lang.String;>", collectionLikeType.getGenericSignature().toString());
 }

 @Test
 public void testToString() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertEquals("[collection-like type; class java.util.ArrayList, contains java.lang.String]", collectionLikeType.toString());
 }

 @Test
 public void testNarrow() {
 JavaType javaType = JavaType.constructRawListType(Map.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(Map.class), null, null, false);
 JavaType narrowedType = collectionLikeType.narrow(java.util.HashMap.class);
 assertEquals(MapType.class, narrowedType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithInvalidType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 collectionLikeType._narrow(int.class);
 }

 @Test
 public void testWithContentType() {
 JavaType javaType = JavaType.constructRawListType(String.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(JavaType.constructRawListType(Integer.class));
 assertNotSame(collectionLikeType, newCollectionLikeType);
 assertEquals(java.util.List.class, newCollectionLikeType.getRawClass());
 assertEquals(JavaType.constructRawListType(Integer.class), newCollectionLikeType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 TypeDeserializer typeDeserializer = new MapTypeDeserializer();
 CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(typeDeserializer);
 assertNotSame(collectionLikeType, newCollectionLikeType);
 assertEquals(typeDeserializer, newCollectionLikeType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 TypeDeserializer typeDeserializer = new MapTypeDeserializer();
 collectionLikeType = collectionLikeType.withContentTypeHandler(typeDeserializer);
 assertNotSame(collectionLikeType, collectionLikeType);
 assertEquals(typeDeserializer, collectionLikeType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 TypeDeserializer typeDeserializer = new MapTypeDeserializer();
 collectionLikeType = collectionLikeType.withValueHandler(typeDeserializer);
 assertNotSame(collectionLikeType, collectionLikeType);
 assertEquals(typeDeserializer, collectionLikeType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 TypeDeserializer typeDeserializer = new MapTypeDeserializer();
 collectionLikeType = collectionLikeType.withContentValueHandler(typeDeserializer);
 assertNotSame(collectionLikeType, collectionLikeType);
 assertEquals(typeDeserializer, collectionLikeType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
 assertNotSame(collectionLikeType, newCollectionLikeType);
 assertTrue(newCollectionLikeType.isStaticTyping());
 }

 @Test
 public void testRefine() {
 JavaType javaType = JavaType.constructRawListType(String.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 JavaType refinedType = collectionLikeType.refine(java.util.LinkedList.class, TypeBindings.emptyBindings(), null, null);
 assertEquals(java.util.LinkedList.class, refinedType.getRawClass());
 }
}