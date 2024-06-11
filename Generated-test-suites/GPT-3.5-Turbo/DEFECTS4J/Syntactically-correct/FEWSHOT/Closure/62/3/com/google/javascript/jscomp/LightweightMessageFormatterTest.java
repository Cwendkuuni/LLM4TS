
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.google.javascript.jscomp.LightweightMessageFormatter.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;

public class LightweightMessageFormatterTest {

    @Test
    public void testConstructorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        assertEquals(null, formatter.getSource());
    }

    @Test
    public void testConstructorWithSource() {
        SourceExcerptProvider source = mock(SourceExcerptProvider.class);
        SourceExcerpt excerpt = mock(SourceExcerpt.class);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
        assertEquals(source, formatter.getSource());
        assertEquals(excerpt, formatter.excerpt);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("error");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mock(SourceExcerptProvider.class));
        assertEquals("ERROR - error\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("warning");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mock(SourceExcerptProvider.class));
        assertEquals("WARNING - warning\n", formatter.formatWarning(warning));
    }

    @Test
    public void testFormat() {
        JSError error = new JSError("error");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mock(SourceExcerptProvider.class));
        assertEquals("ERROR - error\n", formatter.format(error, false));
    }

    @Test
    public void testFormatWithSourceName() {
        JSError error = new JSError("error");
        error.sourceName = "source";
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mock(SourceExcerptProvider.class));
        assertEquals("source: ERROR - error\n", formatter.format(error, false));
    }

    @Test
    public void testFormatWithLineNumber() {
        JSError error = new JSError("error");
        error.sourceName = "source";
        error.lineNumber = 3;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mock(SourceExcerptProvider.class));
        assertEquals("source:3: ERROR - error\n", formatter.format(error, false));
    }

    @Test
    public void testFormatWithSourceExcerpt() {
        JSError error = new JSError("error");
        error.sourceName = "source";
        error.lineNumber = 3;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mock(SourceExcerptProvider.class), mock(SourceExcerpt.class));
        assertEquals("source:3: ERROR - error\n", formatter.format(error, false));
    }

    @Test
    public void testFormatLine() {
        ExcerptFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        assertEquals("  1| line", formatter.formatLine("line", 1));
    }

    @Test
    public void testFormatRegion() {
        ExcerptFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        assertEquals("  1| line\n  2| line2", formatter.formatRegion(new Region("line\nline2")));
    }

    @Test
    public void testFormatRegionNull() {
        ExcerptFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        assertEquals(null, formatter.formatRegion(null));
    }
}
