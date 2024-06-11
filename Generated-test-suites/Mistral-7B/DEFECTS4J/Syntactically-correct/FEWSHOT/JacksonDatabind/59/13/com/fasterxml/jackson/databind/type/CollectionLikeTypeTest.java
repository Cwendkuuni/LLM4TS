package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerTypeAndIsCollectionLikeType() {
        JavaType collectionType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionType.isContainerType());
        assertTrue(collectionType.isCollectionLikeType());

        JavaType listType = CollectionLikeType.construct(JavaType.getType(ArrayList.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(Integer.class));
        assertTrue(listType.isContainerType());
        assertTrue(listType.isCollectionLikeType());

        JavaType mapType = CollectionLikeType.construct(JavaType.getType(HashMap.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(String.class), JavaType.getType(Integer.class));
        assertTrue(mapType.isContainerType());
        assertTrue(mapType.isCollectionLikeType());

        JavaType nonCollectionLikeType = JavaType.constructType(int[].class);
        assertFalse(nonCollectionLikeType.isContainerType());
        assertFalse(nonCollectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType collectionType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionType.isTrueCollectionType());

        JavaType listType = CollectionLikeType.construct(JavaType.getType(ArrayList.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(Integer.class));
        assertTrue(listType.isTrueCollectionType());

        JavaType mapType = CollectionLikeType.construct(JavaType.getType(HashMap.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(String.class), JavaType.getType(Integer.class));
        assertTrue(mapType.isTrueCollectionType());

        JavaType nonCollectionLikeType = JavaType.constructType(int[].class);
        assertFalse(nonCollectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = CollectionLikeType.construct(JavaType.getType(ArrayList.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(Integer.class));
        assertEquals(JavaType.getType(Integer.class), listType.getContentType());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType listType = CollectionLikeType.construct(JavaType.getType(ArrayList.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(Integer.class));
        assertNull(listType.getContentTypeHandler());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType listType = CollectionLikeType.construct(JavaType.getType(ArrayList.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(Integer.class));
        assertNull(listType.getContentValueHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType listType = CollectionLikeType.construct(JavaType.getType(ArrayList.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(Integer.class));
        assertFalse(listType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType listType = CollectionLikeType.construct(JavaType.getType(ArrayList.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(Integer.class));
        assertEquals("Ljava/util/List;", listType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType listType = CollectionLikeType.construct(JavaType.getType(ArrayList.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(Integer.class));
        assertEquals("Ljava/util/List;<Ljava/lang/Integer;>", listType.getGenericSignature().toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType listType = CollectionLikeType.construct(JavaType.getType(ArrayList.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(Integer.class));
        assertEquals("java.util.ArrayList<java.lang.Integer>", listType.buildCanonicalName());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType intArrayType = JavaType.constructType(int[].class);
        JavaType intListType = CollectionLikeType.upgradeFrom(intArrayType, JavaType.getType(Integer.class));
        assertTrue(intListType instanceof CollectionLikeType);
        assertEquals(JavaType.getType(ArrayList.class), intListType._class);
        assertEquals(JavaType.getType(Integer.class), intListType._elementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType intArrayType = JavaType.constructType(int[].class);
        CollectionLikeType.upgradeFrom(intArrayType, JavaType.getType(String.class));
    }

    @Test
    public void testConstructFromBasicType() {
        JavaType intType = JavaType.getType(int.class);
        JavaType intCollectionType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, intType);
        assertTrue(intCollectionType instanceof CollectionLikeType);
        assertEquals(JavaType.getType(Collection.class), intCollectionType._class);
        assertEquals(intType, intCollectionType._elementType);
    }

    @Test
    public void testConstructFromTypeVariable() {
        TypeVariable<?> intTypeVariable = TypeVariable.of(int.class);
        JavaType intCollectionType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, intTypeVariable);
        assertTrue(intCollectionType instanceof CollectionLikeType);
        assertEquals(JavaType.getType(Collection.class), intCollectionType._class);
        assertEquals(intTypeVariable, intCollectionType._elementType);
    }

    @Test
    public void testConstructFromTypeWithTypeBindings() {
        JavaType listType = JavaType.getType(ArrayList.class);
        JavaType stringListType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, JavaType.getType(String.class), listType);
        assertTrue(stringListType instanceof CollectionLikeType);
        assertEquals(JavaType.getType(ArrayList.class), stringListType._class);
        assertEquals(JavaType.getType(String.class), stringListType._elementType);
    }

    @Test
    public void testConstructFromTypeWithContentType() {
        JavaType listType = JavaType.getType(ArrayList.class);
        JavaType stringListType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, listType, JavaType.getType(String.class));
        assertTrue(stringListType instanceof CollectionLikeType);
        assertEquals(JavaType.getType(ArrayList.class), stringListType._class);
        assertEquals(JavaType.getType(String.class), stringListType._elementType);
    }

    @Test
    public void testConstructFromTypeWithContentTypeHandler() {
        JavaType listType = JavaType.getType(ArrayList.class);
        JavaType stringListType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, listType, new StringSerializer());
        assertTrue(stringListType instanceof CollectionLikeType);
        assertEquals(JavaType.getType(ArrayList.class), stringListType._class);
        assertSame(new StringSerializer(), stringListType._typeHandler);
        assertEquals(JavaType.getType(Object.class), stringListType._valueHandler);
        assertEquals(JavaType.getType(String.class), stringListType._elementType);
    }

    @Test
    public void testConstructFromTypeWithContentValueHandler() {
        JavaType listType = JavaType.getType(ArrayList.class);
        JavaType stringListType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, listType, new StringDeserializer());
        assertTrue(stringListType instanceof CollectionLikeType);
        assertEquals(JavaType.getType(ArrayList.class), stringListType._class);
        assertSame(new StringDeserializer(), stringListType._valueHandler);
        assertEquals(JavaType.getType(Object.class), stringListType._typeHandler);
        assertEquals(JavaType.getType(String.class), stringListType._elementType);
    }

    @Test
    public void testConstructFromTypeWithStaticTyping() {
        JavaType listType = JavaType.getType(ArrayList.class);
        JavaType stringListType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, listType, null);
        assertTrue(stringListType instanceof CollectionLikeType);
        assertFalse(stringListType._asStatic);

        JavaType staticStringListType = CollectionLikeType.construct(JavaType.getType(Collection.class), TypeBindings.emptyBindings(), null, null, listType, null, null, true);
        assertTrue(staticStringListType instanceof CollectionLikeType);
        assertTrue(staticStringListType._asStatic);
    }

}