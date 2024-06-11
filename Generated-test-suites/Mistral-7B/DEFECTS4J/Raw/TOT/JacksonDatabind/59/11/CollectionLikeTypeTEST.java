import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

 @Test
 public void testConstructWithCollection() {
 Class<?> collectionType = java.util.ArrayList.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superclass = _bogusSuperClass(collectionType);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = JavaType.construct(int.class);

 CollectionLikeType collectionLikeType = CollectionLikeType.construct(collectionType, bindings, superclass, superInterfaces, elemT);

 assertEquals(collectionType, collectionLikeType.getRawType());
 assertEquals(elemT, collectionLikeType.getContentType());
 }

 @Test
 public void testConstructWithTypeVariable() {
 Class<?> rawType = new ArrayList<>().getClass();
 JavaType elemT = JavaType.construct(int.class);
 TypeBindings bindings = TypeBindings.create(rawType, elemT);
 JavaType superclass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = new JavaType[0];

 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superclass, superInterfaces, elemT);

 assertEquals(rawType, collectionLikeType.getRawType());
 assertEquals(elemT, collectionLikeType.getContentType());
 }

 @Test
 public void testUpgradeFromSimpleType() {
 JavaType baseType = JavaType.construct(java.util.List.class);
 JavaType elemT = JavaType.construct(int.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemT);

 assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
 assertEquals(elemT, collectionLikeType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType baseType = JavaType.construct(String.class);
 JavaType elemT = JavaType.construct(int.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemT);

 fail("Should not reach this point");
 }

 @Test
 public void testRefine() {
 Class<?> listType = java.util.ArrayList.class;
 JavaType elemType = JavaType.construct(int.class);
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superclass = _bogusSuperClass(listType);
 JavaType[] superInterfaces = new JavaType[0];

 CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType, bindings, superclass, superInterfaces, elemType);
 JavaType refinedType = collectionLikeType.refine(Collection.class, bindings, _bogusSuperClass(Collection.class), new JavaType[0]);

 assertEquals(Collection.class, refinedType.getRawType());
 assertEquals(elemType, refinedType.getContentType());
 }

 @Test
 public void testIsTrueCollectionType() {
 Class<?> collectionType = java.util.ArrayList.class;
 CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testIsNotTrueCollectionType() {
 Class<?> notCollectionType = String.class;
 CollectionLikeType collectionLikeType = new CollectionLikeType(notCollectionType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

 assertFalse(collectionLikeType.isTrueCollectionType());
 }
}