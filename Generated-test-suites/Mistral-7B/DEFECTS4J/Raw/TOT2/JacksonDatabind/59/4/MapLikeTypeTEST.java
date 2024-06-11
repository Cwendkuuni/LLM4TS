import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test constructor with valid arguments
 @Test
 public void testMapLikeTypeConstruction() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals("Map<String, Integer>", mapLikeType.toString());
 }

 // Test upgradeFrom method with valid arguments
 @Test
 public void testUpgradeFromMethod() {
 JavaType baseType = new TypeFactory().constructType(HashMap.class);
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
 assertEquals("Map<String, Integer>", mapLikeType.toString());
 }

 // Test isContainerType method
 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isContainerType());
 }

 // Test isMapLikeType method
 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test getKeyType method
 @Test
 public void testgetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals(String.class, mapLikeType.getKeyType());
 }

 // Test getContentType method
 @Test
 public void testGetContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertEquals(Integer.class, mapLikeType.getContentType());
 }

 // Test hasHandlers method
 @Test
 public void testHasHandlers() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.hasHandlers());
 }

 // Test isTrueMapType method
 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test withKeyType method with valid arguments
 @Test
 public void testWithKeyTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 JavaType newKeyType = Integer.class;
 MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
 assertEquals("Map<Integer, Integer>", newMapLikeType.toString());
 }

 // Test withContentType method with valid arguments
 @Test
 public void testWithContentTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 JavaType newContentType = Float.class;
 MapLikeType newMapLikeType = mapLikeType.withContentType(newContentType);
 assertEquals("Map<String, Float>", newMapLikeType.toString());
 }

 // Test withTypeHandler method with valid arguments
 @Test
 public void testWithTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object newHandler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(newHandler);
 assertNotSame(mapLikeType.getTypeHandler(), newMapLikeType.getTypeHandler());
 }

 // Test withContentTypeHandler method with valid arguments
 @Test
 public void testWithContentTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object newHandler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newHandler);
 assertNotSame(mapLikeType.getTypeHandler(), newMapLikeType.getTypeHandler());
 }

 // Test withKeyTypeHandler method with valid arguments
 @Test
 public void testWithKeyTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object newHandler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(newHandler);
 assertNotSame(mapLikeType.getTypeHandler(), newMapLikeType.getTypeHandler());
 }

 // Test withKeyValueHandler method with valid arguments
 @Test
 public void testWithKeyValueHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 Object newHandler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(newHandler);
 assertNotSame(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
 }

 // Test withStaticTyping method
 @Test
 public void testWithStaticTypingMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
 assertTrue(newMapLikeType.isStaticTyping());
 }

 // Test refine method with valid arguments
 @Test
 public void testRefineMethod() {
 JavaType rawType = HashMap.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = new JavaType[] { List.class };
 MapLikeType mapLikeType = new MapLikeType(rawType, bindings, superClass, superInterfaces, String.class, Integer.class);
 MapLikeType newMapLikeType = mapLikeType.refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(mapLikeType, newMapLikeType);
 }

 // Test buildCanonicalName method
 @Test
 public void testBuildCanonicalNameMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
 StringBuilder expected = new StringBuilder("[map-like type; class java.util.Map, Ljava/lang/String;Ljava/lang/Integer;]");
 StringBuilder actual = mapLikeType.buildCanonicalName();
 assertEquals(expected.toString(), actual.toString());
 }
}