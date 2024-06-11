package com.example.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

 private AsPropertyTypeDeserializer _deserializer;
 private JsonParser _parser;
 private DeserializationContext _context;
 private ObjectMapper _objectMapper;
 private TypeIdResolver _typeIdResolver;
 private JavaType _baseType;

 @Before
 public void setup() throws Exception {
 _parser = mock(JsonParser.class);
 _context = mock(DeserializationContext.class);
 _objectMapper = mock(ObjectMapper.class);
 _typeIdResolver = mock(TypeIdResolver.class);
 _baseType = mock(JavaType.class);
 when(_parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
 when(_parser.getText()).thenReturn("MyType");
 when(_objectMapper.readTree(_parser)).thenReturn(new ObjectMapper().readTree(_parser));
 when(_parser.getCodec()).thenReturn(_objectMapper);
 _deserializer = new AsPropertyTypeDeserializer(mock(JavaType.class), _typeIdResolver, "typePropertyName", true, mock(JavaType.class), As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObjectWithProperty() throws Exception {
 Object result = _deserializer.deserializeTypedFromObject(_parser, _context);
 assertEquals("MyType", result.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWithoutProperty() throws Exception {
 _deserializer = new AsPropertyTypeDeserializer(mock(JavaType.class), _typeIdResolver, "typePropertyName", true, mock(JavaType.class), As.PROPERTY);
 _deserializer.deserializeTypedFromObject(_parser, _context);
 }

 @Test
 public void testDeserializeTypedFromObjectWithTokenBuffer() throws Exception {
 TokenBuffer tb = new TokenBuffer(_parser, _context);
 Object result = _deserializer.deserializeTypedForId(_parser, _context, tb);
 assertEquals("MyType", result.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWithInvalidTypeId() throws Exception {
 when(_parser.getText()).thenReturn("InvalidTypeId");
 Object result = _deserializer.deserializeTypedForId(_parser, _context, null);
 }

 @Test
 public void testDeserializeTypedUsingDefaultImpl() throws Exception {
 _deserializer = new AsPropertyTypeDeserializer(mock(JavaType.class), _typeIdResolver, "typePropertyName", true, mock(JavaType.class), As.PROPERTY);
 when(_parser.getText()).thenReturn("defaultImplTypeId");
 when(_objectMapper.findValueDeserializer(_baseType, _typeIdResolver)).thenReturn(mock(JsonDeserializer.class));
 Object result = _deserializer.deserializeTypedUsingDefaultImpl(_parser, _context, null);
 assertEquals("defaultImplTypeId", result.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedUsingDefaultImplWithNullTokenBuffer() throws Exception {
 _deserializer = new AsPropertyTypeDeserializer(mock(JavaType.class), _typeIdResolver, "typePropertyName", true, mock(JavaType.class), As.PROPERTY);
 when(_parser.getText()).thenReturn("defaultImplTypeId");
 Object result = _deserializer.deserializeTypedUsingDefaultImpl(_parser, _context, null);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 when(_parser.getText()).thenReturn("MyTypeArray");
 Object result = _deserializer.deserializeTypedFromAny(_parser, _context);
 assertEquals("[LMyType;", result.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromAnyWithInvalidType() throws Exception {
 when(_parser.getText()).thenReturn("InvalidType");
 _deserializer.deserializeTypedFromAny(_parser, _context);
 }
}