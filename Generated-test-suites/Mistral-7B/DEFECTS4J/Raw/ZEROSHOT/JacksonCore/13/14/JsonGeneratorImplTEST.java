package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private IOContext ioContext;
 private CharacterEscapes characterEscapes;

 @Before
 public void setUp() {
 ioContext = new IOContext();
 characterEscapes = new CharacterEscapes();
 jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
 jsonGenerator.setCharacterEscapes(characterEscapes);
 }

 @Test
 public void testDefaultOutputEscapes() throws IOException {
 int[] outputEscapes = jsonGenerator.getOutputEscapes();
 assertArrayEquals(CharTypes.get7BitOutputEscapes(), outputEscapes);
 }

 @Test
 public void testSetOutputEscapes() throws IOException {
 int[] newOutputEscapes = new int[2];
 newOutputEscapes[0] = 1;
 newOutputEscapes[1] = 2;
 jsonGenerator.setOutputEscapes(newOutputEscapes);
 int[] outputEscapes = jsonGenerator.getOutputEscapes();
 assertArrayEquals(newOutputEscapes, outputEscapes);
 }

 @Test
 public void testGetHighestNonEscapedChar() throws IOException {
 jsonGenerator.setHighestNonEscapedChar(127);
 assertEquals(127, jsonGenerator.getHighestNonEscapedChar());
 }

 @Test
 public void testSetCharacterEscapes() throws IOException {
 CharacterEscapes customEscapes = new CharacterEscapes();
 jsonGenerator.setCharacterEscapes(customEscapes);
 CharacterEscapes actualEscapes = jsonGenerator.getCharacterEscapes();
 assertEquals(customEscapes, actualEscapes);
 }

 @Test
 public void testGetCharacterEscapes() throws IOException {
 assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
 }

 @Test
 public void testSetRootValueSeparator() throws IOException {
 SerializableString rootValueSeparator = new SerializableString("root");
 jsonGenerator.setRootValueSeparator(rootValueSeparator);
 assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
 }

 @Test
 public void testVersion() throws IOException {
 Version expectedVersion = VersionUtil.versionFor(JsonGeneratorImpl.class);
 Version actualVersion = jsonGenerator.version();
 assertEquals(expectedVersion, actualVersion);
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteStringFieldWithNullFieldName() throws IOException {
 jsonGenerator.writeStringField(null, "value");
 fail("IllegalStateException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWriteStringFieldWithEmptyFieldName() throws IOException {
 jsonGenerator.writeStringField("", "value");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testWriteStringField() throws IOException {
 String fieldName = "fieldName";
 String value = "value";
 jsonGenerator.writeStringField(fieldName, value);
 String actualFieldName = jsonGenerator.getString();
 String actualValue = jsonGenerator.getString();
 assertEquals(fieldName, actualFieldName);
 assertEquals(value, actualValue);
 assertNull(jsonGenerator.getString());
 }
}