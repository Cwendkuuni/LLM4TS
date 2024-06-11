package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBufferDecorator;

@SuppressWarnings("unchecked")
public class StringArrayDeserializerTest {

 private ObjectMapper mapper;
 private DeserializationContext ctxt;
 private JsonParser jp;
 private StringArrayDeserializer deserializer;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 ctxt = mapper.getDeserializationContext();
 jp = mapper.getFactory().createParser("[\"abc\",\"def\",\"ghi\"]");
 deserializer = new StringArrayDeserializer();
 }

 @Test
 public void testDeserializeStandard() throws IOException {
 String[] result = deserializer.deserialize(jp, ctxt);
 assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }

 @Test
 public void testDeserializeCustom() throws IOException {
 JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
 when(elementDeserializer.deserialize(jp, ctxt)).thenReturn("custom");
 StringArrayDeserializer customDeserializer = new StringArrayDeserializer(elementDeserializer);
 String[] result = customDeserializer.deserialize(jp, ctxt);
 assertArrayEquals(new String[]{"custom", "custom", "custom"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNonArray() throws IOException {
 jp.nextToken(); // consume start object token
 jp.nextToken(); // consume null value
 String[] result = deserializer.deserialize(jp, ctxt);
 }

 @Test(expected = IOException.class)
 public void testDeserializeEmptyArray() throws IOException {
 jp.nextToken(); // consume start array token
 jp.nextToken(); // consume end array token
 String[] result = deserializer.deserialize(jp, ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 jp.nextToken(); // consume start array token
 jp.nextToken(); // consume invalid json token
 String[] result = deserializer.deserialize(jp, ctxt);
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserializeTypedFromArray(jp, ctxt)).thenReturn(new String[]{"type"});
 Object[] result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
 assertArrayEquals(new Object[]{"type", "type", "type"}, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnknownProperty() throws IOException {
 String[] result = deserializer.createContextual(ctxt, mock(BeanProperty.class)).deserialize(jp, ctxt, null);
 }

 @Test
 public void testDeserializeNullElementDeserializer() throws IOException {
 String[] result = deserializer.createContextual(ctxt, mock(BeanProperty.class)).deserialize(jp, ctxt, null);
 assertEquals(null, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws IOException {
 String[] expected = new String[]{"1", "2", "3"};
 JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
 when(elementDeserializer.deserialize(jp, ctxt)).thenAnswer(invocation -> String.valueOf(invocation.getArgument(0).intValue()));
 StringArrayDeserializer deserializerWithCustomElementDeserializer = new StringArrayDeserializer(elementDeserializer);
 String[] result = deserializerWithCustomElementDeserializer.deserialize(jp, ctxt);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeWithContentConverter() throws IOException {
 ContentConverter contentConverter = mock(ContentConverter.class);
 when(contentConverter.readValue(jp, String[].class)).thenReturn(new String[]{"contentConverter"});
 JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
 when(elementDeserializer.createContextual(ctxt, mock(BeanProperty.class))).thenReturn(contentConverter);
 StringArrayDeserializer deserializerWithContentConverter = new StringArrayDeserializer(elementDeserializer);
 String[] result = deserializerWithContentConverter.deserialize(jp, ctxt);
 assertArrayEquals(new String[]{"contentConverter", "contentConverter", "contentConverter"}, result);
 }
}