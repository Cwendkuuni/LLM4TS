package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.rhino.SourceName;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public class LightweightMessageFormatterTest {

  private static final String SOURCE_CODE =
      "function foo() {" +
      "  if (bar) {" +
      "    alert('baz');" +
      "  }" +
      "}";

  private static final SourceName SOURCE_NAME = new SourceName("Test.js");

  @org.junit.Test
  public void testLightweightMessageFormatterWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = createError(1, "test", "expected error message");
    String result = formatter.formatError(error);

    assertEquals("ERROR - expected error message\n", result);
  }

  @org.junit.Test
  public void testLightweightMessageFormatterWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    when(source.getSourceExcerpt(SOURCE_NAME, 1, 5)).thenReturn("if (bar)");
    JSError error = createError(1, SOURCE_NAME, 5, "test error");
    String result = formatter.formatError(error);

    assertEquals("Test.js:5: ERROR - test error\nif (bar)\n      ^", result);
  }

  @org.junit.Test
  public void testLightweightMessageFormatterWithRegion() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LightweightMessageFormatter.ExcerptFormatter.LINE);

    Node node = createNode(Token.BLOCK, SOURCE_CODE);
    when(source.getSourceExcerpt(SOURCE_NAME, "Test.js", 1, SOURCE_CODE.length()))
        .thenReturn(SOURCE_CODE);
    when(source.getSourceExcerpt(SOURCE_NAME, node)).thenReturn("function foo() {\n" + SOURCE_CODE + "\n}");

    JSError error = createError(1, SOURCE_NAME, 5, "test error", new Region(new int[]{0, 4, 5}, new int[]{0, 12}));
    String result = formatter.formatError(error);

    assertEquals("Test.js:5: ERROR - test error\n  if (bar)\n    ^", result);
  }

  @org.junit.Test
  public void testLightweightMessageFormatterWithLongSourceExcerpt() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    when(source.getSourceExcerpt(SOURCE_NAME, 1, 5)).thenReturn("if (bar) {");
    JSError error = createError(1, SOURCE_NAME, 5, "test error");
    String result = formatter.formatError(error);

    assertEquals("Test.js:5: ERROR - test error\n  if (bar) {\n    ^", result);
  }

  @org.junit.Test
  public void testLightweightMessageFormatterWithWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(mock(SourceExcerptProvider.class));

    JSError warning = createWarning(1, "test warning message");
    String result = formatter.formatWarning(warning);

    assertEquals("WARNING - test warning message\n", result);
  }

  private JSError createError(int lineNumber, SourceName sourceName, int charno, String description) {
    return createError(lineNumber, sourceName, charno, description, new Region(new int[]{0}, new int[]{0}));
  }

  private JSError createError(int lineNumber, SourceName sourceName, int charno, String description, Region region) {
    return new JSError(JSError.Type.ERROR, lineNumber, charno, sourceName, description, region);
  }

  private JSError createWarning(int lineNumber, String description) {
    return new JSError(JSError.Type.WARNING, lineNumber, 0, null, description, null);
  }

  private Node createNode(int type, String code) {
    Node node = new Node();
    node.setType(type);
    node.setSource(SOURCE_NAME);
    node.setStart(0);
    node.setEnd(SOURCE_CODE.length());
    node.setCode(code);
    return node;
  }

  private static class Region {
    private final int[] lines;
    private final int[] chars;

    public Region(int[] lines, int[] chars) {
      this.lines = lines;
      this.chars = chars;
    }

    public int getBeginningLineNumber() {
      return lines[0];
    }

    public int getEndingLineNumber() {
      return lines[lines.length - 1];
    }
  }

}