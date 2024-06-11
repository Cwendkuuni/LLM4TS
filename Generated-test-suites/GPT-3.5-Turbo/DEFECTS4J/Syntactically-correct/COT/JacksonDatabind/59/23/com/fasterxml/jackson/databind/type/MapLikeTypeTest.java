
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType();
        boolean isContainerType = mapLikeType.isContainerType();
        Assert.assertEquals(true, isContainerType);
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType();
        boolean isMapLikeType = mapLikeType.isMapLikeType();
        Assert.assertEquals(true, isMapLikeType);
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType();
        JavaType keyType = mapLikeType.getKeyType();
        Assert.assertEquals(null, keyType);
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType();
        JavaType contentType = mapLikeType.getContentType();
        Assert.assertEquals(null, contentType);
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        Object contentValueHandler = mapLikeType.getContentValueHandler();
        Assert.assertEquals(null, contentValueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType();
        Object contentTypeHandler = mapLikeType.getContentTypeHandler();
        Assert.assertEquals(null, contentTypeHandler);
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType();
        boolean hasHandlers = mapLikeType.hasHandlers();
        Assert.assertEquals(false, hasHandlers);
    }

    @Test
    public void testGetErasedSignature() {
        MapLikeType mapLikeType = new MapLikeType();
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = mapLikeType.getErasedSignature(sb);
        Assert.assertEquals("java.util.Map", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        MapLikeType mapLikeType = new MapLikeType();
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = mapLikeType.getGenericSignature(sb);
        Assert.assertEquals("java.util.Map<null,null>;", genericSignature.toString());
    }

    @Test
    public void testToString() {
        MapLikeType mapLikeType = new MapLikeType();
        String mapLikeTypeString = mapLikeType.toString();
        Assert.assertEquals("[map-like type; class com.fasterxml.jackson.databind.type.MapLikeType, null -> null]", mapLikeTypeString);
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType();
        MapLikeType mapLikeType2 = new MapLikeType();
        boolean areEquals = mapLikeType1.equals(mapLikeType2);
        Assert.assertEquals(true, areEquals);
    }
}
