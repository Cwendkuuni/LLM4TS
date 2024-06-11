package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertNotNull(type);
        assertEquals(Collection.class, type.getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(type);
        assertEquals(Collection.class, type.getRawClass());
    }

    @Test
    public void testNarrow() {
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType baseType = CollectionLikeType.construct(Collection.class, null, null, null, elementType);
        JavaType narrowedType = baseType.narrow(ArrayList.class);
        assertNotNull(narrowedType);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(elementType, narrowedType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType type = (CollectionLikeType) baseType.withContentType(contentType);
        assertNotNull(type);
        assertEquals(contentType, type.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Collection.class);
        Object handler = new Object();
        CollectionLikeType type = (CollectionLikeType) baseType.withTypeHandler(handler);
        assertNotNull(type);
        assertEquals(handler, type.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        CollectionLikeType type = (CollectionLikeType) baseType.withContentTypeHandler(handler);
        assertNotNull(type);
        assertEquals(handler, type.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Collection.class);
        Object handler = new Object();
        CollectionLikeType type = (CollectionLikeType) baseType.withValueHandler(handler);
        assertNotNull(type);
        assertEquals(handler, type.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        CollectionLikeType type = (CollectionLikeType) baseType.withContentValueHandler(handler);
        assertNotNull(type);
        assertEquals(handler, type.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Collection.class);
        CollectionLikeType type = (CollectionLikeType) baseType.withStaticTyping();
        assertNotNull(type);
        assertTrue(type.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType refinedType = (CollectionLikeType) baseType.refine(ArrayList.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertEquals(elementType, refinedType.getContentType());
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertTrue(type.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertTrue(type.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elementType);
        assertEquals(elementType, type.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        JavaType elementTypeWithHandler = elementType.withValueHandler(handler);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elementTypeWithHandler);
        assertEquals(handler, type.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        JavaType elementTypeWithHandler = elementType.withTypeHandler(handler);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elementTypeWithHandler);
        assertEquals(handler, type.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertFalse(type.hasHandlers());

        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        JavaType elementTypeWithHandler = elementType.withValueHandler(handler);
        CollectionLikeType typeWithHandler = CollectionLikeType.construct(Collection.class, null, null, null, elementTypeWithHandler);
        assertTrue(typeWithHandler.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder()
                .append("Ljava/util/Collection;");
        type.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder()
                .append("Ljava/util/Collection;")
                .append("<Ljava/lang/String;>;");
        type.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertEquals("java.util.Collection<java.lang.String>", type.buildCanonicalName());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertTrue(type1.isTrueCollectionType());

        CollectionLikeType type2 = CollectionLikeType.construct(ArrayList.class, null, null, null, null);
        assertTrue(type2.isTrueCollectionType());

        CollectionLikeType type3 = CollectionLikeType.construct(String.class, null, null, null, null);
        assertFalse(type3.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
        CollectionLikeType type2 = CollectionLikeType.construct(Collection.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testToString() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.String]", type.toString());
    }
}