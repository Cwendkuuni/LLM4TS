package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;

 @Test
 public void testJsonGeneratorImplConstructor() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 assertNotNull(jsonGenerator);
 }

 @Test
 public void testEnableFeature() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.enable(Feature.QUOTE_FIELD_Names);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testSetHighestNonEscapedChar() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setHighestNonEscapedChar(128);
 assertEquals(128, jsonGenerator.getHighestEscapedChar());
 }

 @Test
 public void testSetCharacterEscapes() {
 CharacterEscapes characterEscapes = new CharacterEscapes() {
 @Override
 public int[] getEscapeCodesForAscii() {
 return new int[] { 1, 2, 3 };
 }
 };
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertArrayEquals(new int[] { 1, 2, 3 }, jsonGenerator._outputEscapes);
 }

 @Test
 public void testSetRootValueSeparator() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 SerializableString rootValueSeparator = new SerializableString("root");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
 }

 @Test(expected = IOException.class)
 public void testWriteStringFieldWithNullValue() throws IOException {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.writeStringField("fieldName", null);
 fail("Expected IOException to be thrown");
 }

 @Test
 public void testWriteStringField() throws IOException {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.writeStringField("fieldName", "value");
 assertEquals("value", jsonGenerator.toString());
 }

 @Test
 public void testWriteFieldNameWithoutQuotes() throws IOException {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator._cfgUnqNames = true;
 jsonGenerator.writeStringField("fieldName", "value");
 assertEquals("fieldNamevalue", jsonGenerator.toString());
 }

 @Test
 public void testWriteFieldNameWithQuotes() throws IOException {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.writeStringField("\"fieldName\"", "value");
 assertEquals("\"fieldName\":\"value\"", jsonGenerator.toString());
 }

 @Test
 public void testWriteFieldNameWithEscapedChars() throws IOException {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator._outputEscapes = new int[] { 5, 6, 7 };
 jsonGenerator.writeStringField("fieldName\\backslash", "value");
 assertEquals("\"fieldName\\\\backslash\":\"value\"", jsonGenerator.toString());
 }
}