import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 private JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
 private JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
 private JavaType mapType = MapLikeType.construct(Map.class, keyType, valueType);

 @Test
 public void testConstructorWithClassMapTypeKeyValue() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testConstructorWithTypeBaseKeyValue() {
 TypeBase base = new TypeBase() {
 @Override
 public JavaType getRawClass() {
 return Map.class;
 }
 };
 MapLikeType mapLikeType = new MapLikeType(base, keyType, valueType);
 assertEquals(Map.class, mapLikeType.getRawClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test
 public void testUpgradeFromJavaType() {
 JavaType javaType = JavaType.constructCollectionType(Map.class, String.class,
 Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, keyType, valueType);
 assertEquals(javaType.getRawClass(), mapLikeType.getRawClass());
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(valueType, mapLikeType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromNonTypeBase() {
 JavaType javaType = new JavaType() {
 };
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, keyType, valueType);
 fail("Illegal argument exception should be thrown");
 }

 @Test
 public void testNarrow() {
 JavaType subclass = new TypeBase() {
 @Override
 public Class<?> getRawClass() {
 return HashMap.class;
 }
 }.wrap();
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 MapLikeType narrowMapLikeType = mapLikeType.narrow(subclass);
 assertEquals(HashMap.class, narrowMapLikeType.getRawClass());
 }

 @Test
 public void testWithKeyType() {
 JavaType newKeyType = JavaType.constructCollectionType(List.class, String.class);
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
 assertEquals(keyType, mapLikeType.getKeyType());
 assertEquals(newKeyType, newMapLikeType.getKeyType());
 }

 @Test
 public void testWithContentType() {
 JavaType newValueType = JavaType.constructCollectionType(List.class, Long.class);
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
 assertEquals(valueType, mapLikeType.getContentType());
 assertEquals(newValueType, newMapLikeType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 Object handler = new Object() {};
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
 assertNotSame(mapLikeType, newMapLikeType);
 }

 @Test
 public void testWithContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 Object handler = new Object() {};
 JavaType newValueType = JavaType.constructCollectionType(List.class, Long.class)
 .withContentTypeHandler(handler);
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
 assertEquals(valueType, mapLikeType.getContentType());
 assertEquals(newValueType, newMapLikeType.getContentType());
 }

 @Test
 public void testWithValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 Object handler = new Object() {};
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
 assertNotSame(mapLikeType, newMapLikeType);
 }

 @Test
 public void testWithContentValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 Object handler = new Object() {};
 JavaType newValueType = valueType.withValueHandler(handler);
 MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
 assertEquals(valueType, mapLikeType.getContentType());
 assertEquals(newValueType, newMapLikeType.getContentType());
 }

 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
 assertTrue(mapLikeType.isStaticTyping());
 assertTrue(newMapLikeType.isStaticTyping());
 assertFalse(mapLikeType.equals(newMapLikeType));
 }

 @Test
 public void testRefine() {
 JavaType rawType = HashMap.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[] { Collection.class };
 MapLikeType mapLikeType = new MapLikeType(rawType, bindings, superClass,
 superInterfaces, keyType, valueType, null, null, false);
 MapLikeType newMapLikeType = (MapLikeType) mapLikeType.refine(rawType,
 bindings, superClass, superInterfaces);
 assertEquals(mapLikeType, newMapLikeType);
 }

 @Test
 public void testToString() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, keyType, valueType);
 assertEquals("[map-like type; class Map, List<String> -> List<Integer>]",
 mapLikeType.toString());
 }

 @Test
 public void testEquals() {
 MapLikeType mapLikeType1 = new MapLikeType(Map.class, keyType, valueType);
 MapLikeType mapLikeType2 = new MapLikeType(Map.class, keyType, valueType);
 MapLikeType mapLikeType3 = new MapLikeType(HashMap.class, keyType, valueType);
 MapLikeType mapLikeType4 = new MapLikeType(Map.class, valueType, keyType);
 MapLikeType mapLikeType5 = new MapLikeType(HashMap.class, valueType, keyType);
 MapLikeType mapLikeType6 = new MapLikeType(HashMap.class, keyType, valueType);
 MapLikeType mapLikeType7 = new Object();
 assertTrue(mapLikeType1.equals(mapLikeType1));
 assertTrue(mapLikeType1.equals(mapLikeType2));
 assertFalse(mapLikeType1.equals(mapLikeType3));
 assertFalse(mapLikeType1.equals(mapLikeType4));
 assertFalse(mapLikeType1.equals(mapLikeType5));
 assertFalse(mapLikeType1.equals(mapLikeType6));
 assertFalse(mapLikeType1.equals(mapLikeType7));
 }
}