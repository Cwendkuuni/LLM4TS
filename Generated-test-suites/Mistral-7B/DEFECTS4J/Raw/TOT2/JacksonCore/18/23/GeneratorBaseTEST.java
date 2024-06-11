package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonVersion;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GeneratorBaseTest {

 private static final JsonMapper MAPPER = new JsonMapper();

 @Test
 public void testVersion() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 Version version = generator.version();
 assertEquals(JsonVersion.VERSION_2_5, version);
 }

 @Test
 public void testIsEnabled() throws IOException {
 JsonGenerator generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
 assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
 }

 @Test
 public void testEnableDisable() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
 assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
 assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 }

 @Test
 public void testSetFeatureMask() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
 assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFeatureMaskNegative() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.setFeatureMask(-1);
 }

 @Test
 public void testUseDefaultPrettyPrinter() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 PrettyPrinter prettyPrinter = generator.useDefaultPrettyPrinter();
 assertNotNull(prettyPrinter);
 }

 @Test
 public void testSetCodec() throws IOException {
 ObjectCodec codec = MAPPER.getFactory().getCodec();
 JsonGenerator generator = new GeneratorBase(0, codec);
 ObjectCodec newCodec = MAPPER.getFactory().getCodec();
 generator.setCodec(newCodec);
 assertEquals(newCodec, generator.getCodec());
 }

 @Test
 public void testGetOutputContext() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 JsonWriteContext context = generator.getOutputContext();
 assertNotNull(context);
 }

 @Test
 public void testWriteString() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeString("hello");
 }

 @Test
 public void testWriteRawValue() throws IOException {
 String raw = "raw value";
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeRawValue(raw);
 }

 @Test
 public void testWriteRawValueNull() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeRawValue(null);
 }

 @Test
 public void testWriteBinary() throws IOException {
 byte[] data = "test data".getBytes();
 InputStream input = new ByteArrayInputStream(data);
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeBinary(Base64Variant.STANDARD, input, data.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWriteBinaryNull() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeBinary(null, null, 0);
 }

 @Test
 public void testWriteNumberInt() throws IOException, JsonGenerationException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeNumber(42);
 }

 @Test
 public void testWriteNumberLong() throws IOException, JsonGenerationException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeNumber(42L);
 }

 @Test
 public void testWriteNumberDouble() throws IOException, JsonGenerationException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeNumber(3.14);
 }

 @Test
 public void testWriteNumberFloat() throws IOException, JsonGenerationException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeNumber(3.14f);
 }

 @Test
 public void testWriteNumberBigDecimal() throws IOException, JsonGenerationException {
 JsonGenerator generator = new GeneratorBase(0, null);
 BigDecimal dec = new BigDecimal("3.14");
 generator.writeNumber(dec);
 }

 @Test
 public void testWriteBoolean() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeBoolean(true);
 }

 @Test
 public void testWriteNull() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeNull();
 }

 @Test
 public void testWriteObject() throws IOException {
 Object obj = new Object();
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeObject(obj);
 }

 @Test
 public void testWriteTree() throws IOException {
 Object tree = new Object();
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeTree((TreeNode) tree);
 }

 @Test
 public void testFlush() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.setOutput(output);
 generator.writeString("test");
 generator.flush();
 assertEquals("test", output.toString());
 }

 @Test
 public void testClose() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.setOutput(output);
 generator.writeString("test");
 generator.close();
 assertFalse(generator.isClosed());
 }

 @Test
 public void testCloseTwice() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.setOutput(output);
 generator.writeString("test");
 generator.close();
 generator.close();
 assertFalse(generator.isClosed());
 }

 @Test
 public void testCloseAfterFlush() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.setOutput(output);
 generator.writeString("test");
 generator.flush();
 generator.close();
 assertTrue(generator.isClosed());
 }

 @Test
 public void testReleaseBuffers() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 JsonGenerator generator = new GeneratorBase(0, null) {
 @Override protected void _releaseBuffers() throws IOException {
 super._releaseBuffers();
 output.flush();
 }
 };
 generator.setOutput(output);
 generator.writeString("test");
 generator.close();
 assertEquals("test", output.toString());
 }

 @Test
 public void testWriteSurrogate() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeString("\uD800\uDC00");
 String result = generator.getOutputContext().getText();
 assertEquals("\uD800\uDC00", result);
 generator.close();
 }

 @Test
 public void testWriteSurrogatePair() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeString("\uD800\uDCA0\uDC00\uDFFF");
 String result = generator.getOutputContext().getText();
 assertEquals("\uD800\uDCA0\uDC00\uDFFF", result);
 generator.close();
 }

 @Test
 public void testWriteStringWithEscape() throws IOException {
 JsonGenerator generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
 generator.writeString("\u00E5");
 String result = generator.getOutputContext().getText();
 assertEquals("\\u00E5", result);
 generator.close();
 }

 @Test
 public void testWriteStringWithEscapeAndNumbersAsString() throws IOException {
 JsonGenerator generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
 generator.writeString("\u00E542");
 String result = generator.getOutputContext().getText();
 assertEquals("\\u00E542", result);
 generator.close();
 }

 @Test
 public void testWriteStringWithEscapeAndStrictDupDetector() throws IOException {
 JsonGenerator generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
 generator.writeString("\u00E5");
 generator.writeString("\u00E5");
 String result = generator.getOutputContext().getText();
 assertEquals("\u00E5", result);
 generator.close();
 }

 @Test
 public void testWriteStringWithNullOutputContext() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.setOutputContext(null);
 generator.writeString("test");
 }

 @Test(expected = IllegalStateException.class)
 public void testSetOutputContextAfterWrite() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeString("test");
 generator.setOutputContext(null);
 }

 @Test
 public void testWriteNullWithNullOutputContext() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.setOutputContext(null);
 generator.writeNull();
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteStringWithNullInput() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeString(null);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteRawValueWithNullInput() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeRawValue(null);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteRawValueWithNegativeLength() throws IOException {
 JsonGenerator generator = new GeneratorBase(0, null);
 generator.writeRawValue("test", -1, 1);
 }
}