import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

 // Test case for construct method
 @Test
 public void testConstruct() {
 Class<Map<String, Integer>> mapType = Map.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapType expectedMapType = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
 MapType actualMapType = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
 assertEquals(expectedMapType, actualMapType);
 }

 // Test case for deprecated construct method
 @Test
 public void testConstructDeprecated() {
 Class<Map<String, Integer>> mapType = Map.class;
 JavaType keyType = String.class;
 JavaType valueType = Integer.class;
 MapType expectedMapType = MapType.construct(mapType, keyType, valueType);
 MapType actualMapType = MapType.construct(mapType, null, _bogusSuperClass(mapType), null, keyType, valueType);
 assertEquals(expectedMapType, actualMapType);
 }

 // Test case for _narrow method
 @Test
 public void testNarrow() {
 Class<HashMap<String, Integer>> subclass = HashMap.class;
 MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], String.class, Integer.class);
 MapType narrowedMapType = mapType._narrow(subclass);
 MapType expectedNarrowedMapType = new MapType(subclass, null, Object.class, new JavaType[0], String.class, Integer.class);
 assertEquals(expectedNarrowedMapType, narrowedMapType);
 }

 // Test case for withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 Object typeHandler = new Object();
 MapType expectedMapType = new MapType(mapType, null, Object.class, new JavaType[0], String.class, Integer.class, null, typeHandler, false);
 MapType actualMapType = expectedMapType.withTypeHandler(typeHandler);
 assertEquals(expectedMapType, actualMapType);
 }

 // Test case for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 Object contentTypeHandler = new Object();
 JavaType valueTypeWithHandler = Integer.class.withTypeHandler(contentTypeHandler);
 MapType expectedMapType = new MapType(mapType, null, Object.class, new JavaType[0], String.class, valueTypeWithHandler, null, null, false);
 MapType actualMapType = expectedMapType.withContentTypeHandler(contentTypeHandler);
 assertEquals(expectedMapType, actualMapType);
 }

 // Test case for withValueHandler method
 @Test
 public void testWithValueHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 Object valueHandler = new Object();
 MapType expectedMapType = new MapType(mapType, null, Object.class, new JavaType[0], String.class, Integer.class, valueHandler, null, false);
 MapType actualMapType = expectedMapType.withValueHandler(valueHandler);
 assertEquals(expectedMapType, actualMapType);
 }

 // Test case for withContentValueHandler method
 @Test
 public void testWithContentValueHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 Object contentValueHandler = new Object();
 JavaType valueTypeWithHandler = Integer.class.withValueHandler(contentValueHandler);
 MapType expectedMapType = new MapType(mapType, null, Object.class, new JavaType[0], String.class, valueTypeWithHandler, null, null, false);
 MapType actualMapType = expectedMapType.withContentValueHandler(contentValueHandler);
 assertEquals(expectedMapType, actualMapType);
 }

 // Test case for withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 Class<Map<String, Integer>> mapType = Map.class;
 MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
 MapType mapTypeWithoutStaticTyping = new MapType(mapType, null, Object.class, new JavaType[0], String.class, Integer.class, null, null, false);
 assertTrue(mapTypeWithStaticTyping._asStatic);
 assertFalse(mapTypeWithoutStaticTyping._asStatic);
 }

 // Test case for withContentType method
 @Test
 public void testWithContentType() {
 Class<Map<String, Integer>> mapType = Map.class;
 JavaType contentType = String.class;
 MapType expectedMapType = new MapType(mapType, null, Object.class, new JavaType[0], String.class, Integer.class, null, null, false);
 MapType actualMapType = expectedMapType.withContentType(contentType);
 JavaType keyType = actualMapType.getKeyType();
 assertEquals(String.class, keyType);
 JavaType valueType = actualMapType.getValueType();
 assertEquals(contentType, valueType);
 }

 // Test case for withKeyType method
 @Test
 public void testWithKeyType() {
 Class<Map<String, Integer>> mapType = Map.class;
 JavaType keyType = Integer.class;
 MapType expectedMapType = new MapType(mapType, null, Object.class, new JavaType[0], String.class, Integer.class, null, null, false);
 MapType actualMapType = expectedMapType.withKeyType(keyType);
 JavaType keyTypeInMapType = actualMapType.getKeyType();
 assertEquals(keyType, keyTypeInMapType);
 }

 // Test case for refine method
 @Test
 public void testRefine() {
 Class<HashMap<String, Integer>> rawType = HashMap.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 MapType expectedMapType = new MapType(rawType, bindings, superClass, superInterfaces, String.class, Integer.class, null, null, false);
 MapType actualMapType = new MapType(Map.class, null, Object.class, new JavaType[0], String.class, Integer.class).refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(expectedMapType, actualMapType);
 }

 // Test case for withKeyTypeHandler method
 @Test
 public void testWithKeyTypeHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 Object keyTypeHandler = new Object();
 MapType expectedMapType = new MapType(mapType, null, Object.class, new JavaType[0], String.class, Integer.class, null, null, false);
 MapType actualMapType = expectedMapType.withKeyTypeHandler(keyTypeHandler);
 JavaType keyType = actualMapType.getKeyType();
 JavaType keyTypeWithHandler = keyType.withTypeHandler(keyTypeHandler);
 assertEquals(keyTypeWithHandler, actualMapType.getKeyType());
 }

 // Test case for withKeyValueHandler method
 @Test
 public void testWithKeyValueHandler() {
 Class<Map<String, Integer>> mapType = Map.class;
 Object keyValueHandler = new Object();
 MapType expectedMapType = new MapType(mapType, null, Object.class, new JavaType[0], String.class, Integer.class, null, null, false);
 MapType actualMapType = expectedMapType.withKeyValueHandler(keyValueHandler);
 JavaType keyType = actualMapType.getKeyType();
 JavaType valueType = actualMapType.getValueType();
 JavaType keyTypeWithHandler = keyType.withValueHandler(keyValueHandler);
 JavaType valueTypeWithHandler = valueType.withValueHandler(keyValueHandler);
 assertEquals(keyTypeWithHandler, actualMapType.getKeyType());
 assertEquals(valueTypeWithHandler, actualMapType.getValueType());
 }
}