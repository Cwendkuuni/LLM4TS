package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

private static final ObjectMapper objectMapper = new ObjectMapper();

@Test
public void testDeserializeWithNullElementDeserializer() throws IOException {
StringArrayDeserializer serializer = StringArrayDeserializer.instance;
JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
String[] result = serializer.deserialize(jsonParser, new DeserializationContext());
assertNotNull(result);
assertArrayEquals(new String[0], result);
}

@Test
public void testDeserializeWithElementDeserializer() throws IOException {
StringArrayDeserializer serializer = new StringArrayDeserializer(new BasicJsonDeserializer<>());
JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
String[] result = serializer.deserialize(jsonParser, new DeserializationContext());
assertNotNull(result);
assertArrayEquals(new String[] {"test1", "test2"}, result);
}

@Test
public void testDeserializeCustomContentConverter() throws IOException {
ObjectBuffer objectBuffer = new ObjectBuffer(16);
StringArrayDeserializer serializer = new StringArrayDeserializer(null);
JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\", 123]");
String[] result = serializer.deserialize(jsonParser, new DeserializationContext());
assertEquals(2, result.length);
assertEquals("test1", result[0]);
assertEquals(123L, result[1].longValue());
objectMapper.close();
}

@Test(expected = IOException.class)
public void testDeserializeWithInvalidToken() throws IOException {
StringArrayDeserializer serializer = StringArrayDeserializer.instance;
JsonParser jsonParser = objectMapper.getFactory().createParser("{ \"key\": \"value\" }");
serializer.deserialize(jsonParser, new DeserializationContext());
fail("Expected IOException, but did not receive one.");
}

@Test(expected = IOException.class)
public void testDeserializeWithEmptyInput() throws IOException {
StringArrayDeserializer serializer = StringArrayDeserializer.instance;
JsonParser jsonParser = objectMapper.getFactory().createParser("");
serializer.deserialize(jsonParser, new DeserializationContext());
fail("Expected IOException, but did not receive one.");
}

@Test
public void testDeserializeWithTypeDeserializer() throws IOException {
TypeDeserializer typeDeserializer = new StdTypeDeserializer();
StringArrayDeserializer serializer = new StringArrayDeserializer();
JsonParser jsonParser = objectMapper.getFactory().createParser("[\"test1\",\"test2\"]");
Object result = serializer.deserializeWithType(jsonParser, new DeserializationContext(), typeDeserializer);
assertEquals(String[].class, result.getClass());
String[] stringResult = (String[]) result;
assertNotNull(stringResult);
assertArrayEquals(new String[] {"test1", "test2"}, stringResult);
}

@Test
public void testDeserializeWithNullInput() throws IOException {
StringArrayDeserializer serializer = StringArrayDeserializer.instance;
JsonParser jsonParser = objectMapper.getFactory().createParser(null);
String[] result = serializer.deserialize(jsonParser, new DeserializationContext());
assertNull(result);
}

@Test
public void testDeserializeWithEmptyInputAndNullAsDefault() throws IOException {
ObjectMapper objectMapper = new ObjectMapper();
objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
JsonParser parser = objectMapper.getFactory().createParser("[]");
String[] result = deserializer.deserialize(parser, new DeserializationContext());
assertNull(result);
}

@Test
public void testCreateContextual() throws IOException {
StringArrayDeserializer deserializer = new StringArrayDeserializer();
DeserializationContext context = new DeserializationContext(new StandardDeserializationConfig(new AnnotationIntrospector()));
BeanProperty property = new BeanProperty("test", String[].class, context);
JsonDeserializer<?> result = deserializer.createContextual(context, property);
assertEquals(StringArrayDeserializer.class, result.getClass());
}

@Test
public void testCreateContextualWithElementDeserializer() throws IOException {
StringArrayDeserializer deserializer = new StringArrayDeserializer(new BasicJsonDeserializer<>());
DeserializationContext context = new DeserializationContext(new StandardDeserializationConfig(new AnnotationIntrospector()));
BeanProperty property = new BeanProperty("test", String[].class, context);
JsonDeserializer<?> result = deserializer.createContextual(context, property);
assertNotNull(result);
assertNotEquals(deserializer.getClass(), result.getClass());
}
}