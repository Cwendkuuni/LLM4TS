import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestRunner;

public class LightweightMessageFormatterTest {

 private static class TestMessageFormatter extends LightweightMessageFormatter {
   TestMessageFormatter(SourceExcerptProvider source) {
     super(source, LINE);
   }
 }

 @Test
 public void testFormatErrorWithoutSource() {
   LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
   JSError error = new JSError("Error description", "TestFile.js", 10, 10);
   String message = formatter.formatError(error);
   String expected = "ERROR - Error description\n";
   assertEquals(expected, message);
 }

 @Test
 public void testFormatErrorWithSource() {
   CodeSnippetTestRunner runner = new CodeSnippetTestRunner();
   SourceExcerptProvider source = runner.getSourceExcerptProvider();
   LightweightMessageFormatter formatter = new TestMessageFormatter(source);
   JSError error = new JSError("Error description", "TestFile.js", 10, 10);
   String message = formatter.formatError(error);
   String expected = "TestFile.js:10: ERROR - Error description\n";
   assertEquals(expected, message);
 }

 @Test
 public void testFormatWarningWithoutSource() {
   LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
   JSError error = new JSError("Warning description", "TestFile.js", 10, 10);
   String message = formatter.formatWarning(error);
   String expected = "WARNING - Warning description\n";
   assertEquals(expected, message);
 }

 @Test
 public void testFormatWarningWithSource() {
   CodeSnippetTestRunner runner = new CodeSnippetTestRunner();
   SourceExcerptProvider source = runner.getSourceExcerptProvider();
   LightweightMessageFormatter formatter = new TestMessageFormatter(source);
   JSError error = new JSError("Warning description", "TestFile.js", 10, 10);
   String message = formatter.formatWarning(error);
   String expected = "TestFile.js:10: WARNING - Warning description\n";
   assertEquals(expected, message);
 }

 @Test
 public void testFormatWithSourceLineExcerpt() {
   CodeSnippetTestRunner runner = new CodeSnippetTestRunner();
   SourceExcerptProvider source = runner.getSourceExcerptProvider();
   LightweightMessageFormatter formatter = new TestMessageFormatter(source);
   String code = "function test() {\n" +
       "  if (true) {\n" +
       "    alert('foo');\n" +
       "  }\n" +
       "}\n";
   JSError error = new JSError("Error description", "TestFile.js", 3, 1);
   String message = formatter.formatError(error);
   String expected = "TestFile.js:3: ERROR - Error description\n" +
       "  if (true) {\n" +
       "    alert('foo');\n" +
       "  }\n";
   assertEquals(expected, message);
 }

 @Test
 public void testFormatWithSourceRegionExcerpt() {
   CodeSnippetTestRunner runner = new CodeSnippetTestRunner();
   SourceExcerptProvider source = runner.getSourceExcerptProvider();
   LightweightMessageFormatter formatter = new TestMessageFormatter(source);
   String code = "function test() {\n" +
       "  if (true) {\n" +
       "    alert('foo');\n" +
       "  }\n" +
       "}\n";
   JSError error = new JSError("Error description", "TestFile.js", 2, 10, 7);
   String message = formatter.formatError(error);
   String expected = "TestFile.js:2: ERROR - Error description\n" +
       "    alert('foo');\n";
   assertEquals(expected, message);
 }
}