package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private IOContext ioContext;
 private ObjectCodec objectCodec;
 private CharacterEscapes characterEscapes;

 @Before
 public void setup() {
 ioContext = mock(IOContext.class);
 objectCodec = mock(ObjectCodec.class);
 characterEscapes = mock(CharacterEscapes.class);
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 }

 @Test
 public void testDefaultConstructor() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
 assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
 assertEquals(0, jsonGenerator._maximumNonEscapedChar);
 assertNull(jsonGenerator._characterEscapes);
 }

 @Test
 public void testSetCharacterEscapesWithCustomEscapes() {
 int[] expectedOutputEscapes = new int[]{'\\u0001'};
 when(characterEscapes.getEscapeCodesForAscii()).thenReturn(expectedOutputEscapes);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertArrayEquals(expectedOutputEscapes, jsonGenerator._outputEscapes);
 }

 @Test
 public void testSetCharacterEscapesWithNull() {
 jsonGenerator.setCharacterEscapes(null);
 assertArrayEquals(sOutputEscapes, jsonGenerator._outputEscapes);
 }

 @Test
 public void testSetHighestNonEscapedChar() {
 jsonGenerator.setHighestNonEscapedChar(123);
 assertEquals(123, jsonGenerator.getHighestEscapedChar());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeHighestNonEscapedChar() {
 jsonGenerator.setHighestNonEscapedChar(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetHighestNonEscapedCharWithNonInteger() {
 jsonGenerator.setHighestNonEscapedChar('x');
 }

 @Test
 public void testSetRootValueSeparator() {
 SerializableString rootValueSeparator = "root";
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
 }

 @Test
 public void testQuoteFieldNamesEnabledByDefault() {
 JsonGeneratorImpl jsonGeneratorWithQuoteFieldNamesEnabled = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 assertTrue(jsonGeneratorWithQuoteFieldNamesEnabled._cfgUnqNames);
 }

 @Test
 public void testQuoteFieldNamesDisabled() {
 JsonGeneratorImpl jsonGeneratorWithQuoteFieldNamesDisabled = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGeneratorWithQuoteFieldNamesDisabled.enable(Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGeneratorWithQuoteFieldNamesDisabled._cfgUnqNames);
 }
}