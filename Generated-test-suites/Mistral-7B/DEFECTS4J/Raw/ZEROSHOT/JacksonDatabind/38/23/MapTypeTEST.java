import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class MapTypeTest {

 private JavaType keyType = JavaType.constructRawListType(String.class, java.util.List.class);
 private JavaType valueType = JavaType.constructRawType(Integer.class);

 @Test
 public void testConstruct() {
 MapType actualMapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 assertEquals(expectedMapType.toString(), actualMapType.toString());
 }

 @Test
 @SuppressWarnings("deprecation")
 public void testConstructDeprecated() {
 MapType actualMapType = MapType.construct(Map.class, keyType, valueType);
 MapType expectedMapType = new MapType(Map.class, null, _bogusSuperClass(Map.class), null, keyType, valueType);
 assertEquals(expectedMapType.toString(), actualMapType.toString());
 }

 @Test
 public void testWithTypeHandler() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Object();
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType, valueType, handler, null, false);
 assertEquals(expectedMapType.toString(), actualMapType.withTypeHandler(handler).toString());
 }

 @Test
 public void testWithContentTypeHandler() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Object();
 JavaType contentType = JavaType.constructRawListType(String.class, java.util.List.class);
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType, contentType, null, handler, false);
 assertEquals(expectedMapType.toString(), actualMapType.withContentTypeHandler(handler).withContentType(contentType).toString());
 }

 @Test
 public void testWithValueHandler() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Object();
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType, valueType, handler, null, false);
 assertEquals(expectedMapType.toString(), actualMapType.withValueHandler(handler).toString());
 }

 @Test
 public void testWithContentValueHandler() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Object();
 JavaType contentValueType = JavaType.constructRawType(String.class);
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType, contentValueType, null, handler, false);
 assertEquals(expectedMapType.toString(), actualMapType.withContentValueHandler(handler).withContentValueType(contentValueType).toString());
 }

 @Test
 public void testWithStaticTyping() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType.withStaticTyping(), valueType.withStaticTyping(), null, null, true);
 assertEquals(expectedMapType.toString(), actualMapType.withStaticTyping().toString());
 }

 @Test
 public void testWithContentType() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 JavaType contentType = JavaType.constructRawListType(String.class, java.util.List.class);
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType, contentType, null, null, false);
 assertEquals(expectedMapType.toString(), actualMapType.withContentType(contentType).toString());
 }

 @Test
 public void testWithKeyType() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 JavaType keyType1 = JavaType.constructRawType(Integer.class);
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType1, valueType, null, null, false);
 assertEquals(expectedMapType.toString(), actualMapType.withKeyType(keyType1).toString());
 }

 @Test
 public void testRefine() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 Class<?> rawType = HashMap.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.constructRawType(HashMap.class);
 JavaType[] superInterfaces = new JavaType[0];
 MapType expectedMapType = new MapType(rawType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
 assertEquals(expectedMapType.toString(), actualMapType.refine(rawType, bindings, superClass, superInterfaces).toString());
 }

 @Test
 public void testWithKeyTypeHandler() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Object();
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType.withTypeHandler(handler), valueType, null, null, false);
 assertEquals(expectedMapType.toString(), actualMapType.withKeyTypeHandler(handler).toString());
 }

 @Test
 public void testWithKeyValueHandler() {
 MapType actualMapType = new MapType(Map.class, null, null, null, keyType, valueType);
 Object handler = new Object();
 MapType expectedMapType = new MapType(Map.class, null, null, null, keyType, valueType, null, handler, false);
 assertEquals(expectedMapType.toString(), actualMapType.withKeyValueHandler(handler).toString());
 }

 @Test
 public void testToString() {
 MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
 String expected = "[map type; class Map<K,V>, " + keyType.getRawClass().getName() + " -> " + valueType.getRawClass().getName() + "]";
 String actual = mapType.toString();
 assertEquals(expected, actual);
 }
}