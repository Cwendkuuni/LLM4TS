import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test case for constructor with Class, TypeBindings, superClass, superInterfaces, keyT, valueT
 @Test
 public void testConstruct() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(StringType.getInstance(), mapLikeType.getKeyType());
 assertEquals(NumberType.getInstance(), mapLikeType.getValueType());
 }

 // Test case for constructor with TypeBase, keyT, valueT
 @Test
 public void testConstructWithTypeBase() {
 MapLikeType mapLikeType = new MapLikeType(new TypeBase() {
 @Override
 public Class<?> getRawClass() {
 return Map.class;
 }
 }, StringType.getInstance(), NumberType.getInstance());
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(StringType.getInstance(), mapLikeType.getKeyType());
 assertEquals(NumberType.getInstance(), mapLikeType.getValueType());
 }

 // Test case for upgradeFrom method
 @Test
 public void testUpgradeFrom() {
 JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Number.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, StringType.getInstance(), NumberType.getInstance());
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(StringType.getInstance(), mapLikeType.getKeyType());
 assertEquals(NumberType.getInstance(), mapLikeType.getValueType());
 }

 // Test case for isTrueMapType method
 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test case for isContainerType method
 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertTrue(mapLikeType.isContainerType());
 }

 // Test case for isMapLikeType method
 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test case for getKeyType method
 @Test
 public void testGetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertEquals(StringType.getInstance(), mapLikeType.getKeyType());
 }

 // Test case for getValueType method
 @Test
 public void testGetValueType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertEquals(NumberType.getInstance(), mapLikeType.getValueType());
 }

 // Test case for getContentType method
 @Test
 public void testGetContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertEquals(NumberType.getInstance(), mapLikeType.getContentType());
 }

 // Test case for getContentValueHandler method
 @Test
 public void testGetContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertNull(mapLikeType.getContentValueHandler());
 }

 // Test case for getContentTypeHandler method
 @Test
 public void testGetContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertNull(mapLikeType.getContentTypeHandler());
 }

 // Test case for hasHandlers method
 @Test
 public void testHasHandlers() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertFalse(mapLikeType.hasHandlers());
 }

 // Test case for withKeyType method
 @Test
 public void testWithKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 MapLikeType newMapLikeType = mapLikeType.withKeyType(IntegerType.getInstance());
 assertEquals(IntegerType.getInstance(), newMapLikeType.getKeyType());
 }

 // Test case for withContentType method
 @Test
 public void testWithContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 MapLikeType newMapLikeType = mapLikeType.withContentType(BooleanType.getInstance());
 assertEquals(BooleanType.getInstance(), newMapLikeType.getValueType());
 }

 // Test case for withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
 assertEquals(handler, newMapLikeType.getTypeHandler());
 }

 // Test case for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
 assertEquals(handler, newMapLikeType.getContentTypeHandler());
 }

 // Test case for withValueHandler method
 @Test
 public void testWithValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
 assertEquals(handler, newMapLikeType.getValueHandler());
 }

 // Test case for withContentValueHandler method
 @Test
 public void testWithContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
 assertEquals(handler, newMapLikeType.getContentValueHandler());
 }

 // Test case for withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
 assertTrue(newMapLikeType.isStaticTyping());
 }

 // Test case for refine method
 @Test
 public void testRefine() {
 JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Number.class);
 MapLikeType mapLikeType = new MapLikeType(javaType, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 MapLikeType newMapLikeType = (MapLikeType) mapLikeType.refine(HashMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null);
 assertEquals(HashMap.class, newMapLikeType.getRawClass());
 }

 // Test case for buildCanonicalName method
 @Test
 public void testBuildCanonicalName() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), NumberType.getInstance());
 assertEquals("[map-like type; class java.util.Map, java.lang.String->java.lang.Number]", mapLikeType.buildCanonicalName().toString());
 }
}