import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 private JavaType keyType = new SimpleType("java.lang.String");
 private JavaType valueType = new SimpleType("java.lang.Integer");

 @Test
 public void testMapLikeTypeConstructor() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = new SimpleType("java.util.HashMap");
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testWithKeyType() {
 JavaType newKeyType = new SimpleType("java.lang.Integer");
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
 assertEquals(newKeyType, newMapLikeType.getKeyType());
 }

 @Test
 public void testWithContentType() {
 JavaType newValueType = new SimpleType("java.lang.Long");
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
 assertEquals(newValueType, newMapLikeType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
 assertEquals(handler, newMapLikeType.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
 assertEquals(handler, newMapLikeType.getValueHandler());
 }

 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 assertTrue(mapLikeType.isTrueMapType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseClass() {
 JavaType baseType = new SimpleType("java.lang.Object");
 MapLikeType.upgradeFrom(baseType, keyType, valueType);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 MapLikeType staticMapLikeType = mapLikeType.withStaticTyping();
 assertTrue(staticMapLikeType.isStatic());
 }

 @Test
 public void testBuildCanonicalName() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.buildCanonicalName());
 }
}