package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonWriter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeneratorBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testWriteBoolean() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.None);
 writer.writeBoolean(true);
 writer.flush();
 assertEquals("true", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteNull() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.None);
 writer.writeNull();
 writer.flush();
 assertEquals("null", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteNumber() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.None);
 writer.writeNumber(123);
 writer.flush();
 assertEquals("123", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteNumberAsString() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.WRITE_NUMBERS_AS_STRINGS);
 writer.writeNumber(123);
 writer.flush();
 assertEquals("123", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteString() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.None);
 writer.writeString("Hello World!");
 writer.flush();
 assertEquals("Hello World!", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteRawValue() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.None);
 writer.writeRawValue("Hello World!");
 writer.flush();
 assertEquals("Hello World!", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteNullCharacter() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.None);
 writer.writeNull();
 writer.write('\u0000');
 writer.flush();
 assertEquals("null\\u0000", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteSurrogateCharacters() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.None);
 writer.writeNull();
 writer.write('\uD800');
 writer.write('\uDC00');
 writer.flush();
 assertEquals("null\uD800\uDC00", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteStringWithEscapeSequences() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.ESCAPE_NON_ASCII);
 writer.writeString("Hello \u0000 World! \\/ \b \f \n \r \t \u007F");
 writer.flush();
 assertEquals("Hello null World! \\/ \\b \\f \\n \\r \\t \\u007F", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteStringWithInvalidUtf8() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.ESCAPE_NON_ASCII);
 writer.writeString("Hello \u00E0 World!");
 writer.flush();
 assertEquals("Hello \\u00E0 World!", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test
 public void testWriteNumberWithScale() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.WRITE_NUMBERS_AS_STRINGS | GeneratorFeatures.WRITE_BIGDECIMAL_AS_PLAIN);
 ObjectWriter ow = MAPPER.writer().withFeatures(GeneratorFeatures.WRITE_BIGDECIMAL_AS_PLAIN);
 writer.writeNumber(new BigDecimal("123.456789"));
 writer.flush();
 assertEquals("123.456789", new String(writer.getOutputBuffer().toString()));
 writer.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testWriteNumberWithInvalidScale() throws IOException {
 JsonWriter writer = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), GeneratorFeatures.WRITE_NUMBERS_AS_STRINGS | GeneratorFeatures.WRITE_BIGDECIMAL_AS_PLAIN);
 ObjectWriter ow = MAPPER.writer().withFeatures(GeneratorFeatures.WRITE_BIGDECIMAL_AS_PLAIN);
 writer.writeNumber(new BigDecimal("123.456789e10"));
 writer.flush();
 writer.close();
 }
}