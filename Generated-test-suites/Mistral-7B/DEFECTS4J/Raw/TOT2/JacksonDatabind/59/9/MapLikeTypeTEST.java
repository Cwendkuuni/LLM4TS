import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

 // Test case for the constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, JavaType keyType, JavaType valueType
 @Test
 public void testMapLikeTypeConstruction() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertEquals("Map<String, Integer>", mapLikeType.toString());
 }

 // Test case for the upgradeFrom factory method
 @Test
 public void testUpgradeFrom() {
 JavaType javaType = SimpleType.constructUnresolved(Map.class, String.class, Integer.class);
 MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, StringType.getInstance(), IntegerType.getInstance());
 assertEquals("Map<String, Integer>", mapLikeType.toString());
 }

 // Test case for the isMapLikeType() method
 @Test
 public void testIsMapLikeType() {
 JavaType javaType = SimpleType.constructUnresolved(Map.class, String.class, Integer.class);
 assertTrue(javaType instanceof MapLikeType);
 }

 // Test case for the getKeyType() method
 @Test
 public void testGetKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertEquals(StringType.getInstance().getTypeId(), mapLikeType.getKeyType().getTypeId());
 }

 // Test case for the getContentType() method
 @Test
 public void testGetContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertEquals(IntegerType.getInstance().getTypeId(), mapLikeType.getContentType().getTypeId());
 }

 // Test case for the isTrueMapType() method
 @Test
 public void testIsTrueMapType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertTrue(mapLikeType.isTrueMapType());
 }

 // Test case for the withKeyType() method
 @Test
 public void testWithKeyType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 JavaType javaType = mapLikeType.withKeyType(DoubleType.getInstance());
 assertEquals("Map<Double, Integer>", javaType.toString());
 }

 // Test case for the withContentType() method
 @Test
 public void testWithContentType() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 JavaType javaType = mapLikeType.withContentType(DoubleType.getInstance());
 assertEquals("Map<String, Double>", javaType.toString());
 }

 // Test case for the withTypeHandler() method
 @Test
 public void testWithTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 mapLikeType = mapLikeType.withTypeHandler(new ObjectMapper().getTypeFactory().constructType(Map.class, String.class, Integer.class));
 assertEquals("Map<String, Integer>", mapLikeType.toString());
 }

 // Test case for the withContentTypeHandler() method
 @Test
 public void testWithContentTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 mapLikeType = mapLikeType.withContentTypeHandler(new ObjectMapper().getTypeFactory().constructType(Map.class, String.class, Double.class));
 assertEquals("Map<String, Double>", mapLikeType.toString());
 }

 // Test case for the withKeyTypeHandler() method
 @Test
 public void testWithKeyTypeHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 mapLikeType = mapLikeType.withKeyTypeHandler(new ObjectMapper().getTypeFactory().constructType(Map.class, Double.class, Integer.class));
 assertEquals("Map<Double, Integer>", mapLikeType.toString());
 }

 // Test case for the withKeyValueHandler() method
 @Test
 public void testWithKeyValueHandler() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 mapLikeType = mapLikeType.withKeyValueHandler(new ObjectMapper().getTypeFactory().constructType(Map.class, Double.class, String.class));
 assertEquals("Map<Double, String>", mapLikeType.toString());
 }

 // Test case for the withStaticTyping() method
 @Test
 public void testWithStaticTyping() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 MapLikeType mapLikeTypeWithStaticTyping = mapLikeType.withStaticTyping();
 assertEquals(mapLikeType.toString(), mapLikeTypeWithStaticTyping.toString());
 assertTrue(mapLikeTypeWithStaticTyping.isStatic());
 }

 // Test case for the refine() method
 @Test
 public void testRefine() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = _bogusSuperClass(rawType);
 JavaType[] superInterfaces = null;
 JavaType keyType = StringType.getInstance();
 JavaType valueType = IntegerType.getInstance();
 JavaType refinedType = new MapLikeType(rawType, bindings, superClass, superInterfaces, keyType, valueType).refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(mapTypeId(rawType, keyType, valueType), refinedType.getTypeId());
 }

 // Test case for the buildCanonicalName() method
 @Test
 public void testBuildCanonicalName() {
 MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.getInstance(), IntegerType.getInstance());
 assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]", mapLikeType.buildCanonicalName().toString());
 }
}