import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test case for constructor with Class, TypeBindings, JavaType[], JavaType, JavaType, Object, Object, boolean
 @Test
 public void testMapLikeTypeConstruction() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals("java.util.Map", mapLikeType.getCanonicalName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getContentType());
 }

 // Test case for constructor with TypeBase, JavaType, JavaType
 @Test
 public void testMapLikeTypeUpgradeFrom() {
 JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
 JavaType keyT = String.class;
 JavaType valueT = Integer.class;
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyT, valueT);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.getCanonicalName());
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getContentType());
 }

 // Test case for method isContainerType()
 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isContainerType());
 }

 // Test case for method isMapLikeType()
 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test case for method getKeyType() and getContentType()
 @Test
 public void testGetKeyTypeAndContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertEquals(String.class, mapLikeType.getKeyType());
 assertEquals(Integer.class, mapLikeType.getContentType());
 }

 // Test case for method withKeyType() and withContentType()
 @Test
 public void testWithKeyTypeAndContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 JavaType newKeyType = Boolean.class;
 JavaType newContentType = Double.class;
 MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType).withContentType(newContentType);
 assertEquals(Boolean.class, newMapLikeType.getKeyType());
 assertEquals(Double.class, newMapLikeType.getContentType());
 }

 // Test case for method withTypeHandler(), withContentTypeHandler(), withValueHandler(), withContentValueHandler() and withKeyTypeHandler() and withKeyValueHandler()
 @Test
 public void testHandlers() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 Object handler = new Object();
 MapLikeType withHandlerKey = mapLikeType.withKeyTypeHandler(handler);
 MapLikeType withHandlerValue = withHandlerKey.withContentTypeHandler(handler);
 MapLikeType withHandlerContent = withHandlerValue.withContentValueHandler(handler);
 MapLikeType withHandlerKeyValue = withHandlerContent.withKeyValueHandler(handler);
 assertNotNull(withHandlerKey.getKeyTypeHandler());
 assertSame(handler, withHandlerKey.getKeyTypeHandler());
 assertNotNull(withHandlerValue.getValueHandler());
 assertSame(handler, withHandlerValue.getValueHandler());
 assertNotNull(withHandlerContent.getContentValueHandler());
 assertSame(handler, withHandlerContent.getContentValueHandler());
 assertNotNull(withHandlerKeyValue.getKeyTypeHandler());
 assertSame(handler, withHandlerKeyValue.getKeyTypeHandler());
 assertSame(handler, withHandlerKeyValue.getValueHandler());
 }

 // Test case for method isTrueMapType()
 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test edge cases and exceptions

 // Test case for constructor with invalid TypeBase
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeBase() {
 JavaType baseType = new SimpleType("InvalidTypeBase");
 JavaType keyT = String.class;
 JavaType valueT = Integer.class;
 MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyT, valueT, null, null, false);
 }

 // Test case for constructor with null keyType or valueType
 @Test(expected = IllegalArgumentException.class)
 public void testNullKeyTypeOrValueType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, Integer.class, null, null, false);
 }

 // Test case for constructor with null valueHandler or typeHandler
 @Test
 public void testNullValueHandlerOrTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 mapLikeType = mapLikeType.withValueHandler(null);
 assertNull(mapLikeType.getValueHandler());
 mapLikeType = mapLikeType.withTypeHandler(null);
 assertNull(mapLikeType.getTypeHandler());
 }

 // Test case for method withStaticTyping() with asStatic = true
 @Test
 public void testWithStaticTypingTrue() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 MapLikeType withStaticTyping = mapLikeType.withStaticTyping();
 assertTrue(withStaticTyping.isStatic());
 }

 // Test case for method withStaticTyping() with asStatic = false
 @Test
 public void testWithStaticTypingFalse() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false).withStaticTyping();
 MapLikeType withoutStaticTyping = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 assertFalse(withoutStaticTyping.isStatic());
 assertFalse(mapLikeType.isStatic());
 }

 // Test case for method refine() with null rawType or bindings
 @Test(expected = IllegalArgumentException.class)
 public void testNullRawTypeOrBindings() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 JavaType refinedType = mapLikeType.refine(null, null, _bogusSuperClass(Map.class), null);
 }

 // Test case for method refine() with null superInterfaces
 @Test
 public void testNullSuperInterfaces() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
 JavaType refinedType = mapLikeType.refine(Map.class, null, _bogusSuperClass(Map.class), null);
 }
}