package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Test;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;

 @Test
 public void testJsonGeneratorImplConstructor() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 assertNotNull(jsonGenerator);
 }

 @Test
 public void testEnableFeature() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.enable(Feature.QUOTE_FIELD_Names);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testDisableFeature() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
 assertTrue(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testSetHighestNonEscapedChar() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setHighestNonEscapedChar(128);
 assertEquals(128, jsonGenerator.getHighestEscapedChar());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetHighestNonEscapedCharNegativeValue() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setHighestNonEscapedChar(-1);
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 CharacterEscapes escapes = new CharacterEscapes() {
 @Override
 public int[] getEscapeCodesForAscii() {
 return new int[]{ 256 }; // custom escape code
 }
 };
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setCharacterEscapes(escapes);
 assertEquals(new int[]{ 256 }, jsonGenerator._outputEscapes);
 }

 @Test
 public void testGetCharacterEscapes() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setCharacterEscapes(null);
 assertNull(jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testSetRootValueSeparator() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 jsonGenerator.setRootValueSeparator(new SerializableString("custom_separator"));
 assertEquals("custom_separator", jsonGenerator._rootValueSeparator.getString());
 }

 @Test
 public void testVersion() throws IOException {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
 assertEquals(Version.UNKNOWN, jsonGenerator.version());
 }
}