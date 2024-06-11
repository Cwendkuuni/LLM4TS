package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeModifier;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

/**
 * Test class for CollectionLikeType
 */
public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstructor() {
        JavaType javaType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType, bindings, superClass, superInts, elemT);
        assertEquals(javaType, collectionLikeType.getRawType());
        assertEquals(bindings, collectionLikeType.getTypeBindings());
        assertEquals(superClass, collectionLikeType.getSuperClass());
        assertEquals(superInts, collectionLikeType.getSuperInterfaces());
        assertEquals(elemT, collectionLikeType.getElementType());
    }

    @Test
    public void testCollectionLikeTypeUpgradeFrom() {
        JavaType baseType = Integer[].class;
        JavaType elementType = Integer.TYPE;
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertEquals(baseType, collectionLikeType.getRawType());
        assertEquals(elementType, collectionLikeType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionLikeTypeUpgradeFromInvalidType() {
        JavaType baseType = String.class;
        JavaType elementType = Integer.TYPE;
        CollectionLikeType.upgradeFrom(baseType, elementType);
    }

    @Test
    public void testCollectionLikeTypeIsContainerType() {
        JavaType javaType = Collection.class;
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testCollectionLikeTypeIsTrueCollectionType() {
        JavaType javaType = Collection.class;
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testCollectionLikeTypeIsFalseCollectionType() {
        JavaType javaType = Object[].class;
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType, TypeBindings.emptyBindings(), null, null, null);
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testCollectionLikeTypeWithContentType() {
        JavaType javaType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType newElemT = Long.TYPE;
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(newElemT);
        assertEquals(javaType, newCollectionLikeType.getRawType());
        assertEquals(bindings, newCollectionLikeType.getTypeBindings());
        assertEquals(superClass, newCollectionLikeType.getSuperClass());
        assertEquals(superInts, newCollectionLikeType.getSuperInterfaces());
        assertEquals(newElemT, newCollectionLikeType.getElementType());
    }

    @Test
    public void testCollectionLikeTypeWithTypeHandler() {
        JavaType javaType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, bindings, superClass, superInts, elemT, null, null, false);
        Object typeHandler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(typeHandler);
        assertEquals(javaType, newCollectionLikeType.getRawType());
        assertEquals(bindings, newCollectionLikeType.getTypeBindings());
        assertEquals(superClass, newCollectionLikeType.getSuperClass());
        assertEquals(superInts, newCollectionLikeType.getSuperInterfaces());
        assertEquals(elemT, newCollectionLikeType.getElementType());
        assertEquals(typeHandler, newCollectionLikeType.getTypeHandler());
    }

    @Test
    public void testCollectionLikeTypeWithContentTypeHandler() {
        JavaType javaType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, bindings, superClass, superInts, elemT, null, null, false);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(valueHandler).withTypeHandler(typeHandler);
        assertEquals(javaType, newCollectionLikeType.getRawType());
        assertEquals(bindings, newCollectionLikeType.getTypeBindings());
        assertEquals(superClass, newCollectionLikeType.getSuperClass());
        assertEquals(superInts, newCollectionLikeType.getSuperInterfaces());
        assertEquals(elemT.withContentTypeHandler(valueHandler), newCollectionLikeType.getElementType());
        assertEquals(valueHandler, newCollectionLikeType.getValueHandler());
        assertEquals(typeHandler, newCollectionLikeType.getTypeHandler());
    }

    @Test
    public void testCollectionLikeTypeWithValueHandler() {
        JavaType javaType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, bindings, superClass, superInts, elemT, null, null, false);
        Object valueHandler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withValueHandler(valueHandler);
        assertEquals(javaType, newCollectionLikeType.getRawType());
        assertEquals(bindings, newCollectionLikeType.getTypeBindings());
        assertEquals(superClass, newCollectionLikeType.getSuperClass());
        assertEquals(superInts, newCollectionLikeType.getSuperInterfaces());
        assertEquals(elemT, newCollectionLikeType.getElementType());
        assertEquals(valueHandler, newCollectionLikeType.getValueHandler());
    }

    @Test
    public void testCollectionLikeTypeWithContentValueHandler() {
        JavaType javaType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, bindings, superClass, superInts, elemT, null, null, false);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentValueHandler(valueHandler).withTypeHandler(typeHandler);
        assertEquals(javaType, newCollectionLikeType.getRawType());
        assertEquals(bindings, newCollectionLikeType.getTypeBindings());
        assertEquals(superClass, newCollectionLikeType.getSuperClass());
        assertEquals(superInts, newCollectionLikeType.getSuperInterfaces());
        assertEquals(elemT.withValueHandler(valueHandler), newCollectionLikeType.getElementType());
        assertEquals(valueHandler, newCollectionLikeType.getValueHandler());
        assertEquals(typeHandler, newCollectionLikeType.getTypeHandler());
    }

    @Test
    public void testCollectionLikeTypeWithStaticTyping() {
        JavaType javaType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
        assertTrue(newCollectionLikeType.isStaticTyping());
    }

    @Test
    public void testCollectionLikeTypeRefine() {
        JavaType rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        JavaType newElemT = Long.TYPE;
        CollectionLikeType newCollectionLikeType = collectionLikeType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, newCollectionLikeType.getRawType());
        assertEquals(bindings, newCollectionLikeType.getTypeBindings());
        assertEquals(superClass, newCollectionLikeType.getSuperClass());
        assertEquals(superInterfaces, newCollectionLikeType.getSuperInterfaces());
        assertEquals(newElemT, newCollectionLikeType.getElementType());
    }

}