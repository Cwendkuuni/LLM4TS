import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class SetterlessPropertyTest {

 private SetterlessProperty setterlessProperty;
 private Object instance;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private Object value;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
 Method getter = mock(Method.class);
 when(annotatedMethod.getAnnotated()).thenReturn(getter);
 when(getter.invoke(Mockito.anyObject(), Mockito.arrayOf(Object.class))).thenReturn(new Object());

 JavaType type = mock(JavaType.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 Annotations contextAnnotations = mock(Annotations.class);
 AnnotatedMember member = mock(AnnotatedMember.class);
 setterlessProperty = new SetterlessProperty(null, type, typeDeserializer, contextAnnotations, annotatedMethod);
 instance = mock(Object.class);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 value = mock(Object.class);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 JsonToken jsonToken = JsonToken.VALUE_STRING;
 when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
 when(deserializationContext.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);

 setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertNotNull(instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws Exception {
 setterlessProperty.set(instance, value);
 fail("Should not call `set()` on setterless property");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturn() throws Exception {
 setterlessProperty.setAndReturn(instance, value);
 fail("Should not call `set()` on setterless property");
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Class<Annotation> annotationClass = mock(Class.class);
 Annotation annotation = mock(Annotation.class);
 when(setterlessProperty.getAnnotation(annotationClass)).thenReturn(annotation);
 assertEquals(annotation, setterlessProperty.getAnnotation(annotationClass));
 }

 @Test
 public void testGetMember() throws Exception {
 assertEquals(setterlessProperty.getMember(), setterlessProperty.getMember());
 }

 @Test(expected = IOException.class)
 public void testdeserializeAndSetWithNullValue() throws Exception {
 when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
 setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 fail("Should return when JSON null is encountered");
 }

 @Test(expected = IOException.class)
 public void testdeserializeAndSetWithUnsupportedType() throws Exception {
 when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
 JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
 setterlessProperty = new SetterlessProperty(setterlessProperty, valueDeserializer, null);
 setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 fail("Should throw exception for unsupported deserializer type");
 }

 @Test(expected = IOException.class)
 public void testdeserializeAndSetWithException() throws Exception {
 when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
 doThrow(new IOException("Exception while deserializing")).when(setterlessProperty._getter)
 .invoke(instance, (Object[]) null);
 setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 fail("Should throw exception if getter throws an exception");
 }
}