package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.List.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType setType = CollectionLikeType.construct(java.util.Set.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType mapType = CollectionLikeType.construct(java.util.Map.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType arrayType = CollectionLikeType.construct(int[].class, TypeBindings.emptyBindings(), java.lang.reflect.Array.class, new JavaType[0], null);
        CollectionLikeType customType = CollectionLikeType.construct(MyCollection.class, TypeBindings.emptyBindings(), MyCollection.class, new JavaType[0], null);

        assertEquals(true, listType.isContainerType());
        assertEquals(true, setType.isContainerType());
        assertEquals(true, mapType.isContainerType());
        assertEquals(true, arrayType.isContainerType());
        assertEquals(true, customType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.List.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType setType = CollectionLikeType.construct(java.util.Set.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType mapType = CollectionLikeType.construct(java.util.Map.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType arrayType = CollectionLikeType.construct(int[].class, TypeBindings.emptyBindings(), java.lang.reflect.Array.class, new JavaType[0], null);
        CollectionLikeType customType = CollectionLikeType.construct(MyCollection.class, TypeBindings.emptyBindings(), MyCollection.class, new JavaType[0], null);

        assertEquals(true, listType.isCollectionLikeType());
        assertEquals(true, setType.isCollectionLikeType());
        assertEquals(true, mapType.isCollectionLikeType());
        assertEquals(true, arrayType.isCollectionLikeType());
        assertEquals(true, customType.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.List.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType setType = CollectionLikeType.construct(java.util.Set.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType mapType = CollectionLikeType.construct(java.util.Map.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType arrayType = CollectionLikeType.construct(int[].class, TypeBindings.emptyBindings(), java.lang.reflect.Array.class, new JavaType[0], null);

        assertEquals(false, listType.isTrueCollectionType());
        assertEquals(false, setType.isTrueCollectionType());
        assertEquals(false, mapType.isTrueCollectionType());
        assertEquals(false, arrayType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        Class<?> listClass = java.util.List.class;
        JavaType listType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, null);
        Collection<Integer> list = new ArrayList<>();
        JavaType listElementType = JavaType.construct(Integer.class);
        JavaType listContentType = listType.getContentType();

        assertEquals(listElementType, listContentType);
    }

    @Test
    public void testGetContentValueHandler() {
        Class<Integer> integerClass = Integer.class;
        JavaType integerType = JavaType.construct(integerClass);
        Object integerValueHandler = Collections.singletonMap("IntValueHandlerKey", new IntValueHandler());
        JavaType integerTypeWithValueHandler = integerType.withValueHandler(integerValueHandler);
        CollectionLikeType integerCollectionType = CollectionLikeType.construct(integerClass, TypeBindings.emptyBindings(), null, null, integerTypeWithValueHandler);

        assertEquals(integerValueHandler, integerCollectionType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Class<Integer> integerClass = Integer.class;
        JavaType integerType = JavaType.construct(integerClass);
        Object integerTypeHandler = new IntTypeHandler();
        JavaType integerTypeWithTypeHandler = integerType.withTypeHandler(integerTypeHandler);
        CollectionLikeType integerCollectionType = CollectionLikeType.construct(integerClass, TypeBindings.emptyBindings(), null, null, integerTypeWithTypeHandler);

        assertEquals(integerTypeHandler, integerCollectionType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        Class<Integer> integerClass = Integer.class;
        JavaType integerType = JavaType.construct(integerClass);
        Object integerValueHandler = Collections.singletonMap("IntValueHandlerKey", new IntValueHandler());
        JavaType integerTypeWithValueHandler = integerType.withValueHandler(integerValueHandler);
        JavaType integerTypeWithTypeHandler = integerType.withTypeHandler(new IntTypeHandler());
        CollectionLikeType integerCollectionType = CollectionLikeType.construct(integerClass, TypeBindings.emptyBindings(), null, null, integerTypeWithValueHandler);
        CollectionLikeType integerCollectionTypeWithTypeHandler = CollectionLikeType.construct(integerClass, TypeBindings.emptyBindings(), null, null, integerTypeWithTypeHandler);

        assertTrue(integerCollectionType.hasHandlers());
        assertTrue(integerCollectionTypeWithValueHandler.hasHandlers());
        assertTrue(integerCollectionTypeWithTypeHandler.hasHandlers());
        assertFalse(integerType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<Integer>> listClass = java.util.List.class;
        StringBuilder sb = new StringBuilder();
        CollectionLikeType listType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, null);

        listType.getErasedSignature(sb);
        assertEquals("[collection-like type; class [Ljava.lang.Integer;]", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<Integer>> listClass = java.util.List.class;
        StringBuilder sb = new StringBuilder();
        CollectionLikeType listType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, null);

        listType.getGenericSignature(sb);
        assertEquals("[collection-like type; class java.util.List<java.lang.Integer>;]", sb.toString());
    }

    @Test
    public void testEquals() {
        Class<List<Integer>> listClass = java.util.List.class;
        TypeVariable<?> intTypeVariable = TypeVariable.of(Integer.TYPE);
        JavaType listType1 = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, null);
        JavaType listType2 = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, null);
        JavaType listType3 = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, null);
        JavaType listType4 = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType listType5 = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, intTypeVariable);

        assertTrue(listType1.equals(listType1));
        assertTrue(listType1.equals(listType2));
        assertTrue(listType1.equals(listType3));
        assertFalse(listType1.equals(listType4));
        assertFalse(listType1.equals(listType5));
    }

    @Test
    public void testToString() {
        Class<List<Integer>> listClass = java.util.List.class;
        StringBuilder sb = new StringBuilder();
        CollectionLikeType listType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, null);

        assertEquals("[collection-like type; class "+listClass.getName()+", contains "+JavaType.construct(Integer.class)+"]", listType.toString());
    }

    @Test
    public void testRefine() {
        Class<List<Integer>> listClass = java.util.List.class;
        JavaType listType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, null);
        JavaType refinedListType = listType.refine(ArrayList.class, TypeBindings.emptyBindings(), null, new JavaType[0]);

        assertEquals(CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, new JavaType[0], JavaType.construct(Integer.class)).getClass(), refinedListType.getClass());
    }

    @Test
    public void testConstruct() {
        Class<List<Integer>> listClass = java.util.List.class;
        JavaType listType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, new JavaType[0], JavaType.construct(Integer.class));

        assertEquals(listClass, listType._class);
        assertEquals(TypeBindings.emptyBindings(), listType._bindings);
        assertEquals(null, listType._superClass);
        assertNull(listType._superInterfaces);
        assertEquals(JavaType.construct(Integer.class).hashCode(), listType._elementType.hashCode());
        assertEquals(null, listType._valueHandler);
        assertEquals(null, listType._typeHandler);
        assertEquals(false, listType._asStatic);
    }

    @Test
    public void testConstructFromBaseType() {
        Class<List<Integer>> listClass = java.util.List.class;
        JavaType listType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, JavaType.construct(Integer.class));

        assertEquals(listClass, listType._class);
        assertEquals(TypeBindings.emptyBindings(), listType._bindings);
        assertEquals(null, listType._superClass);
        assertNull(listType._superInterfaces);
        assertEquals(JavaType.construct(Integer.class).hashCode(), listType._elementType.hashCode());
        assertEquals(null, listType._valueHandler);
        assertEquals(null, listType._typeHandler);
        assertEquals(false, listType._asStatic);
    }

    @Test
    public void testUpgradeFrom() {
        Class<List<Integer>> listClass = java.util.List.class;
        JavaType listType = JavaType.construct(listClass);
        JavaType integerType = JavaType.construct(Integer.class);
        CollectionLikeType upgradedListType = CollectionLikeType.upgradeFrom(listType, integerType);

        assertEquals(listClass, upgradedListType._class);
        assertEquals(TypeBindings.emptyBindings(), upgradedListType._bindings);
        assertEquals(null, upgradedListType._superClass);
        assertNull(upgradedListType._superInterfaces);
        assertEquals(integerType.hashCode(), upgradedListType._elementType.hashCode());
        assertEquals(null, upgradedListType._valueHandler);
        assertEquals(null, upgradedListType._typeHandler);
        assertEquals(false, upgradedListType._asStatic);
    }

    private static class IntValueHandler {}
    private static class IntTypeHandler {}
    private static class MyCollection {}
}