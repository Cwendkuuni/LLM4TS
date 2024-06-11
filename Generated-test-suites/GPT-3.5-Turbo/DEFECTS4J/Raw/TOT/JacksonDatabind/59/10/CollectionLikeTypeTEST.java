package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;
import java.util.Collection;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructWithRawType() {
        Class clazz = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elementType = new JavaType();
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(clazz, bindings, superClass, superInts, elementType, valueHandler, typeHandler, asStatic);

        Assert.assertNotNull(collectionLikeType);
        Assert.assertEquals(clazz, collectionLikeType.getRawClass());
        Assert.assertEquals(bindings, collectionLikeType.getBindings());
        Assert.assertEquals(superClass, collectionLikeType.getSuperClass());
        Assert.assertArrayEquals(superInts, collectionLikeType.getInterfaces());
        Assert.assertEquals(elementType, collectionLikeType.getElementType());
        Assert.assertEquals(valueHandler, collectionLikeType.getValueHandler());
        Assert.assertEquals(typeHandler, collectionLikeType.getTypeHandler());
        Assert.assertEquals(asStatic, collectionLikeType.isStatic());
    }

    @Test
    public void testConstructWithClassName() {
        String className = "java.util.Collection";
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elementType = new JavaType();
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(className, bindings, superClass, superInts, elementType, valueHandler, typeHandler, asStatic);

        Assert.assertNotNull(collectionLikeType);
        Assert.assertEquals(className, collectionLikeType.getRawClass().getName());
        Assert.assertEquals(bindings, collectionLikeType.getBindings());
        Assert.assertEquals(superClass, collectionLikeType.getSuperClass());
        Assert.assertArrayEquals(superInts, collectionLikeType.getInterfaces());
        Assert.assertEquals(elementType, collectionLikeType.getElementType());
        Assert.assertEquals(valueHandler, collectionLikeType.getValueHandler());
        Assert.assertEquals(typeHandler, collectionLikeType.getTypeHandler());
        Assert.assertEquals(asStatic, collectionLikeType.isStatic());
    }

    @Test
    public void testConstructWithClassNameAndClassLoader() {
        String className = "java.util.Collection";
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elementType = new JavaType();
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        ClassLoader cl = getClass().getClassLoader();

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(className, bindings, superClass, superInts, elementType, valueHandler, typeHandler, asStatic, cl);

        Assert.assertNotNull(collectionLikeType);
        Assert.assertEquals(className, collectionLikeType.getRawClass().getName());
        Assert.assertEquals(bindings, collectionLikeType.getBindings());
        Assert.assertEquals(superClass, collectionLikeType.getSuperClass());
        Assert.assertArrayEquals(superInts, collectionLikeType.getInterfaces());
        Assert.assertEquals(elementType, collectionLikeType.getElementType());
        Assert.assertEquals(valueHandler, collectionLikeType.getValueHandler());
        Assert.assertEquals(typeHandler, collectionLikeType.getTypeHandler());
        Assert.assertEquals(asStatic, collectionLikeType.isStatic());
    }

    @Test
    public void testConstructWithJavaType() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(baseType, elementType);

        Assert.assertNotNull(collectionLikeType);
        Assert.assertEquals(baseType, collectionLikeType.getBaseType());
        Assert.assertEquals(elementType, collectionLikeType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        Assert.assertNotNull(collectionLikeType);
        Assert.assertEquals(baseType, collectionLikeType.getBaseType());
        Assert.assertEquals(elementType, collectionLikeType.getElementType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();

        Assert.assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType collectionLikeType1 = new CollectionLikeType();
        CollectionLikeType collectionLikeType2 = new CollectionLikeType();

        Assert.assertEquals(collectionLikeType1, collectionLikeType2);
        Assert.assertEquals(collectionLikeType2, collectionLikeType1);
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();

        Assert.assertNotNull(collectionLikeType.toString());
    }
}