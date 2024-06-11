package com.example.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DefaultSerializableObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;
 private BeanProperty property;
 private DeserializationContext context;
 private ObjectMapper objectMapper;
 private TypeIdResolver typeIdResolver;
 private JsonParser jsonParser;
 private Converter converter;

 @Before
 public void setup() {
 testee = new TypeDeserializerBaseTestDouble(
 JsonTypeFactory.constructSimpleType(Object.class),
 mock(TypeIdResolver.class),
 "typePropertyName",
 true,
 JsonTypeFactory.constructSimpleType(String.class)
 );
 property = mock(BeanProperty.class);
 context = mock(DeserializationContext.class);
 objectMapper = mock(ObjectMapper.class);
 typeIdResolver = mock(TypeIdResolver.class);
 jsonParser = mock(JsonParser.class);
 converter = mock(Converter.class);
 }

 @Test
 public void testForProperty() throws IOException, JsonProcessingException {
 TypeDeserializer typeDeserializer = testee.forProperty(property);
 assertNotNull(typeDeserializer);
 }

 @Test
 public void testGetTypeInclusion() {
 JsonTypeInfo.As typeInclusion = testee.getTypeInclusion();
 assertNotNull(typeInclusion);
 }

 @Test
 public void testBaseTypeName() {
 String baseTypeName = testee.baseTypeName();
 assertNotNull(baseTypeName);
 }

 @Test
 public void testToString() {
 String toString = testee.toString();
 assertNotNull(toString);
 }

 @Test
 public void testFindDeserializer() throws IOException {
 when(typeIdResolver.typeFromId(context, "typeId"))
 .thenReturn(JsonTypeFactory.constructSimpleType(Integer.class));
 JsonDeserializer<Object> deserializer = testee._findDeserializer(context, "typeId");
 assertNotNull(deserializer);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws IOException {
 Class<?> defaultImplClass = ClassUtil.rawClass(JsonTypeFactory.constructSimpleType(Integer.class));
 when(context.findContextualValueDeserializer(JsonTypeFactory.constructSimpleType(JsonNode.class), property)).thenReturn(mock(JsonDeserializer.class));
 JsonDeserializer<Object> defaultImplDeserializer = testee._findDefaultImplDeserializer(context);
 assertNull(defaultImplDeserializer);

 when(context.findContextualValueDeserializer(JsonTypeFactory.constructSimpleType(Void.class), property)).thenReturn(null);
 defaultImplDeserializer = testee._findDefaultImplDeserializer(context);
 assertNull(defaultImplDeserializer);

 when(context.findContextualValueDeserializer(JsonTypeFactory.constructSimpleType(defaultImplClass), property))
 .thenReturn(mock(JsonDeserializer.class));
 defaultImplDeserializer = testee._findDefaultImplDeserializer(context);
 assertNotNull(defaultImplDeserializer);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException, JsonProcessingException {
 JsonNode jsonNode = new DefaultSerializableObject();
 when(jsonParser.getValueAsTree())
 .thenReturn(jsonNode);
 when(jsonParser.getTypeId())
 .thenReturn("typeId");
 when(jsonParser.isExpectedStartArrayToken())
 .thenReturn(false);
 when(jsonParser.isExpectedStartObjectToken())
 .thenReturn(true);
 when(jsonParser.nextValue())
 .thenReturn(mock(JsonParser.ValueType.class));

 Object result = testee._deserializeWithNativeTypeId(jsonParser, context);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidTypeId() throws IOException, JsonProcessingException {
 when(jsonParser.getValueAsTree())
 .thenReturn(jsonNode("{}"));
 when(jsonParser.getTypeId())
 .thenReturn("InvalidTypeId");
 when(jsonParser.isExpectedStartArrayToken())
 .thenReturn(false);
 when(jsonParser.isExpectedStartObjectToken())
 .thenReturn(true);
 when(jsonParser.nextValue())
 .thenReturn(mock(JsonParser.ValueType.class));

 testee._deserializeWithNativeTypeId(jsonParser, context);
 }

 @Test
 public void testHandleUnknownTypeId() throws IOException, JsonProcessingException {
 when(typeIdResolver.handleUnknownTypeId(JsonTypeFactory.constructSimpleType(String.class), "UnknownTypeId"))
 .thenReturn(JsonTypeFactory.constructSimpleType(String.class));
 JavaType result = testee._handleUnknownTypeId(context, "UnknownTypeId");
 assertNotNull(result);
 assertSame(JsonTypeFactory.constructSimpleType(String.class), result);
 }

 @Test
 public void testHandleMissingTypeId() throws IOException, JsonProcessingException {
 when(typeIdResolver.handleMissingTypeId(JsonTypeFactory.constructSimpleType(String.class), "MissingTypeId"))
 .thenReturn(JsonTypeFactory.constructSimpleType(Integer.class));
 JavaType result = testee._handleMissingTypeId(context, "MissingTypeId");
 assertNotNull(result);
 assertNotSame(JsonTypeFactory.constructSimpleType(String.class), result);
 }
}

class TypeDeserializerBaseTestDouble extends TypeDeserializerBase {
 private static final ObjectMapper objectMapper = new ObjectMapper();

 public TypeDeserializerBaseTestDouble(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
 super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }

 @JsonDeserializer.MethodHandler
 public static JsonDeserializer<?> create(DeserializationContext ctxt) {
 return new StdSerializer<Object>(ctxt.constructType(Object.class)) {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 return null;
 }
 };
 }

 @Override
 public void handleUnexpectedTypeId(DeserializationContext ctxt, String id) throws IOException {
 throw new JsonMappingException(ctxt.getDeserializationConfig(), "Unexpected type id: " + id);
 }

 @Override
 public void handleMissingTypeId(DeserializationContext ctxt, String typeProperty, String id) throws IOException {
 throw new JsonMappingException(ctxt.getDeserializationConfig(), "Missing type id: " + id);
 }
}