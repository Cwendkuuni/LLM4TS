import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.ExcerptFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.MessageFormatter;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.MockSourceExcerptProvider;
import com.google.common.base.Preconditions;

import org.junit.Test;

public class LightweightMessageFormatterTest {

 private MessageFormatter lightweightMessageFormatter;
 private SourceExcerptProvider sourceExcerptProvider;

 @Test
 public void testLightweightMessageFormatterEmptyConstruction() {
   lightweightMessageFormatter = new LightweightMessageFormatter();
   assertEquals("", lightweightMessageFormatter.formatError(createError()));
   assertEquals("", lightweightMessageFormatter.formatWarning(createWarning()));
 }

 @Test
 public void testLightweightMessageFormatterWithSourceConstruction() {
   sourceExcerptProvider = mock(SourceExcerptProvider.class);
   lightweightMessageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);
   assertEquals("", lightweightMessageFormatter.formatError(createError()));
   assertEquals("", lightweightMessageFormatter.formatWarning(createWarning()));

   when(sourceExcerptProvider.getExcerpt(MockSourceExcerptProvider.ANY, "Test.js", 1, LINE))
       .thenReturn("test code");

   JSError error = createError();
   error.sourceName = "Test.js";
   error.lineNumber = 1;

   String result = lightweightMessageFormatter.formatError(error);
   assertEquals("ERROR - test code:1: test code\n", result);

   String resultWarning = lightweightMessageFormatter.formatWarning(createWarning());
   assertEquals("WARNING - Test.js:1: test code\n", resultWarning);
 }

 @Test
 public void testLightweightMessageFormatterWithoutSourceConstruction() {
   lightweightMessageFormatter = LightweightMessageFormatter.withoutSource();
   JSError error = createError();

   assertEquals("ERROR -  :0: \n", lightweightMessageFormatter.formatError(error));
   assertEquals("WARNING -  :0: \n", lightweightMessageFormatter.formatWarning(createWarning()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLightweightMessageFormatterNullSourceProvider() {
   lightweightMessageFormatter = new LightweightMessageFormatter(null, LINE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLightweightMessageFormatterNullExcerpt() {
   lightweightMessageFormatter = new LightweightMessageFormatter(mock(SourceExcerptProvider.class), null);
 }

 @Test
 public void testFormatError() {
   String line = "function testFunction() { return 1 + 1; }";
   SourceExcerptProvider sourceExcerptProvider = new MockSourceExcerptProvider(line);
   lightweightMessageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

   JSError error = createError();
   error.sourceName = "Test.js";
   error.lineNumber = 1;
   error.columnNumber = 5;

   String expectedResult = "ERROR - Test.js:1:5: Expected ';', but found '{'";
   String result = lightweightMessageFormatter.formatError(error);
   assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatWarning() {
   String line = "function testFunction() { return 1 + 1; }";
   SourceExcerptProvider sourceExcerptProvider = new MockSourceExcerptProvider(line);
   lightweightMessageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

   JSError warning = createWarning();
   warning.sourceName = "Test.js";
   warning.lineNumber = 1;
   warning.columnNumber = 5;

   String expectedResult = "WARNING - Test.js:1:5: 'return' outside of function body";
   String result = lightweightMessageFormatter.formatWarning(warning);
   assertEquals(expectedResult, result);
 }

 private JSError createError() {
   return new JSError("Test.js", 1, "Expected ';', but found '{'");
 }

 private JSError createWarning() {
   return new JSError("Test.js", 1, "return outside of function body");
 }
}