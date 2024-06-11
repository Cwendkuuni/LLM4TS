import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeUtil;
import com.fasterxml.jackson.databind.util.ValueInstantiator;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private ObjectMock mockBean;
 private TypeDeserializer mockTypeDeserializer;
 private TypeIdResolver mockTypeIdResolver;
 private ValueInstantiator mockValueInstantiator;

 @Before
 public void setup() {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockBean = mock(Object.class);
 mockTypeDeserializer = mock(TypeDeserializer.class);
 mockTypeIdResolver = mock(TypeIdResolver.class);
 mockValueInstantiator = mock(ValueInstantiator.class);

 handler = new ExternalTypeHandler(
 new ExternalTypeHandler.Builder()
 .addExternal(mock(SettableBeanProperty.class), mockTypeDeserializer)
 .build()
 );
 }

 @Test
 public void testStart() throws IOException {
 handler = handler.start();
 assertTrue(handler instanceof ExternalTypeHandler);
 }

 @Test
 public void testHandleTypePropertyValue_withValidTypeId() throws IOException {
 String propertyName = "propertyName";
 String typeId = "typeId";
 when(mockParser.getText()).thenReturn(typeId);
 when(mockBean != null).thenReturn(true);
 when(mockTokens[_properties.length - 1] != null).thenReturn(mock(TokenBuffer.class));

 handler.handleTypePropertyValue(mockParser, mockContext, propertyName, mockBean);
 assertTrue(handler.hasTypeId(propertyName, typeId));
 }

 @Test
 public void testHandleTypePropertyValue_withInvalidPropertyName() throws IOException {
 String invalidPropertyName = "invalidPropertyName";
 when(mockParser.getText()).thenReturn("typeId");
 when(mockBean != null).thenReturn(true);
 when(mockTokens[_properties.length - 1] != null).thenReturn(mock(TokenBuffer.class));

 handler.handleTypePropertyValue(mockParser, mockContext, invalidPropertyName, mockBean);
 assertFalse(handler.hasTypeId(invalidPropertyName, "typeId"));
 }

 @Test
 public void testHandleTypePropertyValue_withNullBean() throws IOException {
 String propertyName = "propertyName";
 String typeId = "typeId";
 when(mockParser.getText()).thenReturn(typeId);
 when(mockTokens[_properties.length - 1] != null).thenReturn(mock(TokenBuffer.class));

 handler.handleTypePropertyValue(mockParser, mockContext, propertyName, null);
 assertFalse(handler.hasTypeId(propertyName, typeId));
 }

 @Test
 public void testHandleTypePropertyValue_withNullTypeIdAndNonNullTokens() throws IOException {
 String propertyName = "propertyName";
 when(mockParser.getText()).thenReturn(null);
 when(mockTokens[_properties.length - 1] != null).thenReturn(mock(TokenBuffer.class));

 handler.handleTypePropertyValue(mockParser, mockContext, propertyName, mockBean);
 assertFalse(handler.hasTypeId(propertyName, null));
 }

 @Test
 public void testHandleTypePropertyValue_withNullTypeIdAndNullTokens() throws IOException {
 String propertyName = "propertyName";
 when(mockParser.getText()).thenReturn(null);
 when(mockTokens[_properties.length - 1] == null).thenReturn(null);

 handler.handleTypePropertyValue(mockParser, mockContext, propertyName, mockBean);
 assertFalse(handler.hasTypeId(propertyName, null));
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValue_withNullBeanAndNonNullTokens() throws IOException {
 String propertyName = "propertyName";
 String typeId = "typeId";
 when(mockParser.getText()).thenReturn(typeId);
 when(mockTokens[_properties.length - 1] != null).thenReturn(mock(TokenBuffer.class));

 handler.handleTypePropertyValue(mockParser, mockContext, propertyName, null);
 fail("Method should throw IOException when bean is null and tokens are not null.");
 }

 @Test
 public void testHandlePropertyValue_withValidPropertyNameAndValue() throws IOException {
 String propertyName = "propertyName";
 String validValue = "validValue";
 when(mockParser.getText()).thenReturn(propertyName);
 when(mockBean != null).thenReturn(true);
 when(mockTokens[_properties.length - 1] == null).thenReturn(null);

 handler.handlePropertyValue(mockParser, mockContext, propertyName, mockBean);
 assertEquals(propertyName, handler.getPropertyValue(propertyName));
 }

 @Test
 public void testHandlePropertyValue_withInvalidPropertyName() throws IOException {
 String invalidPropertyName = "invalidPropertyName";
 when(mockParser.getText()).thenReturn(invalidPropertyName);
 when(mockBean != null).thenReturn(true);
 when(mockTokens[_properties.length - 1] == null).thenReturn(null);

 handler.handlePropertyValue(mockParser, mockContext, invalidPropertyName, mockBean);
 assertNull(handler.getPropertyValue(invalidPropertyName));
 }

 @Test
 public void testHandlePropertyValue_withNullBean() throws IOException {
 String propertyName = "propertyName";
 when(mockParser.getText()).thenReturn(propertyName);
 when(mockTokens[_properties.length - 1] == null).thenReturn(null);

 handler.handlePropertyValue(mockParser, mockContext, propertyName, null);
 assertNull(handler.getPropertyValue(propertyName));
 }

 @Test
 public void testComplete_withValidTypeIds() throws IOException {
 String propertyName = "propertyName";
 String typeId1 = "typeId1";
 String typeId2 = "typeId2";
 when(mockParser.getText()).thenReturn(typeId1);
 when(mockTypeIdResolver.idFromString(eq("typeId1"))).thenReturn(TypeId.STANDARD_TYPES.LONG_TYPE);
 when(mockTypeIdResolver.idFromString(eq("typeId2"))).thenReturn(TypeId.STANDARD_TYPES.INTEGER_TYPE);
 when(mockTypeDeserializer.deserialize(any(JsonParser.class), eq(mockContext), any(TypeDeserializer.class), anyBoolean()))
         .thenReturn(123);
 when(mockTypeDeserializer.deserialize(any(JsonParser.class), eq(mockContext), any(TypeDeserializer.class), anyBoolean()))
         .thenReturn(456);
 when(mockValueInstantiator.findValueInstantiator(TypeId.STANDARD_TYPES.LONG_TYPE)).thenReturn(new LongValueInstantiator());
 when(mockValueInstantiator.findValueInstantiator(TypeId.STANDARD_TYPES.INTEGER_TYPE)).thenReturn(new IntegerValueInstantiator());
 when(mockTokens[_properties.length - 1] != null).thenReturn(mock(TokenBuffer.class));

 Object result = handler.complete(mockParser, mockContext, mockBean);
 assertEquals(123L, result);

 result = handler.getPropertyValue("propertyName");
 assertEquals(123L, result);
 }

 @Test
 public void testComplete_withNullTypeId_andValidTypeProperty() throws IOException {
 String propertyName = "propertyName";
 when(mockParser.getText()).thenReturn(null);
 when(mockTypeIdResolver.idFromString(eq("typePropertyName"))).thenReturn(TypeId.STANDARD_TYPES.LONG_TYPE);
 when(mockTypeDeserializer.deserialize(any(JsonParser.class), eq(mockContext), any(TypeDeserializer.class), anyBoolean()))
         .thenReturn(123);
 when(mockTokens[_properties.length - 1] == null).thenReturn(null);
 when(_properties[0].hasDefaultType()).thenReturn(true);
 when(_properties[0].getDefaultTypeId()).thenReturn("defaultTypeId");

 Object result = handler.complete(mockParser, mockContext, mockBean);
 assertEquals(123, result);

 result = handler.getPropertyValue("propertyName");
 assertEquals(123, result);
 }

 @Test
 public void testComplete_withNullTypeId_andNullTypeProperty() throws IOException {
 String propertyName = "propertyName";
 when(mockParser.getText()).thenReturn(null);
 when(_properties[0].hasDefaultType()).thenReturn(true);
 when(_properties[0].getDefaultTypeId()).thenReturn(null);
 when(mockTokens[_properties.length - 1] == null).thenReturn(null);

 Object result = handler.complete(mockParser, mockContext, mockBean);
 assertNull(result);

 result = handler.getPropertyValue("propertyName");
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testComplete_withInvalidTypeId() throws IOException {
 String propertyName = "propertyName";
 String invalidTypeId = "invalidTypeId";
 when(mockParser.getText()).thenReturn(invalidTypeId);
 when(mockTypeIdResolver.idFromString(eq(invalidTypeId))).thenThrow(new IOException());
 when(mockTokens[_properties.length - 1] == null).thenReturn(null);

 handler.complete(mockParser, mockContext, mockBean);
 fail("Method should throw IOException when type id is invalid.");
 }

 @Test(expected = NullPointerException.class)
 public void testComplete_withNullProperties() throws IOException {
 handler = handler.start();
 handler = handler.start();
 handler = null;
 handler.complete(mockParser, mockContext, mockBean);
 fail("Method should throw NullPointerException when handler is null.");
 }

 @Test(expected = NullPointerException.class)
 public void testComplete_withNullBean() throws IOException {
 handler.complete(mockParser, mockContext, null);
 fail("Method should throw NullPointerException when bean is null.");
 }

 @Test
 public void testComplete_withNullParser() throws IOException {
 handler.complete(null, mockContext, mockBean);
 fail("Method should throw NullPointerException when parser is null.");
 }

 @Test
 public void testComplete_withNullContext() throws IOException {
 handler.complete(mockParser, null);
 fail("Method should throw NullPointerException when context is null.");
 }
}