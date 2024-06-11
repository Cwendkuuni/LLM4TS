import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

 // Test for construct method with valid arguments
 @Test
 public void testConstructValidArguments() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType keyType = JavaType.constructType(String.class, int.class);
 JavaType valueType = JavaType.constructType(String.class);
 MapType expectedMapType = MapType.construct(rawType, bindings, null, new JavaType[0], keyType, valueType);
 MapType actualMapType = MapType.construct(rawType, bindings, null, new JavaType[0], keyType, valueType);
 assertEquals(expectedMapType, actualMapType);
 }

 // Test for construct method with deprecated argument
 @Test(expected = DeprecationWarning.class)
 public void testConstructDeprecatedArgument() {
 Class<?> rawType = Map.class;
 JavaType keyType = JavaType.constructType(String.class, int.class);
 JavaType valueType = JavaType.constructType(String.class);
 MapType actualMapType = MapType.construct(rawType, keyType, valueType);
 }

 // Test for _narrow method with valid argument
 @Test
 public void testNarrowValidArgument() {
 Class<?> subclass = HashMap.class;
 MapType parentMapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType expectedChildMapType = MapType.construct(subclass, parentMapType._bindings, parentMapType._superClass,
 parentMapType._superInterfaces, String.class, Integer.class);
 MapType actualChildMapType = parentMapType._narrow(subclass);
 assertEquals(expectedChildMapType, actualChildMapType);
 }

 // Test for _narrow method with invalid argument
 @Test(expected = IllegalArgumentException.class)
 public void testNarrowInvalidArgument() {
 Class<?> subclass = ArrayList.class;
 MapType parentMapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType actualChildMapType = parentMapType._narrow(subclass);
 }

 // Test for withTypeHandler method with valid argument
 @Test
 public void testWithTypeHandlerValidArgument() {
 Object typeHandler = new Object();
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType expectedMapTypeWithHandler = MapType.construct(mapType._class, mapType._bindings, mapType._superClass,
 mapType._superInterfaces, mapType._keyType, mapType._valueType, typeHandler, null, mapType._asStatic);
 MapType actualMapTypeWithHandler = mapType.withTypeHandler(typeHandler);
 assertEquals(expectedMapTypeWithHandler, actualMapTypeWithHandler);
 }

 // Test for withTypeHandler method with null argument
 @Test(expected = NullPointerException.class)
 public void testWithTypeHandlerNullArgument() {
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 mapType.withTypeHandler(null);
 }

 // Test for withContentTypeHandler method with valid argument
 @Test
 public void testWithContentTypeHandlerValidArgument() {
 Object contentTypeHandler = new Object();
 JavaType valueTypeWithHandler = JavaType.constructType(String.class, new JavaType[0], contentTypeHandler);
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType expectedMapTypeWithContentTypeHandler = MapType.construct(mapType._class, mapType._bindings, mapType._superClass,
 mapType._superInterfaces, mapType._keyType, valueTypeWithHandler, null, null, mapType._asStatic);
 MapType actualMapTypeWithContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
 assertEquals(expectedMapTypeWithContentTypeHandler, actualMapTypeWithContentTypeHandler);
 }

 // Test for withContentTypeHandler method with null argument
 @Test(expected = NullPointerException.class)
 public void testWithContentTypeHandlerNullArgument() {
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 mapType.withContentTypeHandler(null);
 }

 // Test for withValueHandler method with valid argument
 @Test
 public void testWithValueHandlerValidArgument() {
 Object valueHandler = new Object();
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType expectedMapTypeWithValueHandler = MapType.construct(mapType._class, mapType._bindings, mapType._superClass,
 mapType._superInterfaces, mapType._keyType, mapType._valueType, null, valueHandler, mapType._asStatic);
 MapType actualMapTypeWithValueHandler = mapType.withValueHandler(valueHandler);
 assertEquals(expectedMapTypeWithValueHandler, actualMapTypeWithValueHandler);
 }

 // Test for withValueHandler method with null argument
 @Test(expected = NullPointerException.class)
 public void testWithValueHandlerNullArgument() {
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 mapType.withValueHandler(null);
 }

 // Test for withContentValueHandler method with valid argument
 @Test
 public void testWithContentValueHandlerValidArgument() {
 Object contentValueHandler = new Object();
 JavaType valueTypeWithHandler = JavaType.constructType(String.class, new JavaType[0], contentValueHandler);
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType expectedMapTypeWithContentValueHandler = MapType.construct(mapType._class, mapType._bindings, mapType._superClass,
 mapType._superInterfaces, mapType._keyType, valueTypeWithHandler, null, null, mapType._asStatic);
 MapType actualMapTypeWithContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
 assertEquals(expectedMapTypeWithContentValueHandler, actualMapTypeWithContentValueHandler);
 }

 // Test for withContentValueHandler method with null argument
 @Test(expected = NullPointerException.class)
 public void testWithContentValueHandlerNullArgument() {
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 mapType.withContentValueHandler(null);
 }

 // Test for withStaticTyping method with static typing MapType
 @Test
 public void testWithStaticTypingStaticTypingMapType() {
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType expectedMapTypeWithStaticTyping = MapType.construct(mapType._class, mapType._bindings, mapType._superClass,
 mapType._superInterfaces, mapType._keyType.withStaticTyping(), mapType._valueType.withStaticTyping(), null, null, true);
 MapType actualMapTypeWithStaticTyping = mapType.withStaticTyping();
 assertEquals(expectedMapTypeWithStaticTyping, actualMapTypeWithStaticTyping);
 }

 // Test for withStaticTyping method with non-static typing MapType
 @Test
 public void testWithStaticTypingNonStaticTypingMapType() {
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class, null, null, false);
 MapType expectedMapTypeWithStaticTyping = MapType.construct(mapType._class, mapType._bindings, mapType._superClass,
 mapType._superInterfaces, mapType._keyType.withStaticTyping(), mapType._valueType.withStaticTyping(), null, null, true);
 MapType actualMapTypeWithStaticTyping = mapType.withStaticTyping();
 assertEquals(expectedMapTypeWithStaticTyping, actualMapTypeWithStaticTyping);
 }

 // Test for withContentType method with valid argument
 @Test
 public void testWithContentTypeValidArgument() {
 JavaType contentType = JavaType.constructType(String[].class);
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType expectedMapTypeWithContentType = MapType.construct(mapType._class, mapType._bindings, mapType._superClass,
 mapType._superInterfaces, mapType._keyType, contentType, null, null, mapType._asStatic);
 MapType actualMapTypeWithContentType = mapType.withContentType(contentType);
 assertEquals(expectedMapTypeWithContentType, actualMapTypeWithContentType);
 }

 // Test for withContentType method with same argument
 @Test
 public void testWithContentTypeSameArgument() {
 JavaType contentType = JavaType.constructType(String[].class);
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class, null, null, false);
 MapType expectedMapTypeWithContentType = mapType;
 MapType actualMapTypeWithContentType = mapType.withContentType(contentType);
 assertEquals(expectedMapTypeWithContentType, actualMapTypeWithContentType);
 }

 // Test for withKeyType method with valid argument
 @Test
 public void testWithKeyTypeValidArgument() {
 JavaType keyType = JavaType.constructType(Integer.class, String.class);
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType expectedMapTypeWithKeyType = MapType.construct(mapType._class, mapType._bindings, mapType._superClass,
 mapType._superInterfaces, keyType, mapType._valueType, null, null, mapType._asStatic);
 MapType actualMapTypeWithKeyType = mapType.withKeyType(keyType);
 assertEquals(expectedMapTypeWithKeyType, actualMapTypeWithKeyType);
 }

 // Test for withKeyType method with same argument
 @Test
 public void testWithKeyTypeSameArgument() {
 JavaType keyType = JavaType.constructType(Integer.class, String.class);
 MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new JavaType[0],
 String.class, Integer.class);
 MapType expectedMapTypeWithKeyType = mapType;
 MapType actualMapTypeWithKeyType = mapType.withKeyType(keyType);
 assertEquals(expectedMapTypeWithKeyType, actualMapTypeWithKeyType);
 }

 // Test for refine method with valid arguments
 @Test
 public void testRefineValidArguments() {
 Class<?> rawType = HashMap.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[]{List.class};
 MapType parentMapType = MapType.construct(Map.class, bindings, superClass, superInterfaces,
 String.class, Integer.class);
 MapType expectedChildMapType = MapType.construct(rawType, bindings, parentMapType._superClass, parentMapType._superInterfaces,
 parentMapType._keyType, parentMapType._valueType);
 MapType actualChildMapType = parentMapType.refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(expectedChildMapType, actualChildMapType);
 }

 // Test for refine method with invalid argument (superclass)
 @Test(expected = IllegalArgumentException.class)
 public void testRefineInvalidSuperclassArgument() {
 Class<?> rawType = HashMap.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = ArrayList.class;
 JavaType[] superInterfaces = new JavaType[]{List.class};
 MapType parentMapType = MapType.construct(Map.class, bindings, null, new JavaType[0],
 String.class, Integer.class);
 MapType actualChildMapType = parentMapType.refine(rawType, bindings, superClass, superInterfaces);
 }

 // Test for refine method with invalid argument (superinterfaces)
 @Test(expected = IllegalArgumentException.class)
 public void testRefineInvalidSuperinterfacesArgument() {
 Class<?> rawType = HashMap.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[]{ArrayList.class};
 MapType parentMapType = MapType.construct(Map.class, bindings, superClass, new JavaType[0],
 String.class, Integer.class);
 MapType actualChildMapType = parentMapType.refine(rawType, bindings, superClass, superInterfaces);
 }
}