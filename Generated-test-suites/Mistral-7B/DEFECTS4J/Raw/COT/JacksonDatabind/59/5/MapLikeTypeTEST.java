import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 private JavaType keyType = new BasicType(int.class);
 private JavaType valueType = new BasicType(String.class);
 private JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

 @Test
 public void testConstructorWithClassMapAndTypeBindings() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertEquals(Map.class, mapLikeType.getRawType());
 Assert.assertEquals(keyType, mapLikeType.getKeyType());
 Assert.assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testConstructorWithTypeBaseAndKeyTypeAndValueType() {
 MapLikeType mapLikeType = new MapLikeType((TypeBase) Map.class, keyType, valueType);
 Assert.assertEquals(Map.class, mapLikeType.getRawType());
 Assert.assertEquals(keyType, mapLikeType.getKeyType());
 Assert.assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testUpgradeFromJavaType() {
 JavaType baseType = new BasicType(List.class);
 JavaType keyT = new BasicType(int.class);
 JavaType valueT = new BasicType(String.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyT, valueT);
 Assert.assertNotNull(mapLikeType);
 Assert.assertEquals(Map.class, mapLikeType.getRawType());
 Assert.assertEquals(keyT, mapLikeType.getKeyType());
 Assert.assertEquals(valueT, mapLikeType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType baseType = new BasicType(String.class);
 JavaType keyT = new BasicType(int.class);
 JavaType valueT = new BasicType(String.class);
 MapLikeType.upgradeFrom(baseType, keyT, valueT);
 }

 @Test
 public void testFactoryMethodWithTypeBaseKeyTypeAndValueType() {
 TypeBase baseType = new TypeBase() {
 @Override
 public JavaType getType(Class<?> rawType, TypeBindings bindings,
 JavaType superclass, JavaType[] superInterfaces) {
 return null;
 }
 };
 JavaType keyType = new BasicType(int.class);
 JavaType valueType = new BasicType(String.class);
 MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
 Assert.assertEquals(Map.class, mapLikeType.getRawType());
 Assert.assertEquals(keyType, mapLikeType.getKeyType());
 Assert.assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testNarrowMethod() {
 JavaType subclass = new TypeBase() {
 @Override
 public JavaType getType(Class<?> rawType, TypeBindings bindings,
 JavaType superclass, JavaType[] superInterfaces) {
 return null;
 }
 }.getType(Map.class, TypeBindings.emptyBindings(), MapLikeType.class, null);
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(new Object() {});
 Assert.assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
 Assert.assertEquals(subclass, newMapLikeType.getType());
 }

 @Test
 public void testWithKeyType() {
 JavaType newKeyType = new BasicType(long.class);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
 Assert.assertNotSame(mapLikeType, newMapLikeType);
 Assert.assertEquals(mapLikeType.getRawType(), newMapLikeType.getRawType());
 Assert.assertNotEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
 Assert.assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
 }

 @Test
 public void testWithContentType() {
 JavaType newValueType = new BasicType(double.class);
 MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
 Assert.assertNotSame(mapLikeType, newMapLikeType);
 Assert.assertEquals(mapLikeType.getRawType(), newMapLikeType.getRawType());
 Assert.assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
 Assert.assertNotEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Object newValueHandler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(newValueHandler);
 Assert.assertNotSame(mapLikeType, newMapLikeType);
 Assert.assertEquals(mapLikeType.getRawType(), newMapLikeType.getRawType());
 Assert.assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
 Assert.assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
 Assert.assertEquals(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
 Assert.assertNotEquals(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Object newContentTypeHandler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newContentTypeHandler);
 Assert.assertNotSame(mapLikeType, newMapLikeType);
 Assert.assertEquals(mapLikeType.getRawType(), newMapLikeType.getRawType());
 Assert.assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
 Assert.assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
 Assert.assertEquals(mapLikeType.getTypeHandler(), newMapLikeType.getTypeHandler());
 Assert.assertNotEquals(mapLikeType.getTypeHandler(), newMapLikeType.getTypeHandler());
 Assert.assertEquals(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
 }

 @Test
 public void testWithKeyTypeHandler() {
 Object newKeyTypeHandler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(newKeyTypeHandler);
 Assert.assertNotSame(mapLikeType, newMapLikeType);
 Assert.assertEquals(mapLikeType.getRawType(), newMapLikeType.getRawType());
 Assert.assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
 Assert.assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
 Assert.assertEquals(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
 Assert.assertNotEquals(mapLikeType.getKeyTypeHandler(), newMapLikeType.getKeyTypeHandler());
 }

 @Test
 public void testWithKeyValueHandler() {
 Object newKeyValueHandler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(newKeyValueHandler);
 Assert.assertNotSame(mapLikeType, newMapLikeType);
 Assert.assertEquals(mapLikeType.getRawType(), newMapLikeType.getRawType());
 Assert.assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
 Assert.assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
 Assert.assertEquals(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
 Assert.assertNotEquals(mapLikeType.getKeyValueHandler(), newMapLikeType.getKeyValueHandler());
 }

 @Test
 public void testIsContainerType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertTrue(mapLikeType.isContainerType());
 }

 @Test
 public void testIsMapLikeType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertTrue(mapLikeType.isMapLikeType());
 }

 @Test
 public void testGetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertEquals(keyType, mapLikeType.getKeyType());
 }

 @Test
 public void testGetContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertEquals(valueType.getValueHandler(), mapLikeType.getContentValueHandler());
 }

 @Test
 public void testGetContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertEquals(valueType.getTypeHandler(), mapLikeType.getContentTypeHandler());
 }

 @Test
 public void testHasHandlers() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertTrue(mapLikeType.hasHandlers());
 }

 @Test
 public void testBuildCanonicalName() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertEquals("[map-like type; class java.util.Map, class java.lang.Integer -> class java.lang.String]", mapLikeType.buildCanonicalName());
 }

 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertTrue(mapLikeType.isTrueMapType());
 }

 @Test
 public void testToString() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertEquals("[map-like type; class java.util.Map, class java.lang.Integer -> class java.lang.String]", mapLikeType.toString());
 }

 @Test
 public void testEquals() {
 MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 MapLikeType mapLikeType3 = new MapLikeType(List.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
 Assert.assertEquals(mapLikeType1, mapLikeType2);
 Assert.assertNotEquals(mapLikeType1, mapLikeType3);
 }
}