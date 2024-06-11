import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapTypeTest {

 @Test
 public void testMapTypeConstruction() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 TypeBindings bindings = TypeBindings.defaultBindings();
 MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
 assertTrue(mapType instanceof MapType);
 assertEquals(Map.class, mapType.getRawClass());
 assertEquals(keyType, mapType.getKeyType());
 assertEquals(valueType, mapType.getValueType());
 assertNotNull(mapType.getTypeBindings());
 }

 @Test
 public void testDeprecatedConstructors() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.construct(String.class);
 JavaType valueType = JavaType.construct(Integer.class);
 MapType mapType = MapType.construct(rawType, keyType, valueType);
 MapType mapTypeDeprecated1 = MapType.construct(rawType, keyType, valueType, null, null);
 MapType mapTypeDeprecated2 = MapType.construct(rawType, keyType, valueType, null);

 assertEquals(mapType, mapTypeDeprecated1);
 assertEquals(mapType, mapTypeDeprecated2);
 }

 @Test
 public void testNarrowing() {
 Class<?> subclass = HashMap.class;
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 MapType narrowedMapType = mapType.narrow(subclass);

 assertEquals(HashMap.class, narrowedMapType.getRawClass());
 assertEquals(keyType, narrowedMapType.getKeyType());
 assertEquals(valueType, narrowedMapType.getValueType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 Object handler = new Object() {
 };
 MapType mapTypeWithHandler = mapType.withTypeHandler(handler);

 assertEquals(mapType, mapTypeWithHandler);
 assertEquals(handler, mapTypeWithHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 JavaType contentTypeHandler = JavaType.constructCollectionType(LinkedList.class, String.class);
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 MapType mapTypeWithContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);

 assertEquals(mapType, mapTypeWithContentTypeHandler);
 assertEquals(contentTypeHandler, mapTypeWithContentTypeHandler.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 Object handler = new Object() {
 };
 MapType mapTypeWithValueHandler = mapType.withValueHandler(handler);

 assertEquals(mapType, mapTypeWithValueHandler);
 assertEquals(handler, mapTypeWithValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 Object contentValueHandler = new Object() {
 };
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 MapType mapTypeWithContentValueHandler = mapType.withContentValueHandler(contentValueHandler);

 assertEquals(mapType, mapTypeWithContentValueHandler);
 assertEquals(contentValueHandler, mapTypeWithContentValueHandler.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 MapType mapTypeWithStaticTyping = mapType.withStaticTyping();

 assertFalse(mapType.isStaticTyping());
 assertTrue(mapTypeWithStaticTyping.isStaticTyping());
 }

 @Test
 public void testWithContentType() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 JavaType contentType = JavaType.constructCollectionType(LinkedList.class, String.class);
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 MapType mapTypeWithContentType = mapType.withContentType(contentType);

 assertEquals(mapType, mapTypeWithContentType);
 assertEquals(contentType, mapTypeWithContentType.getValueType());
 }

 @Test
 public void testWithKeyType() {
 Class<?> rawType = Map.class;
 JavaType oldKeyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType newKeyType = JavaType.constructCollectionType(LinkedList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 MapType mapType = MapType.construct(rawType, null, null, null, oldKeyType, valueType);
 MapType mapTypeWithNewKeyType = mapType.withKeyType(newKeyType);

 assertEquals(mapType, mapTypeWithNewKeyType);
 assertEquals(newKeyType, mapTypeWithNewKeyType.getKeyType());
 }

 @Test
 public void testRefine() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 TypeBindings bindings = TypeBindings.defaultBindings();
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 Class<?> subclass = HashMap.class;
 JavaType[] superInterfaces = new JavaType[] { JavaType.constructClassType(Serializable.class) };
 MapType refinedMapType = mapType.refine(subclass, bindings, null, superInterfaces);

 assertEquals(HashMap.class, refinedMapType.getRawClass());
 assertEquals(keyType, refinedMapType.getKeyType());
 assertEquals(valueType, refinedMapType.getValueType());
 assertEquals(bindings, refinedMapType.getTypeBindings());
 }

 @Test
 public void testWithKeyTypeHandler() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 Object keyTypeHandler = new Object() {
 };
 MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);

 assertEquals(mapType, mapTypeWithKeyTypeHandler);
 assertEquals(keyTypeHandler, mapTypeWithKeyTypeHandler.getKeyTypeHandler());
 }

 @Test
 public void testWithKeyValueHandler() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructCollectionType(ArrayList.class, String.class);
 JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
 MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
 Object keyValueHandler = new Object() {
 };
 MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);

 assertEquals(mapType, mapTypeWithKeyValueHandler);
 assertEquals(keyValueHandler, mapTypeWithKeyValueHandler.getKeyValueHandler());
 }
}