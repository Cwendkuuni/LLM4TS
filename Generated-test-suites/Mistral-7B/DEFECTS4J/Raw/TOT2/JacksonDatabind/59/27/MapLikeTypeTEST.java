import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test for constructor with Class, TypeBindings, JavaType[], JavaType keyT, JavaType valueT, Object valueHandler, Object typeHandler, boolean asStatic
 @Test
 public void testMapLikeTypeConstructor() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", mapLikeType.getClassName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 assertFalse(mapLikeType.isStatic());
 }

 // Test for constructor with TypeBase, JavaType keyT, JavaType valueT
 @Test
 public void testMapLikeTypeUpgradeConstructor() {
 JavaType baseType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, String.class, Integer.class);
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", mapLikeType.getClassName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getValueType());
 assertFalse(mapLikeType.isStatic());
 }

 // Test for factory method upgradeFrom(JavaType baseType, JavaType keyT, JavaType valueT)
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMapLikeTypeUpgrade() {
 JavaType baseType = TypeFactory.defaultInstance().constructType(List.class, String.class);
 MapLikeType.upgradeFrom(baseType, String.class, Integer.class);
 }

 // Test for deprecated construct method
 @Test
 public void testDeprecatedConstructMethod() {
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 TypeBindings bindings = TypeBindings.emptyBindings();
 MapLikeType mapLikeType = new MapLikeType(Map.class, bindings, _bogusSuperClass(Map.class), null, keyType, valueType, null, null, false);
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", mapLikeType.getClassName());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getValueType());
 assertFalse(mapLikeType.isStatic());
 }

 // Test for deprecated _narrow method
 @Test
 public void testDeprecatedNarrowMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 JavaType narrowedType = mapLikeType.withKeyType(Integer.class);
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", narrowedType.getClassName());
 assertEquals(Integer.class, narrowedType.getKeyType());
 assertEquals(Integer.class, narrowedType.getValueType());
 assertFalse(narrowedType.isStatic());
 }

 // Test for withKeyType method
 @Test
 public void testWithKeyTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 JavaType newKeyType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", newMapLikeType.getClassName());
 assertEquals(newKeyType, newMapLikeType.getKeyType());
 assertEquals(Integer.class, newMapLikeType.getValueType());
 assertFalse(newMapLikeType.isStatic());
 }

 // Test for withContentType method
 @Test
 public void testWithContentTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 JavaType newValueType = TypeFactory.defaultInstance().constructType(Double.class);
 MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", newMapLikeType.getClassName());
 assertEquals(String.class, newMapLikeType.getKeyType());
 assertEquals(newValueType, newMapLikeType.getValueType());
 assertFalse(newMapLikeType.isStatic());
 }

 // Test for withTypeHandler method
 @Test
 public void testWithTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(new MapTypeSerializer());
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", newMapLikeType.getClassName());
 assertEquals(String.class, newMapLikeType.getKeyType());
 assertEquals(Integer.class, newMapLikeType.getValueType());
 assertNotNull(newMapLikeType.getTypeHandler());
 assertFalse(newMapLikeType.isStatic());
 }

 // Test for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(new MapContentSerializer());
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", newMapLikeType.getClassName());
 assertEquals(String.class, newMapLikeType.getKeyType());
 assertEquals(Integer.class, newMapLikeType.getValueType());
 assertNotNull(newMapLikeType.getValueHandler());
 assertFalse(newMapLikeType.isStatic());
 }

 // Test for withValueHandler method
 @Test
 public void testWithValueHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(new MapValueDeserializer());
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", newMapLikeType.getClassName());
 assertEquals(String.class, newMapLikeType.getKeyType());
 assertEquals(Integer.class, newMapLikeType.getValueType());
 assertNotNull(newMapLikeType.getValueHandler());
 assertFalse(newMapLikeType.isStatic());
 }

 // Test for withContentValueHandler method
 @Test
 public void testWithContentValueHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(new MapContentValueDeserializer());
 assertEquals("com.fasterxml.jackson.databind.type.MapLikeType", newMapLikeType.getClassName());
 assertEquals(String.class, newMapLikeType.getKeyType());
 assertEquals(Integer.class, newMapLikeType.getValueType());
 assertNotNull(newMapLikeType.getValueHandler());
 assertFalse(newMapLikeType.isStatic());
 }

 // Test for isContainerType method
 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isContainerType());
 }

 // Test for isMapLikeType method
 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test for getKeyType method
 @Test
 public void testGetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals(String.class, mapLikeType.getKeyType());
 }

 // Test for getContentType method
 @Test
 public void testGetContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals(Integer.class, mapLikeType.getValueType());
 }

 // Test for getContentValueHandler method
 @Test
 public void testGetContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertNull(mapLikeType.getValueHandler());
 }

 // Test for getContentTypeHandler method
 @Test
 public void testGetContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertNotNull(mapLikeType.getTypeHandler());
 }

 // Test for hasHandlers method
 @Test
 public void testHasHandlers() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.hasHandlers());
 }

 // Test for buildCanonicalName method
 @Test
 public void testBuildCanonicalName() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]", mapLikeType.buildCanonicalName());
 }
}