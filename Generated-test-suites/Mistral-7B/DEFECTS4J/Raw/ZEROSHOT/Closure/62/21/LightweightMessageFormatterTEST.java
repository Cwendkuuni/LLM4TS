import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.jscomp.testing.testingHelper;

import java.io.IOException;
import java.util.List;

public class LightweightMessageFormatterTest {

 private LightweightMessageFormatter lightweightMessageFormatter;
 private SourceExcerptProvider sourceExcerptProvider;
 private TestSourceFile testSourceFile;

 @org.junit.Before
 public void setUp() {
 sourceExcerptProvider = mock(SourceExcerptProvider.class);
 testSourceFile = new TestSourceFile("TestSourceFile.js", "sourceCode");
 lightweightMessageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
 }

 @org.junit.Test
 public void testFormatErrorWithoutSource() {
 JSError error = new JSError(CheckLevel.ERROR, "testErrorMessage", "testFile.js", 1);
 when(sourceExcerptProvider.getSource(testingHelper.TEST_SCOPE)).thenReturn(null);
 String expectedErrorMessage = "ERROR - testErrorMessage\n";
 String actualErrorMessage = lightweightMessageFormatter.formatError(error);
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

 @org.junit.Test
 public void testFormatErrorWithSource() throws IOException {
 JSError error = new JSError(CheckLevel.ERROR, "testErrorMessage", "testFile.js", 1, 2, 3);
 when(sourceExcerptProvider.getSource(testingHelper.TEST_SCOPE)).thenReturn(testSourceFile);
 when(sourceExcerptProvider.getSourceExcerpt(testingHelper.TEST_SCOPE, "testFile.js", 1)).thenReturn(createSourceExcerpt("line 1"));
 String expectedErrorMessage = "TestFile.js:1: ERROR - testErrorMessage\nline 1\n";
 String actualErrorMessage = lightweightMessageFormatter.formatError(error);
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

 @org.junit.Test
 public void testFormatWarningWithoutSource() {
 JSError warning = new JSError(CheckLevel.WARNING, "testWarningMessage", "testFile.js", 1);
 when(sourceExcerptProvider.getSource(testingHelper.TEST_SCOPE)).thenReturn(null);
 String expectedWarningMessage = "WARNING - testWarningMessage\n";
 String actualWarningMessage = lightweightMessageFormatter.formatWarning(warning);
 assertEquals(expectedWarningMessage, actualWarningMessage);
 }

 @org.junit.Test
 public void testFormatWarningWithSource() throws IOException {
 JSError warning = new JSError(CheckLevel.WARNING, "testWarningMessage", "testFile.js", 1, 2, 3);
 when(sourceExcerptProvider.getSource(testingHelper.TEST_SCOPE)).thenReturn(testSourceFile);
 when(sourceExcerptProvider.getSourceExcerpt(testingHelper.TEST_SCOPE, "testFile.js", 1)).thenReturn(createSourceExcerpt("line 1"));
 String expectedWarningMessage = "TestFile.js:1: WARNING - testWarningMessage\nline 1\n";
 String actualWarningMessage = lightweightMessageFormatter.formatWarning(warning);
 assertEquals(expectedWarningMessage, actualWarningMessage);
 }

 @org.junit.Test
 public void testFormatWithoutSource() {
 JSError error = new JSError(CheckLevel.ERROR, "testErrorMessage", null, 0);
 when(sourceExcerptProvider.getSource(testingHelper.TEST_SCOPE)).thenReturn(null);
 String expectedErrorMessage = "ERROR - testErrorMessage\n";
 String actualErrorMessage = lightweightMessageFormatter.formatError(error);
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

 @org.junit.Test
 public void testFormatWithSource() throws IOException {
 JSError error = new JSError(CheckLevel.ERROR, "testErrorMessage", "testFile.js", 1, 2, 3);
 when(sourceExcerptProvider.getSource(testingHelper.TEST_SCOPE)).thenReturn(testSourceFile);
 when(sourceExcerptProvider.getSourceExcerpt(testingHelper.TEST_SCOPE, "testFile.js", 1)).thenReturn(createSourceExcerpt("line 1 content"));
 String expectedErrorMessage = "testFile.js:1: ERROR - testErrorMessage\nline 1 content\n";
 String actualErrorMessage = lightweightMessageFormatter.formatError(error);
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

 private SourceExcerpt createSourceExcerpt(String content) {
 List<String> lines = List.of(content.split("\n"));
 return new SourceExcerpt(SourceExcerptProvider.LINE, lines);
 }
}