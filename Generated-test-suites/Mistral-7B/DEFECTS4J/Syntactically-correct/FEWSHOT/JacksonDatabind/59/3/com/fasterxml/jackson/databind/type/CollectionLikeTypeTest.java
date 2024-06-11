package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<List<String>> listType = List.class.getTypeParameters()[0].getType();
        JavaType javaType = CollectionLikeType.construct(listType, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance.constructType(String.class));
        assertInstanceOf(CollectionLikeType.class, javaType);
        assertEquals(String.class, javaType.getContentType());
    }

    @Test
    public void testConstructWithBindings() {
        Class<List<String, Integer>> listType = List.class.getTypeParameters()[0].getType();
        JavaType javaType = CollectionLikeType.construct(listType, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance.constructType(String.class), TypeFactory.defaultInstance.constructType(Integer.class));
        assertInstanceOf(CollectionLikeType.class, javaType);
        assertEquals(String.class, javaType.getContentType()._elementType);
        assertEquals(Integer.class, javaType.getContentType()._superClass.getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        JavaType javaTypeUpgraded = CollectionLikeType.upgradeFrom(javaType, TypeFactory.defaultInstance.constructType(String.class));
        assertInstanceOf(CollectionLikeType.class, javaTypeUpgraded);
        assertEquals(String.class, javaTypeUpgraded.getContentType()._elementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String.class);
        CollectionLikeType.upgradeFrom(javaType, TypeFactory.defaultInstance.constructType(List.class));
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertTrue(javaType.isTrueCollectionType());

        JavaType javaTypeWrongType = TypeFactory.defaultInstance.constructType(Map.class);
        assertFalse(javaTypeWrongType.isTrueCollectionType());
    }

    @Test
    public void testIsTrueCollectionTypeWithSubclass() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(ArrayList.class);
        assertTrue(javaType.isTrueCollectionType());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertEquals(Object.class, javaType.getContentType().getRawClass());

        JavaType javaTypeStringList = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance.constructType(String.class));
        assertEquals(String.class, javaTypeStringList.getContentType().getRawClass());
    }

    @Test
    public void testToString() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertEquals("[collection-like type; class java.util.List, contains class java.lang.Object]", javaType.toString());

        JavaType javaTypeStringList = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance.constructType(String.class));
        assertEquals("[collection-like type; class java.util.List, contains class java.lang.String]", javaTypeStringList.toString());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance.constructType(String.class));
        JavaType javaType2 = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance.constructType(String.class));
        assertTrue(javaType1.equals(javaType2));

        JavaType javaType3 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance.constructType(String.class));
        assertTrue(javaType1.equals(javaType3));

        JavaType javaType4 = TypeFactory.defaultInstance.constructType(LinkedList.class);
        assertFalse(javaType1.equals(javaType4));

        JavaType javaType5 = TypeFactory.defaultInstance.constructType(ArrayList.class);
        assertTrue(javaType1.equals(javaType5));

        JavaType javaType6 = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance.constructType(Integer.class));
        assertFalse(javaType1.equals(javaType6));
    }
}