import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDescriptor;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeBindingsBuilder;

public class CollectionLikeTypeTest {

 @Test
 public void testConstruct() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 assertEquals(java.util.ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType javaType = JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
 assertEquals(java.util.List.class, collectionLikeType.getRawClass());
 assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
 }

 @Test
 public void testRefine() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 JavaType refinedType = collectionLikeType.refine(java.util.LinkedList.class, TypeBindings.emptyBindings(), null, null);
 assertEquals(java.util.LinkedList.class, refinedType.getRawClass());
 assertEquals(String.class, refinedType.getContentType().getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidType() {
 JavaType javaType = JavaType.constructCollectionType(JavaType.constructRawType(java.util.Map.class), String.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testIsTrueCollectionType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 assertEquals(JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class), collectionLikeType.getContentType());
 }

 @Test
 public void testHasHandlers() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 assertTrue(collectionLikeType.hasHandlers());
 }

 @Test
 public void testWithContentType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 JavaType newContentType = JavaType.constructCollectionType(JavaType.constructRawType(java.util.Set.class), Integer.class);
 CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(newContentType);
 assertEquals(newContentType, newCollectionLikeType.getContentType());
 assertEquals(java.util.ArrayList.class, newCollectionLikeType.getRawClass());
 }

 @Test
 public void testWithTypeHandler() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 Object handler = new Object();
 CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(handler);
 assertEquals(handler, newCollectionLikeType.getTypeHandler());
 assertEquals(java.util.ArrayList.class, newCollectionLikeType.getRawClass());
 }

 @Test
 public void testWithContentTypeHandler() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 Object handler = new Object();
 JavaType newContentType = JavaType.constructCollectionType(JavaType.constructRawType(java.util.Set.class), Integer.class);
 CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(handler);
 assertEquals(handler, newCollectionLikeType.getContentTypeHandler());
 assertEquals(newContentType, newCollectionLikeType.getContentType());
 }

 @Test
 public void testWithValueHandler() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 Object handler = new Object();
 CollectionLikeType newCollectionLikeType = collectionLikeType.withValueHandler(handler);
 assertEquals(handler, newCollectionLikeType.getValueHandler());
 assertEquals(java.util.ArrayList.class, newCollectionLikeType.getRawClass());
 }

 @Test
 public void testWithContentValueHandler() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 Object handler = new Object();
 JavaType newContentType = JavaType.constructCollectionType(JavaType.constructRawType(java.util.Set.class), Integer.class);
 CollectionLikeType newCollectionLikeType = collectionLikeType.withContentValueHandler(handler);
 assertEquals(handler, newCollectionLikeType.getContentValueHandler());
 assertEquals(newContentType, newCollectionLikeType.getContentType());
 }

 @Test
 public void testWithStaticTyping() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(JavaType.constructRawType(java.util.List.class), String.class));
 CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
 assertTrue(newCollectionLikeType.isStatic());
 }
}