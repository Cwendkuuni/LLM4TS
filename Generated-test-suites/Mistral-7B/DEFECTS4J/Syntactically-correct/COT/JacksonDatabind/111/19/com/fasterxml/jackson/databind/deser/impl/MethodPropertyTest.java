import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.StdSerializer;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(BaseTest.class)
public class MethodPropertyTest {

 private MethodProperty _methodProperty;
 private Object _instance;
 private JsonParser _parser;
 private DeserializationContext _context;
 private TypeDeserializer _typeDeserializer;
 private JsonDeserializer<String> _valueDeserializer;
 private NullValueProvider _nullProvider;
 private AnnotatedMember _annotated;
 private Method _setter;
 private Object _value;
 private Object _expectedValue;

 @org.junit.Before
 public void setUp() throws Exception {
 _annotated = mock(AnnotatedMember.class);
 _setter = Mockito.spy(new MethodTestMethod());
 _valueDeserializer = Mockito.spy(new JsonDeserializer<String>() {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "value";
 }
 });
 _typeDeserializer = Mockito.mock(TypeDeserializer.class);
 _nullProvider = Mockito.mock(NullValueProvider.class);
 _instance = new TestClass();
 _parser = Mockito.mock(JsonParser.class);
 _context = Mockito.mock(DeserializationContext.class);

 _methodProperty = new MethodProperty(new BeanPropertyDefinition(new PropertyName("testProperty"), StdSerializer.instance, _typeDeserializer), _instance.getClass().getTypeByName("java.lang.String"), _typeDeserializer, Annotations.empty(), _annotated, _setter, null, null, _valueDeserializer, _nullProvider);
 }

 @Test
 public void testBasicSetterMethod() throws Exception {
 // Basic test for the setter method
 _value = "testValue";
 when(_parser.getValueAsString()).thenReturn(_value);
 when(_context.getAnnotationInstance(_annotated, JsonProperty.class)).thenReturn(new JsonProperty());

 _methodProperty.deserializeAndSet(_parser, _context, _instance);
 assertEquals("testValue", _instance.getPropertyValue());
 }

 @Test
 public void testNullValue() throws Exception {
 // Test for null value handling
 _value = null;
 when(_parser.getValueAsString()).thenReturn(_value);
 when(_context.getAnnotationInstance(_annotated, JsonProperty.class)).thenReturn(new JsonProperty());

 _methodProperty.deserializeAndSet(_parser, _context, _instance);
 assertNull(_instance.getPropertyValue());
 }

 @Test
 public void testValueDeserializer() throws Exception {
 // Test for value deserializer
 _expectedValue = new TestClass("expectedValue");
 when(_parser.getValueAsString()).thenReturn("expectedValue");
 when(_context.getAnnotationInstance(_annotated, JsonProperty.class)).thenReturn(new JsonProperty());
 when(_valueDeserializer.deserialize(_parser, _context)).thenReturn(_expectedValue);

 _methodProperty = new MethodProperty(_methodProperty, _valueDeserializer, _nullProvider);
 _methodProperty.deserializeAndSet(_parser, _context, _instance);
 assertEquals(_expectedValue, _instance.getPropertyValue());
 }

 @Test
 public void testTypeDeserializer() throws Exception {
 // Test for type deserializer
 _expectedValue = new TestClass(10);
 when(_parser.getValueAsString()).thenReturn("10");
 when(_context.getAnnotationInstance(_annotated, JsonProperty.class)).thenReturn(new JsonProperty());
 when(_valueTypeDeserializer.deserialize(_parser, _context, _instance.getClass().getTypeByName("java.lang.Integer"))).thenReturn(10);

 _methodProperty = new MethodProperty(_methodProperty, null, _typeDeserializer);
 _methodProperty.deserializeAndSet(_parser, _context, _instance);
 assertEquals(_expectedValue, _instance.getPropertyValue());
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 // Test for IOException
 when(_parser.getValueAsString()).thenThrow(new IOException());
 when(_context.getAnnotationInstance(_annotated, JsonProperty.class)).thenReturn(new JsonProperty());

 _methodProperty.deserializeAndSet(_parser, _context, _instance);
 }

 @Test(expected = IllegalAccessException.class)
 public void testIllegalAccessException() throws Exception {
 // Test for IllegalAccessException
 when(_parser.getValueAsString()).thenReturn("testValue");
 when(_context.getAnnotationInstance(_annotated, JsonProperty.class)).thenReturn(new JsonProperty());
 when(_setter.isAccessible()).thenReturn(false);
 when(_setter.invoke(_instance, "testValue")).thenThrow(new IllegalAccessException());

 _methodProperty.deserializeAndSet(_parser, _context, _instance);
 }

 @Test
 public void testSetterInvokeNull() throws Exception {
 // Test for null value passed to setter method
 _value = null;
 when(_parser.getValueAsString()).thenReturn(_value);
 when(_context.getAnnotationInstance(_annotated, JsonProperty.class)).thenReturn(new JsonProperty());
 when(_setter.invoke(_instance, _value)).thenReturn(null);

 _methodProperty.deserializeAndSet(_parser, _context, _instance);
 assertNull(_instance.getPropertyValue());
 }

 @Test
 public void testSetterInvokeNullWithSkipNulls() throws Exception {
 // Test for null value passed to setter method with skipNulls set to true
 _value = null;
 when(_parser.getValueAsString()).thenReturn(_value);
 when(_context.getAnnotationInstance(_annotated, JsonProperty.class)).thenReturn(new JsonProperty());
 when(_setter.invoke(_instance, _value)).thenReturn(null);

 _methodProperty = new MethodProperty(_methodProperty, null, null, null, _nullProvider);
 _methodProperty.deserializeAndSet(_parser, _context, _instance);
 assertNull(_instance.getPropertyValue());
 }

 @Test
 public void testSetterInvokeException() throws Exception {
 // Test for exception thrown by setter method
 _value = new TestClass();
 when(_parser.getValueAsString()).thenReturn(_value);
 when(_context.getAnnotationInstance(_annotated, JsonProperty.class)).thenReturn(new JsonProperty());
 when(_setter.invoke(_instance, _value)).thenThrow(new Exception());

 _methodProperty.deserializeAndSet(_parser, _context, _instance);
 assertNull(_instance.getPropertyValue());
 }

 // Inner class TestClass
 static class TestClass {
 private String propertyValue;

 public TestClass() {
 }

 public TestClass(String value) {
 this.propertyValue = value;
 }

 public TestClass(int value) {
 this.propertyValue = Integer.toString(value);
 }

 public String getPropertyValue() {
 return propertyValue;
 }
 }

 // Inner class MethodTestMethod
 static class MethodTestMethod {
 public void set(Object obj, Object value) {
 ((TestClass) obj).propertyValue = value.toString();
 }
 }
}