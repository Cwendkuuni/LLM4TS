package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.CodeSnippetTestHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test cases for {@link LightweightMessageFormatter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LightweightMessageFormatterTest {

  private static final String SOURCE = "function foo() {" +
      "  alert('bar');" +
      "}";

  @Mock
  private SourceExcerptProvider sourceProvider;

  @InjectMocks
  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    when(sourceProvider.getSourceFile(Mockito.anyString())).thenReturn(new SourceFile(
        "TestSource.js", SOURCE));
  }

  @Test
  public void testFormatErrorNoSource() {
    JSError error = new JSError("TestError", CheckLevel.ERROR, "test error", null);
    assertEquals("test error\n", formatter.formatError(error));
  }

  @Test
  public void testFormatErrorHasSource() {
    JSError error = new JSError("TestError", CheckLevel.ERROR, "test error", "TestSource.js:1");
    SourceExcerpt excerpt = new SourceExcerpt("TestSource.js:1:1-10:1", SOURCE);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, excerpt);
    assertEquals("TestSource.js:1: test error\n1: function foo() {\n" +
        "  alert('bar');\n" +
        "}\n", formatter.formatError(error));
  }

  @Test
  public void testFormatErrorMissingSource() {
    JSError error = new JSError("TestError", CheckLevel.ERROR, "test error", "TestSource.js:1");
    SourceExcerpt excerpt = new SourceExcerpt("TestSource.js:1:1-10:1", "");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, excerpt);
    assertEquals("TestSource.js:1: test error\n", formatter.formatError(error));
  }

  @Test
  public void testFormatWarningNoSource() {
    JSError warning = new JSError("TestWarning", CheckLevel.WARNING, "test warning", null);
    assertEquals("test warning\n", formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningHasSource() {
    JSError warning = new JSError("TestWarning", CheckLevel.WARNING, "test warning", "TestSource.js:1");
    SourceExcerpt excerpt = new SourceExcerpt("TestSource.js:1:1-10:1", SOURCE);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, excerpt);
    assertEquals("TestSource.js:1: warning - test warning\n1: function foo() {\n" +
        "  alert('bar');\n" +
        "}\n", formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningMissingSource() {
    JSError warning = new JSError("TestWarning", CheckLevel.WARNING, "test warning", "TestSource.js:1");
    SourceExcerpt excerpt = new SourceExcerpt("TestSource.js:1:1-10:1", "");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, excerpt);
    assertEquals("TestSource.js:1: warning - test warning\n", formatter.formatWarning(warning));
  }
}