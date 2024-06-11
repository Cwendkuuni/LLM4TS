package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  private JSError error;
  private JSError warning;

  @Before
  public void setUp() {
    formatter = LightweightMessageFormatter.withoutSource();
    error = new JSError("error description");
    warning = new JSError("warning description");
  }

  @Test
  public void testFormatErrorWithoutSource() {
    String expected = "ERROR - error description\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    String expected = "WARNING - warning description\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithSource() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        return "source excerpt";
      }
    });

    error = new JSError("error description", "source name", 10);
    String expected = "source name:10: ERROR - error description\nsource excerpt\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithSource() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        return "source excerpt";
      }
    });

    warning = new JSError("warning description", "source name", 5);
    String expected = "source name:5: WARNING - warning description\nsource excerpt\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithLineExcerpt() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        if (lineNumber == 10) {
          return "line excerpt";
        } else {
          return null;
        }
      }
    });

    error = new JSError("error description", "source name", 10);
    String expected = "source name:10: ERROR - error description\nline excerpt\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithLineExcerpt() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        if (lineNumber == 5) {
          return "line excerpt";
        } else {
          return null;
        }
      }
    });

    warning = new JSError("warning description", "source name", 5);
    String expected = "source name:5: WARNING - warning description\nline excerpt\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorNullSourceExcerpt() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        return null;
      }
    });

    error = new JSError("error description", "source name", 10);
    String expected = "source name:10: ERROR - error description\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningNullSourceExcerpt() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        return null;
      }
    });

    warning = new JSError("warning description", "source name", 5);
    String expected = "source name:5: WARNING - warning description\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithArrowIndicator() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        if (lineNumber == 10) {
          return "source excerpt";
        } else {
          return null;
        }
      }
    });

    error = new JSError("error description", "source name", 10, 5);
    String expected = "source name:10: ERROR - error description\nsource excerpt\n     ^\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithArrowIndicator() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        if (lineNumber == 5) {
          return "source excerpt";
        } else {
          return null;
        }
      }
    });

    warning = new JSError("warning description", "source name", 5, 3);
    String expected = "source name:5: WARNING - warning description\nsource excerpt\n  ^\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithoutLineNumber() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        return "source excerpt";
      }
    });

    error = new JSError("error description", "source name");
    String expected = "source name: ERROR - error description\nsource excerpt\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithoutLineNumber() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        return "source excerpt";
      }
    });

    warning = new JSError("warning description", "source name");
    String expected = "source name: WARNING - warning description\nsource excerpt\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithInvalidLineNumber() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        if (lineNumber == 10) {
          return "source excerpt";
        } else {
          return null;
        }
      }
    });

    error = new JSError("error description", "source name", 5);
    String expected = "source name: ERROR - error description\n";
    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithInvalidLineNumber() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProvider() {
      @Override
      public String getSourceExcerpt(String sourceName, int lineNumber) {
        if (lineNumber == 5) {
          return "source excerpt";
        } else {
          return null;
        }
      }
    });

    warning = new JSError("warning description", "source name", 3);
    String expected = "source name: WARNING - warning description\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }
}