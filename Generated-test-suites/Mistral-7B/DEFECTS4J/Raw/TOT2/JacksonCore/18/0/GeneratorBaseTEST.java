package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.version.VersionUtil;

public class GeneratorBaseTest {

 private static final JsonGenerator TEST_INSTANCE;

 static {
 try {
 TEST_INSTANCE = new GeneratorBase(GeneratorBase.Feature.AUTO_CLOSE_SOURCE.getMask() | GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testVersion() {
 assertEquals("2.12.0", TEST_INSTANCE.version().toString());
 }

 @Test
 public void testIsEnabled() {
 assertTrue(TEST_INSTANCE.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
 assertFalse(TEST_INSTANCE.isEnabled(GeneratorBase.Feature.WRITE_BINARY));
 }

 @Test
 public void testSetEnabled() throws IOException {
 // test setting and getting bool flag
 TEST_INSTANCE.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
 assertFalse(TEST_INSTANCE.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
 TEST_INSTANCE.enable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
 assertTrue(TEST_INSTANCE.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));

 // test setting and getting Feature mask
 int mask = GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask();
 TEST_INSTANCE.overrideStdFeatures(0, mask);
 assertTrue(TEST_INSTANCE.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
 assertTrue(TEST_INSTANCE.isEnabled(GeneratorBase.Feature.ESCAPE_NON_ASCII));
 TEST_INSTANCE.overrideStdFeatures(0, ~mask);
 assertFalse(TEST_INSTANCE.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
 assertFalse(TEST_INSTANCE.isEnabled(GeneratorBase.Feature.ESCAPE_NON_ASCII));
 }

 @Test
 public void testWriteString() throws IOException, JsonProcessingException {
 String text = "This is a test string";
 TEST_INSTANCE.writeString(text);
 assertEquals(text, TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteRawValue() throws IOException {
 String text = "This is a raw value test";
 TEST_INSTANCE.writeRawValue(text);
 assertEquals(text, TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteRawValueWithOffset() throws IOException {
 char[] text = {'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 'r', 'a', 'w', ' ', 'v', 'a', 'l', 'u', 'e', ' ', 't', 'e', 's', 't'};
 TEST_INSTANCE.writeRawValue(text, 0, text.length);
 assertEquals(new String(text), TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteRawValueWithSerialization() throws IOException, JsonProcessingException {
 TEST_INSTANCE.writeRawValue("This is a raw value test");
 TEST_INSTANCE.flush();
 String result = TEST_INSTANCE.getOutputContext().getTextOutput();
 assertEquals("This is a raw value test", result);
 }

 @Test
 public void testWriteNumber() throws IOException {
 TEST_INSTANCE.writeNumber(123);
 assertEquals("123", TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteNumberWithCommas() throws IOException {
 TEST_INSTANCE.setFeatureMask(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
 TEST_INSTANCE.writeNumber(1234567890);
 assertEquals("123,456,789,0", TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteBoolean() throws IOException {
 TEST_INSTANCE.writeBoolean(true);
 assertEquals("true", TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteNull() throws IOException {
 TEST_INSTANCE.writeNull();
 assertEquals("null", TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteObject() throws IOException {
 String s = "test";
 TEST_INSTANCE.writeObject(s);
 assertEquals(s, TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteObjectWithNullValue() throws IOException {
 TEST_INSTANCE.writeObject(null);
 assertEquals("null", TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteObjectWithCustomCodec() throws IOException {
 TestObject obj = new TestObject("Test", 123);
 TEST_INSTANCE.setCodec(new ObjectMapper().getFactory().findValueSerializer(TestObject.class));
 TEST_INSTANCE.writeObject(obj);
 TEST_INSTANCE.flush();
 String result = TEST_INSTANCE.getOutputContext().getTextOutput();
 assertEquals("{\"name\":\"Test\",\"value\":123}", result);
 }

 @Test
 public void testWriteRawValueWithEmptyString() throws IOException {
 TEST_INSTANCE.writeRawValue("");
 TEST_INSTANCE.flush();
 String result = TEST_INSTANCE.getOutputContext().getTextOutput();
 assertEquals("", result);
 }

 @Test
 public void testWriteRawValueWithEmptyStringAndOffset() throws IOException {
 char[] text = {};
 TEST_INSTANCE.writeRawValue(text, 0, text.length);
 TEST_INSTANCE.flush();
 String result = TEST_INSTANCE.getOutputContext().getTextOutput();
 assertEquals("", result);
 }

 @Test
 public void testWriteRawValueWithBigDecimal() throws IOException, JsonProcessingException {
 BigDecimal dec = new BigDecimal("123456789012345678901234567890");
 TEST_INSTANCE.writeNumber(dec);
 assertEquals(dec.toString(), TEST_INSTANCE.getCurrentValue().asText());
 }

 @Test
 public void testWriteRawValueWithBigDecimalAndOffset() throws IOException, JsonProcessingException {
 BigDecimal dec = new BigDecimal("123456789012345678901234567890");
 TEST_INSTANCE.writeNumber(dec);
 TEST_INSTANCE.flush();
 String result = TEST_INSTANCE.getOutputContext().getTextOutput();
 assertEquals(dec.toString(), result);
 }

 @Test
 public void testWriteStringWithSurrogate() throws IOException {
 TEST_INSTANCE.writeString("\uD800\uDC00");
 TEST_INSTANCE.flush();
 String result = TEST_INSTANCE.getOutputContext().getTextOutput();
 assertEquals("\uD800\uDC00", result);
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteStringWithInvalidSurrogate() throws IOException {
 TEST_INSTANCE.writeString("\uD800\uDDDD");
 }

 @Test
 public void testWriteStringWithUnicodeChar() throws IOException {
 TEST_INSTANCE.writeString("TEST\u00E9");
 TEST_INSTANCE.flush();
 String result = TEST_INSTANCE.getOutputContext().getTextOutput();
 assertEquals("TEST\u00E9", result);
 }

 @Test
 public void testIsClosed() throws IOException {
 TEST_INSTANCE.close();
 assertTrue(TEST_INSTANCE.isClosed());
 }
}

class TestObject {
 private final String name;
 private final int value;

 public TestObject(String name, int value) {
 this.name = name;
 this.value = value;
 }

 @Override
 public String toString() {
 return "{\"name\":\"" + name + "\",\"value\":\"" + value + "\"}";
 }
}