package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGeneratorImpl;
 private IOContext ioContext;
 private ObjectCodec objectCodec;
 private CharacterEscapes characterEscapes;

 @Before
 public void setUp() {
 ioContext = Mockito.mock(IOContext.class);
 objectCodec = Mockito.mock(ObjectCodec.class);
 characterEscapes = Mockito.mock(CharacterEscapes.class);

 jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, objectCodec) {
 @Override
 protected CharacterEscapes _characterEscapes() {
 return characterEscapes;
 }
 };
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 JsonGeneratorImpl jsonGeneratorImplDefault = new JsonGeneratorImpl(ioContext, 0, objectCodec);

 // Test constructor with characterEscapes
 JsonGeneratorImpl jsonGeneratorImplWithCharacterEscapes = new JsonGeneratorImpl(ioContext, 0, objectCodec, characterEscapes);

 assertEquals(ioContext, jsonGeneratorImplDefault._ioContext);
 assertEquals(ioContext, jsonGeneratorImplWithCharacterEscapes._ioContext);
 assertEquals(objectCodec, jsonGeneratorImplDefault.getCodec());
 assertEquals(objectCodec, jsonGeneratorImplWithCharacterEscapes.getCodec());
 assertNull(jsonGeneratorImplDefault._characterEscapes);
 assertSame(characterEscapes, jsonGeneratorImplWithCharacterEscapes._characterEscapes);
 }

 @Test
 public void testGetHighestEscapedChar() throws Exception {
 jsonGeneratorImpl.setHighestNonEscapedChar(127);
 assertEquals(127, jsonGeneratorImpl.getHighestEscapedChar());
 }

 @Test
 public void testSetCharacterEscapes() throws Exception {
 CharacterEscapes newCharacterEscapes = Mockito.mock(CharacterEscapes.class);
 jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
 assertSame(characterEscapes, jsonGeneratorImpl._characterEscapes);
 jsonGeneratorImpl.setCharacterEscapes(newCharacterEscapes);
 assertSame(newCharacterEscapes, jsonGeneratorImpl._characterEscapes);
 }

 @Test
 public void testGetCharacterEscapes() throws Exception {
 assertEquals(characterEscapes, jsonGeneratorImpl.getCharacterEscapes());
 }

 @Test
 public void testSetRootValueSeparator() throws Exception {
 SerializableString newSeparator = Mockito.mock(SerializableString.class);
 jsonGeneratorImpl.setRootValueSeparator(newSeparator);
 assertSame(newSeparator, jsonGeneratorImpl._rootValueSeparator);
 }

 @Test
 public void testIsQuoteFieldNamesEnabled() throws Exception {
 assertTrue(jsonGeneratorImpl.isQuoteFieldNamesEnabled());
 jsonGeneratorImpl.disable(Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGeneratorImpl.isQuoteFieldNamesEnabled());
 }

 @Test
 public void testIsEscapeNonAsciiEnabled() throws Exception {
 assertFalse(jsonGeneratorImpl.isEscapeNonAsciiEnabled());
 jsonGeneratorImpl.enable(Feature.ESCAPE_NON_ASCII);
 assertTrue(jsonGeneratorImpl.isEscapeNonAsciiEnabled());
 }

 @Test
 public void testVersion() throws Exception {
 Version version = jsonGeneratorImpl.version();
 assertEquals("2.11.3", version.toString());
 }
}