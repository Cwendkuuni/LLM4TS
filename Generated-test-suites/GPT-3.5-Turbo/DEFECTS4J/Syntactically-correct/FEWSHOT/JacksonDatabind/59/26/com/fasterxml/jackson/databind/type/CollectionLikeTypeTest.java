package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class CollectionLikeTypeTest {
    @Test
    public void testConstructor() {
        Class<?> collT = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        CollectionLikeType collectionLikeType = new CollectionLikeType(collT, bindings, superClass, superInts, elemT,
                valueHandler, typeHandler, asStatic);

        assertEquals(collT, collectionLikeType.getBaseType());
        assertEquals(bindings, collectionLikeType.getTypeBindings());
        assertEquals(superClass, collectionLikeType.getSuperClass());
        assertEquals(superInts.length, collectionLikeType.getInterfaces().length);
        assertEquals(elemT, collectionLikeType.getElementType());
        assertEquals(valueHandler, collectionLikeType.getValueHandler());
        assertEquals(typeHandler, collectionLikeType.getTypeHandler());
        assertEquals(asStatic, collectionLikeType.isStatic());
    }

    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts,
                elemT);

        assertEquals(rawType, collectionLikeType.getBaseType());
        assertEquals(bindings, collectionLikeType.getTypeBindings());
        assertEquals(superClass, collectionLikeType.getSuperClass());
        assertEquals(superInts.length, collectionLikeType.getInterfaces().length);
        assertEquals(elemT, collectionLikeType.getElementType());
        assertEquals(null, collectionLikeType.getValueHandler());
        assertEquals(null, collectionLikeType.getTypeHandler());
        assertFalse(collectionLikeType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        Object baseType = new Object();
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom((JavaType) baseType, elementType);
    }

    @Test
    public void testConstructWithUpgradeFrom() {
        JavaType baseType = new TypeBase();
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertEquals(baseType, collectionLikeType.getBaseType());
        assertEquals(Collection.class, collectionLikeType.getBaseType().getClass());
        assertEquals(elementType, collectionLikeType.getElementType());
        assertEquals(null, collectionLikeType.getValueHandler());
        assertEquals(null, collectionLikeType.getTypeHandler());
        assertFalse(collectionLikeType.isStatic());
    }

    @Test
    public void testTrueCollectionType() {
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = new CollectionLikeType();

        assertFalse(collectionLikeType.isTrueCollectionType());

        collectionLikeType = CollectionLikeType.upgradeFrom(new JavaType(), elementType);
        assertFalse(collectionLikeType.isTrueCollectionType());

        collectionLikeType = CollectionLikeType.upgradeFrom(new TypeBase(), elementType);
        assertFalse(collectionLikeType.isTrueCollectionType());

        collectionLikeType = CollectionLikeType.upgradeFrom(new CollectionLikeType(), elementType);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType1 = new CollectionLikeType();
        collectionLikeType1.setBaseType(Collection.class);
        collectionLikeType1.setElementType(elementType);

        CollectionLikeType collectionLikeType2 = new CollectionLikeType();
        collectionLikeType2.setBaseType(Collection.class);
        collectionLikeType2.setElementType(elementType);

        assertTrue(collectionLikeType1.equals(collectionLikeType2));

        collectionLikeType1.setBaseType(null);
        assertFalse(collectionLikeType1.equals(collectionLikeType2));

        collectionLikeType1.setBaseType(Collection.class);
        collectionLikeType1.setElementType(null);
        assertFalse(collectionLikeType1.equals(collectionLikeType2));

        collectionLikeType1.setElementType(elementType);
        assertFalse(collectionLikeType1.equals(null));

        assertFalse(collectionLikeType1.equals(new Object()));

        assertTrue(collectionLikeType1.equals(collectionLikeType1));
    }

    @Test
    public void testToString() {
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        collectionLikeType.setBaseType(Collection.class);
        collectionLikeType.setElementType(elementType);

        assertEquals("[collection-like type; class java.util.Collection, contains " + elementType + "]",
                collectionLikeType.toString());
    }
}