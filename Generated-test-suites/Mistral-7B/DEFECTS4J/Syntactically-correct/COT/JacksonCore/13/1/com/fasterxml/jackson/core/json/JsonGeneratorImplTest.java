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

 private JsonGeneratorImpl jsonGeneratorImpl;
 private IOContext ioContext;
 private ObjectCodec objectCodec;

 @Before
 public void setup() {
 ioContext = new IOContext();
 objectCodec = new ObjectMapper().getFactory().createObjectCodec();
 jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 }

 @Test
 public void testConstructor() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 assertNotNull(jsonGenerator);
 }

 @Test
 public void testEnableFeature() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetHighestNonEscapedCharNegativeValue() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setHighestNonEscapedChar(-1);
 }

 @Test
 public void testSetHighestNonEscapedCharValue() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setHighestNonEscapedChar(128);
 assertEquals(128, jsonGenerator.getHighestEscapedChar());
 }

 @Test
 public void testSetCharacterEscapesNull() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setCharacterEscapes(null);
 assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
 }

 @Test
 public void testSetCharacterEscapesCustom() {
 CharacterEscapes customEscapes = new CharacterEscapes() {
 @Override
 public int[] getEscapeCodesForAscii() {
 return new int[] { 255, 254 };
 }
 };
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setCharacterEscapes(customEscapes);
 assertArrayNotEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
 }

 @Test
 public void testSetRootValueSeparator() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 SerializableString rootValueSeparator = new SerializableString(":");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
 }

 @Test
 public void testGetCharacterEscapes() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 CharacterEscapes characterEscapes = jsonGenerator.getCharacterEscapes();
 assertNull(characterEscapes);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetCharacterEscapesAfterSet() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 CharacterEscapes characterEscapes = new CharacterEscapes() {
 @Override
 public int[] getEscapeCodesForAscii() {
 return new int[] { 255, 254 };
 }
 };
 jsonGenerator.setCharacterEscapes(characterEscapes);
 jsonGenerator.getCharacterEscapes();
 jsonGenerator.setCharacterEscapes(null);
 jsonGenerator.getCharacterEscapes();
 }

 @Test
 public void testVersion() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), jsonGenerator.version());
 }
}