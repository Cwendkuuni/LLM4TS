
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;

public class LightweightMessageFormatterTest {

    @Mock
    SourceExcerptProvider mockSource;

    LightweightMessageFormatter formatterWithSource;
    LightweightMessageFormatter formatterWithoutSource;

    @Before
    public void setUp() {
        mockSource = mock(SourceExcerptProvider.class);
        formatterWithSource = new LightweightMessageFormatter(mockSource);
        formatterWithoutSource = LightweightMessageFormatter.withoutSource();
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("test error");
        error.sourceName = "test.js";
        error.lineNumber = 10;

        String expected = "test.js:10: ERROR - test error\n";
        assertEquals(expected, formatterWithSource.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("test warning");
        warning.sourceName = "test.js";
        warning.lineNumber = 15;

        String expected = "test.js:15: WARNING - test warning\n";
        assertEquals(expected, formatterWithSource.formatWarning(warning));
    }

    @Test
    public void testFormatWithoutSource() {
        JSError error = new JSError("test error");
        error.sourceName = "test.js";
        error.lineNumber = 10;

        String expected = "ERROR - test error\n";
        assertEquals(expected, formatterWithoutSource.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError("test warning");
        warning.sourceName = "test.js";
        warning.lineNumber = 15;

        String expected = "WARNING - test warning\n";
        assertEquals(expected, formatterWithoutSource.formatWarning(warning));
    }
}
