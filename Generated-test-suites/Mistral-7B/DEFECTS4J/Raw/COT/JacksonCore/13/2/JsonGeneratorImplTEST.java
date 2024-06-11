package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

 private IOContext ioContext;
 private JsonGeneratorImpl jsonGenerator;

 @Before
 public void setup() {
 ioContext = new IOContext();
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
 }

 @Test
 public void testConstructor() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
 assertNotNull(jsonGenerator);
 }

 @Test
 public void testSetCharacterEscapes() {
 CharacterEscapes characterEscapes = new CharacterEscapes();
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertSame(characterEscapes, jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testGetCharacterEscapes() {
 CharacterEscapes characterEscapes = new CharacterEscapes();
 jsonGenerator.setCharacterEscapes(characterEscapes);
 CharacterEscapes expectedCharacterEscapes = jsonGenerator.getCharacterEscapes();
 assertSame(characterEscapes, expectedCharacterEscapes);
 }

 @Test
 public void testSetRootValueSeparator() {
 SerializableString rootValueSeparator = new SerializableString("root");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 SerializableString expectedRootValueSeparator = jsonGenerator.getRootValueSeparator();
 assertSame(rootValueSeparator, expectedRootValueSeparator);
 }

 @Test
 public void testGetRootValueSeparator() {
 SerializableString rootValueSeparator = new SerializableString("root");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 SerializableString expectedRootValueSeparator = jsonGenerator.getRootValueSeparator();
 assertSame(rootValueSeparator, expectedRootValueSeparator);
 }

 @Test
 public void testSetHighestNonEscapedChar() {
 jsonGenerator.setHighestNonEscapedChar(128);
 assertEquals(128, jsonGenerator.getHighestEscapedChar());
 }

 @Test
 public void testSetHighestNonEscapedCharNegativeValue() {
 jsonGenerator.setHighestNonEscapedChar(-1);
 assertEquals(0, jsonGenerator.getHighestEscapedChar());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetHighestNonEscapedCharInvalidValue() {
 jsonGenerator.setHighestNonEscapedChar(Integer.MAX_VALUE);
 }

 @Test
 public void testEnableQuoteFieldNames() {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testDisableQuoteFieldNames() {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
 assertTrue(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testWriteStringFieldBasicFunctionality() throws IOException {
 String fieldName = "field";
 String value = "value";
 jsonGenerator.writeStringField(fieldName, value);
 String expectedJson = String.format("\"%s\":\"%s\"", fieldName, value);
 assertEquals(expectedJson, jsonGenerator.toString());
 }

 @Test
 public void testWriteStringFieldNullValue() throws IOException {
 jsonGenerator.writeStringField("field", null);
 String expectedJson = "null";
 assertEquals(expectedJson, jsonGenerator.toString());
 }

}