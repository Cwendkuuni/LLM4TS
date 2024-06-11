package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.Region;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.rhino.JSON;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class LightweightMessageFormatterTest {
 // Test cases from the first expert
 // ...

 // Test cases from the second expert
 // ...

 // Test cases from the third expert
 // ...

 @Test
 public void testFormatErrorWithEmptySourceCodeAndNonEmptyDescription() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 JSError error = new JSError("Test Error", "Test Source", 1, 1, "Test Description");
 String message = formatter.formatError(error);
 assertEquals("ERROR - Test Error: Test Description\n", message);
 }

 @Test
 public void testFormatErrorWithEmptySourceCodeAndEmptyDescription() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 JSError error = new JSError("Test Error", "Test Source", 1, 1, "");
 String message = formatter.formatError(error);
 assertEquals("ERROR - Test Error\n", message);
 }

 @Test
 public void testFormatWarningWithEmptySourceCodeAndNonEmptyDescription() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 JSError warning = new JSError("Test Warning", "Test Source", 1, 1, "Test Description");
 String message = formatter.formatWarning(warning);
 assertEquals("WARNING - Test Warning: Test Description\n", message);
 }

 @Test
 public void testFormatWarningWithEmptySourceCodeAndEmptyDescription() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 JSError warning = new JSError("Test Warning", "Test Source", 1, 1, "");
 String message = formatter.formatWarning(warning);
 assertEquals("WARNING - Test Warning\n", message);
 }

 // Test case for the formatError method with empty error description and empty source code
 @Test
 public void testFormatErrorWithEmptyDescriptionAndEmptySourceCode() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 JSError error = new JSError("", "", 1, 1);
 String message = formatter.formatError(error);
 assertEquals("ERROR - \n", message);
 }

 // Test case for the formatWarning method with empty warning description and empty source code
 @Test
 public void testFormatWarningWithEmptyDescriptionAndEmptySourceCode() {
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
 JSError warning = new JSError("", "", 1, 1);
 String message = formatter.formatWarning(warning);
 assertEquals("WARNING - \n", message);
 }
}