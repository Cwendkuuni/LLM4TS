package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    JSError error = new JSError("testError", "Error message", 1, 1, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatError(error);
    assertEquals("1: Error message\n", result);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("testWarning", "Warning message", 1, 1, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatWarning(warning);
    assertEquals("1: WARNING - Warning message\n", result);
  }

  @Test
  public void testFormatErrorWithSource() {
    String source = "function test() {\n  foo();\n}";
    JSError error = new JSError("testError", "Error message", 1, 1, 1, source.length() + 1);
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        if (sourceName.equals("test.js") && lineNumber == 1) {
          return new SourceExcerpt() {
            @Override
            public String getSource() {
              return source;
            }

            @Override
            public int getLength() {
              return source.length();
            }
          };
        }
        return null;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String result = formatter.formatError(error);
    assertEquals("test.js:1: Error message\nfunction test() {\n  foo();\n}^\n", result);
  }

  @Test
  public void testFormatWarningWithSource() {
    String source = "function test() {\n  foo();\n}";
    JSError warning = new JSError("testWarning", "Warning message", 1, 1, 1, source.length() + 1);
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        if (sourceName.equals("test.js") && lineNumber == 1) {
          return new SourceExcerpt() {
            @Override
            public String getSource() {
              return source;
            }

            @Override
            public int getLength() {
              return source.length();
            }
          };
        }
        return null;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String result = formatter.formatWarning(warning);
    assertEquals("test.js:1: WARNING - Warning message\nfunction test() {\n  foo();\n}^\n", result);
  }

  @Test
  public void testFormatErrorWithRegion() {
    String source = "function test() {\n  if (foo) {\n    bar();\n  } else {\n    baz();\n  }\n}";
    JSError error = new JSError("testError", "Error message", 1, 3, 2, source.length() + 1);
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        if (sourceName.equals("test.js")) {
          return new SourceExcerpt() {
            @Override
            public String getSource() {
              return source;
            }

            @Override
            public int getLength() {
              return source.length();
            }

            @Override
            public Region getRegion() {
              return new Region() {
                @Override
                public int getBeginningLineNumber() {
                  return 1;
                }

                @Override
                public int getEndingLineNumber() {
                  return 1;
                }

                @Override
                public int getLength() {
                  return source.substring(0, 5).length();
                }

                @Override
                public String getSourceExcerpt() {
                  return source.substring(0, 5);
                }
              };
            }
          };
        }
        return null;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String result = formatter.formatError(error);
    assertEquals("test.js:1: Error message\n  if (foo) {\n   ^\n", result);
  }

  @Test
  public void testFormatWarningWithRegion() {
    String source = "function test() {\n  if (foo) {\n    bar();\n  } else {\n    baz();\n  }\n}";
    JSError warning = new JSError("testWarning", "Warning message", 1, 3, 2, source.length() + 1);
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        if (sourceName.equals("test.js")) {
          return new SourceExcerpt() {
            @Override
            public String getSource() {
              return source;
            }

            @Override
            public int getLength() {
              return source.length();
            }

            @Override
            public Region getRegion() {
              return new Region() {
                @Override
                public int getBeginningLineNumber() {
                  return 1;
                }

                @Override
                public int getEndingLineNumber() {
                  return 1;
                }

                @Override
                public int getLength() {
                  return source.substring(0, 5).length();
                }

                @Override
                public String getSourceExcerpt() {
                  return source.substring(0, 5);
                }
              };
            }
          };
        }
        return null;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    String result = formatter.formatWarning(warning);
    assertEquals("test.js:1: WARNING - Warning message\n  if (foo) {\n   ^\n", result);
  }

  @Test
  public void testFormatErrorWithEmptySource() {
    JSError error = new JSError("testError", "Error message", 1, 1, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatError(error);
    assertEquals("1: Error message\n", result);
  }

  @Test
  public void testFormatWarningWithEmptySource() {
    JSError warning = new JSError("testWarning", "Warning message", 1, 1, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    String result = formatter.formatWarning(warning);
    assertEquals("1: WARNING - Warning message\n", result);
  }

  @Test
  public void testFormatErrorWithNullSource() {
    JSError error = new JSError("testError", "Error message", 1, 1, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatError(error);
    assertEquals("1: Error message\n", result);
  }

  @Test
  public void testFormatWarningWithNullSource() {
    JSError warning = new JSError("testWarning", "Warning message", 1, 1, 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String result = formatter.formatWarning(warning);
    assertEquals("1: WARNING - Warning message\n", result);
  }
}