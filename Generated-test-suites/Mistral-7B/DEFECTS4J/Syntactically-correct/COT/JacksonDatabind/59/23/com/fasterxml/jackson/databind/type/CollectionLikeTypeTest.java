package com.yourpackage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructor() {
        JavaType elementType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, elementType);
        assertNotNull(collectionLikeType);
    }

    @Test
    public void testConstructWithBindings() {
        JavaType elementType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType baseType = JavaType.construct(ArrayList.class);
        TypeBindings bindings = TypeBindings.create(List.class, elementType);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(baseType, bindings, null, null, elementType);
        assertNotNull(collectionLikeType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = JavaType.construct(ArrayList.class);
        JavaType elementType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, elementType);
        assertNotNull(collectionLikeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType javaType = JavaType.construct(String.class);
        JavaType elementType = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionLikeType.upgradeFrom(javaType, elementType);
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = getCollectionLikeType();
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = getCollectionLikeType();
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = getCollectionLikeType();
        JavaType expectedType = JavaType.constructCollectionType(List.class, Integer.class);
        assertEquals(expectedType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = getCollectionLikeType();
        Object expectedValueHandler = Arrays.asList(1, 2, 3).get(0);
        assertEquals(expectedValueHandler, collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = getCollectionLikeType();
        Object expectedTypeHandler = new ArrayList<Integer>().getClass();
        assertEquals(expectedTypeHandler, collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = getCollectionLikeType();
        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = getCollectionLikeType();
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType javaType2 = JavaType.constructCollectionType(List.class, Integer.class);
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(javaType1, TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(javaType2, TypeBindings.emptyBindings());
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = getCollectionLikeType();
        String expectedString = "[collection-like type; class java.util.List, contains class java.lang.Integer]";
        assertEquals(expectedString, collectionLikeType.toString());
    }

    @Test
    public void testRefine() {
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());
        JavaType refinedType = collectionLikeType.refine(LinkedList.class, TypeBindings.emptyBindings(), null, null);
        JavaType expectedType = JavaType.constructCollectionType(LinkedList.class, Integer.class);
        assertEquals(expectedType, refinedType);
    }

    @Test
    public void testNarrow() {
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());
        JavaType narrowedType = collectionLikeType.narrow(LinkedList.class.asSubclass(Collection.class));
        JavaType expectedType = JavaType.constructCollectionType(LinkedList.class, Integer.class);
        assertEquals(expectedType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());
        JavaType newElementType = JavaType.constructCollectionType(LinkedList.class, Integer.class);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(newElementType);
        JavaType expectedType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, newElementType, null, null, false);
        assertEquals(expectedType, newCollectionLikeType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());
        Object newTypeHandler = new ArrayList<Integer>().getClass();
        CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(newTypeHandler);
        Object expectedTypeHandler = newTypeHandler;
        assertEquals(expectedTypeHandler, newCollectionLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());
        Object newContentTypeHandler = new ArrayList<Integer>().getClass();
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(newContentTypeHandler);
        Object expectedContentTypeHandler = newContentTypeHandler;
        assertEquals(expectedContentTypeHandler, newCollectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());
        Object newValueHandler = new ArrayList<Integer>().getClass();
        CollectionLikeType newCollectionLikeType = collectionLikeType.withValueHandler(newValueHandler);
        Object expectedValueHandler = newValueHandler;
        assertEquals(expectedValueHandler, newCollectionLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());
        Object newContentValueHandler = new ArrayList<Integer>().get(0);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentValueHandler(newContentValueHandler);
        Object expectedContentValueHandler = newContentValueHandler;
        assertEquals(expectedContentValueHandler, newCollectionLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());
        CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
        boolean expectedStaticTyping = true;
        assertTrue(newCollectionLikeType.isStaticTyping());
    }

    private CollectionLikeType getCollectionLikeType() {
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        return new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());
    }
}