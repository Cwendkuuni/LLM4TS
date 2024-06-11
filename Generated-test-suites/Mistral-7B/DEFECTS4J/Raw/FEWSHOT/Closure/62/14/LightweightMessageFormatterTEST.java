package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSError.Region;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Strings;
import java.util.Arrays;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testEmptyConstructor() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    assertEquals("ERROR - ", formatter.formatError(mockError()));
  }

  @Test
  public void testConstructorWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.get(Mockito.any(SourceExcerptProvider.class), Mockito.anyString(), Mockito.anyInt(),
        Mockito.any(ExcerptFormatter.class)))
        .thenReturn("source");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    assertEquals(
        "ERROR - someFile.js:1: " +
            "someErrorMessage" +
            "^\nsource",
        formatter.formatError(mockErrorWithSource("someFile.js", 1, "someErrorMessage")));
  }

  @Test
  public void testConstructorWithRegionSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.get(Mockito.any(SourceExcerptProvider.class), Mockito.anyString(), Mockito.anyInt(),
        Mockito.any(ExcerptFormatter.class)))
        .thenReturn("source");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.REGION);

    JSError error = mockErrorWithRegion("someFile.js", 1, 2, 3, "someErrorMessage");
    assertEquals(
        "ERROR - someFile.js:1:2-3: " +
            "someErrorMessage" +
            "^\n" +
            "  someCode\nsource",
        formatter.formatError(error));
  }

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    assertEquals("ERROR - someFile.js:1: someErrorMessage", formatter.formatError(mockErrorWithSource("someFile.js", 1, "someErrorMessage")));
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    assertEquals("WARNING - someFile.js:1: someErrorMessage", formatter.formatWarning(mockErrorWithSource("someFile.js", 1, "someErrorMessage")));
  }

  @Test
  public void testFormatErrorWithRegionSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.get(Mockito.any(SourceExcerptProvider.class), Mockito.anyString(), Mockito.anyInt(),
        Mockito.any(ExcerptFormatter.class)))
        .thenReturn("source");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.REGION);

    JSError error = mockErrorWithRegion("someFile.js", 1, 2, 3, "someErrorMessage");
    assertEquals(
        "ERROR - someFile.js:1:2-3: someErrorMessage" +
            "^\n" +
            "  someCode\nsource",
        formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithEmptySource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.get(Mockito.any(SourceExcerptProvider.class), Mockito.anyString(), Mockito.anyInt(),
        Mockito.any(ExcerptFormatter.class)))
        .thenReturn("");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.REGION);

    JSError error = mockErrorWithRegion("someFile.js", 1, 2, 3, "someErrorMessage");
    assertEquals(
        "ERROR - someFile.js:1:2-3: someErrorMessage",
        formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithNoSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    assertEquals("ERROR - someErrorMessage", formatter.formatError(mockError()));
  }

  @Test
  public void testFormatWarningWithNoSourceAndEmptyMessage() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    assertEquals("", formatter.formatWarning(mockError()));
  }

  @Test
  public void testFormatWarningWithNoSourceAndMessage() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();

    assertEquals("WARNING - someErrorMessage", formatter.formatWarning(mockErrorWithMessage("someErrorMessage")));
  }

  private JSError mockError() {
    return new JSError(null, null, null, null, null, null, null, null, null, null, null, CheckLevel.ERROR);
  }

  private JSError mockErrorWithSource(String sourceName, int lineNumber, String description) {
    return new JSError(sourceName, lineNumber, null, null, null, null, null, null, null, null, null, CheckLevel.ERROR, description);
  }

  private JSError mockErrorWithMessage(String description) {
    return new JSError(null, 0, null, null, null, null, null, null, null, null, null, CheckLevel.ERROR, description);
  }

  private JSError mockErrorWithRegion(String sourceName, int lineNumber, int charno, int endLineNumber,
      String description) {
    Region region = new Region(sourceName, lineNumber, charno, endLineNumber);
    return new JSError(sourceName, lineNumber, region, null, null, null, null, null, null, null, null, CheckLevel.ERROR, description);
  }
}

class Region {
  final String sourceName;
  final int beginningLineNumber;
  final int startingColumn;
  final int endingLineNumber;

  Region(String sourceName, int beginningLineNumber, int startingColumn, int endingLineNumber) {
    this.sourceName = sourceName;
    this.beginningLineNumber = beginningLineNumber;
    this.startingColumn = startingColumn;
    this.endingLineNumber = endingLineNumber;
  }

  int getBeginningLineNumber() {
    return beginningLineNumber;
  }

  int getEndingLineNumber() {
    return endingLineNumber;
  }

  int getCharno() {
    return startingColumn;
  }

  String getSourceExcerpt() {
    int length = endingLineNumber - beginningLineNumber + 1;
    String[] lines = new String[length];
    Arrays.fill(lines, "someCode");
    return new String(Arrays.copyOf(lines, length));
  }
}

class Mockito {

  public static Object mock(Class<?> clazz) {
    return mock(clazz, false);
  }

  public static Object mock(Class<?> clazz, boolean lenient) {
    return org.mockito.Mockito.mock(clazz, lenient);
  }
}

class Strings {
  public static String repeat(String str, int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; ++i) {
      sb.append(str);
    }
    return sb.toString();
  }
}

class LineNumberingFormatter implements ExcerptFormatter {
  @Override
  public String formatLine(String line, int lineNumber) {
    return line;
  }

  @Override
  public String formatRegion(Region region) {
    if (region == null) {
      return null;
    }
    String code = region.getSourceExcerpt();
    if (code.length() == 0) {
      return null;
    }

    // max length of the number display
    int numberLength = Integer.toString(region.getEndingLineNumber()).length();

    // formatting
    StringBuilder builder = new StringBuilder(code.length() * 2);
    int start = 0;
    int end = code.indexOf('\n', start);
    int lineNumber = region.getBeginningLineNumber();
    while (start >= 0) {
      // line extraction
      String line;
      if (end < 0) {
        line = code.substring(start);
        if (line.length() == 0) {
          return builder.substring(0, builder.length() - 1);
        }
      } else {
        line = code.substring(start, end);
      }
      builder.append("  ");

      // nice spaces for the line number
      int spaces = numberLength - Integer.toString(lineNumber).length();
      builder.append(Strings.repeat(" ", spaces));
      builder.append(lineNumber);
      builder.append("| ");

      // end & update
      if (end < 0) {
        builder.append(line);
        start = -1;
      } else {
        builder.append(line);
        builder.append('\n');
        start = end + 1;
        end = code.indexOf('\n', start);
        lineNumber++;
      }
    }
    return builder.toString();
  }
}