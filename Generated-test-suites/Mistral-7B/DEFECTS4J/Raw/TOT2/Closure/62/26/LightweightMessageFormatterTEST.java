package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.test.BaseTest;

public class LightweightMessageFormatterTest extends BaseTest {

 private static class TestSourceProvider implements SourceExcerptProvider {
 @Override
 public String getSourceForLine(String file, int lineNumber) {
 return "Test Source";
 }
 }

 private static class JSTestError extends JSError {
 public JSTestError(String description, String sourceName, int lineNumber, int charno, String message) {
 super(description, sourceName, lineNumber, charno, message);
 }
 }

 @BeforeClass
 public static void setup() {
 LightweightMessageFormatter.withoutSource();
 }

 @Test
 public void testLightweightMessageFormatterWithoutSource() {
 LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
 JSError error = new JSError("Test Error", null, 1, 1, "Test Message");
 String formattedError = formatter.formatError(error);
 assertEquals("ERROR - Test Error\n", formattedError);
 }

 @Test
 public void testLightweightMessageFormatterWithSource() {
 SourceExcerptProvider source = new TestSourceProvider();
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
 JSError error = new JSError("Test Error", "Test Source", 1, 1, "Test Message");
 String formattedError = formatter.formatError(error);
 assertEquals("TEST_SOURCE:1: ERROR - Test Error\n", formattedError);
 }

 @Test
 public void testLightweightMessageFormatterWithNullSource() {
 JSError error = new JSError("Test Error", "Test Source", 1, 1, "Test Message");
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 String formattedError = formatter.formatError(error);
 assertEquals("ERROR - Test Error\n", formattedError);
 }

 @Test
 public void testLightweightMessageFormatterWithNullError() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 String formattedError = formatter.formatError(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testLightweightMessageFormatterWithWarning() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 JSError warning = new JSError("Test Warning", "Test Source", 1, 1, "Test Message");
 String formattedWarning = formatter.formatWarning(warning);
 assertEquals("WARNING - Test Warning\n", formattedWarning);
 }

 @Test(expected = NullPointerException.class)
 public void testLightweightMessageFormatterWithNullSourceExcerptProvider() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 Preconditions.checkArgument(formatter.getSource() != null);
 }

 @Test(expected = NullPointerException.class)
 public void testLightweightMessageFormatterWithNullExcerpt() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 formatter.formatError(new JSError("Test Error", null, 1, 1, "Test Message"));
 }
}