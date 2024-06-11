package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LINE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Region;
import com.google.javascript.jscomp.util.Strings;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);

    String errorDescription = "Description of the error";
    JSError error = new JSError("sourceName", 1, 1, errorDescription);
    String expected = "sourceName: 1: " + CheckLevel.ERROR + " - Description of the error\n";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
            String source = "function foo() {\n  // Line with error\n}";
            return new SourceExcerpt(source, lineNumber, 0, 0, source.length());
          }
        });

    String errorDescription = "Description of the error";
    JSError error = new JSError("sourceName", 1, 1, errorDescription);

    String expected = "sourceName: 1: " + CheckLevel.ERROR + " - Description of the error\n// Line with error\n";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);

    String warningDescription = "Description of the warning";
    JSError warning = new JSError("sourceName", 1, 1, warningDescription);

    String expected = "sourceName: 1: " + CheckLevel.WARNING + " - Description of the warning\n";

    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
            String source = "function foo() {\n  // Line with warning\n}";
            return new SourceExcerpt(source, lineNumber, 0, 0, source.length());
          }
        });

    String warningDescription = "Description of the warning";
    JSError warning = new JSError("sourceName", 1, 1, warningDescription);

    String expected = "sourceName: 1: " + CheckLevel.WARNING + " - Description of the warning\n// Line with warning\n";

    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorNoSourceName() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);

    String errorDescription = "Description of the error";
    JSError error = new JSError(null, 1, 1, errorDescription);

    String expected = "1: " + CheckLevel.ERROR + " - Description of the error\n";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningNoSourceName() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);

    String warningDescription = "Description of the warning";
    JSError warning = new JSError(null, 1, 1, warningDescription);

    String expected = "1: " + CheckLevel.WARNING + " - Description of the warning\n";

    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithRegion() {
    String code = "function foo() {\n  if (x < 0) {\n    throw new Error('Error description');\n  }\n}";
    Region region = new Region("sourceName.js", 1, 1, 1, code.length(), 0);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
            if (sourceName.equals("sourceName.js")) {
              return new SourceExcerpt(code, lineNumber, 0, 0, code.length());
            } else {
              return null;
            }
          }
        });

    String errorDescription = "Error description";
    JSError error = new JSError("sourceName.js", 1, 1, errorDescription);

    String expected = "sourceName.js: 1: " + CheckLevel.ERROR + " - Error description\n  if (x < 0) {\n    throw new Error('Error description');\n  }\n";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithRegionStartAfterLine() {
    String code = "function foo() {\n  if (x < 0) {\n    throw new Error('Error description');\n  }\n}";
    Region region = new Region("sourceName.js", 2, 1, 1, code.length(), 0);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
  public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
    if (sourceName.equals("sourceName.js")) {
      return new SourceExcerpt(code, lineNumber, 0, 0, code.length());
    } else {
      return null;
    }
  }
});

String errorDescription = "Error description";
JSError error = new JSError("sourceName.js", 1, 1, errorDescription);

String expected = "sourceName.js: 1: " + CheckLevel.ERROR + " - Error description\n";

assertEquals(expected, formatter.formatError(error));
}

@Test
public void testFormatWarningWithRegion() {
String code = "function foo() {\n  if (x < 0) {\n    console.log('Warning description');\n  }\n}";
Region region = new Region("sourceName.js", 1, 1, 1, code.length(), 0);

LightweightMessageFormatter formatter = new LightweightMessageFormatter(
new SourceExcerptProvider() {
@Override
public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
if (sourceName.equals("sourceName.js")) {
return new SourceExcerpt(code, lineNumber, 0, 0, code.length());
} else {
return null;
}
}
});

String warningDescription = "Warning description";
JSError warning = new JSError("sourceName.js", 1, 1, warningDescription);

String expected = "sourceName.js: 1: " + CheckLevel.WARNING + " - Warning description\nfunction foo() {\n  if (x < 0) {\n    console.log('Warning description');\n  }\n}";

assertEquals(expected, formatter.formatWarning(warning));
}

@Test
public void testFormatWarningWithRegionStartAfterLineAndRegionEndsInLine() {
String code = "function foo() {\n  if (x < 0) {\n    console.log('Warning description');\n  } else {\n    throw new Error('Error description');\n  }\n}";
Region region = new Region("sourceName.js", 2, 1, 3, code.length(), 0);

LightweightMessageFormatter formatter = new LightweightMessageFormatter(
new SourceExcerptProvider() {
@Override
public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
if (sourceName.equals("sourceName.js")) {
return new SourceExcerpt(code, lineNumber, 0, 0, code.length());
} else {
return null;
}
}
});

String warningDescription = "Warning description";
JSError warning = new JSError("sourceName.js", 1, 1, warningDescription);

String expected = "sourceName.js: 1: " + CheckLevel.WARNING + " - Warning description\nfunction foo() {\n  if (x < 0) {\n    console.log('Warning description');\n  } else {\n    throw new Error('Error description');\n  }\n}";

assertEquals(expected, formatter.formatWarning(warning));
}

@Test
public void testFormatWarningWithRegionStartAfterLineEndsBeforeLine() {
String code = "function foo() {\n  if (x < 0) {\n    console.log('Warning description');\n  } else {\n    throw new Error('Error description');\n  }\n}";
Region region = new Region("sourceName.js", 2, 1, 1, code.length(), 0);

LightweightMessageFormatter formatter = new LightweightMessageFormatter(
new SourceExcerptProvider() {
@Override
public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
if (sourceName.equals("sourceName.js")) {
return new SourceExcerpt(code, lineNumber, 0, 0, code.length());
} else {
return null;
}
}
});

String warningDescription = "Warning description";
JSError warning = new JSError("sourceName.js", 1, 1, warningDescription);

String expected = "sourceName.js: 1: " + CheckLevel.WARNING + " - Warning description\n";

assertEquals(expected, formatter.formatWarning(warning));
}

@Test
public void testFormatWarningWithRegionStartAfterLineEndsAtEndOfFile() {
String code = "function foo() {\n  if (x < 0) {\n    console.log('Warning description');\n  } else {\n    throw new Error('Error description');\n  }\n}";
Region region = new Region("sourceName.js", 2, 1, code.length(), code.length(),
    0);

LightweightMessageFormatter formatter = new LightweightMessageFormatter(
new SourceExcerptProvider() {
@Override
public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
if (sourceName.equals("sourceName.js")) {
return new SourceExcerpt(code, lineNumber, 0, 0, code.length());
} else {
return null;
}
}
});

String warningDescription = "Warning description";
JSError warning = new JSError("sourceName.js", 1, 1, warningDescription);

String expected = "sourceName.js: 1: " + CheckLevel.WARNING + " - Warning description\nfunction foo() {\n  if (x < 0) {\n    console.log('Warning description');\n  } else {\n    throw new Error('Error description');\n  }\n}";

assertEquals(expected, formatter.formatWarning(warning));
}

@Test
public void testFormatErrorWithEmptySourceExcerpt() {
LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);

String errorDescription = "Description of the error";
JSError error = new JSError("sourceName", 1, 1, errorDescription);
String emptySource = "";

String expected = "sourceName: 1: " + CheckLevel.ERROR + " - Description of the error\n";

assertEquals(expected, formatter.formatError(error));
}

@Test
public void testFormatWarningWithEmptySourceExcerpt() {
LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);

String warningDescription = "Description of the warning";
JSError warning = new JSError("sourceName", 1, 1, warningDescription);
String emptySource = "";

String expected = "sourceName: 1: " + CheckLevel.WARNING + " - Description of the warning\n";

assertEquals(expected, formatter.formatWarning(warning));
}

@Test
public void testFormatErrorWithNullSourceExcerpt() {
LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);

String errorDescription = "Description of the error";
JSError error = new JSError("sourceName", 1, 1, errorDescription);
String nullSource = null;

String expected = "sourceName: 1: " + CheckLevel.ERROR + " - Description of the error\n";

assertEquals(expected, formatter.formatError(error));
}

@Test
public void testFormatWarningWithNullSourceExcerpt() {
LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);

String warningDescription = "Description of the warning";
JSError warning = new JSError("sourceName", 1, 1, warningDescription);
String nullSource = null;

String expected = "sourceName: 1: " + CheckLevel.WARNING + " - Description of the warning\n";

assertEquals(expected, formatter.formatWarning(warning));
}

@Test
public void testFormatErrorWithEmptySource() {
LightweightMessageFormatter formatter = new LightweightMessageFormatter(
new SourceExcerptProvider() {
@Override
public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
return null;
}
});

String errorDescription = "Description of the error";
JSError error = new JSError("sourceName", 1, 1, errorDescription);

String expected = "sourceName: 1: " + CheckLevel.ERROR + " - Description of the error\n";

assertEquals(expected, formatter.formatError(error));
}

@Test
public void testFormatWarningWithEmptySource() {
LightweightMessageFormatter formatter = new LightweightMessageFormatter(
new SourceExcerptProvider() {
@Override
public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
return null;
}
});

String warningDescription = "Description of the warning";
JSError warning = new JSError("sourceName", 1, 1, warningDescription);

String expected = "sourceName: 1: " + CheckLevel.WARNING + " - Description of the warning\n";

assertEquals(expected, formatter.formatWarning(warning));
}
}