package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.ExcerptFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSError.SourceLocation;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestingUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for {@link LightweightMessageFormatter}.
 */
public class LightweightMessageFormatterTest {

  private static final String SOURCE = "src/test/java/com/google/javascript/jscomp/LightweightMessageFormatterTest.java";

  // ----------------------------------------------------------------------- //

  @org.junit.Test
  public void testLightweightMessageFormatterConstructionNoSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertEquals(AbstractMessageFormatter.class, formatter.getClass());
  }

  @org.junit.Test
  public void testLightweightMessageFormatterConstructionWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    assertEquals(LightweightMessageFormatter.class, formatter.getClass());
    Preconditions.checkState(formatter.getSource() != null);
  }

  @org.junit.Test
  public void testLightweightMessageFormatterConstructionWithExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter =
        new LightweightMessageFormatter(source, LightweightMessageFormatter.LINE);
    assertEquals(LightweightMessageFormatter.class, formatter.getClass());
    Preconditions.checkState(formatter.getSource() != null);
    Preconditions.checkState(formatter.getExcerpt() == LightweightMessageFormatter.LINE);
  }

  // ----------------------------------------------------------------------- //

  @org.junit.Test
  public void testFormatError() {
    JSError error = createError("TestError", SOURCE, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String actual = formatter.formatError(error);
    String expected = "ERROR - TestError\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithSource() {
    JSError error = createError("TestError", SOURCE, 1, 1);
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String actual = formatter.formatError(error);
    String expected = "TestError:1:1 ERROR - TestError\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarning() {
    JSError warning = createWarning("TestWarning", SOURCE, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String actual = formatter.formatWarning(warning);
    String expected = "WARNING - TestWarning\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithSource() {
    JSError warning = createWarning("TestWarning", SOURCE, 1, 1);
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String actual = formatter.formatWarning(warning);
    String expected = "TestWarning:1:1 WARNING - TestWarning\n";
    assertEquals(expected, actual);
  }

  // ----------------------------------------------------------------------- //

  @org.junit.Test(expected = NullPointerException.class)
  public void testFormatErrorNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    formatter.formatError(null);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testFormatWarningNullWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    formatter.formatWarning(null);
  }

  @org.junit.Test
  public void testFormatErrorNullSource() {
    JSError error = createError("TestError", SOURCE, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String actual = formatter.formatError(error);
    String expected = "ERROR - TestError\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorEmptySource() {
    JSError error = createError("TestError", SOURCE, 1, 1);
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(SOURCE, 1)).thenReturn("");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String actual = formatter.formatError(error);
    String expected = "ERROR - TestError\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorSourceMissing() {
    JSError error = createError("TestError", SOURCE, 1, 1);
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(SOURCE, 1)).thenReturn(null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String actual = formatter.formatError(error);
    String expected = "ERROR - TestError\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningNullSource() {
    JSError warning = createWarning("TestWarning", SOURCE, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String actual = formatter.formatWarning(warning);
    String expected = "WARNING - TestWarning\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningEmptySource() {
    JSError warning = createWarning("TestWarning", SOURCE, 1, 1);
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(SOURCE, 1)).thenReturn("");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String actual = formatter.formatWarning(warning);
    String expected = "WARNING - TestWarning\n";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningSourceMissing() {
    JSError warning = createWarning("TestWarning", SOURCE, 1, 1);
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSourceExcerpt(SOURCE, 1)).thenReturn(null);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String actual = formatter.formatWarning(warning);
    String expected = "WARNING - TestWarning\n";
    assertEquals(expected, actual);
  }

  // ----------------------------------------------------------------------- //

  private JSError createError(String message, String source, int line, int charno) {
    return new JSError(
        new SourceLocation(source, line, charno),
        CheckLevel.ERROR,
        message);
  }

  private JSError createWarning(String message, String source, int line, int charno) {
    return new JSError(
        new SourceLocation(source, line, charno),
        CheckLevel.WARNING,
        message);
  }

  // ----------------------------------------------------------------------- //

  @org.junit.Test
  public void testLineNumberingFormatterFormatLine() {
    ExcerptFormatter formatter = new LineNumberingFormatter();
    String source = "class Test {}\n";
    int lineNumber = 1;
    String line = "class Test {}\n";
    String actual = formatter.formatLine(line, lineNumber);
    String expected = "  " + lineNumber + "| ";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testLineNumberingFormatterFormatRegion() {
    ExcerptFormatter formatter = new LineNumberingFormatter();
    String source = "class Test {\n" +
        "  void foo() {\n" +
        "    int x = 1;\n" +
        "  }\n" +
        "}";
    Region region = TestingUtils.createRegion(source, 1, 4, 4, 6);
    String actual = formatter.formatRegion(region);
    String expected = " 1| class Test {\n" +
        "    ^^^^|\n" +
        "   1| void foo() {\n" +
        "   2|   int x = 1;\n" +
        "   3| }\n" +
        "  }";
    assertEquals(expected, actual);
  }

  // ----------------------------------------------------------------------- //

  @org.junit.Test
  public void testLineNumberingFormatterFormatRegionNoSource() {
    ExcerptFormatter formatter = new LineNumberingFormatter();
    String source = null;
    Region region = TestingUtils.createRegion("", 1, 1, 1, 1);
    String actual = formatter.formatRegion(region);
    String expected = "";
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testLineNumberingFormatterFormatRegionEmptySource() {
    ExcerptFormatter formatter = new LineNumberingFormatter();
    String source = "";
    Region region = TestingUtils.createRegion(source, 1, 1, 1, 1);
    String actual = formatter.formatRegion(region);
    String expected = "";
    assertEquals(expected, actual);
  }

  // ----------------------------------------------------------------------- //

  private List<String> lines(String source) {
    return Arrays.asList(source.split("\n"));
  }

  private int lineNumber(List<String> lines, int index) {
    return lines.get(index).isEmpty() ? index + 1 : index;
  }

  private Region createRegion(int beginLine, int beginColumn, int endLine,
      int endColumn) {
    return TestingUtils.createRegion(SOURCE, beginLine, beginColumn, endLine, endColumn);
  }

  private Region createRegion(String source, int beginLine, int beginColumn,
      int endLine, int endColumn) {
    return TestingUtils.createRegion(source, beginLine, beginColumn, endLine, endColumn);
  }
}