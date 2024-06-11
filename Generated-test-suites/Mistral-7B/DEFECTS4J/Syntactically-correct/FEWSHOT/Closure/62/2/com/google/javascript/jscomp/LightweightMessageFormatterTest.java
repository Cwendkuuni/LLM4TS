package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.LINE_NUMBER_FORMATTER;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Region;
import com.google.javascript.jscomp.util.Strings;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class LightweightMessageFormatterTest {
  private static final String SOURCE = "class Foo {\n" +
          "  constructor() {\n" +
          "    this.x = 1;\n" +
          "  }\n" +
          "  // error: test\n" +
          "  foo() {\n" +
          "    this.bar = 2;\n" +
          "  }\n" +
          "}";

  @Test
  public void testWithoutSource() {
    JSError error = new JSError("test", CheckLevel.ERROR, 0, "Error message");
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String message = formatter.formatError(error);
    assertEquals("ERROR - Error message\n", message);
  }

  @Test
  public void testWithSource() {
    JSError error = new JSError("Foo.js", 4, "test", "Error message");
    SourceExcerptProvider mockSource = mock(SourceExcerptProvider.class);
    when(mockSource.getExcerpt(eq("Foo.js"), anyInt(), anyInt(), LINE_NUMBER_FORMATTER))
        .thenReturn("  constructor() {\n" +
                "    this.x = 1;\n" +
                "  }\n" +
                "  foo() {\n" +
                "    this.bar = 2;\n" +
                "  }\n");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource);
    String message = formatter.formatError(error);
    assertEquals("Foo.js:4: ERROR - Error message\n" +
            "      constructor() {\n" +
            "          this.x = 1;\n" +
            "      }\n" +
            "      foo() {\n" +
            "          this.bar = 2;\n" +
            "      }\n" +
            "\n", message);
  }

  @Test
  public void testWarningMessage() {
    JSError warning = new JSError("Foo.js", 4, "test", "Warning message", CheckLevel.WARNING);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    String message = formatter.formatWarning(warning);
    assertEquals("WARNING - Warning message\n", message);
  }

  @Test
  public void testRegionFormatting() {
    ExcerptFormatter lineNumberingFormatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String source = "class Foo {\n" +
            "  constructor() {\n" +
            "    this.x = 1;\n" +
            "  }\n" +
            "  foo() {\n" +
            "    this.bar = 2;\n" +
            "  }\n" +
            "}";

    Region region = new Region(0, 5, SOURCE.length(), SOURCE);
    String formattedRegion = lineNumberingFormatter.formatRegion(region);
    assertEquals(" 1| class Foo {\n" +
            "      constructor() {\n" +
            "          this.x = 1;\n" +
            "      }\n" +
            "      2| foo() {\n" +
            "          this.bar = 2;\n" +
            "      }\n" +
            "  }\n", formattedRegion);
  }

  @Test
  public void testErrorMessageWithSourceExcerpt() {
    JSError error = new JSError("Foo.js", 4, "test", "Error message", CheckLevel.ERROR);
    SourceExcerptProvider mockSource = mock(SourceExcerptProvider.class);
    when(mockSource.getExcerpt(eq("Foo.js"), anyInt(), anyInt(), LINE_NUMBER_FORMATTER))
        .thenReturn("  constructor() {\n" +
                "    this.x = 1;\n" +
                "  }\n" +
                "  foo() {\n" +
                "    this.bar = 2;\n" +
                "  }\n");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource);
    String message = formatter.formatError(error);
    assertEquals("Foo.js:4: ERROR - Error message\n" +
            "      constructor() {\n" +
            "          this.x = 1;\n" +
            "      }\n" +
            "      foo() {\n" +
            "          this.bar = 2;\n" +
            "      }\n" +
            "\n", message);

    String charPositionMessage = formatter.formatError(new JSError("Foo.js", 4, "test", "Error message", CheckLevel.ERROR, 10));
    assertTrue(charPositionMessage.endsWith("  ^"));
  }
}