package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectDeserializer;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.TestBase;

@JacksonStdImpl
public class StringArrayDeserializerTest extends TestBase {

 private static final StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();

 @SuppressWarnings("unchecked")
 private static final ObjectDeserializer<String> dummyStringDeserializer = mock(ObjectDeserializer.class);

 @Test
 public void testDeserializeStandard() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(createParser("[\"foo\", \"bar\"]"), context());
 assertArrayEquals(new String[]{"foo", "bar"}, result);
 }

 @Test
 public void testDeserializeWithCustomDeserializer() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(createParser("[\"\u0000\"]"), context());
 assertArrayEquals(new String[0], result);

 when(dummyStringDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
         .thenReturn("custom");
 StringArrayDeserializer deserializerWithCustomDeserializer = new StringArrayDeserializer(dummyStringDeserializer);
 result = deserializerWithCustomDeserializer.deserialize(createParser("[\u0000]"), context());
 assertArrayEquals(new String[]{"custom"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidJson() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 deserializer.deserialize(createParser("{foo: 123}"), context());
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(createParser("[]"), context());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeNullArray() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(createParser("null"), context());
 assertNull(result);
 }

 @Test
 public void testDeserializeSingleValueAsArray() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
deserializer.setEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
 String[] result = deserializer.deserialize(createParser("\"foo\""), context());
 assertArrayEquals(new String[]{"foo"}, result);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
deserializer.setEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
 String[] result = deserializer.deserialize(createParser("\"\""), context());
 assertNull(result);
 }

 @Test
 public void testDeserializeCustomContextual() throws IOException {
 String[] result = stringArrayDeserializer.deserialize(createParser("[\u0000]"), context());
 assertArrayEquals(new String[0], result);

 ObjectDeserializer<String> customStringDeserializer = mock(ObjectDeserializer.class);
 when(customStringDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
         .thenReturn("custom");
 StringArrayDeserializer deserializerWithCustomContextual = new StringArrayDeserializer(customStringDeserializer);
 result = deserializerWithCustomContextual.deserialize(createParser("[\u0000]"), context());
 assertArrayEquals(new String[]{"custom"}, result);
 }

 @Test
 public void testDeserializeCustomContextualWithTypeHandler() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserializeTypedFromArray(any(JsonParser.class), any(DeserializationContext.class)))
         .thenReturn(new String[0]);
 StringArrayDeserializer deserializerWithTypeHandler = new StringArrayDeserializer();
 String[] result = deserializerWithTypeHandler.deserializeWithType(createParser("[]"), context(), typeDeserializer);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeWithObjectArrayDeserializer() throws IOException {
 ObjectArrayDeserializer objectArrayDeserializer = mock(ObjectArrayDeserializer.class);
 when(objectArrayDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class), eq(String[].class)))
         .thenReturn(new String[]{"foo", "bar"});
 StringArrayDeserializer deserializerWithObjectArrayDeserializer = new StringArrayDeserializer();
 StringArrayDeserializer stringArrayDeserializerWithObjectArrayDeserializer = new StringArrayDeserializer(objectArrayDeserializer);
 String[] result = stringArrayDeserializerWithObjectArrayDeserializer.deserialize(createParser("[\u0000]"), context());
 assertArrayEquals(new String[]{"foo", "bar"}, result);
 }

 @Test
 public void testCreateContextual() {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 StringArrayDeserializer deserializerWithContextual = deserializer.createContextual(context(), null);
 assertNotSame(deserializer, deserializerWithContextual);
 }

 @Test
 public void testCreateContextualWithDefaultDeserializer() {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 ObjectDeserializer<String> defaultStringDeserializer = mock(ObjectDeserializer.class);
 StringArrayDeserializer deserializerWithDefaultDeserializer = new StringArrayDeserializer(defaultStringDeserializer);
 ObjectDeserializer<String> deserializerWithContextual = deserializerWithDefaultDeserializer.createContextual(context(), null);
 assertEquals(deserializerWithDefaultDeserializer, deserializerWithContextual);
 }

 @Test
 public void testFindConvertingContentDeserializer() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 ObjectDeserializer<Instance> instanceDeserializer = mock(ObjectDeserializer.class);
 when(instanceDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
         .thenReturn(new Instance());
 StringArrayDeserializer deserializerWithInstanceDeserializer = new StringArrayDeserializer(instanceDeserializer);
 ObjectDeserializer<String> convertingContentDeserializer = deserializerWithInstanceDeserializer.findConvertingContentDeserializer(context(), null, String.class);
 assertNotNull(convertingContentDeserializer);
 }

 @Test
 public void testFindConvertingContentDeserializerWithNull() throws IOException {
 StringArrayDeserializer deserializer = new StringArrayDeserializer();
 ObjectDeserializer<String> convertingContentDeserializer = deserializer.findConvertingContentDeserializer(context(), null, null);
 assertNull(convertingContentDeserializer);
 }

 @Test
 public void testIsDefaultDeserializer() {
 ObjectDeserializer<String> defaultStringDeserializer = mock(ObjectDeserializer.class);
 assertFalse(stringArrayDeserializer.isDefaultDeserializer(defaultStringDeserializer));

 ObjectDeserializer<String> stringDeserializer = new StringDeserializer();
 assertTrue(stringArrayDeserializer.isDefaultDeserializer(stringDeserializer));
 }

}