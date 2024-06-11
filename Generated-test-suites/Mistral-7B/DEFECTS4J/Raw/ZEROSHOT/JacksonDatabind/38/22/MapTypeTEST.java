import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

@RunWith(org.mockito.jUnit.MockitoJUnitRunner.class)
public class MapTypeTest {

 @Test
 public void testConstruct() {
 Class<?> rawType = Map.class;
 TypeBindings bindings = mock(TypeBindings.class);
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType keyType = mock(JavaType.class);
 JavaType valueType = mock(JavaType.class);

 MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyType, valueType);

 assert(mapType.getTypeBindings() == bindings);
 assert(mapType.getRawClass() == rawType);
 assert(mapType.getSuperClass() == superClass);
 assert(mapType.getSuperInterfaces() == superInts);
 assert(mapType.getKeyType() == keyType);
 assert(mapType.getValueType() == valueType);
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> rawType = Map.class;
 JavaType keyType = mock(JavaType.class);
 JavaType valueType = mock(JavaType.class);

 MapType mapType = MapType.construct(rawType, keyType, valueType);

 assert(mapType.getTypeBindings() == null);
 assert(mapType.getRawClass() == rawType);
 assert(mapType.getSuperClass() == _bogusSuperClass(rawType));
 assert(mapType.getSuperInterfaces() == null);
 assert(mapType.getKeyType() == keyType);
 assert(mapType.getValueType() == valueType);
 }

 @Test
 public void testNarrow() {
 Class<?> subclass = HashMap.class;
 MapType mapType = mock(MapType.class);

 JavaType narrowedType = mapType.narrow(subclass);

 assert(narrowedType instanceof MapType);
 assert(narrowedType.getTypeBindings() == mapType.getTypeBindings());
 assert(narrowedType.getRawClass() == subclass);
 assert(narrowedType.getSuperClass() == mapType.getSuperClass());
 assert(narrowedType.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(narrowedType.getKeyType() == mapType.getKeyType());
 assert(narrowedType.getValueType() == mapType.getValueType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowUnsupported() {
 MapType mapType = mock(MapType.class);

 mapType.narrow(Boolean.class);
 }

 @Test
 public void testWithTypeHandler() {
 Object valueHandler = mock(Object.class);
 MapType mapType = mock(MapType.class);

 JavaType withTypeHandler = mapType.withTypeHandler(valueHandler);

 assert(withTypeHandler instanceof MapType);
 assert(withTypeHandler.getTypeBindings() == mapType.getTypeBindings());
 assert(withTypeHandler.getRawClass() == mapType.getRawClass());
 assert(withTypeHandler.getSuperClass() == mapType.getSuperClass());
 assert(withTypeHandler.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(withTypeHandler.getKeyType() == mapType.getKeyType());
 assert(withTypeHandler.getValueType() == mapType.getValueType());
 assert(withTypeHandler.getValueHandler() == valueHandler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithTypeHandlerUnsupported() {
 MapType mapType = mock(MapType.class);

 mapType.withTypeHandler(Mockito.mock(String.class));
 }

 @Test
 public void testWithContentTypeHandler() {
 Object contentTypeHandler = mock(Object.class);
 MapType mapType = mock(MapType.class);
 JavaType valueType = mock(JavaType.class);

 JavaType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);

 assert(withContentTypeHandler instanceof MapType);
 assert(withContentTypeHandler.getTypeBindings() == mapType.getTypeBindings());
 assert(withContentTypeHandler.getRawClass() == mapType.getRawClass());
 assert(withContentTypeHandler.getSuperClass() == mapType.getSuperClass());
 assert(withContentTypeHandler.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(withContentTypeHandler.getKeyType() == mapType.getKeyType());
 assert(withContentTypeHandler.getValueType() == valueType);
 assert(withContentTypeHandler.getValueHandler() == mapType.getValueHandler());
 assert(withContentTypeHandler.getTypeHandler() == contentTypeHandler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentTypeHandlerUnsupported() {
 MapType mapType = mock(MapType.class);
 Object contentTypeHandler = mock(Object.class);

 mapType.withContentTypeHandler(contentTypeHandler);
 }

 @Test
 public void testWithValueHandler() {
 Object valueHandler = mock(Object.class);
 MapType mapType = mock(MapType.class);

 JavaType withValueHandler = mapType.withValueHandler(valueHandler);

 assert(withValueHandler instanceof MapType);
 assert(withValueHandler.getTypeBindings() == mapType.getTypeBindings());
 assert(withValueHandler.getRawClass() == mapType.getRawClass());
 assert(withValueHandler.getSuperClass() == mapType.getSuperClass());
 assert(withValueHandler.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(withValueHandler.getKeyType() == mapType.getKeyType());
 assert(withValueHandler.getValueType() == mapType.getValueType());
 assert(withValueHandler.getValueHandler() == valueHandler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithValueHandlerUnsupported() {
 MapType mapType = mock(MapType.class);

 mapType.withValueHandler(Mockito.mock(String.class));
 }

 @Test
 public void testWithContentValueHandler() {
 Object contentValueHandler = mock(Object.class);
 MapType mapType = mock(MapType.class);
 JavaType valueType = mock(JavaType.class);

 JavaType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);

 assert(withContentValueHandler instanceof MapType);
 assert(withContentValueHandler.getTypeBindings() == mapType.getTypeBindings());
 assert(withContentValueHandler.getRawClass() == mapType.getRawClass());
 assert(withContentValueHandler.getSuperClass() == mapType.getSuperClass());
 assert(withContentValueHandler.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(withContentValueHandler.getKeyType() == mapType.getKeyType());
 assert(withContentValueHandler.getValueType() == valueType);
 assert(withContentValueHandler.getValueHandler() == mapType.getValueHandler());
 assert(withContentValueHandler.getValueTypeHandler() == contentValueHandler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentValueHandlerUnsupported() {
 MapType mapType = mock(MapType.class);
 Object contentValueHandler = mock(Object.class);

 mapType.withContentValueHandler(contentValueHandler);
 }

 @Test
 public void testWithStaticTyping() {
 MapType mapType = mock(MapType.class);

 MapType withStaticTyping = mapType.withStaticTyping();

 assert(withStaticTyping instanceof MapType);
 assert(withStaticTyping.getTypeBindings() == mapType.getTypeBindings());
 assert(withStaticTyping.getRawClass() == mapType.getRawClass());
 assert(withStaticTyping.getSuperClass() == mapType.getSuperClass());
 assert(withStaticTyping.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(withStaticTyping.getKeyType().isStatic() == mapType.getKeyType().isStatic());
 assert(withStaticTyping.getValueType().isStatic() == mapType.getValueType().isStatic());
 assert(withStaticTyping._asStatic == true);
 }

 @Test
 public void testWithContentType() {
 JavaType contentType = mock(JavaType.class);
 MapType mapType = mock(MapType.class);

 JavaType withContentType = mapType.withContentType(contentType);

 assert(withContentType instanceof MapType);
 assert(withContentType.getTypeBindings() == mapType.getTypeBindings());
 assert(withContentType.getRawClass() == mapType.getRawClass());
 assert(withContentType.getSuperClass() == mapType.getSuperClass());
 assert(withContentType.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(withContentType.getKeyType() == mapType.getKeyType());
 assert(withContentType.getValueType() == contentType);
 assert(withContentType.getValueHandler() == mapType.getValueHandler());
 assert(withContentType.getTypeHandler() == mapType.getTypeHandler());
 assert(withContentType._asStatic == mapType._asStatic);
 }

 @Test
 public void testWithKeyType() {
 JavaType keyType = mock(JavaType.class);
 MapType mapType = mock(MapType.class);

 JavaType withKeyType = mapType.withKeyType(keyType);

 assert(withKeyType instanceof MapType);
 assert(withKeyType.getTypeBindings() == mapType.getTypeBindings());
 assert(withKeyType.getRawClass() == mapType.getRawClass());
 assert(withKeyType.getSuperClass() == mapType.getSuperClass());
 assert(withKeyType.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(withKeyType.getKeyType() == keyType);
 assert(withKeyType.getValueType() == mapType.getValueType());
 assert(withKeyType.getValueHandler() == mapType.getValueHandler());
 assert(withKeyType.getTypeHandler() == mapType.getTypeHandler());
 assert(withKeyType._asStatic == mapType._asStatic);
 }

 @Test
 public void testRefine() {
 Class<?> rawType = HashMap.class;
 TypeBindings bindings = mock(TypeBindings.class);
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInterfaces = new JavaType[0];
 MapType mapType = mock(MapType.class);

 JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);

 assert(refinedType instanceof MapType);
 assert(refinedType.getTypeBindings() == bindings);
 assert(refinedType.getRawClass() == rawType);
 assert(refinedType.getSuperClass() == superClass);
 assert(refinedType.getSuperInterfaces() == superInterfaces);
 assert(refinedType.getKeyType() == mapType.getKeyType());
 assert(refinedType.getValueType() == mapType.getValueType());
 assert(refinedType.getValueHandler() == mapType.getValueHandler());
 assert(refinedType.getTypeHandler() == mapType.getTypeHandler());
 assert(refinedType._asStatic == mapType._asStatic);
 }

 @Test
 public void testWithKeyTypeHandler() {
 Object keyTypeHandler = mock(Object.class);
 MapType mapType = mock(MapType.class);

 JavaType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);

 assert(withKeyTypeHandler instanceof MapType);
 assert(withKeyTypeHandler.getTypeBindings() == mapType.getTypeBindings());
 assert(withKeyTypeHandler.getRawClass() == mapType.getRawClass());
 assert(withKeyTypeHandler.getSuperClass() == mapType.getSuperClass());
 assert(withKeyTypeHandler.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(withKeyTypeHandler.getKeyType() == mapType.getKeyType());
 assert(withKeyTypeHandler.getValueType() == mapType.getValueType());
 assert(withKeyTypeHandler.getValueHandler() == mapType.getValueHandler());
 assert(withKeyTypeHandler.getTypeHandler() == mapType.getTypeHandler());
 assert(withKeyTypeHandler._asStatic == mapType._asStatic);
 assert(withKeyTypeHandler.getKeyTypeHandler() == keyTypeHandler);
 }

 @Test
 public void testWithKeyValueHandler() {
 Object keyValueHandler = mock(Object.class);
 MapType mapType = mock(MapType.class);

 JavaType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);

 assert(withKeyValueHandler instanceof MapType);
 assert(withKeyValueHandler.getTypeBindings() == mapType.getTypeBindings());
 assert(withKeyValueHandler.getRawClass() == mapType.getRawClass());
 assert(withKeyValueHandler.getSuperClass() == mapType.getSuperClass());
 assert(withKeyValueHandler.getSuperInterfaces() == mapType.getSuperInterfaces());
 assert(withKeyValueHandler.getKeyType() == mapType.getKeyType());
 assert(withKeyValueHandler.getValueType() == mapType.getValueType());
 assert(withKeyValueHandler.getValueHandler() == mapType.getValueHandler());
 assert(withKeyValueHandler.getTypeHandler() == mapType.getTypeHandler());
 assert(withKeyValueHandler._asStatic == mapType._asStatic);
 assert(withKeyValueHandler.getKeyValueHandler() == keyValueHandler);
 }
}