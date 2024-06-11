package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.fasterxml.jackson.core.JsonToken.*;

public class AsWrapperTypeDeserializerTest {

 private AsWrapperTypeDeserializer deserializer;
 private JsonDeserializer<Object> mockDeserializer;
 private TypeIdResolver mockIdResolver;
 private BeanProperty mockProperty;
 private DeserializationContext mockContext;
 private ObjectMapper mockObjectMapper;
 private JsonParser mockParser;
 private TokenBuffer mockTokenBuffer;

 @Before
 public void setUp() {
 mockDeserializer = mock(JsonDeserializer.class);
 mockIdResolver = mock(TypeIdResolver.class);
 mockProperty = mock(BeanProperty.class);
 mockContext = mock(DeserializationContext.class);
 mockObjectMapper = mock(ObjectMapper.class);
 mockParser = mock(JsonParser.class);
 mockTokenBuffer = mock(TokenBuffer.class);
 deserializer = new AsWrapperTypeDeserializer(
 mockObjectMapper.getTypeFactory().constructType(String.class), mockIdResolver, "_typeProperty", true, String.class);
 }

 @After
 public void tearDown() {
 reset(mockDeserializer, mockIdResolver, mockProperty, mockContext, mockObjectMapper, mockParser, mockTokenBuffer);
 }

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn("StringTypeId");
 when(mockIdResolver.idFromValueAndType("StringTypeId", String.class)).thenReturn("StringTypeId");
 when(mockObjectMapper.findDeserializerForType(String.class)).thenReturn(mockDeserializer);

 when(mockParser.nextToken()).thenReturn(START_OBJECT);
 when(mockParser.nextToken()).thenReturn(FIELD_NAME);
 when(mockParser.getText()).thenReturn("StringTypeId");
 when(mockParser.nextToken()).thenReturn(END_OBJECT);

 deserializer.deserializeTypedFromObject(mockParser, mockContext);

 verify(mockDeserializer).deserialize(mockParser, mockContext);
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObject_wrongTokenException() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(false);
 when(mockParser.nextToken()).thenReturn(FIELD_NAME);
 when(mockParser.nextToken()).thenReturn(START_OBJECT);

 deserializer.deserializeTypedFromObject(mockParser, mockContext);

 verify(mockParser, times(2)).nextToken();
 verify(mockContext).wrongTokenException(mockParser, JsonToken.START_OBJECT, "need JSON Object to contain As.WRAPPER_OBJECT type information for class String");
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObject_wrongTokenType() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn("NonStringTypeId");
 when(mockIdResolver.idFromValueAndType("NonStringTypeId", String.class)).thenReturn(null);

 deserializer.deserializeTypedFromObject(mockParser, mockContext);

 verify(mockParser, times(3)).nextToken();
 verify(mockContext).wrongTokenException(mockParser, JsonToken.START_OBJECT, "need JSON Object to contain As.WRAPPER_OBJECT type information for class String");
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn("StringTypeId");
 when(mockIdResolver.idFromValueAndType("StringTypeId", String[].class)).thenReturn("StringTypeId");
 when(mockObjectMapper.findDeserializerForValueType(String[].class)).thenReturn(mockDeserializer);

 when(mockParser.nextToken()).thenReturn(START_ARRAY);
 when(mockParser.nextToken()).thenReturn(START_OBJECT);
 when(mockParser.nextToken()).thenReturn(FIELD_NAME);
 when(mockParser.getText()).thenReturn("StringTypeId");
 when(mockParser.nextToken()).thenReturn(END_OBJECT);
 when(mockParser.nextToken()).thenReturn(END_ARRAY);

 deserializer.deserializeTypedFromArray(mockParser, mockContext);

 verify(mockDeserializer).deserialize(mockParser, mockContext);
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromArray_wrongTokenException() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(false);
 when(mockParser.nextToken()).thenReturn(START_ARRAY);

 deserializer.deserializeTypedFromArray(mockParser, mockContext);

 verify(mockParser, times(2)).nextToken();
 verify(mockContext).wrongTokenException(mockParser, JsonToken.START_ARRAY, "need JSON Array to contain As.WRAPPER_OBJECT type information for class [Ljava.lang.String;]");
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromArray_wrongTokenType() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn("NonStringTypeId");
 when(mockParser.nextToken()).thenReturn(START_ARRAY);

 deserializer.deserializeTypedFromArray(mockParser, mockContext);

 verify(mockParser, times(3)).nextToken();
 verify(mockContext).wrongTokenException(mockParser, JsonToken.START_ARRAY, "need JSON Array to contain As.WRAPPER_OBJECT type information for class [Ljava.lang.String;]");
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(false);
 when(mockParser.getText()).thenReturn("StringValue");
 when(mockObjectMapper.findDeserializerForValueType(String.class)).thenReturn(mockDeserializer);

 deserializer.deserializeTypedFromScalar(mockParser, mockContext);

 verify(mockDeserializer).deserialize(mockParser, mockContext);
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromScalar_wrongTokenException() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.nextToken()).thenReturn(START_OBJECT);

 deserializer.deserializeTypedFromScalar(mockParser, mockContext);

 verify(mockParser, times(2)).nextToken();
 verify(mockContext).wrongTokenException(mockParser, JsonToken.START_OBJECT, "need JSON Scalar value to deserialize using As.WRAPPER_OBJECT type information for class java.lang.String");
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromAny_wrongTokenException() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(false);
 when(mockParser.nextToken()).thenReturn(START_ARRAY);

 deserializer.deserializeTypedFromAny(mockParser, mockContext);

 verify(mockParser, times(2)).nextToken();
 verify(mockContext).wrongTokenException(mockParser, JsonToken.START_ARRAY, "need JSON Scalar value to deserialize using As.WRAPPER_OBJECT type information for class java.lang.String");
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn("StringTypeId");
 when(mockIdResolver.idFromValueAndType("StringTypeId", String.class)).thenReturn("StringTypeId");
 when(mockObjectMapper.findDeserializerForValueType(String.class)).thenReturn(mockDeserializer);

 when(mockParser.nextToken()).thenReturn(START_OBJECT);
 when(mockParser.nextToken()).thenReturn(FIELD_NAME);
 when(mockParser.getText()).thenReturn("StringTypeId");
 when(mockParser.nextToken()).thenReturn(END_OBJECT);

 deserializer._deserializeWithNativeTypeId(mockParser, mockContext, "StringTypeId");

 verify(mockDeserializer).deserialize(mockParser, mockContext);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeId_wrongTokenException() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn("NonStringTypeId");
 when(mockParser.nextToken()).thenReturn(START_OBJECT);

 deserializer._deserializeWithNativeTypeId(mockParser, mockContext, "NonStringTypeId");

 verify(mockParser, times(3)).nextToken();
 verify(mockContext).wrongTokenException(mockParser, JsonToken.START_OBJECT, "need JSON Object to contain As.WRAPPER_OBJECT type information for class java.lang.String");
 }

 @Test
 public void testMergeTypeIdIn() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn("StringTypeId");
 when(mockParser.nextToken()).thenReturn(START_OBJECT);
 when(mockParser.nextToken()).thenReturn(FIELD_NAME);
 when(mockParser.getText()).thenReturn("StringTypeId");
 when(mockParser.nextToken()).thenReturn(END_OBJECT);

 deserializer = new AsWrapperTypeDeserializer(deserializer, mockProperty);
 deserializer.deserializeTypedFromObject(mockParser, mockContext);

 verify(mockParser, times(6)).nextToken();
 }

 @Test(expected = IOException.class)
 public void testMergeTypeIdIn_wrongTokenException() throws Exception {
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn("StringTypeId");
 when(mockParser.nextToken()).thenReturn(START_OBJECT);
 when(mockParser.nextToken()).thenReturn(FIELD_NAME);
 when(mockParser.getText()).thenReturn("StringTypeId");
 when(mockParser.nextToken()).thenReturn(START_OBJECT);

 deserializer = new AsWrapperTypeDeserializer(deserializer, mockProperty);
 deserializer.deserializeTypedFromObject(mockParser, mockContext);

 verify(mockParser, times(8)).nextToken();
 verify(mockContext).wrongTokenException(mockParser, JsonToken.START_OBJECT, "expected closing END_OBJECT after type information and deserialized value");
 }
}