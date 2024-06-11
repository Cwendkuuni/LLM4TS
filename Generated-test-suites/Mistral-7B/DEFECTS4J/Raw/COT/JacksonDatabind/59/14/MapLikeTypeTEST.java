import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

 public class MapLikeTypeTest {

 private JavaType keyType = new SimpleType("java.lang.String").makeParameterized(0);
 private JavaType valueType = new SimpleType("java.lang.Integer").makeParameterized(0);
 private JavaType mapType = MapLikeType.construct(Map.class, keyType, valueType);

 @Test
 public void testConstruct() {
 MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = new SimpleType("com.example.MyMap").makeGeneric();
 JavaType keyT = new SimpleType("java.lang.String").makeParameterized(0);
 JavaType valueT = new SimpleType("java.lang.Integer").makeParameterized(0);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyT, valueT);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(keyT, mapLikeType.getKeyType());
 assertEquals(valueT, mapLikeType.getContentType());
 }

 @Test
 public void testGetKeyType() {
 JavaType keyType1 = new SimpleType("java.lang.String").makeParameterized(0);
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType1, valueType, null, null, false);
 assertEquals(keyType1, mapLikeType.getKeyType());
 }

 @Test
 public void testGetContentType() {
 JavaType valueType1 = new SimpleType("java.lang.Integer").makeParameterized(0);
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType1, null, null, false);
 assertEquals(valueType1, mapLikeType.getContentType());
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidNarrowing() {
 JavaType baseType = new SimpleType("com.example.MyMap").makeGeneric();
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, new SimpleType("java.lang.String").makeParameterized(0),
 new SimpleType("java.lang.Integer").makeParameterized(0), null, null, false);
 try {
 mapLikeType = mapLikeType.withStaticTyping();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testNarrowing() {
 JavaType mapType1 = new MapLikeType(List.class, TypeBindings.emptyBindings(),
 null, null, new SimpleType("java.lang.Integer").makeParameterized(0),
 new SimpleType("java.lang.String").makeParameterized(0), null, null, false)
 .withStaticTyping();
 JavaType mapType2 = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(),
 null, null, new SimpleType("java.lang.String").makeParameterized(0),
 new SimpleType("java.lang.Integer").makeParameterized(0), null, null, false)
 .withStaticTyping();
 assertEquals(mapType1, mapType2);
 }

 @Test
 public void testWithKeyType() {
 JavaType keyType2 = new SimpleType("java.lang.Long").makeParameterized(0);
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 MapLikeType mapLikeType2 = mapLikeType.withKeyType(keyType2);
 assertEquals(mapLikeType.getClass(), mapLikeType2.getClass());
 assertEquals(keyType2, mapLikeType2.getKeyType());
 }

 @Test
 public void testWithContentType() {
 JavaType valueType2 = new SimpleType("java.lang.Double").makeParameterized(0);
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 MapLikeType mapLikeType2 = mapLikeType.withContentType(valueType2);
 assertEquals(mapLikeType.getClass(), mapLikeType2.getClass());
 assertEquals(valueType2, mapLikeType2.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 Object handler = new Object();
 MapLikeType mapLikeType2 = mapLikeType.withTypeHandler(handler);
 assertEquals(handler, mapLikeType2.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 Object handler = new Object();
 mapLikeType = mapLikeType.withContentTypeHandler(handler);
 assertEquals(handler, mapLikeType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 Object handler = new Object();
 MapLikeType mapLikeType2 = mapLikeType.withValueHandler(handler);
 assertEquals(handler, mapLikeType2.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 Object handler = new Object();
 mapLikeType = mapLikeType.withContentValueHandler(handler);
 assertEquals(handler, mapLikeType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 MapLikeType mapLikeType2 = mapLikeType.withStaticTyping();
 assertEquals(true, mapLikeType2.isStatic());
 assertEquals(mapLikeType.getClass(), mapLikeType2.getClass());
 }

 @Test
 public void testRefine() {
 JavaType mapType1 = new MapLikeType(List.class, TypeBindings.emptyBindings(),
 null, null, new SimpleType("java.lang.Integer").makeParameterized(0),
 new SimpleType("java.lang.String").makeParameterized(0), null, null, false)
 .withStaticTyping();
 JavaType mapType2 = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(),
 null, null, new SimpleType("java.lang.String").makeParameterized(0),
 new SimpleType("java.lang.Integer").makeParameterized(0), null, null, false)
 .withStaticTyping();
 MapLikeType refinedType = mapType1.refine(Map.class, TypeBindings.emptyBindings(),
 null, null);
 assertEquals(mapType2, refinedType);
 }

 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 assertTrue(mapLikeType.isContainerType());
 }

 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 assertTrue(mapLikeType.isMapLikeType());
 }

 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
 null, null, keyType, valueType, null, null, false);
 assertTrue(mapLikeType.isTrueMapType());
 }
 }