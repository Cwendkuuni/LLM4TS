import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

 @Test
 public void testMapTypeConstruction() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 assertEquals(Map.class, mapType.getRawClass());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 }

 @Test
 public void testMapTypeWithTypeHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 Object valueHandler = new Object() {};
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, valueHandler, null, false);
 MapType newMapType = mapType.withTypeHandler(new Object() {});
 assertNotSame(mapType, newMapType);
 assertEquals(mapType.getRawClass(), newMapType.getRawClass());
 assertNotNull(newMapType.getTypeHandler());
 }

 @Test
 public void testMapTypeWithContentTypeHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 Object contentTypeHandler = new Object() {};
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
 JavaType newValueType = valueType.withTypeHandler(contentTypeHandler);
 MapType newMapType = mapType.withContentTypeHandler(newValueType);
 assertNotSame(mapType, newMapType);
 assertEquals(mapType.getRawClass(), newMapType.getRawClass());
 assertNotNull(newMapType.getValueType());
 assertSame(contentTypeHandler, newMapType.getValueType().getTypeHandler());
 }

 @Test
 public void testMapTypeWithValueHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 Object valueHandler = new Object() {};
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
 MapType newMapType = mapType.withValueHandler(valueHandler);
 assertNotSame(mapType, newMapType);
 assertEquals(mapType.getRawClass(), newMapType.getRawClass());
 assertNotNull(newMapType.getValueHandler());
 }

 @Test
 public void testMapTypeWithContentValueHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 Object contentValueHandler = new Object() {};
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
 JavaType newValueType = valueType.withValueHandler(contentValueHandler);
 MapType newMapType = mapType.withContentValueHandler(newValueType);
 assertNotSame(mapType, newMapType);
 assertEquals(mapType.getRawClass(), newMapType.getRawClass());
 assertNotNull(newMapType.getValueType());
 assertSame(contentValueHandler, newMapType.getValueType().getValueHandler());
 }

 @Test
 public void testMapTypeWithStaticTyping() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
 MapType newMapType = mapType.withStaticTyping();
 assertNotSame(mapType, newMapType);
 assertEquals(mapType.getRawClass(), newMapType.getRawClass());
 assertTrue(newMapType.isStatic());
 }

 @Test
 public void testMapTypeWithContentType() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 JavaType newValueType = JavaType.construct(Long.class, null);
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
 MapType newMapType = mapType.withContentType(newValueType);
 assertNotSame(mapType, newMapType);
 assertEquals(mapType.getRawClass(), newMapType.getRawClass());
 assertNotSame(mapType.getValueType(), newMapType.getValueType());
 assertSame(newValueType, newMapType.getValueType());
 }

 @Test
 public void testMapTypeWithKeyType() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 JavaType newKeyType = JavaType.construct(Long.class, null);
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
 MapType newMapType = mapType.withKeyType(newKeyType);
 assertNotSame(mapType, newMapType);
 assertEquals(mapType.getRawClass(), newMapType.getRawClass());
 assertNotSame(mapType.getKeyType(), newMapType.getKeyType());
 assertSame(newKeyType, newMapType.getKeyType());
 }

 @Test
 public void testMapTypeRefine() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = JavaType.construct(Object.class, null);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
 JavaType refinedMapType = mapType.refine(Map.class, bindings, superClass, superInterfaces);
 assertNotSame(mapType, refinedMapType);
 assertEquals(Map.class, refinedMapType.getRawClass());
 assertEquals(mapType.getKeyType(), refinedMapType.getKeyType());
 assertEquals(mapType.getValueType(), refinedMapType.getValueType());
 }

 @Test
 public void testMapTypeWithKeyTypeHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 Object keyTypeHandler = new Object() {};
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
 MapType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
 assertNotSame(mapType, newMapType);
 assertEquals(mapType.getRawClass(), newMapType.getRawClass());
 assertNotNull(newMapType.getKeyTypeHandler());
 }

 @Test
 public void testMapTypeWithKeyValueHandler() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.construct(String.class, null);
 JavaType valueType = JavaType.construct(Integer.class, null);
 Object keyValueHandler = new Object() {};
 MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
 MapType newMapType = mapType.withKeyValueHandler(keyValueHandler);
 assertNotSame(mapType, newMapType);
 assertEquals(mapType.getRawClass(), newMapType.getRawClass());
 assertNotNull(newMapType.getKeyValueHandler());
 }
}