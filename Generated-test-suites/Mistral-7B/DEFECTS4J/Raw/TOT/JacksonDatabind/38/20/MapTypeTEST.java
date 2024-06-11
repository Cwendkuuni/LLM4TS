import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MapTypeTest {

 // Test case for construct method
 @Test
 public void testConstruct() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", mapType.toString());
 }

 // Test case for withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
 Object handler = new MyMapTypeHandler();
 MapType newMapType = mapType.withTypeHandler(handler);
 assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", newMapType.toString());
 }

 // Test case for withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
 Object handler = new MyMapContentTypeHandler();
 MapType newMapType = mapType.withContentTypeHandler(handler);
 assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", newMapType.toString());
 }

 // Test case for withValueHandler method
 @Test
 public void testWithValueHandler() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
 Object handler = new MyMapValueHandler();
 MapType newMapType = mapType.withValueHandler(handler);
 assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", newMapType.toString());
 }

 // Test case for withContentValueHandler method
 @Test
 public void testWithContentValueHandler() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
 Object handler = new MyMapContentValueHandler();
 MapType newMapType = mapType.withContentValueHandler(handler);
 assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer", newMapType.toString());
 }

 // Test case for withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
 MapType newMapType = mapType.withStaticTyping();
 assertEquals("map type; class Map<java.lang.String, java.lang.Integer> [static]", newMapType.toString());
 }

 // Test case for withKeyType method
 @Test
 public void testWithKeyType() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType oldKeyType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType newKeyType = TypeFactory.defaultInstance().constructType(Integer.class);
 MapType mapType = new MapType(rawType, bindings, null, null, oldKeyType, TypeFactory.defaultInstance().constructType(Integer.class));
 MapType newMapType = mapType.withKeyType(newKeyType);
 assertEquals("map type; class Map<java.lang.Integer, java.lang.Integer>, java.lang.Integer -> java.lang.Integer", newMapType.toString());
 }

 // Test case for withContentType method
 @Test
 public void testWithContentType() {
 Class<Map<String, Integer>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType oldValueType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType newValueType = TypeFactory.defaultInstance().constructType(Double.class);
 MapType mapType = new MapType(rawType, bindings, null, null, TypeFactory.defaultInstance().constructType(String.class), oldValueType);
 MapType newMapType = mapType.withContentType(newValueType);
 assertEquals("map type; class Map<java.lang.String, java.lang.Double>, java.lang.String -> java.lang.Double", newMapType.toString());
 }

 // Test case for refine method
 @Test
 public void testRefine() {
 Class<Map<Integer, String>> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = TypeFactory.defaultInstance().constructType(Map.class);
 JavaType[] superInterfaces = new JavaType[0];
 MapType mapType = new MapType(rawType, bindings, null, superInterfaces, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
 Class<Map<Long, Double>> newRawType = Map.class;
 TypeBindings newBindings = TypeBindings.empty();
 JavaType newSuperClass = TypeFactory.defaultInstance().constructType(Map.class);
 JavaType[] newSuperInterfaces = new JavaType[0];
 MapType newMapType = mapType.refine(newRawType, newBindings, newSuperClass, newSuperInterfaces);
 assertEquals("map type; class Map<java.lang.Long, java.lang.Double>, java.lang.Long -> java.lang.Double", newMapType.toString());
 }
}