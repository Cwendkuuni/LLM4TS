import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

 // Test case for the constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, JavaType keyType, JavaType valueType
 @Test
 public void testMapLikeTypeConstruction() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertEquals("java.util.Map", mapLikeType.toString());
 assertTrue(mapLikeType.isMapLikeType());
 assertTrue(mapLikeType.isContainerType());
 assertEquals(String.class, mapLikeType.getKeyType().getType());
 assertEquals(Integer.class, mapLikeType.getContentType().getType());
 }

 // Test case for the factory method upgradeFrom
 @Test
 public void testUpgradeFrom() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, StringType.getInstance(), IntegerType.getInstance());
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.toString());
 assertTrue(mapLikeType.isMapLikeType());
 assertTrue(mapLikeType.isContainerType());
 assertEquals(String.class, mapLikeType.getKeyType().getType());
 assertEquals(Integer.class, mapLikeType.getContentType().getType());
 }

 // Test case for the deprecated constructor
 @Test
 // Deprecated since 2.7
 public void testDeprecatedConstructor() {
 TypeVariable<?>[] vars = Map.class.getTypeParameters();
 TypeBindings bindings;
 if ((vars == null) || (vars.length != 2)) {
 bindings = TypeBindings.emptyBindings();
 } else {
 bindings = TypeBindings.create(Map.class, StringType.getInstance(), IntegerType.getInstance());
 }
 MapLikeType mapLikeType = new MapLikeType(Map.class, bindings, _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance(), null, null, false);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.toString());
 assertTrue(mapLikeType.isMapLikeType());
 assertTrue(mapLikeType.isContainerType());
 assertEquals(String.class, mapLikeType.getKeyType().getType());
 assertEquals(Integer.class, mapLikeType.getContentType().getType());
 }

 // Test case for the deprecated method _narrow
 @Test
 // Deprecated since 2.7
 public void testDeprecatedNarrowMethod() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, Integer.class);
 MapLikeType mapLikeType = new MapLikeType(javaType, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null, StringType.getInstance(), IntegerType.getInstance(), null, null, false);
 MapLikeType narrowedMapLikeType = mapLikeType.withKeyType(StringType.getInstance()).withContentType(IntegerType.getInstance());
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", narrowedMapLikeType.toString());
 assertTrue(narrowedMapLikeType.isMapLikeType());
 assertTrue(narrowedMapLikeType.isContainerType());
 assertEquals(String.class, narrowedMapLikeType.getKeyType().getType());
 assertEquals(Integer.class, narrowedMapLikeType.getContentType().getType());
 }

 // Test case for the method withKeyType
 @Test
 public void testWithKeyTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 MapLikeType newMapLikeType = mapLikeType.withKeyType(LongType.getInstance());
 assertEquals("java.util.Map<java.lang.Long, java.lang.Integer>", newMapLikeType.toString());
 assertTrue(newMapLikeType.isMapLikeType());
 assertTrue(newMapLikeType.isContainerType());
 assertEquals(Long.class, newMapLikeType.getKeyType().getType());
 assertEquals(Integer.class, newMapLikeType.getContentType().getType());
 }

 // Test case for the method withContentType
 @Test
 public void testWithContentTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 MapLikeType newMapLikeType = mapLikeType.withContentType(DoubleType.getInstance());
 assertEquals("java.util.Map<java.lang.String, java.lang.Double>", newMapLikeType.toString());
 assertTrue(newMapLikeType.isMapLikeType());
 assertTrue(newMapLikeType.isContainerType());
 assertEquals(String.class, newMapLikeType.getKeyType().getType());
 assertEquals(Double.class, newMapLikeType.getContentType().getType());
 }

 // Test case for the method withTypeHandler
 @Test
 public void testWithTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", newMapLikeType.toString());
 assertTrue(newMapLikeType.isMapLikeType());
 assertTrue(newMapLikeType.isContainerType());
 assertEquals(String.class, newMapLikeType.getKeyType().getType());
 assertEquals(Integer.class, newMapLikeType.getContentType().getType());
 assertNotNull(newMapLikeType.getTypeHandler());
 assertEquals(handler, newMapLikeType.getTypeHandler());
 }

 // Test case for the method withContentTypeHandler
 @Test
 public void testWithContentTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", newMapLikeType.toString());
 assertTrue(newMapLikeType.isMapLikeType());
 assertTrue(newMapLikeType.isContainerType());
 assertEquals(String.class, newMapLikeType.getKeyType().getType());
 assertEquals(Integer.class, newMapLikeType.getContentType().getType());
 assertNotNull(newMapLikeType.getValueHandler());
 assertEquals(handler, newMapLikeType.getValueHandler());
 }

 // Test case for the method withValueHandler
 @Test
 public void testWithValueHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", newMapLikeType.toString());
 assertTrue(newMapLikeType.isMapLikeType());
 assertTrue(newMapLikeType.isContainerType());
 assertEquals(String.class, newMapLikeType.getKeyType().getType());
 assertEquals(Integer.class, newMapLikeType.getContentType().getType());
 assertNotNull(newMapLikeType.getValueHandler());
 assertEquals(handler, newMapLikeType.getValueHandler());
 }

 // Test case for the method withContentValueHandler
 @Test
 public void testWithContentValueHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 Object handler = new Object();
 MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", newMapLikeType.toString());
 assertTrue(newMapLikeType.isMapLikeType());
 assertTrue(newMapLikeType.isContainerType());
 assertEquals(String.class, newMapLikeType.getKeyType().getType());
 assertEquals(Integer.class, newMapLikeType.getContentType().getType());
 assertNotNull(newMapLikeType.getContentValueHandler());
 assertEquals(handler, newMapLikeType.getContentValueHandler());
 }

 // Test case for the method withStaticTyping
 @Test
 public void testWithStaticTypingMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", newMapLikeType.toString());
 assertTrue(newMapLikeType.isMapLikeType());
 assertTrue(newMapLikeType.isContainerType());
 assertTrue(newMapLikeType.isStaticTyping());
 }

 // Test case for the method refine
 @Test
 public void testRefineMethod() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, Integer.class);
 MapLikeType mapLikeType = new MapLikeType(javaType, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null, StringType.getInstance(), IntegerType.getInstance());
 MapLikeType refinedMapLikeType = mapLikeType.refine(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null);
 assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", refinedMapLikeType.toString());
 assertTrue(refinedMapLikeType.isMapLikeType());
 assertTrue(refinedMapLikeType.isContainerType());
 assertEquals(String.class, refinedMapLikeType.getKeyType().getType());
 assertEquals(Integer.class, refinedMapLikeType.getContentType().getType());
 }

 // Test case for the method isTrueMapType
 @Test
 public void testIsTrueMapTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test case for the method isMapLikeType
 @Test
 public void testIsMapLikeTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertTrue(mapLikeType.isMapLikeType());
 }

 // Test case for the method isContainerType
 @Test
 public void testIsContainerTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertTrue(mapLikeType.isContainerType());
 }

 // Test case for the method getKeyType
 @Test
 public void testGetKeyTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertEquals(String.class, mapLikeType.getKeyType().getType());
 }

 // Test case for the method getContentType
 @Test
 public void testGetContentTypeMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertEquals(Integer.class, mapLikeType.getContentType().getType());
 }

 // Test case for the method getContentValueHandler
 @Test
 public void testGetContentValueHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertNull(mapLikeType.getContentValueHandler());
 }

 // Test case for the method getContentTypeHandler
 @Test
 public void testGetContentTypeHandlerMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertNull(mapLikeType.getContentTypeHandler());
 }

 // Test case for the method hasHandlers
 @Test
 public void testHasHandlersMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertFalse(mapLikeType.hasHandlers());
 }

 // Test case for the method getErasedSignature
 @Test
 public void testGetErasedSignatureMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 StringBuilder sb = new StringBuilder();
 mapLikeType.getErasedSignature(sb);
 assertEquals("java.util.Map", sb.toString());
 }

 // Test case for the method getGenericSignature
 @Test
 public void testGetGenericSignatureMethod() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 StringBuilder sb = new StringBuilder();
 mapLikeType.getGenericSignature(sb);
 assertEquals("java.util.Map<K, V>", sb.toString());
 }
}