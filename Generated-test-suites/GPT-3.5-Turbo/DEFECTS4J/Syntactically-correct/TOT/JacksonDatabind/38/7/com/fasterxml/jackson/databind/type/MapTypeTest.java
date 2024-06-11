
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct_WithSuperClassAndSuperInterfaces_ShouldCreateMapTypeInstance() {
        // Given
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        
        // When
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        
        // Then
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertEquals(superInts, mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isContainerType());
    }
    
    @Test
    public void testConstruct_WithKeyTAndValueT_ShouldCreateMapTypeInstance() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        
        // When
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        
        // Then
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(null, mapType.getBindings());
        assertEquals(null, mapType.getKeyT());
        assertEquals(null, mapType.getValueT());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isContainerType());
    }
    
    @Test
    public void testWithKeyType_WithNewKeyType_ShouldReturnNewMapTypeWithUpdatedKeyType() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        JavaType newKeyType = JavaType.INT;
        
        // When
        MapType newMapType = mapType.withKeyType(newKeyType);
        
        // Then
        assertEquals(rawType, newMapType.getRawClass());
        assertEquals(null, newMapType.getBindings());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(null, newMapType.getValueType());
        assertEquals(null, newMapType.getValueHandler());
        assertEquals(null, newMapType.getTypeHandler());
        assertEquals(false, newMapType.isContainerType());
    }
    
    @Test
    public void testWithContentValueHandler_WithNewContentValueHandler_ShouldReturnNewMapTypeWithUpdatedContentValueHandler() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        Object newContentValueHandler = new Object();
        
        // When
        MapType newMapType = mapType.withContentValueHandler(newContentValueHandler);
        
        // Then
        assertEquals(rawType, newMapType.getRawClass());
        assertEquals(null, newMapType.getBindings());
        assertEquals(null, newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertEquals(null, newMapType.getValueHandler());
        assertEquals(null, newMapType.getTypeHandler());
        assertEquals(false, newMapType.isContainerType());
    }
    
    @Test
    public void testWithStaticTyping_WithAsStaticFalse_ShouldReturnNewMapTypeWithAsStaticTrue() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        
        // When
        MapType newMapType = mapType.withStaticTyping();
        
        // Then
        assertEquals(rawType, newMapType.getRawClass());
        assertEquals(null, newMapType.getBindings());
        assertEquals(null, newMapType.getKeyType());
        assertEquals(null, newMapType.getValueType());
        assertEquals(null, newMapType.getValueHandler());
        assertEquals(null, newMapType.getTypeHandler());
        assertEquals(true, newMapType.isContainerType());
    }
    
    @Test
    public void testWithContentType_WithNewContentType_ShouldReturnNewMapTypeWithUpdatedContentType() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        JavaType newContentType = JavaType.STRING;
        
        // When
        MapType newMapType = mapType.withContentType(newContentType);
        
        // Then
        assertEquals(rawType, newMapType.getRawClass());
        assertEquals(null, newMapType.getBindings());
        assertEquals(null, newMapType.getKeyType());
        assertEquals(newContentType, newMapType.getValueType());
        assertEquals(null, newMapType.getValueHandler());
        assertEquals(null, newMapType.getTypeHandler());
        assertEquals(false, newMapType.isContainerType());
    }

    @Test
    public void testWithKeyTypeHandler_WithNewKeyTypeHandler_ShouldReturnNewMapTypeWithUpdatedKeyTypeHandler() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        Object newKeyTypeHandler = new Object();
        
        // When
        MapType newMapType = mapType.withKeyTypeHandler(newKeyTypeHandler);
        
        // Then
        assertEquals(rawType, newMapType.getRawClass());
        assertEquals(null, newMapType.getBindings());
        assertEquals(null, newMapType.getKeyType());
        assertEquals(null, newMapType.getValueType());
        assertEquals(null, newMapType.getValueHandler());
        assertEquals(null, newMapType.getTypeHandler());
        assertEquals(false, newMapType.isContainerType());
    }
    
    @Test
    public void testWithKeyValueHandler_WithNewKeyValueHandler_ShouldReturnNewMapTypeWithUpdatedKeyValueHandler() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        Object newKeyValueHandler = new Object();
        
        // When
        MapType newMapType = mapType.withKeyValueHandler(newKeyValueHandler);
        
        // Then
        assertEquals(rawType, newMapType.getRawClass());
        assertEquals(null, newMapType.getBindings());
        assertEquals(null, newMapType.getKeyType());
        assertEquals(null, newMapType.getValueType());
        assertEquals(null, newMapType.getValueHandler());
        assertEquals(null, newMapType.getTypeHandler());
        assertEquals(false, newMapType.isContainerType());
    }
    
    @Test
    public void testNarrow_WithSubclass_ShouldReturnMapTypeWithSubclass() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        Class<?> subclass = HashMap.class;
        
        // When
        MapType newMapType = (MapType) mapType._narrow(subclass);
        
        // Then
        assertEquals(subclass, newMapType.getRawClass());
        assertEquals(null, newMapType.getBindings());
        assertEquals(keyT, newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertEquals(null, newMapType.getValueHandler());
        assertEquals(null, newMapType.getTypeHandler());
        assertEquals(false, newMapType.isContainerType());
    }
    
    @Test
    public void testToString_ShouldReturnStringRepresentationOfMapType() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        
        // When
        String toString = mapType.toString();
        
        // Then
        assertEquals("[map type; class com.fasterxml.jackson.databind.type.MapType, null -> null]", toString);
    }
    
    @Test
    public void testWithObjectType_WithTypeHandler_ShouldReturnNewMapTypeWithUpdatedObjectTypeAndTypeHandler() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        Object h = new Object();
        
        // When
        MapType mapTypeWithObjectType = mapType.withObjectType(h);
        
        // Then
        assertEquals(rawType, mapTypeWithObjectType.getRawClass());
        assertEquals(null, mapTypeWithObjectType.getBindings());
        assertEquals(null, mapTypeWithObjectType.getKeyType());
        assertEquals(null, mapTypeWithObjectType.getValueType());
        assertEquals(h, mapTypeWithObjectType.getValueHandler());
        assertEquals(null, mapTypeWithObjectType.getTypeHandler());
        assertEquals(false, mapTypeWithObjectType.isContainerType());
    }
    
    @Test
    public void testWithNumericType_WithTypeHandler_ShouldReturnNewMapTypeWithUpdatedNumericTypeAndTypeHandler() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        Object h = new Object();
        
        // When
        MapType mapTypeWithNumericType = mapType.withNumericType(h);
        
        // Then
        assertEquals(rawType, mapTypeWithNumericType.getRawClass());
        assertEquals(null, mapTypeWithNumericType.getBindings());
        assertEquals(null, mapTypeWithNumericType.getKeyType());
        assertEquals(null, mapTypeWithNumericType.getValueType());
        assertEquals(h, mapTypeWithNumericType.getValueHandler());
        assertEquals(null, mapTypeWithNumericType.getTypeHandler());
        assertEquals(false, mapTypeWithNumericType.isContainerType());
    }
    
    @Test
    public void testWithMapLikeType_WithTypeHandler_ShouldReturnNewMapTypeWithUpdatedMapLikeTypeAndTypeHandler() {
        // Given
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        Object h = new Object();
        
        // When
        MapType mapTypeWithMapLikeType = mapType.withMapLikeType(h);
        
        // Then
        assertEquals(rawType, mapTypeWithMapLikeType.getRawClass());
        assertEquals(null, mapTypeWithMapLikeType.getBindings());
        assertEquals(null, mapTypeWithMapLikeType.getKeyType());
        assertEquals(null, mapTypeWithMapLikeType.getValueType());
        assertEquals(null, mapTypeWithMapLikeType.getValueHandler());
        assertEquals(h, mapTypeWithMapLikeType.getTypeHandler());
        assertEquals(false, mapTypeWithMapLikeType.isContainerType());
    }
}

