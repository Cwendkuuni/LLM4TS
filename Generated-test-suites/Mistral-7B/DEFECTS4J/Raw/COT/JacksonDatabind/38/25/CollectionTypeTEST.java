package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CollectionTypeTest {

    @Test
    public void testCollectionTypeConstructor() {
        Class<List<Integer>> collectionClass = List.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInts, elemT);
        Assertions.assertEquals(collectionClass, collectionType.getRawClass());
        Assertions.assertNotNull(collectionType.getBindings());
        Assertions.assertEquals(superClass, collectionType.getSuperClass());
        Assertions.assertArrayEquals(superInts, collectionType.getSuperInterfaces());
        Assertions.assertEquals(elemT, collectionType.getElementType());
        Assertions.assertNull(collectionType.getValueHandler());
        Assertions.assertNull(collectionType.getTypeHandler());
        Assertions.assertFalse(collectionType.isStatic());
    }

    @Test
    public void testCollectionTypeWithTypeHandler() {
        Class<List<Integer>> collectionClass = List.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInts, elemT);
        Object handler = new Object() {};
        CollectionType newCollectionType = collectionType.withTypeHandler(handler);
        Assertions.assertNotNull(newCollectionType.getTypeHandler());
        Assertions.assertEquals(handler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testCollectionTypeWithContentTypeHandler() {
        Class<List<Integer>> collectionClass = List.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        JavaType contentTypeHandler = JavaType.construct(String.class);
        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInts, elemT);
        Object handler = new Object() {};
        CollectionType newCollectionType = collectionType.withContentTypeHandler(handler);
        Assertions.assertNotNull(newCollectionType.getContentTypeHandler());
        Assertions.assertEquals(handler, newCollectionType.getContentTypeHandler());
    }

    @Test
    public void testCollectionTypeWithValueHandler() {
        Class<List<Integer>> collectionClass = List.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInts, elemT);
        Object handler = new Object() {};
        CollectionType newCollectionType = collectionType.withValueHandler(handler);
        Assertions.assertNotNull(newCollectionType.getValueHandler());
        Assertions.assertEquals(handler, newCollectionType.getValueHandler());
    }

    @Test
    public void testCollectionTypeWithContentValueHandler() {
        Class<List<Integer>> collectionClass = List.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        JavaType contentValueHandler = JavaType.construct(String.class);
        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInts, elemT);
        Object handler = new Object() {};
        CollectionType newCollectionType = collectionType.withContentValueHandler(handler);
        Assertions.assertNotNull(newCollectionType.getContentValueHandler());
        Assertions.assertEquals(handler, newCollectionType.getContentValueHandler());
    }

    @Test
    public void testCollectionTypeWithStaticTyping() {
        Class<List<Integer>> collectionClass = List.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInts, elemT);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        Assertions.assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testCollectionTypeRefine() {
        Class<List<Integer>> collectionClass = List.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInts, elemT);
        Class<ArrayList<Integer>> subclass = ArrayList.class;
        TypeBindings newBindings = TypeBindings.empty();
        JavaType[] newSuperInts = new JavaType[0];
        CollectionType refinedCollectionType = collectionType.refine(subclass, newBindings, superClass, newSuperInts);
        Assertions.assertEquals(subclass, refinedCollectionType.getRawClass());
        Assertions.assertNotNull(refinedCollectionType.getBindings());
        Assertions.assertEquals(superClass, refinedCollectionType.getSuperClass());
        Assertions.assertArrayEquals(newSuperInts, refinedCollectionType.getSuperInterfaces());
        Assertions.assertEquals(elemT, refinedCollectionType.getElementType());
        Assertions.assertNull(refinedCollectionType.getValueHandler());
        Assertions.assertNull(refinedCollectionType.getTypeHandler());
        Assertions.assertFalse(refinedCollectionType.isStatic());
    }

    @Test
    public void testDeprecatedConstructors() {
        try {
            CollectionType collectionType = CollectionType.construct(List.class, null);
            Assertions.fail("Should not reach this point with deprecated constructor");
        } catch (Exception e) {
            // Expected exception
        }

        try {
            CollectionType collectionType = CollectionType.construct(List.class, JavaType.construct(Integer.class));
            Assertions.fail("Should not reach this point with deprecated constructor");
        } catch (Exception e) {
            // Expected exception
        }
    }
}