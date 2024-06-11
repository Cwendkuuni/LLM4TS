package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() {
 ObjectCar carrier = new ObjectCar();
 ObjectMapper.SerializerProvider serializers = new DefaultSerializerProvider();
 SimpleModule module = new SimpleModule();
 module.addSerializer(ObjectCar.class, new ObjectCarSerializer());
 mapper.registerModule(module);
 mapper.setSerializerProvider(serializers);
 }

 @Test
 public void testDeserializeTypedFromObjectWithNullTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(Object.class, null, null, null, null, As.PROPERTY);
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getText()).thenReturn(null);
 Object result = deserializer.deserializeTypedFromObject(mockParser, createContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithEmptyTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(Object.class, null, null, null, null, As.PROPERTY);
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getText()).thenReturn("");
 Object result = deserializer.deserializeTypedFromObject(mockParser, createContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(ObjectCar.class, null, "carTypeId", null, null, As.PROPERTY);
 ObjectCar expected = new ObjectCar();
 expected.setCarTypeId("carTypeIdValue");
 String typeId = "carTypeIdValue";
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getText()).thenReturn(typeId);
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn(typeId);
 Object result = deserializer.deserializeTypedFromObject(mockParser, createContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithPropertyName() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(ObjectCar.class, null, "carTypeId", true, null, As.PROPERTY);
 ObjectCar expected = new ObjectCar();
 expected.setCarTypeId("carTypeIdValue");
 String typeId = "carTypeIdValue";
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getText()).thenReturn(typeId);
 when(mockParser.getCurrentName()).thenReturn("carTypeId");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 Object result = deserializer.deserializeTypedFromObject(mockParser, createContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(String.class, null, null, null, null, As.PROPERTY);
 String result = (String) deserializer.deserializeTypedFromObject(createParser("{\"value\":\"test\"}"), createContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(ObjectCar.class, null, null, null, null, As.PROPERTY);
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getText()).thenReturn("carTypeIdValue");
 when(mockParser.canReadTypeId()).thenReturn(true);
 when(mockParser.getTypeId()).thenReturn("carTypeId");
 ObjectCar expected = new ObjectCar();
 expected.setCarTypeId("carTypeIdValue");
 Object result = deserializer.deserializeTypedFromObject(mockParser, createContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithWrongToken() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(ObjectCar.class, null, null, null, null, As.PROPERTY);
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getText()).thenReturn("test");
 when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
 Object result = deserializer.deserializeTypedFromObject(mockParser, createContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithWrongType() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(ObjectCar.class, null, null, null, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createParser("{\"value\":123}"), createContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(Object[].class, null, null, null, null, As.PROPERTY);
 Object[] result = (Object[]) deserializer.deserializeTypedFromArray(createParser("[\n" +
         "  {\"typeId\":\"TypeA\"},\n" +
         "  {\"value\":\"TypeAValue1\"},\n" +
         "  {\"typeId\":\"TypeB\"},\n" +
         "  {\"value\":\"TypeBValue1\"}\n" +
         "]"), createContext());
 Object[] expected = new Object[]{new TypeA(), "TypeAValue1", new TypeB(), "TypeBValue1"};
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(Object.class, null, null, null, null, As.PROPERTY);
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getText()).thenReturn("test");
 Object result = deserializer.deserializeTypedFromAny(mockParser, createContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithTokenBuffer() throws Exception {
 AsPropertyTypeDeserializer deserializer = createDeserializer(ObjectCar.class, null, null, null, null, As.PROPERTY);
 JsonParser mockParser = mock(JsonParser.class);
 TokenBuffer tokenBuffer = new TokenBuffer(mockParser, createContext());
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getCurrentName()).thenReturn("carTypeId");
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getText()).thenReturn("carTypeIdValue");
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 ObjectCar expected = new ObjectCar();
 expected.setCarTypeId("carTypeIdValue");
 Object result = deserializer.deserializeTypedFromObject(mockParser, createContext());
 assertEquals(expected, result);
 }

 private AsPropertyTypeDeserializer createDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName,
         boolean typeIdVisible, JavaType defaultImpl, As inclusion) {
 return new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion) {
 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }
 };
 }

 private JsonParser createParser(String json) throws IOException {
 String content = "{\"json\":\"" + json + "\"}";
 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(content.getBytes()));
 return parser;
 }

 private DeserializationContext createContext() {
 return mapper.getDeserializationContext();
 }
}