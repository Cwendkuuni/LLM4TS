import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.checker.Checker;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class LightweightMessageFormatterTest {

 private LightweightMessageFormatter formatter;
 private SourceExcerptProvider source;

 @Before
 public void setup() {
 source = mock(SourceExcerptProvider.class);
 formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
 }

 @Test
 public void testConstructors() {
 // Test default constructor
 LightweightMessageFormatter noSourceFormatter =
 LightweightMessageFormatter.withoutSource();

 // Test constructor with source provider and excerpt type LINE
 LightweightMessageFormatter lineExcerptFormatter =
 new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);

 // Test constructor with source provider and excerpt type REGION
 LightweightMessageFormatter regionExcerptFormatter =
 new LightweightMessageFormatter(source, LightweightMessageFormatter.REGION);

 checkState(noSourceFormatter != null, "Default constructor should not return null");
 checkState(lineExcerptFormatter != null, "Constructor with source provider and LINE excerpt should not return null");
 checkState(regionExcerptFormatter != null, "Constructor with source provider and REGION excerpt should not return null");
 }

 @Test
 public void testFormatError() {
 JSError error = new JSError("TypeError", "foo", 1, 5, 6);
 String expected = "ERROR - TypeError: foo\nfoo:1:5\n^\n";
 String actual = formatter.formatError(error);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWarning() {
 JSError warning = new JSError("ReferenceError", "bar is not defined", 3, 7, 8);
 String expected = "WARNING - ReferenceError: bar is not defined\n3:7\n^\n";
 String actual = formatter.formatWarning(warning);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithSourceExcerpt() {
 // Arrange
 JSError error = new JSError("TypeError", "foo is not a function", 1, 5, 6,
 "MyJavaScriptFile.js", 10);
 SourceExcerptProvider source = mock(SourceExcerptProvider.class);
 when(source.getSourceExcerpt(eq("MyJavaScriptFile.js"), anyInt(), anyInt()))
 .thenReturn("function foo() { // some code }");
 LightweightMessageFormatter formatter =
 new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);

 // Act
 String actual = formatter.formatError(error);

 // Assert
 String expected = "ERROR - TypeError: foo is not a function\nMyJavaScriptFile.js:10:5\n  function foo() { // some code }\n^\n";
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testFormatWithNullSourceExcerpt() {
 // Arrange
 JSError error = new JSError("TypeError", "foo is not a function", 1, 5, 6, null, 10);
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LightweightMessageFormatter.LINE);

 // Act and Assert
 // NPE is thrown by the getSource() method when source is null
 }

 @Test
 public void testFormatRegion() {
 // Arrange
 JSError error = new JSError("TypeError", "foo is not a function", 1, 5, 6);
 Region region = new Region(1, 5, 1, 10, "MyJavaScriptFile.js");
 when(source.getSourceExcerpt(eq("MyJavaScriptFile.js"), anyInt(), anyInt()))
 .thenReturn("function foo(bar) { // some code }");
 LightweightMessageFormatter formatter =
 new LightweightMessageFormatter(source, LightweightMessageFormatter.REGION);

 // Act
 String actual = formatter.formatError(error, region);

 // Assert
 String expected = "ERROR - TypeError: foo is not a function\nMyJavaScriptFile.js:1:5\nfunction foo(bar) { // some code }\n^\n";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatRegionWithNullRegion() {
 // Arrange
 JSError error = new JSError("TypeError", "foo is not a function", 1, 5, 6);
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.REGION);

 // Act and Assert
 // NPE is thrown by the getSource() method when region is null
 }

 @Test
 public void testFormatRegionWithEmptyRegion() {
 // Arrange
 JSError error = new JSError("TypeError", "foo is not a function", 1, 5, 6);
 Region region = new Region(1, 5, 1, 1, "MyJavaScriptFile.js");
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.REGION);

 // Act
 String actual = formatter.formatError(error, region);

 // Assert
 String expected = "ERROR - TypeError: foo is not a function\nMyJavaScriptFile.js:1:5\n^\n";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatRegionWithTooLongLineNumber() {
 // Arrange
 JSError error = new JSError("TypeError", "foo is not a function", 1, 5, 6);
 Region region = new Region(1, 5, Long.MAX_VALUE, 1, "MyJavaScriptFile.js");
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.REGION);

 // Act and Assert
 // java.lang.IllegalArgumentException: Line number too large is thrown by the formatRegion method
 }

 @Test
 public void testFormatRegionWithNegativeLineNumber() {
 // Arrange
 JSError error = new JSError("TypeError", "foo is not a function", 1, 5, 6);
 Region region = new Region(1, 5, -1, 1, "MyJavaScriptFile.js");
 LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.REGION);

 // Act and Assert
 // java.lang.IllegalArgumentException: Line number must be non-negative is thrown by the formatRegion method
 }
}