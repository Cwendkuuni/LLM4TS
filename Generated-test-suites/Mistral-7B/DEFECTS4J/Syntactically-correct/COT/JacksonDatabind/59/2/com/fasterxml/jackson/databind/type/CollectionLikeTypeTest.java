package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructor() {
        JavaType elemType = JavaType.constructCollectionType(Collection.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(elemType, collectionLikeType.getElementType());
    }

    @Test
    public void testConstructWithTypeBindings() {
        JavaType elemType = JavaType.constructCollectionType(Collection.class, String.class);
        TypeVariable<?>[] typeVariables = Collection.class.getTypeParameters();
        TypeBindings bindings = TypeBindings.create(Collection.class, elemType);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(elemType, collectionLikeType.getElementType());
        assertEquals(elemType, collectionLikeType.getTypeVariable(0).getBoundType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.constructRawListType(ArrayList.class, String.class);
        JavaType elemType = JavaType.constructCollectionType(Collection.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
        assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
        assertEquals(elemType, collectionLikeType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = JavaType.constructRawArrayType(int[].class);
        JavaType elemType = JavaType.constructCollectionType(Collection.class, String.class);
        CollectionLikeType.upgradeFrom(baseType, elemType);
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType elemType = JavaType.constructCollectionType(Collection.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType listType = JavaType.constructRawListType(ArrayList.class, String.class);
        collectionLikeType = new CollectionLikeType(listType, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType mapType = JavaType.constructParameterizedType(HashMap.class, String.class, String.class);
        collectionLikeType = new CollectionLikeType(mapType, TypeBindings.emptyBindings(), null, null, String.class, null, null, false);
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType elemType1 = JavaType.constructCollectionType(Collection.class, String.class);
        JavaType elemType2 = JavaType.constructCollectionType(Collection.class, Integer.class);

        CollectionLikeType collectionLikeType1 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, elemType1, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, elemType2, null, null, false);

        assertFalse(collectionLikeType1.equals(collectionLikeType2));

        CollectionLikeType collectionLikeType3 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, elemType1, null, null, false);
        assertTrue(collectionLikeType1.equals(collectionLikeType3));
    }

    @Test
    public void testToString() {
        JavaType elemType = JavaType.constructCollectionType(Collection.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        String expected = "[collection-like type; class Collection, contains " + elemType.toString() + "]";
        assertEquals(expected, collectionLikeType.toString());
    }

}