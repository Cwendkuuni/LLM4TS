import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test case for the constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, JavaType keyType, JavaType valueType
 @Test
 public void testMapLikeTypeConstruction() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals("Map<String, Integer>", mapLikeType.toString());
 }

 // Test case for the upgradeFrom method
 @Test
 public void testUpgradeFrom() {
 JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, String.class, Integer.class);
 assertEquals("Map<String, Integer>", mapLikeType.toString());
 }

 // Test case for the isTrueMapType method
 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test case for the getKeyType method
 @Test
 public void testgetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals(String.class, mapLikeType.getKeyType());
 }

 // Test case for the getContentType method
 @Test
 public void testGetContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals(Integer.class, mapLikeType.getContentType());
 }

 // Test case for the withKeyType method
 @Test
 public void testWithKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 JavaType javaType = mapLikeType.withKeyType(Long.TYPE);
 assertEquals("Map<Long, Integer>", javaType.toString());
 }

 // Test case for the withContentType method
 @Test
 public void testWithContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 JavaType javaType = mapLikeType.withContentType(Float.TYPE);
 assertEquals("Map<String, Float>", javaType.toString());
 }

 // Test case for the withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object handler = new MyMapHandler();
 JavaType javaType = mapLikeType.withTypeHandler(handler);
 assertEquals("Map<String, Integer>", javaType.toString());
 }

 // Test case for the withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object handler = new MyContentMapHandler();
 JavaType javaType = mapLikeType.withContentTypeHandler(handler);
 assertEquals("Map<String, Float>", javaType.toString());
 }

 // Test case for the withValueHandler method
 @Test
 public void testWithValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object handler = new MyValueMapHandler();
 JavaType javaType = mapLikeType.withValueHandler(handler);
 assertEquals("Map<String, Float>", javaType.toString());
 }

 // Test case for the withContentValueHandler method
 @Test
 public void testWithContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object handler = new MyContentValueMapHandler();
 JavaType javaType = mapLikeType.withContentValueHandler(handler);
 assertEquals("Map<String, Float>", javaType.toString());
 }

 // Test case for the withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 JavaType javaType = mapLikeType.withStaticTyping();
 assertTrue(javaType.isStatic());
 }

 // Test case for the refine method
 @Test
 public void testRefine() {
 JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = (MapLikeType) javaType.refine(HashMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null);
 assertEquals("Map<String, Integer>", mapLikeType.toString());
 }

 // Test case for the equals method
 @Test
 public void testEquals() {
 MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 MapLikeType mapLikeType3 = new MapLikeType(List.class, TypeBindings.emptyBindings(), _bogusSuperClass(List.class), null, String.class, Integer.class);
 assertTrue(mapLikeType1.equals(mapLikeType2));
 assertFalse(mapLikeType1.equals(mapLikeType3));
 }
}