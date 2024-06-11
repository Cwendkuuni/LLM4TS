package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.fasterxml.jackson.databind.JsonMapping.TypeSerializer.empty;
import static com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer.TypeIdVisible.*;

public class AsPropertyTypeDeserializerTest {

 private AsPropertyTypeDeserializer deserializer;
 private ObjectMapper mapper;
 private TypeIdResolver typeIdResolver;
 private JavaType javaType;
 private BeanProperty property;
 private JsonParser jsonParser;
 private DeserializationContext context;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 typeIdResolver = mock(TypeIdResolver.class);
 javaType = mock(JavaType.class);
 property = mock(BeanProperty.class);
 jsonParser = mock(JsonParser.class);
 context = mock(DeserializationContext.class);
 deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", VISIBLE, null, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 // Given
 given(jsonParser.isExpectedStartArrayType()).willReturn(false);
 given(jsonParser.isExpectedStartObjectType()).willReturn(true);
 given(jsonParser.nextToken()).willReturn(JsonToken.START_OBJECT);
 given(jsonParser.getText()).willReturn("typePropertyName");
 given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_STRING);
 given(jsonParser.getText()).willReturn("typeId");
 given(typeIdResolver.getIdResolverInstance()).willReturn(typeIdResolver);
 given(typeIdResolver.resolveTypeId(jsonParser, context, javaType)).willReturn("typeId");
 given(jsonParser.nextToken()).willReturn(JsonToken.START_OBJECT);
 given(jsonParser.getText()).willReturn("className");
 given(typeIdResolver.findValueDeserializer(context, javaType, "typeId")).willReturn(mock(JsonDeserializer.class));

 // When
 Object result = deserializer.deserializeTypedFromObject(jsonParser, context);

 // Then
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser, times(2)).getText();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(typeIdResolver).resolveTypeId(jsonParser, context, javaType);
 verify(typeIdResolver).findValueDeserializer(context, javaType, "typeId");
 assertNotNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 // Given
 given(jsonParser.isExpectedStartArrayType()).willReturn(false);
 given(jsonParser.isExpectedStartObjectType()).willReturn(true);
 given(jsonParser.nextToken()).willReturn(JsonToken.START_OBJECT);
 given(jsonParser.nextToken()).willReturn(JsonToken.FIELD_NAME);
 given(jsonParser.getCurrentName()).willReturn("typePropertyName");
 given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_STRING);
 given(jsonParser.getText()).willReturn("typeId");
 given(jsonParser.nextToken()).willReturn(JsonToken.END_OBJECT);
 given(jsonParser.getText()).willReturn("className");
 given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_STRING);
 given(jsonParser.getText()).willReturn("value");
 given(typeIdResolver.getIdResolverInstance()).willReturn(typeIdResolver);
 given(typeIdResolver.resolveTypeId(jsonParser, context, javaType)).willReturn(Id.CLASS);
 given(jsonParser.nextToken()).willReturn(JsonToken.START_ARRAY);
 given(typeIdResolver.findValueDeserializer(context, javaType, "classId")).willReturn(mock(JsonDeserializer.class));

 // When
 Object result = deserializer.deserializeTypedFromObject(jsonParser, context);

 // Then
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser, times(3)).getText();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(typeIdResolver, times(2)).resolveTypeId(jsonParser, context, javaType);
 verify(typeIdResolver).findValueDeserializer(context, javaType, "classId");
 assertNotNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWithMissingTypeProperty() throws Exception {
 // Given
 given(jsonParser.isExpectedStartArrayType()).willReturn(false);
 given(jsonParser.isExpectedStartObjectType()).willReturn(true);
 given(jsonParser.nextToken()).willReturn(JsonToken.START_OBJECT);
 given(jsonParser.nextToken()).willReturn(JsonToken.FIELD_NAME);
 given(jsonParser.getCurrentName()).willReturn("invalidPropertyName");
 given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_NULL);
 given(jsonParser.nextToken()).willReturn(JsonToken.END_OBJECT);

 // When & Then
 deserializer.deserializeTypedFromObject(jsonParser, context);
 verify(context).reportWrongTokenException(jsonParser, JsonToken.FIELD_NAME,
 "missing property 'typePropertyName' that is to contain type id  (for class " + javaType.getClassName() + ")");
 }

 @Test
 public void testDeserializeTypedUsingDefaultImpl() throws Exception {
 // Given
 given(jsonParser.isExpectedStartArrayType()).willReturn(false);
 given(jsonParser.isExpectedStartObjectType()).willReturn(false);
 given(jsonParser.getText()).willReturn("typeId");
 given(typeIdResolver.getIdResolverInstance()).willReturn(typeIdResolver);
 given(typeIdResolver.resolveTypeId(jsonParser, context, javaType)).willReturn(Id.CLASS);
 given(typeIdResolver.findValueDeserializer(context, javaType, "typeId")).willReturn(null);
 given(jsonParser.nextToken()).willReturn(JsonToken.START_ARRAY);
 given(context.findValueDeserializer(javaType, empty()))
         .willReturn(mock(JsonDeserializer.class));

 // When
 Object result = deserializer.deserializeTypedFromAny(jsonParser, context);

 // Then
 verify(jsonParser, times(2)).getText();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(typeIdResolver, times(2)).resolveTypeId(jsonParser, context, javaType);
 verify(context).findValueDeserializer(javaType, empty());
 assertNotNull(result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 // Given
 given(jsonParser.isExpectedStartArrayType()).willReturn(true);
 given(jsonParser.nextToken()).willReturn(JsonToken.START_ARRAY);
 given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_STRING);
 given(jsonParser.getText()).willReturn("classId");
 given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_NULL);
 given(jsonParser.nextToken()).willReturn(JsonToken.END_ARRAY);
 given(typeIdResolver.getIdResolverInstance()).willReturn(typeIdResolver);
 given(typeIdResolver.resolveTypeId(jsonParser, context, javaType)).willReturn(Id.CLASS);
 given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_STRING);
 given(jsonParser.getText()).willReturn("value");
 given(typeIdResolver.findValueDeserializer(context, javaType, "classId")).willReturn(mock(JsonDeserializer.class));

 // When
 Object result = deserializer.deserializeTypedFromArray(jsonParser, context);

 // Then
 verify(jsonParser, times(5)).nextToken();
 verify(jsonParser, times(2)).getText();
 verify(jsonParser).getText().equals("classId");
 verify(typeIdResolver, times(2)).resolveTypeId(jsonParser, context, javaType);
 verify(typeIdResolver).findValueDeserializer(context, javaType, "classId");
 assertNotNull(result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 // Given
 given(jsonParser.isExpectedStartArrayType()).willReturn(false);
 given(jsonParser.isExpectedStartObjectType()).willReturn(false);
 given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_NUMBER_INT);
 given(jsonParser.nextIntValue()).willReturn(1);
 given(jsonParser.nextToken()).willReturn(JsonToken.VALUE_STRING);
 given(jsonParser.getText()).willReturn("value");
 given(jsonParser.nextToken()).willReturn(JsonToken.END_OBJECT);

 // When
 Object result = deserializer.deserializeTypedFromScalar(jsonParser, context);

 // Then
 verify(jsonParser, times(2)).nextToken();
 verify(jsonParser).getText().equals("value");
 assertNotNull(result);
 }
}