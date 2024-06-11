package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private CharacterEscapes characterEscapes;
 private IOContext ioContext;

 @Before
 public void setUp() {
 ioContext = new IOContext();
 characterEscapes = new CharacterEscapes();
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, new SimpleObjectMapper().getFactory());
 jsonGenerator.setCharacterEscapes(characterEscapes);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#JsonGeneratorImpl(IOContext, int, ObjectCodec)}
 */
 @Test
 public void testJsonGeneratorImplConstructor() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, new SimpleObjectMapper().getFactory());
 assertNotNull(jsonGenerator);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#JsonGeneratorImpl(IOContext, int, ObjectCodec)} with CharacterEscapes
 */
 @Test
 public void testJsonGeneratorImplConstructorWithCharacterEscapes() {
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, new SimpleObjectMapper().getFactory());
 jsonGenerator.setCharacterEscapes(characterEscapes);
 assertNotNull(jsonGenerator);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#enable(Feature)}
 */
 @Test
 public void testEnableFeature() {
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#_checkStdFeatureChanges(int, int)}
 */
 @Test
 public void testCheckStdFeatureChanges() {
 int newFeatureFlags = Feature.QUOTE_FIELD_NAMES.mask();
 jsonGenerator._checkStdFeatureChanges(newFeatureFlags, 0);
 assertFalse(jsonGenerator._cfgUnqNames);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#setHighestNonEscapedChar(int)}
 */
 @Test
 public void testSetHighestNonEscapedChar() {
 jsonGenerator.setHighestNonEscapedChar(128);
 assertEquals(128, jsonGenerator.getHighestEscapedChar());
 }

 /**
 * Test method for {@link JsonGeneratorImpl#setCharacterEscapes(CharacterEscapes)}
 */
 @Test
 public void testSetCharacterEscapes() {
 CharacterEscapes customEscapes = new CharacterEscapes();
 jsonGenerator.setCharacterEscapes(customEscapes);
 assertNotNull(jsonGenerator._characterEscapes);
 assertArrayEquals(customEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#setRootValueSeparator(SerializableString)}
 */
 @Test
 public void testSetRootValueSeparator() {
 SerializableString rootValueSeparator = new SerializableString("|");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
 }

 /**
 * Test method for {@link JsonGeneratorImpl#version()}
 */
 @Test
 public void testVersion() {
 Version version = jsonGenerator.version();
 assertNotNull(version);
 }
}