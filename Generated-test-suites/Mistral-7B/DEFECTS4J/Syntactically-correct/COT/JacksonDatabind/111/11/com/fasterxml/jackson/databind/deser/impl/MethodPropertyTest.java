import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class MethodPropertyTest {

 private MethodProperty methodProperty;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private Object instance;
 private AnnotatedMethod annotatedMethod;
 private Method setter;
 private TypeDeserializer typeDeserializer;
 private NullValueProvider nullValueProvider;

 @Before
 public void setUp() {
 Class<?> clazz = MethodProperty.class;
 java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
 try {
 for (java.lang.reflect.Field field : fields) {
 field.setAccessible(true);
 if (field.getType() == MethodProperty.class) {
 methodProperty = (MethodProperty) field.get(null);
 break;
 }
 }
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 instance = mock(Object.class);
 annotatedMethod = mock(AnnotatedMethod.class);
 setter = mock(Method.class);
 typeDeserializer = mock(TypeDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 }

 @Test
 public void testConstructorWithBeanPropertyDefinition() {
 // Test constructor with BeanPropertyDefinition, JavaType, TypeDeserializer, Annotations, AnnotatedMethod
 methodProperty = new MethodProperty(
 new BeanPropertyDefinition(ClassUtil.constructMethodType(
 new Type[] { Object.class, Integer.class }, MethodPropertyTest.class),
 MethodPropertyTest.class, "testMethod", null, null, null, null),
 ObjectType.getInstance(Integer.class), typeDeserializer, Annotations.empty(), annotatedMethod);
 verify(annotatedMethod).getAnnotated();
 verify(annotatedMethod).getMethod();
 verifyNoMoreInteractions(jsonParser, deserializationContext, instance, setter, typeDeserializer, nullValueProvider);
 }

 @Test
 public void testConstructorWithMethodProperty() {
 // Test constructor with MethodProperty
 MethodProperty src = methodProperty;
 methodProperty = new MethodProperty(src, jsonParser, nullValueProvider);
 verify(jsonParser).hasToken(JsonToken.START_OBJECT);
 verify(jsonParser).nextToken();
 verifyNoMoreInteractions(deserializationContext, instance, setter, typeDeserializer, nullValueProvider);
 }

 @Test
 public void testConstructorWithPropertyName() {
 // Test constructor with PropertyName
 MethodProperty src = methodProperty;
 methodProperty = new MethodProperty(src, "newName");
 verifyNoInteractions(jsonParser, deserializationContext, instance, setter, typeDeserializer, nullValueProvider);
 }

 @Test
 public void testConstructorWithMethod() {
 // Test constructor with Method
 MethodProperty src = methodProperty;
 methodProperty = new MethodProperty(src, setter);
 verify(setter).getDeclaringClass();
 verifyNoMoreInteractions(jsonParser, deserializationContext, instance, typeDeserializer, nullValueProvider);
 }

 @Test
 public void testGetAnnotation() {
 // Test getAnnotation()
 Class<?> annotationType = Annotation.class;
 when(annotatedMethod.getAnnotation(annotationType)).thenReturn(mock(Annotation.class));
 Object annotation = methodProperty.getAnnotation(annotationType);
 verify(annotatedMethod).getAnnotation(annotationType);
 assertEquals(annotation, annotation);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithNullValue() throws IOException {
 // Test deserializeAndSet() with null value and skipNulls = true
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 verify(jsonParser).nextToken();
 verifyNoMoreInteractions(deserializationContext, instance, setter, typeDeserializer, nullValueProvider);
 }

 @Test
 public void testDeserializeAndSetWithValue() throws IOException {
 // Test deserializeAndSet() with value
 when(jsonParser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
 when(jsonParser.getValueAsString()).thenReturn("42");
 when(typeDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(42);
 methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 verify(jsonParser).nextToken();
 verify(setter).invoke(instance, 42);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithException() throws IOException {
 // Test deserializeAndSet() with exception
 when(jsonParser.hasToken(JsonToken.VALUE_NUMBER)).thenThrow(new IOException());
 methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 verify(jsonParser).nextToken();
 }

 @Test
 public void testDeserializeSetAndReturnWithNullValue() throws IOException {
 // Test deserializeSetAndReturn() with null value and skipNulls = true
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 when(setter.invoke(instance, null)).thenReturn(instance);
 Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertEquals(instance, result);
 verify(jsonParser).nextToken();
 }

 @Test
 public void testDeserializeSetAndReturnWithValue() throws IOException {
 // Test deserializeSetAndReturn() with value
 when(jsonParser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
 when(jsonParser.getValueAsString()).thenReturn("42");
 when(setter.invoke(instance, 42)).thenReturn(new Object() {});
 Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertNotNull(result);
 verify(jsonParser).nextToken();
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnWithException() throws IOException {
 // Test deserializeSetAndReturn() with exception
 when(jsonParser.hasToken(JsonToken.VALUE_NUMBER)).thenThrow(new IOException());
 methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 verify(jsonParser).nextToken();
 }

 @Test
 public void testSet() throws IOException {
 // Test set()
 when(setter.invoke(instance, 42)).thenReturn(new Object() {});
 methodProperty.set(instance, 42);
 verify(setter).invoke(instance, 42);
 }

 @Test
 public void testSetWithException() throws IOException {
 // Test set() with exception
 when(setter.invoke(instance, 42)).thenThrow(new IOException());
 methodProperty.set(instance, 42);
 // 15-Sep-2015, tatu: How could we get a ref to JsonParser?
 verify(jsonParser).hasToken(JsonToken.START_OBJECT);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithTypeWithNullValue() throws IOException {
 // Test deserializeWithType() with null value and skipNulls = true
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 methodProperty.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 verify(jsonParser).nextToken();
 }

 @Test
 public void testDeserializeWithTypeWithValue() throws IOException {
 // Test deserializeWithType() with value
 when(jsonParser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
 when(jsonParser.getValueAsString()).thenReturn("42");
 when(typeDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer)).thenReturn(42);
 Object result = methodProperty.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 assertEquals(42, result);
 verify(jsonParser).nextToken();
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithTypeWithException() throws IOException {
 // Test deserializeWithType() with exception
 when(jsonParser.hasToken(JsonToken.VALUE_NUMBER)).thenThrow(new IOException());
 methodProperty.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 verify(jsonParser).nextToken();
 }

 @Test
 public void testWithName() {
 // Test withName()
 MethodProperty newMethodProperty = methodProperty.withName("newName");
 assertEquals("newName", newMethodProperty.getName().getShortName());
 assertEquals(methodProperty, newMethodProperty.getParent());
 }

 @Test
 public void testWithValueDeserializer() {
 // Test withValueDeserializer()
 JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
 MethodProperty newMethodProperty = methodProperty.withValueDeserializer(valueDeserializer);
 assertEquals(methodProperty, newMethodProperty.getParent());
 assertEquals(valueDeserializer, newMethodProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 // Test withNullProvider()
 NullValueProvider nullValueProvider2 = mock(NullValueProvider.class);
 MethodProperty newMethodProperty = methodProperty.withNullProvider(nullValueProvider2);
 assertEquals(methodProperty, newMethodProperty.getParent());
 assertEquals(nullValueProvider2, newMethodProperty.getNullProvider());
 }

 @Test
 public void testFixAccess() {
 // Test fixAccess()
 verifyNoMoreInteractions(jsonParser, deserializationContext, instance, setter, typeDeserializer, nullValueProvider);
 doCallRealMethod().when(methodProperty).fixAccess(deserializationContext);
 methodProperty.fixAccess(deserializationContext);
}
}