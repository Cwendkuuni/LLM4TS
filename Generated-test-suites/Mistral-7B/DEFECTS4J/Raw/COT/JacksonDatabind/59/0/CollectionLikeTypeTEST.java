package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeHandler;
import com.fasterxml.jackson.databind.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeDescriptor;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemType = new TypeFactory().constructType(String.class);
        JavaType collectionType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new TypeFactory().constructType(ArrayList.class);
        JavaType elemType = new TypeFactory().constructType(String.class);
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
        assertEquals(CollectionLikeType.class, collectionLikeType.getRawClass());
        assertEquals(elemType, collectionLikeType.getContentType());
    }

    @Test
    public void testIsContainerType() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        assertTrue(collectionType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        assertTrue(collectionType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        assertEquals(JavaType.of(String.class), collectionType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        assertNull(collectionType.getContentValueHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        assertFalse(collectionType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        assertTrue(collectionType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType collectionType1 = new TypeFactory().constructType(ArrayList.class);
        JavaType collectionType2 = new TypeFactory().constructType(ArrayList.class);
        JavaType collectionType3 = new TypeFactory().constructType(LinkedList.class);

        assertEquals(collectionType1, collectionType1);
        assertEquals(collectionType1, collectionType1);
        assertNotEquals(collectionType1, collectionType2);
        assertNotEquals(collectionType1, collectionType3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new TypeFactory().constructType(String.class);
        JavaType elemType = new TypeFactory().constructType(String.class);
        CollectionLikeType.upgradeFrom(baseType, elemType);
    }

    @Test
    public void testRefine() {
        JavaType rawType = new TypeFactory().constructType(ArrayList.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[]{Object.class};
        JavaType elemType = new TypeFactory().constructType(String.class);

        JavaType collectionType = CollectionLikeType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
    }

    @Test
    public void testToString() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.String]", collectionType.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        StringBuilder sb = new StringBuilder();
        collectionType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        StringBuilder sb = new StringBuilder();
        collectionType.getGenericSignature(sb);
        assertEquals("[Ljava.util.ArrayList<Ljava.lang.String;>;]", sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        JavaType newContentType = new TypeFactory().constructType(LinkedList.class);
        JavaType newCollectionType = collectionType.withContentType(newContentType);
        assertEquals(newContentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        TypeHandler handler = new ObjectMapper().getTypeHandlerFor(LinkedList.class);
        JavaType newCollectionType = collectionType.withContentTypeHandler(handler);
        assertNotNull(newCollectionType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        ValueHandler handler = new ObjectMapper().getFactory().valueHandlerFor(String.class);
        JavaType newCollectionType = collectionType.withValueHandler(handler);
        assertNotNull(newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        ValueHandler handler = new ObjectMapper().getFactory().valueHandlerFor(String.class);
        JavaType newCollectionType = collectionType.withContentValueHandler(handler);
        assertNotNull(newCollectionType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        JavaType newCollectionType = collectionType.withStaticTyping();
        assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        JavaType newCollectionType = collectionType.narrow(ArrayList.class);
        assertEquals(collectionType, newCollectionType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowUnsupported() {
        JavaType collectionType = new TypeFactory().constructType(ArrayList.class);
        collectionType.narrow(LinkedList.class);
    }
}

class TypeFactory {
    public JavaType constructType(Class<?> type) {
        return new TypeFactory().constructType(type, null);
    }

    public JavaType constructType(Class<?> type, TypeBindings bindings) {
        return TypeDeserializer.constructType(TypeFactory.class.getDeclaredConstructor(), type, bindings, null, null, null, null, null);
    }

    private TypeFactory() {
    }
}