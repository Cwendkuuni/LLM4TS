import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class SetterlessPropertyTest {

 private Object testInstance;
 private SetterlessProperty property;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private TypeDeserializer mockTypeDeserializer;
 private NullValueProvider mockNullProvider;

 @Before
 public void setUp() throws Exception {
 testInstance = mock(Object.class);
 property = new SetterlessProperty(mockBeanPropertyDefinition, mockType, mockTypeDeserializer, mockContextAnnotations, mockAnnotatedMethod);
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockNullProvider = mock(NullValueProvider.class);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getValueAsString()).thenReturn("testValue");
 when(mockContext.findValueDeserializer(property.getType())).thenReturn(mockValueDeserializer);

 property.deserializeAndSet(mockParser, mockContext, testInstance);
 assertEquals("testValue", property.getMember().getAnnotation(JsonProperty.class).getValue());
 }

 @Test
 public void testDeserializeAndSetEmpty() throws IOException {
 when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
 property.deserializeAndSet(mockParser, mockContext, testInstance);
 assertEquals(null, property.getMember().getAnnotation(JsonProperty.class).getValue());
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetFail() throws IOException {
 when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER);
 property.deserializeAndSet(mockParser, mockContext, testInstance);
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object original = testInstance;
 when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getValueAsString()).thenReturn("testValue");
 when(mockContext.findValueDeserializer(property.getType())).thenReturn(mockValueDeserializer);

 assertEquals(original, property.deserializeSetAndReturn(mockParser, mockContext, testInstance));
 assertEquals("testValue", property.getMember().getAnnotation(JsonProperty.class).getValue());
 }

 @Test
 public void testSet() throws IOException, IllegalAccessException, InvocationTargetException {
 property.set(testInstance, new Object());
 assertEquals(new Object(), property.getMember().getAnnotation(JsonProperty.class).getValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetUnsupported() throws IOException {
 property.set(testInstance, new Object());
 }

 @Test
 public void testSetAndReturn() throws IOException, IllegalAccessException, InvocationTargetException {
 Object original = testInstance;
 property.setAndReturn(testInstance, new Object());
 assertEquals(new Object(), property.getMember().getAnnotation(JsonProperty.class).getValue());
 assertEquals(original, testInstance);
 }

 private BeanPropertyDefinition mockBeanPropertyDefinition = mock(BeanPropertyDefinition.class);
 private JavaType mockType = mock(JavaType.class);
 private TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);
 private Annotations mockContextAnnotations = mock(Annotations.class);
 private AnnotatedMethod mockAnnotatedMethod = mock(AnnotatedMethod.class);
 private JsonDeserializer mockValueDeserializer = mock(JsonDeserializer.class);
}