import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

 @Test
 public void testConstruct() {
 Class<Collection<Integer>> collectionType = Collection.class;
 JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(Collection.class, Integer.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(collectionType, TypeBindings.emptyBindings(), javaType.getRawClass(), null, javaType);
 assertEquals(collectionType, collectionLikeType.getRawClass());
 assertEquals(Integer.class, collectionLikeType.getContentType());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
 JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
 assertEquals(collectionType, collectionLikeType.getRawClass());
 assertEquals(String.class, collectionLikeType.getContentType());
 }

 @Test
 public void testIsContainerTypeAndIsCollectionLikeType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType.getRawClass()), null, javaType.getComponentType(), null, null, false);
 assertTrue(collectionLikeType.isContainerType());
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType.getRawClass()), null, javaType.getComponentType(), null, null, false);
 assertEquals(String.class, collectionLikeType.getContentType());
 }

 @Test
 public void testIsTrueCollectionType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType.getRawClass()), null, javaType.getComponentType(), null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());

 JavaType notCollectionType = TypeFactory.defaultInstance().constructType(Map.class);
 CollectionLikeType notCollectionLikeType = new CollectionLikeType(notCollectionType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(notCollectionType.getRawClass()), null, notCollectionType.getComponentType(), null, null, false);
 assertFalse(notCollectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testEquals() {
 JavaType javaType1 = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class);
 CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType1.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType1.getRawClass()), null, javaType1.getComponentType(), null, null, false);
 CollectionLikeType collectionLikeType2 = new CollectionLikeType(javaType2.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType2.getRawClass()), null, javaType2.getComponentType(), null, null, false);
 assertTrue(collectionLikeType1.equals(collectionLikeType2));

 JavaType javaType3 = TypeFactory.defaultInstance().constructType(HashSet.class);
 CollectionLikeType collectionLikeType3 = new CollectionLikeType(javaType3.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType3.getRawClass()), null, javaType3.getComponentType(), null, null, false);
 assertFalse(collectionLikeType1.equals(collectionLikeType3));
 }

 @Test
 public void testToString() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType.getRawClass()), null, javaType.getComponentType(), null, null, false);
 String expected = "[collection-like type; class java.util.ArrayList, contains class java.lang.Integer]";
 String actual = collectionLikeType.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidBaseType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Object.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null);
 fail("Should not reach this point");
 }
}