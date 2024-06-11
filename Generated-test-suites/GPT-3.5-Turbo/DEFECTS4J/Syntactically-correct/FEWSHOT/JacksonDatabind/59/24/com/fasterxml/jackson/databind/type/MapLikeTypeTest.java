package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType.ReferenceTypeBuilder;

public class MapLikeTypeTest {
    
    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = createMapLikeType();
        assertTrue(mapLikeType.isContainerType());
    }
    
    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = createMapLikeType();
        assertTrue(mapLikeType.isMapLikeType());
    }
    
    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = createMapLikeType();
        JavaType keyType = mapLikeType.getKeyType();
        assertEquals(String.class, keyType.getRawClass());
    }
    
    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = createMapLikeType();
        JavaType contentType = mapLikeType.getContentType();
        assertEquals(Integer.class, contentType.getRawClass());
    }
    
    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = createMapLikeType();
        assertFalse(mapLikeType.hasHandlers());
    }
    
    @Test
    public void testGetErasedSignature() {
        MapLikeType mapLikeType = createMapLikeType();
        StringBuilder sb = new StringBuilder();
        StringBuilder result = mapLikeType.getErasedSignature(sb);
        assertEquals("Map<Ljava/lang/String;Ljava/lang/Integer;>;", result.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        MapLikeType mapLikeType = createMapLikeType();
        StringBuilder sb = new StringBuilder();
        StringBuilder result = mapLikeType.getGenericSignature(sb);
        assertEquals("Map<Ljava/lang/String;Ljava/lang/Integer;>;", result.toString());
    }
    
    @Test
    public void testToString() {
        MapLikeType mapLikeType = createMapLikeType();
        String result = mapLikeType.toString();
        assertEquals("[map-like type; class java.util.Map, String -> Integer]", result);
    }
    
    @Test
    public void testEqualsSameObject() {
        MapLikeType mapLikeType = createMapLikeType();
        assertTrue(mapLikeType.equals(mapLikeType));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        MapLikeType mapLikeType = createMapLikeType();
        assertFalse(mapLikeType.equals(new Object()));
    }
    
    @Test
    public void testEqualsNull() {
        MapLikeType mapLikeType = createMapLikeType();
        assertFalse(mapLikeType.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClassObject() {
        MapLikeType mapLikeType = createMapLikeType();
        assertFalse(mapLikeType.equals(createSimpleType()));
    }
    
    @Test
    public void testEqualsDifferentKeyType() {
        MapLikeType mapLikeType1 = createMapLikeType();
        MapLikeType mapLikeType2 = createMapLikeType();
        mapLikeType2 = mapLikeType2.withKeyType(createSimpleType());
        assertFalse(mapLikeType1.equals(mapLikeType2));
    }
    
    @Test
    public void testEqualsDifferentContentType() {
        MapLikeType mapLikeType1 = createMapLikeType();
        MapLikeType mapLikeType2 = createMapLikeType();
        mapLikeType2 = mapLikeType2.withContentType(createSimpleType());
        assertFalse(mapLikeType1.equals(mapLikeType2));
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = createSimpleType();
        JavaType keyT = createSimpleType();
        JavaType valueT = createSimpleType();
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyT, valueT);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
    }
    
    // Helper method to create a MapLikeType
    private MapLikeType createMapLikeType() {
        return new MapLikeType(Map.class,
                TypeBindings.emptyBindings(),
                null,
                null,
                createSimpleType(),
                createSimpleType(),
                null,
                null,
                false);
    }
    
    // Helper method to create a simple Type
    private JavaType createSimpleType() {
        return new ReferenceTypeBuilder<JavaType>() {}.build();
    }

}