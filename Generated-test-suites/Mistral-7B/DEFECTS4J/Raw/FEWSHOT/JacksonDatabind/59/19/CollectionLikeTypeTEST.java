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
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        CollectionLikeType nonCollectionType = new CollectionLikeType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(nonCollectionType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        CollectionLikeType nonCollectionLikeType = new CollectionLikeType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(nonCollectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        Class<?> collectionType = Collection.class;
        JavaType javaType = JavaType.construct(collectionType, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        JavaType expectedType = JavaType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        JavaType actualType = collectionLikeType.getContentType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetContentTypeHandler() {
        Class<?> collectionType = Collection.class;
        JavaType javaType = JavaType.construct(collectionType, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        Object expectedHandler = new Object() {};
        Object actualHandler = collectionLikeType.getContentTypeHandler();
        assertSame(expectedHandler, actualHandler);
    }

    @Test
    public void testHasHandlers() {
        Class<?> collectionType = Collection.class;
        JavaType javaType = JavaType.construct(collectionType, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        collectionLikeType.setValueHandler(valueHandler);
        collectionLikeType.setTypeHandler(typeHandler);

        boolean hasHandlers = collectionLikeType.hasHandlers();
        assertTrue(hasHandlers);
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        CollectionLikeType nonCollectionLikeType = new CollectionLikeType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(nonCollectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testConstruct() {
        Class<?> collectionType = Collection.class;
        TypeVariable<?>[] typeVariables = collectionType.getTypeParameters();
        JavaType javaType = new JavaType(collectionType, TypeBindings.emptyBindings());

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(collectionType, TypeBindings.emptyBindings(), null, null, javaType);
        assertEquals(collectionType, collectionLikeType.getClass());
        assertEquals(javaType, collectionLikeType.getContentType());
    }

    @Test
    public void testConstructWithValueHandler() {
        Class<?> collectionType = Collection.class;
        TypeVariable<?>[] typeVariables = collectionType.getTypeParameters();
        JavaType javaType = new JavaType(collectionType, TypeBindings.emptyBindings());

        Object valueHandler = new Object() {};
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(collectionType, javaType, valueHandler);
        assertEquals(collectionType, collectionLikeType.getClass());
        assertEquals(javaType, collectionLikeType.getContentType());
        assertEquals(valueHandler, collectionLikeType.getValueHandler());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<?> collectionType = Collection.class;
        TypeVariable<?>[] typeVariables = collectionType.getTypeParameters();
        JavaType javaType = new JavaType(collectionType, TypeBindings.emptyBindings());

        Object typeHandler = new Object() {};
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(collectionType, javaType, null, typeHandler);
        assertEquals(collectionType, collectionLikeType.getClass());
        assertEquals(javaType, collectionLikeType.getContentType());
        assertEquals(typeHandler, collectionLikeType.getTypeHandler());
    }

    @Test
    public void testConstructWithValueHandlerAndTypeHandler() {
        Class<?> collectionType = Collection.class;
        TypeVariable<?>[] typeVariables = collectionType.getTypeParameters();
        JavaType javaType = new JavaType(collectionType, TypeBindings.emptyBindings());

        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(collectionType, javaType, valueHandler, typeHandler);
        assertEquals(collectionType, collectionLikeType.getClass());
        assertEquals(javaType, collectionLikeType.getContentType());
        assertEquals(valueHandler, collectionLikeType.getValueHandler());
        assertEquals(typeHandler, collectionLikeType.getTypeHandler());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(ArrayList.class, TypeBindings.emptyBindings());
        JavaType elementType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertEquals(ArrayList.class, collectionLikeType.getClass());
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType elementType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        CollectionLikeType.upgradeFrom(baseType, elementType);
    }

    @Test
    public void testWithContentType() {
        Class<?> collectionType = Collection.class;
        JavaType javaType = JavaType.construct(collectionType, TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        JavaType newContentType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(newContentType);

        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
        assertFalse(collectionLikeType.equals(newCollectionLikeType));
        assertEquals(newContentType, newCollectionLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> collectionType = Collection.class;
        JavaType javaType = JavaType.construct(collectionType, TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        Object newTypeHandler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(newTypeHandler);

        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
        assertFalse(collectionLikeType.equals(newCollectionLikeType));
        assertEquals(newTypeHandler, newCollectionLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> collectionType = Collection.class;
        JavaType javaType = JavaType.construct(collectionType, TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        Object newContentValueHandler = new Object() {};
        Object newContentTypeHandler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(newContentTypeHandler).withValueHandler(newContentValueHandler);

        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
        assertFalse(collectionLikeType.equals(newCollectionLikeType));
        assertEquals(newContentTypeHandler, newCollectionLikeType.getTypeHandler());
        assertEquals(newContentValueHandler, newCollectionLikeType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> collectionType = Collection.class;
        JavaType javaType = JavaType.construct(collectionType, TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();

        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
        assertFalse(collectionLikeType.equals(newCollectionLikeType));
        assertTrue(newCollectionLikeType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> collectionType = Collection.class;
        TypeVariable<?>[] typeVariables = collectionType.getTypeParameters();
        JavaType javaType = new JavaType(collectionType, TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        Class<?> subclass = ArrayList.class;
        TypeBindings newBindings = TypeBindings.create(subclass, javaType);
        JavaType newJavaType = new JavaType(subclass, newBindings);
        CollectionLikeType newCollectionLikeType = collectionLikeType.refine(subclass, newBindings, null, null);

        assertEquals(subclass, newCollectionLikeType.getClass());
        assertEquals(newJavaType, newCollectionLikeType.getContentType());
    }
}