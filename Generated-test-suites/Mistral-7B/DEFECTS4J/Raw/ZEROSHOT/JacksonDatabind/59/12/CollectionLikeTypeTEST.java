import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

 @Test
 public void testCollectionLikeTypeConstruction() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.constructCollectionType(List.class, Integer.class), TypeBindings.emptyBindings(),
 null, null, JavaType.constructNumberType(Integer.class));
 assertEquals(List.class, collectionLikeType.getRawClass());
 assertEquals(Integer.class, collectionLikeType.getContentType().getRawClass());
 }

 @Test
 public void testCollectionLikeTypeUpgradeFrom() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.class);
 assertEquals(ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(Integer.class, collectionLikeType.getContentType().getRawClass());
 }

 @Test
 public void testCollectionLikeTypeIsContainerType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.constructCollectionType(List.class, Integer.class), TypeBindings.emptyBindings(),
 null, null, JavaType.constructNumberType(Integer.class));
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testCollectionLikeTypeIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.constructCollectionType(List.class, Integer.class), TypeBindings.emptyBindings(),
 null, null, JavaType.constructNumberType(Integer.class));
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testCollectionLikeTypeGetContentType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.constructCollectionType(List.class, Integer.class), TypeBindings.emptyBindings(),
 null, null, JavaType.constructNumberType(Integer.class));
 assertEquals(JavaType.constructNumberType(Integer.class), collectionLikeType.getContentType());
 }

 @Test
 public void testCollectionLikeTypeEquals() {
 CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(
 JavaType.constructCollectionType(List.class, Integer.class), TypeBindings.emptyBindings(),
 null, null, JavaType.constructNumberType(Integer.class));
 CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(
 JavaType.constructCollectionType(List.class, Integer.class), TypeBindings.emptyBindings(),
 null, null, JavaType.constructNumberType(Integer.class));
 CollectionLikeType collectionLikeType3 = CollectionLikeType.construct(
 JavaType.constructCollectionType(ArrayList.class, Integer.class), TypeBindings.emptyBindings(),
 null, null, JavaType.constructNumberType(Integer.class));

 assertEquals(collectionLikeType1, collectionLikeType2);
 assertNotEquals(collectionLikeType1, collectionLikeType3);
 }

 @Test
 public void testCollectionLikeTypeToString() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.constructCollectionType(List.class, Integer.class), TypeBindings.emptyBindings(),
 null, null, JavaType.constructNumberType(Integer.class));
 assertEquals("[collection-like type; class List, contains class java.lang.Integer]", collectionLikeType.toString());
 }

 @Test
 public void testCollectionLikeTypeIsTrueCollectionType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.constructCollectionType(List.class, Integer.class), TypeBindings.emptyBindings(),
 null, null, JavaType.constructNumberType(Integer.class));
 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollectionLikeTypeUpgradeFromInvalidBaseType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 CollectionLikeType.upgradeFrom(javaType, Integer.class);
 }
}