package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 ObjectCodec codec = new ObjectMapper().getFactory().getCodec();
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, codec);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testConstructor() {
 assertNotNull(jsonGenerator);
 }

 @Test
 public void testEnableQuoteFieldNames() throws IOException {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 String json = objectMapper.writeValueAsString(new TestObject("test"));
 assertEquals("{\"test\":\"test\"}", json);
 }

 @Test
 public void testDisableQuoteFieldNames() throws IOException {
 jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
 String json = objectMapper.writeValueAsString(new TestObject("test"));
 assertEquals("{\ntest: test\n}", json);
 }

 @Test
 public void testSetHighestNonEscapedChar() throws IOException {
 jsonGenerator.setHighestNonEscapedChar(123);
 String json = objectMapper.writeValueAsString(new TestObject((char) 123));
 assertNotEquals("{\"\\123\":null}", json);
 assertEquals("{\"\\u007b\\u0071\\u0073\\u0073\":null}", json);
}

 @Test
 public void testSetCharacterEscapes() throws IOException {
 CharacterEscapes characterEscapes = new SimpleCharacterEscapes();
 jsonGenerator.setCharacterEscapes(characterEscapes);
 String json = objectMapper.writeValueAsString(new TestObject('~'));
 assertEquals("{\"~\":null}", json);
}

 @Test
 public void testGetCharacterEscapes() throws IOException {
 CharacterEscapes characterEscapes = jsonGenerator.getCharacterEscapes();
 assertNotNull(characterEscapes);
}

 @Test
 public void testSetRootValueSeparator() throws IOException {
 jsonGenerator.setRootValueSeparator(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
 String json = objectMapper.writeValueAsString(new TestObject("test"));
 assertEquals("test", json);
}

 @Test
 public void testVersion() {
 Version version = jsonGenerator.version();
 assertNotNull(version);
}

 /**
 * Test object for testing JsonGeneratorImpl methods.
 */
 static class TestObject {
 String name;

 TestObject(String name) {
 this.name = name;
 }
 }
}