import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

@RunWith(JUnit4.class)
public class MapTypeTest {

 private MapType mapType;
 private MapType mapTypeWithTypeHandler;
 private MapType mapTypeWithContentTypeHandler;
 private MapType mapTypeWithValueHandler;
 private MapType mapTypeWithContentValueHandler;
 private MapType mapTypeWithStaticTyping;
 private MapType mapTypeWithKeyType;
 private MapType mapTypeWithKeyTypeHandler;
 private MapType mapTypeWithKeyValueHandler;

 @Before
 public void setUp() {
 String keyTypeString = "java.lang.String";
 String valueTypeString = "java.lang.Integer";
 Class<?> keyType = (Class<?>) Class.forName(keyTypeString);
 Class<?> valueType = (Class<?>) Class.forName(valueTypeString);
 JavaType keyJavaType = new TypeFactory().constructType(keyType);
 JavaType valueJavaType = new TypeFactory().constructType(valueType);
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 mapType = MapType.construct(Map.class, bindings, superClass, superInterfaces, keyJavaType, valueJavaType);
 mapTypeWithTypeHandler = MapType.construct(Map.class, bindings, superClass, superInterfaces, keyJavaType, valueJavaType, new TypeHandler());
 mapTypeWithContentTypeHandler = MapType.construct(Map.class, bindings, superClass, superInterfaces, keyJavaType, valueJavaType.withTypeHandler(new TypeHandler()));
 mapTypeWithValueHandler = MapType.construct(Map.class, bindings, superClass, superInterfaces, keyJavaType, valueJavaType, new ValueHandler());
 mapTypeWithContentValueHandler = MapType.construct(Map.class, bindings, superClass, superInterfaces, keyJavaType, valueJavaType.withValueHandler(new ValueHandler()), null, null, false);
 mapTypeWithStaticTyping = mapType.withStaticTyping();
 mapTypeWithKeyType = MapType.construct(Map.class, bindings, superClass, superInterfaces, new TypeHandler().getType(), valueJavaType);
 mapTypeWithKeyTypeHandler = MapType.construct(Map.class, bindings, superClass, superInterfaces, keyJavaType, valueJavaType, null, new TypeHandler(), false);
 mapTypeWithKeyValueHandler = MapType.construct(Map.class, bindings, superClass, superInterfaces, keyJavaType.withValueHandler(new ValueHandler()), valueJavaType);
 }

 @Test
 public void testConstruct() {
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapType.toString());
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithTypeHandler.toString());
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithContentTypeHandler.toString());
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithValueHandler.toString());
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithContentValueHandler.toString());
 assertEquals("map type; class class java.util.Map, java.lang.TypeHandler -> java.lang.Integer", mapTypeWithKeyType.toString());
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithKeyTypeHandler.toString());
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithKeyValueHandler.toString());
 }

 @Test
 public void testWithTypeHandler() {
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithTypeHandler.toString());
 }

 @Test
 public void testWithContentTypeHandler() {
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithContentTypeHandler.toString());
 }

 @Test
 public void testWithValueHandler() {
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithValueHandler.toString());
 }

 @Test
 public void testWithContentValueHandler() {
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithContentValueHandler.toString());
 }

 @Test
 public void testWithStaticTyping() {
 assertEquals("map type; class class java.util.Map<?,?>, java.lang.String -> java.lang.Integer", mapTypeWithStaticTyping.toString());
 }

 @Test
 public void testWithKeyType() {
 assertEquals("map type; class class java.util.Map, java.lang.TypeHandler -> java.lang.Integer", mapTypeWithKeyType.toString());
 }

 @Test
 public void testWithKeyTypeHandler() {
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithKeyTypeHandler.toString());
 }

 @Test
 public void testWithKeyValueHandler() {
 assertEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithKeyValueHandler.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidArguments() {
 assertNotNull(MapType.construct(String.class, null, null, null, null, null)); // null bindings
 assertNotNull(MapType.construct(Map.class, null, null, null, null, null, null, null, true)); // asStatic = true
 assertNotNull(MapType.construct(Object.class, null, null, null, null, null, null, null, false)); // invalid rawType
 assertNotNull(MapType.construct(Map.class, null, null, null, null, null, null, null, false)); // no supertypes
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithInvalidArguments() {
 MapType mapTypeWithInvalidValueHandler = new MapType(Map.class, null, null, null, null, null, null, new ValueHandler(), false); // invalid valueHandler
 assertNotEquals("map type; class class java.util.Map, java.lang.Object -> java.lang.Integer", mapTypeWithInvalidValueHandler.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithInvalidArgumentsTypeHandler() {
 MapType mapTypeWithInvalidTypeHandler = new MapType(Map.class, null, null, null, null, null, new TypeHandler(), null, false); // invalid typeHandler
 assertNotEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Object", mapTypeWithInvalidTypeHandler.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithInvalidArgumentsStaticTyping() {
 MapType mapTypeWithInvalidStaticTyping = new MapType(Map.class, null, null, null, null, null, null, null, true); // invalid asStatic
 assertNotEquals("map type; class class java.util.Map, java.lang.String -> java.lang.Integer", mapTypeWithInvalidStaticTyping.toString());
 }

 @Test
 public void testToString() {
 String toStringMapType = "[map type; class class java.util.Map, java.lang.String -> java.lang.Integer]";
 String toStringMapTypeWithTypeHandler = "[map type; class class java.util.Map, java.lang.String -> java.lang.Integer]";
 String toStringMapTypeWithContentTypeHandler = "[map type; class class java.util.Map, java.lang.String -> java.lang.Integer]";
 String toStringMapTypeWithValueHandler = "[map type; class class java.util.Map, java.lang.String -> java.lang.Integer]";
 String toStringMapTypeWithContentValueHandler = "[map type; class class java.util.Map, java.lang.String -> java.lang.Integer]";
 String toStringMapTypeWithKeyType = "[map type; class class java.util.Map, java.lang.TypeHandler -> java.lang.Integer]";
 String toStringMapTypeWithKeyTypeHandler = "[map type; class class java.util.Map, java.lang.String -> java.lang.Integer]";
 String toStringMapTypeWithKeyValueHandler = "[map type; class class java.util.Map, java.lang.String -> java.lang.Integer]";
 String toStringMapTypeWithStaticTyping = "[map type; class class java.util.Map<?,?>, java.lang.String -> java.lang.Integer]";
 assertEquals(toStringMapType, mapType.toString());
 assertEquals(toStringMapTypeWithTypeHandler, mapTypeWithTypeHandler.toString());
 assertEquals(toStringMapTypeWithContentTypeHandler, mapTypeWithContentTypeHandler.toString());
 assertEquals(toStringMapTypeWithValueHandler, mapTypeWithValueHandler.toString());
 assertEquals(toStringMapTypeWithContentValueHandler, mapTypeWithContentValueHandler.toString());
 assertEquals(toStringMapTypeWithKeyType, mapTypeWithKeyType.toString());
 assertEquals(toStringMapTypeWithKeyTypeHandler, mapTypeWithKeyTypeHandler.toString());
 assertEquals(toStringMapTypeWithKeyValueHandler, mapTypeWithKeyValueHandler.toString());
 assertEquals(toStringMapTypeWithStaticTyping, mapTypeWithStaticTyping.toString());
 }
}