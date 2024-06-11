package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

 private CollectionLikeType _collectionLikeType;
 private CollectionLikeType _collectionLikeTypeWithElementType;
 private JavaType _javaType;
 private JavaType _javaTypeWithElementType;

 @Before
 public void setUp() {
 _javaType = new TypeFactory().constructType(Collection.class);
 _javaTypeWithElementType = new TypeFactory().constructType(ArrayList.class, Integer.class);
 _collectionLikeType = CollectionLikeType.construct(_javaType.getRawClass(), new TypeBindings(), _javaType.getSuperClass(), null, _javaType.getComponentType());
 _collectionLikeTypeWithElementType = CollectionLikeType.construct(_javaTypeWithElementType.getRawClass(), new TypeBindings(), _javaTypeWithElementType.getSuperClass(), null, _javaTypeWithElementType.getComponentType());
 }

 @Test
 public void testIsContainerType() {
 assertTrue(_collectionLikeType.isContainerType());
 assertTrue(_collectionLikeTypeWithElementType.isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 assertTrue(_collectionLikeType.isCollectionLikeType());
 assertTrue(_collectionLikeTypeWithElementType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 assertEquals(_javaType.getComponentType(), _collectionLikeType.getContentType());
 assertEquals(_javaTypeWithElementType.getComponentType(), _collectionLikeTypeWithElementType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 assertNull(_collectionLikeType.getContentValueHandler());
 assertNotNull(_collectionLikeTypeWithElementType.getContentValueHandler());
 assertEquals(Integer.class, _collectionLikeTypeWithElementType.getContentValueHandler().getType());
 }

 @Test
 public void testGetContentTypeHandler() {
 assertNull(_collectionLikeType.getContentTypeHandler());
 assertNotNull(_collectionLikeTypeWithElementType.getContentTypeHandler());
 assertEquals(ArrayList.class, _collectionLikeTypeWithElementType.getContentTypeHandler().getType());
 }

 @Test
 public void testHasHandlers() {
 assertFalse(_collectionLikeType.hasHandlers());
 assertTrue(_collectionLikeTypeWithElementType.hasHandlers());
 }

 @Test
 public void testIsTrueCollectionType() {
 assertFalse(_collectionLikeType.isTrueCollectionType());
 assertTrue(_collectionLikeTypeWithElementType.isTrueCollectionType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 CollectionLikeType.upgradeFrom(String.class, _javaType.getComponentType());
 }

 @Test
 public void testUpgradeFromValidType() {
 JavaType upgradedType = CollectionLikeType.upgradeFrom(_javaType, _javaType.getComponentType());
 assertEquals(_collectionLikeType, upgradedType);
 }

 @Test
 public void testConstructWithEmptyBindings() {
 JavaType constructedType = CollectionLikeType.construct(_ArrayList.class, TypeBindings.emptyBindings(), _javaType.getSuperClass(), null, _javaType.getComponentType());
 assertEquals(_collectionLikeType, constructedType);
 }

 @Test
 public void testConstructWithBindings() {
 TypeVariable<?>[] typeVariables = _javaTypeWithElementType.getRawTypes();
 JavaType constructedType = CollectionLikeType.construct(_ArrayList.class, new TypeBindings().setTypeVariable(typeVariables[0], Integer.class), _javaType.getSuperClass(), null, _javaType.getComponentType());
 assertEquals(_collectionLikeTypeWithElementType, constructedType);
 }

}