package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeModifier;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.TypeResolverBuilder;

public class CollectionLikeTypeTest {

    // Basic test cases for each method

    @Test
    public void testConstruct() {
        JavaType elementType = new SimpleType(String.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType type = CollectionLikeType.construct(List.class, bindings, null, null, elementType);
        assertInstanceOf(CollectionLikeType.class, type);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new TypeModifier().modifyCollectionType(new SimpleType(ArrayList.class), null);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType collectionType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertInstanceOf(CollectionLikeType.class, collectionType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
    }

    @Test
    public void testIsContainerType() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        assertTrue(collectionType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        assertTrue(collectionType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        assertEquals(Object.class, collectionType.getContentType().getRawClass());
    }

    @Test
    public void testGetElementType() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        assertEquals(Object.class, collectionType.getElementType().getRawClass());

        JavaType stringListType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertEquals(String.class, stringListType.getElementType().getRawClass());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        assertTrue(collectionType.isTrueCollectionType());

        JavaType wrapperType = new SimpleType(ArrayList.class.getName().substring(0, ArrayList.class.getName().lastIndexOf(".")) + "$WrapperType");
        assertFalse(wrapperType.isTrueCollectionType());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType.upgradeFrom(baseType, elementType);
    }

    @Test
    public void testRefine() {
        JavaType rawType = new SimpleType(ArrayList.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(Collection.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(Serializable.class) };
        JavaType elementType = new SimpleType(String.class);

        JavaType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elementType);
        JavaType refinedType = collectionLikeType.refine(ArrayList.class, bindings, superClass, superInterfaces);
        assertEquals(ArrayList.class, refinedType.getRawClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        Object handler = new CustomHandler();
        JavaType newCollectionType = collectionType.withContentTypeHandler(handler);
        assertNotSame(collectionType, newCollectionType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidContentTypeHandler() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        Object handler = new Object();
        collectionType.withContentTypeHandler(handler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        Object handler = new CustomHandler();
        JavaType newCollectionType = collectionType.withContentValueHandler(handler);
        assertNotSame(collectionType, newCollectionType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidContentValueHandler() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        Object handler = new Object();
        collectionType.withContentValueHandler(handler);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        Object handler = new CustomHandler();
        JavaType newCollectionType = collectionType.withTypeHandler(handler);
        assertNotSame(collectionType, newCollectionType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidTypeHandler() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        Object handler = new Object();
        collectionType.withTypeHandler(handler);
    }

    @Test
    public void testWithValueHandler() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        Object handler = new CustomHandler();
        JavaType newCollectionType = collectionType.withValueHandler(handler);
        assertNotSame(collectionType, newCollectionType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidValueHandler() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        Object handler = new Object();
        collectionType.withValueHandler(handler);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        JavaType newCollectionType = collectionType.withStaticTyping();
        assertNotSame(collectionType, newCollectionType);
        assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        JavaType narrowedType = collectionType.narrow(ArrayList.class);
        assertSame(collectionType, narrowedType);

        JavaType listType = new SimpleType(ArrayList.class);
        JavaType subListType = new SimpleType(SubList.class);
        JavaType narrowedSubListType = collectionType.narrow(subListType);
        assertNotSame(collectionType, narrowedSubListType);
    }

    @Test
    public void testEquals() {
        JavaType collectionType1 = new SimpleType(ArrayList.class);
        JavaType collectionType2 = new SimpleType(ArrayList.class);
        JavaType collectionType3 = new SimpleType(ArrayList.class);

        JavaType objectType1 = new SimpleType(Object.class);
        JavaType objectType2 = new SimpleType(Object.class);

        assertTrue(collectionType1.equals(collectionType1));
        assertTrue(collectionType1.equals(collectionType2));
        assertFalse(collectionType1.equals(objectType1));
        assertFalse(collectionType1.equals(null));
        assertFalse(collectionType1.equals(collectionType3));
    }

    @Test
    public void testToString() {
        JavaType collectionType = new SimpleType(ArrayList.class);
        String expected = "[collection-like type; class java.util.ArrayList, contains class java.lang.Object]";
        assertEquals(expected, collectionType.toString());
    }

    class CustomHandler {}
}