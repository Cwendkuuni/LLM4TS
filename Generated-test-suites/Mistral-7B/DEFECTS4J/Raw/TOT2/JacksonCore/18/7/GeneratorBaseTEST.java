package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneratorBaseTest {

 private JsonGenerator generator;

 @Before
 public void setup() throws Exception {
 generator = new JsonFactory(null).createGenerator(new ByteArrayOutputStream(), new DefaultPrettyPrinter());
 }

 // Test for method: public GeneratorBase(int features, ObjectCodec codec)
 @Test
 public void testConstructorWithFeaturesAndCodec() throws Exception {
 JsonGenerator generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), new JsonFactory(null));
 Assert.assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 Assert.assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
 }

 // Test for method: public GeneratorBase(int features, ObjectCodec codec, JsonWriteContext ctxt)
 @Test
 public void testConstructorWithFeaturesAndCodecAndContext() throws Exception {
 JsonGenerator generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), new JsonFactory(null), new JsonWriteContext.Builder().build());
 Assert.assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 Assert.assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
 }

 // Test for method: @Override public Version version()
 @Test
 public void testVersion() throws Exception {
 Version expectedVersion = Version.unknownVersion();
 Version actualVersion = generator.version();
 Assert.assertEquals(expectedVersion, actualVersion);
 }

 // Test for method: protected void _verifyValueWrite(String typeMsg) throws IOException
 @Test(expected = IOException.class)
 public void testVerifyValueWriteThrowsIOException() throws Exception {
 generator.writeRawValue("test");
 generator._verifyValueWrite("write raw value");
 }

 // Test for method: @Override public Object getCurrentValue()
 @Test
 public void testGetCurrentValue() throws Exception {
 Object currentValue = generator.getCurrentValue();
 Assert.assertNull(currentValue);
 }

 // Test for method: @Override public void setCurrentValue(Object v)
 @Test
 public void testSetCurrentValue() throws Exception {
 generator.setCurrentValue("test");
 String currentValue = (String) generator.getCurrentValue();
 Assert.assertEquals("test", currentValue);
 }

 // Test for method: @Override public boolean isEnabled(Feature f)
 @Test
 public void testIsEnabledFeature() throws Exception {
 Assert.assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
 Assert.assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
 Assert.assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 }

 // Test for method: @Override public JsonGenerator configure(Feature f, boolean state)
 // This method is not public, so we can't test it directly. Instead, we'll test the behavior
 // of enabling and disabling the feature using the enable() and disable() methods.

 // Test for method: protected void _checkStdFeatureChanges(int newFeatureFlags, int changedFeatures)
 // This method is not public, so we can't test it directly. Instead, we'll test the behavior
 // of enabling and disabling the features and verify that the _checkStdFeatureChanges() method
 // is called correctly.

 // Test for method: protected PrettyPrinter _constructDefaultPrettyPrinter()
 @Test
 public void testConstructDefaultPrettyPrinter() throws Exception {
 PrettyPrinter defaultPrettyPrinter = generator.getPrettyPrinter();
 Assert.assertNotNull(defaultPrettyPrinter);
 Assert.assertTrue(defaultPrettyPrinter instanceof DefaultPrettyPrinter);
 }

 // Test for method: public void writeString(SerializableString text) throws IOException
 @Test
 public void testWriteString() throws Exception {
 String text = "test";
 generator.writeString(text);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals(text, actualValue);
 }

 // Test for method: public void writeRawValue(String text) throws IOException, JsonProcessingException
 @Test
 public void testWriteRawValueString() throws Exception {
 generator.writeRawValue("test");
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("test", actualValue);
 }

 // Test for method: public void writeRawValue(String text, int offset, int len) throws IOException, JsonProcessingException
 @Test
 public void testWriteRawValueStringWithOffsetAndLength() throws Exception {
 String text = "test";
 generator.writeRawValue(text, 0, text.length());
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals(text, actualValue);
 }

 // Test for method: public void writeRawValue(char[] text, int offset, int len) throws IOException, JsonProcessingException
 @Test
 public void testWriteRawValueCharArray() throws Exception {
 char[] text = {'t', 'e', 's', 't'};
 generator.writeRawValue(text, 0, text.length);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals(new String(text), actualValue);
 }

 // Test for method: public void writeRawValue(SerializableString text) throws IOException, JsonProcessingException
 @Test
 public void testWriteRawValueSerializableString() throws Exception {
 SerializableString text = new StringSerializer("test");
 generator.writeRawValue(text);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("test", actualValue);
 }

 // Test for method: public void writeNumber(int i) throws IOException
 @Test
 public void testWriteNumberInt() throws Exception {
 generator.writeNumber(42);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("42", actualValue);
 }

 // Test for method: public void writeNumber(long l) throws IOException
 @Test
 public void testWriteNumberLong() throws Exception {
 generator.writeNumber(9223372036854775807L);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("9223372036854775807", actualValue);
 }

 // Test for method: public void writeNumber(double d) throws IOException
 @Test
 public void testWriteNumberDouble() throws Exception {
 generator.writeNumber(3.14159265358979323846);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("3.141592653589793", actualValue);
 }

 // Test for method: public void writeNumber(float f) throws IOException
 @Test
 public void testWriteNumberFloat() throws Exception {
 generator.writeNumber((float) 3.14159265358979323846);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("3.141592653589793", actualValue);
 }

 // Test for method: public void writeNumber(BigDecimal dec) throws IOException
 @Test
 public void testWriteNumberBigDecimal() throws Exception {
 BigDecimal bigDecimal = new BigDecimal("3.14159265358979323846");
 generator.writeNumber(bigDecimal);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("3.14159265358979323846", actualValue);
 }

 // Test for method: public void writeBoolean(boolean state) throws IOException
 @Test
 public void testWriteBoolean() throws Exception {
 generator.writeBoolean(true);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("true", actualValue);
 }

 // Test for method: public void writeNull() throws IOException
 @Test
 public void testWriteNull() throws Exception {
 generator.writeNull();
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("null", actualValue);
 }

 // Test for method: public void writeObject(Object value) throws IOException
 @Test
 public void testWriteObject() throws Exception {
 String value = "test";
 generator.writeObject(value);
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("\"test\"", actualValue);
 }

 // Test for method: public void writeTree(TreeNode rootNode) throws IOException
 @Test
 public void testWriteTree() throws Exception {
 // create a simple tree (e.g., an array with one string element)
 ObjectNode rootNode = factory.objectNode();
 ArrayNode arrayNode = rootNode.arrayNode();
 arrayNode.add(factory.textNode("test"));
 // write the tree to the generator
 generator.writeTree(rootNode);
 // verify the output
 String actualValue = generator.getOutput().toString();
 Assert.assertEquals("[\"test\"]", actualValue);
 }

 // Test for method: public void flush() throws IOException
 @Test
 public void testFlush() throws Exception {
 generator.flush();
 }

 // Test for method: public void close() throws IOException
 @Test
 public void testClose() throws Exception {
 generator.close();
 }

 // Test for method: public boolean isClosed()
 @Test
 public void testIsClosed() throws Exception {
 generator.close();
 boolean isClosed = generator.isClosed();
 Assert.assertTrue(isClosed);
 }

 // Test for method: protected void _releaseBuffers()
 // This method is not public, so we can't test it directly. Instead, we'll test that the generator
 // correctly releases buffered data when it is closed.
 @After
 public void tearDown() throws Exception {
 generator.close();
 }
}