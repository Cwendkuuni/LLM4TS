package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class AsWrapperTypeDeserializerTest {

 private AsWrapperTypeDeserializer deserializer;
 private DeserializationContext context;
 private JsonParser parser;
 private ObjectMapper mapper;
 private JsonFactory factory;
 private TypeIdResolver idResolver;
 private JavaType baseType;
 private String typePropertyName;
 private boolean typeIdVisible;
 private Class<?> defaultImpl;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 factory = mapper.getFactory();
 idResolver = mock(TypeIdResolver.class);
 baseType = mock(JavaType.class);
 typePropertyName = "typeProperty";
 typeIdVisible = true;
 defaultImpl = Object.class;
 context = mock(DeserializationContext.class);
 when(context.findValueDeserializer(anyString())).thenReturn(null);
 when(context.handleUnknownProperty(_eq(typePropertyName), anyObject(), anyObject(), anyObject()))
 .thenCallRealMethod();
 parser = factory.getParser("{\"type\":\"SomeType\",\"value\":\"someValue\"}");
 deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertNotNull(result);
 assertEquals("someValue", result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 Object result = deserializer.deserializeTypedFromArray(parser, context);
 assertSame(result, deserializer.deserializeTypedFromObject(parser, context));
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 Object result = deserializer.deserializeTypedFromScalar(parser, context);
 assertSame(result, deserializer.deserializeTypedFromObject(parser, context));
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 Object result = deserializer.deserializeTypedFromAny(parser, context);
 assertSame(result, deserializer.deserializeTypedFromObject(parser, context));
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 when(parser.canReadTypeId()).thenReturn(true);
 when(parser.getTypeId()).thenReturn("SomeType");
 when(idResolver.resolveTypeId(anyString(), anyObject())).thenReturn(baseType);
 when(context.findValueDeserializer(eq("SomeType"))).thenReturn(mock(JsonDeserializer.class));
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertNotNull(result);
 }

 @Test
 public void testDeserializeWithoutTypeId() throws IOException {
 parser.nextToken();
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithInvalidTypeId() throws IOException {
 when(parser.getText()).thenReturn("InvalidTypeId");
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithWrongJsonStructure() throws IOException {
 parser.nextToken();
 parser.nextToken();
 Object result = deserializer.deserializeTypedFromObject(parser, context);
 assertThrows(IOException.class, () -> deserializer.deserializeTypedFromObject(parser, context));
 }
}