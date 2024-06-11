import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp. testing.CodeSnippetTestBuilder;
import com.google.javascript.jscomp.testing.TestSourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestSourceExcerptProvider.TestSourceExcerpt;
import com.google.javascript.jscomp.testing.TypeChecker;

public class LightweightMessageFormatterTest {

 private LightweightMessageFormatter formatter;
 private SourceExcerptProvider sourceExcerptProvider;

 @Test
 public void testWithoutSourceConstructor() {
 formatter = LightweightMessageFormatter.withoutSource();
 JSError error = new JSError(CHECK, "TestFile.js", 1, "Test error");
 String errorMessage = formatter.formatError(error);
 assertEquals("ERROR - Test error\n", errorMessage);
 }

 @Test
 public void testWithSourceConstructor() {
 sourceExcerptProvider = new TestSourceExcerptProvider();
 formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);
 JSError error = new JSError(CHECK, "TestFile.js", 1, "Test error");
 String errorMessage = formatter.formatError(error);
 TestSourceExcerpt testSourceExcerpt = sourceExcerptProvider.getTestSourceExcerptAt(0);
 String sourceExcerpt = testSourceExcerpt.getSource();
 int charno = testSourceExcerpt.getSourceStart();
 String expectedErrorMessage = "TestFile.js:1: ERROR - Test error\n"
 + sourceExcerpt.substring(0, charno) + "\n^";
 assertEquals(expectedErrorMessage, errorMessage);
 }

 @Test
 public void testFormatWarning() {
 formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);
 JSError warning = new JSError(WARNING, "TestFile.js", 1, "Test warning");
 String warningMessage = formatter.formatWarning(warning);
 assertEquals("WARNING - Test warning\n", warningMessage);
 }

 @Test
 public void testFormatErrorWithSourceExcerpt() {
 sourceExcerptProvider = new TestSourceExcerptProvider();
 formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);
 JSError error = new JSError(CHECK, "TestFile.js", 1, "Test error");
 String errorMessage = formatter.formatError(error);
 TestSourceExcerpt testSourceExcerpt = sourceExcerptProvider.getTestSourceExcerptAt(0);
 String sourceExcerpt = testSourceExcerpt.getSource();
 int charno = testSourceExcerpt.getSourceStart();
 int charnoEnd = testSourceExcerpt.getSourceEnd();
 String expectedErrorMessage = "TestFile.js:1: ERROR - Test error\n"
 + sourceExcerpt.substring(0, charno) + "\n^\n";
 assertEquals(expectedErrorMessage, errorMessage);
 }

 @Test
 public void testFormatErrorWithRegion() {
 sourceExcerptProvider = new TestSourceExcerptProvider();
 formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);
 JSError error = new JSError(CHECK, "TestFile.js", 1, "Test error");
 Region region = new Region("TestFile.js", 1, 1, 1, 5);
 String errorMessage = formatter.formatError(error, region);
 TestSourceExcerpt testSourceExcerpt = sourceExcerptProvider.getTestSourceExcerptAt(0);
 String sourceExcerpt = testSourceExcerpt.getSource();
 int charno = testSourceExcerpt.getSourceStart();
 int charnoEnd = testSourceExcerpt.getSourceEnd();
 int lineNumber = region.getBeginningLineNumber();
 int lineNumberEnd = region.getEndingLineNumber();
 String expectedErrorMessage = "TestFile.js:" + lineNumber + ":\n"
 + "  " + (lineNumber + "| ") + sourceExcerpt.substring(0, charno) + "\n"
 + "  ^^\n";
 assertEquals(expectedErrorMessage, errorMessage);
 }

 @Test
 public void testFormatErrorWithNullSource() {
 formatter = new LightweightMessageFormatter(null, LINE);
 JSError error = new JSError(CHECK, "TestFile.js", 1, "Test error");
 String errorMessage = formatter.formatError(error);
 assertEquals("ERROR - Test error\n", errorMessage);
 }

 @Test
 public void testFormatErrorWithNullExcerpt() {
 formatter = new LightweightMessageFormatter(sourceExcerptProvider, null);
 JSError error = new JSError(CHECK, "TestFile.js", 1, "Test error");
 String errorMessage = formatter.formatError(error);
 assertEquals("ERROR - Test error\n", errorMessage);
 }

 @Test
 public void testFormatErrorWithInvalidExcerpt() {
 formatter = new LightweightMessageFormatter(sourceExcerptProvider, new StringExcerptFormatter());
 JSError error = new JSError(CHECK, "TestFile.js", 1, "Test error");
 assertThrows(RuntimeException.class, () -> formatter.formatError(error));
 }

 @Test
 public void testFormatWarningWithNullSource() {
 formatter = new LightweightMessageFormatter(null, LINE);
 JSError warning = new JSError(WARNING, "TestFile.js", 1, "Test warning");
 String warningMessage = formatter.formatWarning(warning);
 assertEquals("WARNING - Test warning\n", warningMessage);
 }

 @Test
 public void testFormatWarningWithNullExcerpt() {
 formatter = new LightweightMessageFormatter(sourceExcerptProvider, null);
 JSError warning = new JSError(WARNING, "TestFile.js", 1, "Test warning");
 String warningMessage = formatter.formatWarning(warning);
 assertEquals("WARNING - Test warning\n", warningMessage);
 }

 @Test
 public void testFormatWarningWithInvalidExcerpt() {
 formatter = new LightweightMessageFormatter(sourceExcerptProvider, new StringExcerptFormatter());
 JSError warning = new JSError(WARNING, "TestFile.js", 1, "Test warning");
 assertThrows(RuntimeException.class, () -> formatter.formatWarning(warning));
 }

 private static class StringExcerptFormatter implements ExcerptFormatter {
 @Override
 public String formatLine(String line, int lineNumber) {
 throw new RuntimeException("Invalid ExcerptFormatter used");
 }

 @Override
 public String formatRegion(Region region) {
 throw new RuntimeException("Invalid ExcerptFormatter used");
 }
 }

}