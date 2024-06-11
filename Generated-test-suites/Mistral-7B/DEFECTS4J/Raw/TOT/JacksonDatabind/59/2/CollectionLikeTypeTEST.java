import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

 // Test for the constructor with Class, TypeBindings, JavaType, JavaType[]
 @Test
 public void testConstructor1() {
 Class<?> rawType = Collection.class;
 Class<?> elemType = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(Collection.class);
 JavaType[] superInts = new JavaType[0];
 JavaType javaType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemType);
 CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
 assertEquals(Collection.class, collectionLikeType.getRawType());
 assertEquals(elemType, collectionLikeType.getElementType());
 }

 // Test for the constructor with TypeBase and JavaType
 @Test
 public void testConstructor2() {
 TypeBase base = new TypeBase() {
 @Override
 public Class<?> getRawClass() {
 return Collection.class;
 }
 };
 JavaType elemType = String.class;
 JavaType collectionLikeType = CollectionLikeType.construct(base, elemType);
 assertEquals(Collection.class, collectionLikeType.getRawType());
 assertEquals(elemType, collectionLikeType.getElementType());
 }

 // Test for the upgradeFrom method
 @Test
 public void testUpgradeFrom() {
 JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType elemType = String.class;
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(listType, elemType);
 assertEquals(ArrayList.class, collectionLikeType.getRawType());
 assertEquals(elemType, collectionLikeType.getElementType());
 }

 // Test for the equals method
 @Test
 public void testEquals() {
 Class<?> rawType1 = Collection.class;
 Class<?> elemType1 = String.class;
 TypeBindings bindings1 = TypeBindings.emptyBindings();
 JavaType superClass1 = _bogusSuperClass(Collection.class);
 JavaType[] superInts1 = new JavaType[0];
 JavaType javaType1 = CollectionLikeType.construct(rawType1, bindings1, superClass1, superInts1, elemType1);
 Class<?> rawType2 = Collection.class;
 Class<?> elemType2 = String.class;
 TypeBindings bindings2 = TypeBindings.emptyBindings();
 JavaType superClass2 = _bogusSuperClass(Collection.class);
 JavaType[] superInts2 = new JavaType[0];
 JavaType javaType2 = CollectionLikeType.construct(rawType2, bindings2, superClass2, superInts2, elemType2);
 CollectionLikeType collectionLikeType1 = (CollectionLikeType) javaType1;
 CollectionLikeType collectionLikeType2 = (CollectionLikeType) javaType2;
 assertTrue(collectionLikeType1.equals(collectionLikeType2));
 }

 // Test for the getRawType method
 @Test
 public void testGetRawType() {
 Class<?> rawType = Collection.class;
 JavaType javaType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), 
 _bogusSuperClass(Collection.class), null, null);
 assertEquals(Collection.class, javaType.getRawType());
 }

 // Test for the getElementType method
 @Test
 public void testGetElementType() {
 Class<?> rawType = Collection.class;
 Class<?> elemType = String.class;
 JavaType javaType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), 
 _bogusSuperClass(Collection.class), null, elemType);
 assertEquals(String.class, javaType.getRawType().getComponentType());
 }

 // Test for the isContainerType and isCollectionLikeType methods
 @Test
 public void testIsContainerTypeAndIsCollectionLikeType() {
 Class<?> rawType = Collection.class;
 JavaType javaType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), 
 _bogusSuperClass(Collection.class), null, null);
 assertTrue(javaType.isContainerType());
 assertTrue(javaType.isCollectionLikeType());
 }

 // Test for the getCanonicalName method
 @Test
 public void testGetCanonicalName() {
 Class<?> rawType = Collection.class;
 Class<?> elemType = String.class;
 JavaType javaType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), 
 _bogusSuperClass(Collection.class), null, elemType);
 assertEquals("java.util.Collection<java.lang.String>", javaType.toCanonical());
 }

 // Test for the getErasedSignature and getGenericSignature methods
 @Test
 public void testGetErasedSignatureAndGetGenericSignature() {
 Class<?> rawType = Collection.class;
 Class<?> elemType = String.class;
 JavaType javaType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), 
 _bogusSuperClass(Collection.class), null, elemType);
 StringBuilder sb = new StringBuilder();
 javaType.getErasedSignature(sb);
 assertEquals("java.util.Collection", sb.toString());
 javaType.getGenericSignature(sb);
 assertEquals("java.util.Collection<java.lang.String>", sb.toString());
 }
}