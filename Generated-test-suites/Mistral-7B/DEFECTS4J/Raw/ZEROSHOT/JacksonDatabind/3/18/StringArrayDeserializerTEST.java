package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.TestBase;

@JacksonAnnotationsInside
public class StringArrayDeserializerTest extends TestBase {

 private JsonFactory factory;
 private StringArrayDeserializer deserializer;
 private TypeDeserializer typeDeserializer;
 private JsonDeserializer<String> elementDeserializer;

 @Before
 public void setUp() throws Exception {
 factory = new JsonFactory();
 deserializer = StringArrayDeserializer.instance;
 typeDeserializer = mock(TypeDeserializer.class);
 elementDeserializer = mock(JsonDeserializer.class);
 when(typeDeserializer.deserializeTypedFromArray(any(JsonParser.class), any(DeserializationContext.class))).thenCallRealMethod();
 }

 @Test
 public void testEmptyArray() throws IOException {
 String[] result = deserializer.deserialize(factory.createParser(getResourceAsStream("/test-empty-array.json")), getContext());
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testSingleString() throws IOException {
 String[] result = deserializer.deserialize(factory.createParser(getResourceAsStream("/test-single-string.json")), getContext());
 assertArrayEquals(new String[] {"test"}, result);
 }

 @Test
 public void testMultipleStrings() throws IOException {
 String[] result = deserializer.deserialize(factory.createParser(getResourceAsStream("/test-multiple-strings.json")), getContext());
 assertArrayEquals(new String[] {"test1", "test2", "test3"}, result);
 }

 @Test
 public void testNullValue() throws IOException {
 String[] result = deserializer.deserialize(factory.createParser(getResourceAsStream("/test-null-value.json")), getContext());
 assertNull(result);
 }

 @Test
 public void testCustomElementDeserializer() throws Exception {
 when(elementDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("customValue");
 deserializer = new StringArrayDeserializer(elementDeserializer);
 String[] result = deserializer.deserialize(factory.createParser(getResourceAsStream("/test-single-string.json")), getContext());
 assertArrayEquals(new String[] {"customValue"}, result);
 }

 @Test
 public void testTypeDeserializer() throws IOException {
 when(typeDeserializer.deserializeTypedFromArray(any(JsonParser.class), any(DeserializationContext.class)))
 .thenReturn(new String[] {"typeDeserializerValue1", "typeDeserializerValue2"});
 String[] result = deserializer.deserialize(factory.createParser(getResourceAsStream("/test-multiple-strings.json")), getContext(), typeDeserializer);
 assertArrayEquals(new String[] {"typeDeserializerValue1", "typeDeserializerValue2"}, result);
 }

}