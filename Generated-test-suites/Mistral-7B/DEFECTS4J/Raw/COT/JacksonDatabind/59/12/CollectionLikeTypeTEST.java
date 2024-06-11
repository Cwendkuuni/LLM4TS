package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeModifier;

/**
 * Test class for CollectionLikeType
 */
public class CollectionLikeTypeTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, elementType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    /**
     * Test constructor with TypeBindings
     */
    @Test
    public void testConstructorWithBindings() {
        TypeVariable<String> typeVariable = new TypeVariable<String>("T") {};
        JavaType elementType = TypeFactory.defaultInstance().constructType(typeVariable);
        TypeBindings bindings = TypeBindings.create(List.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, bindings, null, null, elementType);
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getContentType());
        assertEquals(bindings, collectionLikeType.getBindings());
    }

    /**
     * Test upgradeFrom method
     */
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    /**
     * Test isTrueCollectionType method
     */
    @Test
    public void testIsTrueCollectionType() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertTrue(collectionType.isTrueCollectionType());
        JavaType listLikeType = TypeFactory.defaultInstance().constructType(ArrayListLikeType.class);
        assertFalse(listLikeType.isTrueCollectionType());
    }

    /**
     * Test getContentType method
     */
    @Test
    public void testGetContentType() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    /**
     * Test equals method
     */
    @Test
    public void testEquals() {
        JavaType collectionType1 = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType collectionType2 = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(collectionType1, elementType);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(collectionType2, elementType);
        CollectionLikeType collectionLikeType3 = new CollectionLikeType(collectionType1, elementType);
        assertTrue(collectionLikeType1.equals(collectionLikeType1));
        assertTrue(collectionLikeType1.equals(collectionLikeType3));
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
        assertFalse(collectionLikeType1.equals(null));
        assertFalse(collectionLikeType1.equals(""));
        JavaType listLikeType = TypeFactory.defaultInstance().constructType(ArrayListLikeType.class);
        assertFalse(collectionLikeType1.equals(listLikeType));
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        String expected = "[collection-like type; class java.util.ArrayList, contains class java.lang.String]";
        assertEquals(expected, collectionLikeType.toString());
    }

    /**
     * Test getErasedSignature method
     */
    @Test
    public void testGetErasedSignature() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        StringBuilder expected = new StringBuilder("[Ljava.util.ArrayList;");
        StringBuilder actual = collectionLikeType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test getGenericSignature method with empty bindings
     */
    @Test
    public void testGetGenericSignatureWithEmptyBindings() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        StringBuilder expected = new StringBuilder("[collection-like type; class java.util.ArrayList, contains ");
        expected.append(elementType.getGenericSignature(new StringBuilder()));
        expected.append("]");
        StringBuilder actual = new StringBuilder("[collection-like type; class java.util.ArrayList, contains ");
        actual.append(elementType.getClassName());
        actual.append("]");
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test getGenericSignature method with bindings
     */
    @Test
    public void testGetGenericSignatureWithBindings() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, bindings, elementType);
        StringBuilder expected = new StringBuilder("[collection-like type; class java.util.ArrayList, contains ");
        expected.append(elementType.getGenericSignature(new StringBuilder()));
        expected.append("]");
        StringBuilder actual = new StringBuilder("[collection-like type; class java.util.ArrayList, contains ");
        actual.append(bindings.getGenericSignature(new StringBuilder()));
        expected.append("]");
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test isContainerType method
     */
    @Test
    public void testIsContainerType() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertTrue(collectionType.isContainerType());
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(stringType.isContainerType());
    }

    /**
     * Test isCollectionLikeType method
     */
    @Test
    public void testIsCollectionLikeType() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertTrue(collectionType.isCollectionLikeType());
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(stringType.isCollectionLikeType());
    }

    /**
     * Test hasHandlers method
     */
    @Test
    public void testHasHandlers() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        assertFalse(collectionLikeType.hasHandlers());
        JavaType valueHandler = TypeFactory.defaultInstance().constructType(StringValueHandler.class);
        CollectionLikeType collectionLikeTypeWithValueHandler = collectionLikeType.withValueHandler(valueHandler);
        assertTrue(collectionLikeTypeWithValueHandler.hasHandlers());
    }

    /**
     * Test refine method
     */
    @Test
    public void testRefine() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        JavaType newCollectionType = TypeFactory.defaultInstance().constructType(LinkedList.class);
        TypeBindings newBindings = TypeBindings.create(LinkedList.class, String.class);
        CollectionLikeType refinedCollectionLikeType = collectionLikeType.refine(LinkedList.class, newBindings, null, null);
        assertEquals(newCollectionType, refinedCollectionLikeType.getRawClass());
        assertEquals(elementType, refinedCollectionLikeType.getContentType());
    }

    /**
     * Test withContentType method
     */
    @Test
    public void testWithContentType() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType oldElementType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType newElementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, oldElementType);
        CollectionLikeType collectionLikeTypeWithNewElementType = collectionLikeType.withContentType(newElementType);
        assertEquals(newElementType, collectionLikeTypeWithNewElementType.getContentType());
    }

    /**
     * Test withTypeHandler method
     */
    @Test
    public void testWithTypeHandler() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        Object stringValueHandler = new StringValueHandler();
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        CollectionLikeType collectionLikeTypeWithStringValueHandler = collectionLikeType.withTypeHandler(stringValueHandler);
        assertEquals(stringValueHandler, collectionLikeTypeWithStringValueHandler.getValueHandler());
    }

    /**
     * Test withContentTypeHandler method
     */
    @Test
    public void testWithContentTypeHandler() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        Object stringTypeHandler = new StringTypeHandler();
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        CollectionLikeType collectionLikeTypeWithStringTypeHandler = collectionLikeType.withContentTypeHandler(stringTypeHandler);
        assertEquals(stringTypeHandler, collectionLikeTypeWithStringTypeHandler.getTypeHandler());
    }

    /**
     * Test withValueHandler method
     */
    @Test
    public void testWithValueHandler() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        Object stringValueHandler = new StringValueHandler();
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        CollectionLikeType collectionLikeTypeWithStringValueHandler = collectionLikeType.withValueHandler(stringValueHandler);
        assertEquals(stringValueHandler, collectionLikeTypeWithStringValueHandler.getValueHandler());
    }

    /**
     * Test withContentValueHandler method
     */
    @Test
    public void testWithContentValueHandler() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        Object stringContentValueHandler = new StringContentValueHandler();
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        CollectionLikeType collectionLikeTypeWithStringContentValueHandler = collectionLikeType.withContentValueHandler(stringContentValueHandler);
        assertEquals(stringContentValueHandler, collectionLikeTypeWithStringContentValueHandler.getContentValueHandler());
    }

    /**
     * Test withStaticTyping method
     */
    @Test
    public void testWithStaticTyping() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(collectionType, elementType);
        CollectionLikeType collectionLikeTypeWithStaticTyping = collectionLikeType.withStaticTyping();
        assertTrue(collectionLikeTypeWithStaticTyping.isStaticTyping());
    }
}