
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapLikeTypeTest {
  
  @Test
  public void testConstructor() {
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    
    MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, valueType, null, null, false);
    assertEquals(HashMap.class, mapLikeType.getRawClass());
    assertNotNull(mapLikeType.getBindings());
    assertNotNull(mapLikeType.getSuperClass());
    assertNotNull(mapLikeType.getSuperInterfaces());
    assertEquals(keyType, mapLikeType.getKeyType());
    assertEquals(valueType, mapLikeType.getContentType());
    assertFalse(mapLikeType.hasHandlers());
  }
  
  @Test
  public void testUpgradeFrom() {
    JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    
    MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
    assertEquals(HashMap.class, mapLikeType.getRawClass());
    assertNotNull(mapLikeType.getBindings());
    assertNotNull(mapLikeType.getSuperClass());
    assertNotNull(mapLikeType.getSuperInterfaces());
    assertEquals(keyType, mapLikeType.getKeyType());
    assertEquals(valueType, mapLikeType.getContentType());
    assertFalse(mapLikeType.hasHandlers());
  }
  
  @Test
  public void testWithKeyType() {
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    
    MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, valueType, null, null, false);
    JavaType newKeyType = TypeFactory.defaultInstance().constructType(Boolean.class);
    
    MapLikeType updatedType = mapLikeType.withKeyType(newKeyType);
    assertEquals(HashMap.class, updatedType.getRawClass());
    assertNotNull(updatedType.getBindings());
    assertNotNull(updatedType.getSuperClass());
    assertNotNull(updatedType.getSuperInterfaces());
    assertEquals(newKeyType, updatedType.getKeyType());
    assertEquals(valueType, updatedType.getContentType());
    assertFalse(updatedType.hasHandlers());
  }
  
  @Test
  public void testWithContentType() {
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    
    MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, valueType, null, null, false);
    JavaType newContentType = TypeFactory.defaultInstance().constructType(Long.class);
    
    MapLikeType updatedType = mapLikeType.withContentType(newContentType);
    assertEquals(HashMap.class, updatedType.getRawClass());
    assertNotNull(updatedType.getBindings());
    assertNotNull(updatedType.getSuperClass());
    assertNotNull(updatedType.getSuperInterfaces());
    assertEquals(keyType, updatedType.getKeyType());
    assertEquals(newContentType, updatedType.getContentType());
    assertFalse(updatedType.hasHandlers());
  }
  
  @Test
  public void testWithStaticTyping() {
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    
    MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, valueType, null, null, false);
    
    assertFalse(mapLikeType.isStaticTyping());
    
    MapLikeType staticType = mapLikeType.withStaticTyping();
    
    assertTrue(staticType.isStaticTyping());
  }
  
  @Test
  public void testIsTrueMapType() {
    JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
    
    MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, valueType, null, null, false);
    
    assertTrue(mapLikeType.isTrueMapType());
  }
  
}
