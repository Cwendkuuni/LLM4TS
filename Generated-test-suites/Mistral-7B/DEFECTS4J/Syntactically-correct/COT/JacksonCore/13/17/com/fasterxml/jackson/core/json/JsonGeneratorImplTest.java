package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private IOContext ioContext;
 private ObjectCodec objectCodec;
 private CharacterEscapes characterEscapes;

 @Before
 public void setup() {
 ioContext = new IOContext();
 objectCodec = new ObjectMapper().getFactory().createObjectCodec();
 characterEscapes = new CharacterEscapes();
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 }

 @Test
 public void testConstructor() {
 // Test constructor with default settings
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 Assert.assertNotNull(jsonGenerator);
 }

 @Test
 public void testEnableFeature() {
 // Test enabling a feature
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
 Assert.assertFalse(jsonGenerator._cfgUnqNames);
 }

 @Test
 public void testSetHighestNonEscapedChar() {
 // Test setting highest non-escaped character
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setHighestNonEscapedChar(128);
 Assert.assertEquals(128, jsonGenerator.getHighestEscapedChar());
 }

 @Test
 public void testSetCharacterEscapes() {
 // Test setting custom character escapes
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 Assert.assertSame(characterEscapes, jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testWriteFieldName() throws IOException {
 // Test writing a field name without quotes
 jsonGenerator.writeFieldName("fieldName");
 Assert.assertEquals("fieldName", jsonGenerator.getOutputBuffer().toString());

 // Test writing a field name with quotes
 jsonGenerator.writeFieldName("\"fieldName\"");
 Assert.assertEquals("\"fieldName\"", jsonGenerator.getOutputBuffer().toString());

 // Test writing a field name with a quote in it
 jsonGenerator.writeFieldName("field\"Name");
 Assert.assertEquals("\"field\\\"Name\"", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testWriteString() throws IOException {
 // Test writing a simple string
 jsonGenerator.writeString("hello");
 Assert.assertEquals("hello", jsonGenerator.getOutputBuffer().toString());

 // Test writing a string with escaped characters
 jsonGenerator.setCharacterEscapes(characterEscapes);
 jsonGenerator.writeString("hello\\world");
 Assert.assertEquals("hello\\\\world", jsonGenerator.getOutputBuffer().toString());
 }

 @Test(expected = IOException.class)
 public void testWriteNull() throws IOException {
 // Test writing a null value
 jsonGenerator.writeNull();
 jsonGenerator.flush();
 }

 @Test
 public void testSetRootValueSeparator() {
 // Test setting root value separator
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGenerator.setRootValueSeparator(new SerializableString(","));
 }

 @Test
 public void testVersion() {
 // Test version
 JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 Assert.assertTrue(jsonGenerator.version().isAtLeast(new Version("2.1.0")));
 }
}