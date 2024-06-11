package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;

 @Before
 public void setUp() {
 jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null) {
 @Override
 public void close() throws IOException {
 // empty implementation
 }
 };
 }

 @Test
 public void testDefaultConstructor() {
 assertNotNull(jsonGenerator);
 assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
 assertEquals(127, jsonGenerator._maximumNonEscapedChar);
 assertNull(jsonGenerator._characterEscapes);
 assertNull(jsonGenerator._rootValueSeparator);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testSetHighestNonEscapedChar() {
 jsonGenerator.setHighestNonEscapedChar(123);
 assertEquals(123, jsonGenerator._maximumNonEscapedChar);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetHighestNonEscapedCharNegativeValue() {
 jsonGenerator.setHighestNonEscapedChar(-123);
 }

 @Test
 public void testSetCharacterEscapes() {
 CharacterEscapes characterEscapes = new CharacterEscapes() {};
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertSame(characterEscapes, jsonGenerator._characterEscapes);
 assertArrayEquals(characterEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);
 }

 @Test
 public void testSetRootValueSeparator() {
 SerializableString rootValueSeparator = new SerializableString("root");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 assertSame(rootValueSeparator, jsonGenerator._rootValueSeparator);
 }

 @Test
 public void testEnableQuoteFieldNames() {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 assertTrue(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testDisableQuoteFieldNames() {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES); // to disable
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetHighestNonEscapedCharOutOfBounds() {
 jsonGenerator.setHighestNonEscapedChar(65536);
 }
}