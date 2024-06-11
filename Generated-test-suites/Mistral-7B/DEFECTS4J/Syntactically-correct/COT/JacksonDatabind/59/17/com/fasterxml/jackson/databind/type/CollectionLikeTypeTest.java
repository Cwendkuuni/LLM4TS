package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.bean.BeanDescription;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class CollectionLikeTypeTest {

 private JavaType createCollectionLikeType(Class<?> rawType, TypeVariable<?> elemTVar, JavaType elemType) {
 return CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), null, null, elemType);
 }

 @Test
 public void testConstructorWithTypeBindings() {
 Class<Collection<String>> collectionType = Collection.class;
 TypeVariable<Collection<String>, Class<String>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = createCollectionLikeType(collectionType, elemTVar, elemType);
 assertEquals(Collection.class, collectionLikeType.getRawClass());
 assertEquals(elemType, collectionLikeType.getContentType());
 }

 @Test
 public void testConstructorWithEmptyBindingsAndElementType() {
 Class<Collection<String>> collectionType = Collection.class;
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = CollectionLikeType.construct(collectionType, elemType);
 assertEquals(Collection.class, collectionLikeType.getRawClass());
 assertEquals(elemType, collectionLikeType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidType() {
 Class<String> stringType = String.class;
 JavaType invalidType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType collectionLikeType = CollectionLikeType.construct(stringType, invalidType);
 fail("Should not be able to construct CollectionLikeType from invalid type");
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
 assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
 assertEquals(elemType, collectionLikeType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
 fail("Should not be able to upgrade from an invalid base type");
 }

 @Test
 public void testIsTrueCollectionType() {
 Class<Collection<String>> collectionType = Collection.class;
 JavaType collectionLikeType = createCollectionLikeType(collectionType, null, null);
 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testIsCollectionLikeType() {
 Class<Collection<String>> collectionType = Collection.class;
 JavaType collectionLikeType = createCollectionLikeType(collectionType, null, null);
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 Class<Collection<String>> collectionType = Collection.class;
 TypeVariable<Collection<String>, Class<String>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = createCollectionLikeType(collectionType, elemTVar, elemType);
 assertEquals(elemType, collectionLikeType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 Class<Collection<String>> collectionType = Collection.class;
 TypeVariable<Collection<String>, Class<String>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = createCollectionLikeType(collectionType, elemTVar, elemType);
 assertEquals(elemType.getValueHandler(), collectionLikeType.getContentValueHandler());
 }

 @Test
 public void testGetContentTypeHandler() {
 Class<Collection<String>> collectionType = Collection.class;
 TypeVariable<Collection<String>, Class<String>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = createCollectionLikeType(collectionType, elemTVar, elemType);
 assertEquals(elemType.getTypeHandler(), collectionLikeType.getContentTypeHandler());
 }

 @Test
 public void testHasHandlers() {
 Class<Collection<String>> collectionType = Collection.class;
 TypeVariable<Collection<String>, Class<String>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = createCollectionLikeType(collectionType, elemTVar, elemType);
 assertTrue(collectionLikeType.hasHandlers());
 }

 @Test
 public void testBuildCanonicalName() {
 Class<Collection<String>> collectionType = Collection.class;
 TypeVariable<Collection<String>, Class<String>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = createCollectionLikeType(collectionType, elemTVar, elemType);
 assertEquals("[collection-like type; class java.util.Collection, contains class java.lang.String]", collectionLikeType.buildCanonicalName());
 }

 @Test
 public void testEquals() {
 Class<Collection<String>> collectionType = Collection.class;
 TypeVariable<Collection<String>, Class<String>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemType1 = TypeFactory.defaultInstance().constructType(String.class);
 JavaType elemType2 = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType1 = createCollectionLikeType(collectionType, elemTVar, elemType1);
 JavaType collectionLikeType2 = createCollectionLikeType(collectionType, elemTVar, elemType2);
 JavaType collectionLikeType3 = createCollectionLikeType(collectionType, elemTVar, elemType1);
 assertTrue(collectionLikeType1.equals(collectionLikeType1));
 assertTrue(collectionLikeType1.equals(collectionLikeType3));
 assertTrue(collectionLikeType1.equals(collectionLikeType2));
 assertTrue(collectionLikeType2.equals(collectionLikeType3));
 }

 @Test
 public void testToString() {
 Class<Collection<String>> collectionType = Collection.class;
 TypeVariable<Collection<String>, Class<String>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionLikeType = createCollectionLikeType(collectionType, elemTVar, elemType);
 assertEquals("[collection-like type; class java.util.Collection, contains class java.lang.String]", collectionLikeType.toString());
 }

 @Test
 public void testIsContainerType() {
 Class<Collection<String>> collectionType = Collection.class;
 JavaType collectionLikeType = createCollectionLikeType(collectionType, null, null);
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testIsMapLikeType() {
 Class<Collection<String>> collectionType = Collection.class;
 JavaType collectionLikeType = createCollectionLikeType(collectionType, null, null);
 assertFalse(collectionLikeType.isMapLikeType());
 }

 @Test
 public void testWithContentType() {
 Class<Collection<Collection<String>>> listOfCollectionsType = Collection.class;
 TypeVariable<Collection<Collection<String>>, Class<Collection<String>>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemCollectionType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType collectionLikeType = createCollectionLikeType(listOfCollectionsType, elemTVar, elemCollectionType);
 JavaType newContentType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 JavaType newCollectionLikeType = collectionLikeType.withContentType(newContentType);
 assertEquals(newContentType, newCollectionLikeType.getContentType());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<Collection<Collection<String>>> listOfCollectionsType = Collection.class;
 TypeVariable<Collection<Collection<String>>, Class<Collection<String>>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemCollectionType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType collectionLikeType = createCollectionLikeType(listOfCollectionsType, elemTVar, elemCollectionType);
 TypeSerializer elemTypeSerializer = new TestTypeSerializer();
 JavaType newContentTypeHandler = new TypeSerializer(elemTypeSerializer);
 JavaType newCollectionLikeType = collectionLikeType.withContentTypeHandler(newContentTypeHandler);
 assertNotNull(newCollectionLikeType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<Collection<Collection<String>>> listOfCollectionsType = Collection.class;
 TypeVariable<Collection<Collection<String>>, Class<Collection<String>>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemCollectionType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType collectionLikeType = createCollectionLikeType(listOfCollectionsType, elemTVar, elemCollectionType);
 ValueHandler elemValueHandler = new TestValueHandler();
 JavaType newCollectionLikeType = collectionLikeType.withValueHandler(elemValueHandler);
 assertNotNull(newCollectionLikeType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<Collection<Collection<String>>> listOfCollectionsType = Collection.class;
 TypeVariable<Collection<Collection<String>>, Class<Collection<String>>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemCollectionType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType collectionLikeType = createCollectionLikeType(listOfCollectionsType, elemTVar, elemCollectionType);
 ContentValueHandler elemContentValueHandler = new TestContentValueHandler();
 JavaType newCollectionLikeType = collectionLikeType.withContentValueHandler(elemContentValueHandler);
 assertNotNull(newCollectionLikeType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<Collection<Collection<String>>> listOfCollectionsType = Collection.class;
 TypeVariable<Collection<Collection<String>>, Class<Collection<String>>> elemTVar = TypeVariable.where(Collection.class, "E");
 JavaType elemCollectionType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType collectionLikeType = createCollectionLikeType(listOfCollectionsType, elemTVar, elemCollectionType);
 JavaType newCollectionLikeType = collectionLikeType.withStaticTyping();
 assertTrue(newCollectionLikeType.isStatic());
 }

 @Test
 public void testRefine() {
 Class<Collection<Collection<Integer>>> listOfCollectionsType = Collection.class;
 JavaType elemCollectionType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType collectionLikeType = createCollectionLikeType(listOfCollectionsType, null, elemCollectionType);
 JavaType refinedCollectionLikeType = collectionLikeType.refine(Collection.class, TypeBindings.emptyBindings(), null, null);
 assertEquals(Collection.class, refinedCollectionLikeType.getRawClass());
 assertEquals(elemCollectionType, refinedCollectionLikeType.getContentType());
 }

}