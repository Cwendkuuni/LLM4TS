
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

    private SourceExcerptProvider sourceExcerptProvider;
    private SourceExcerpt sourceExcerpt;
    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        sourceExcerptProvider = mock(SourceExcerptProvider.class);
        sourceExcerpt = mock(SourceExcerpt.class);
        formatter = new LightweightMessageFormatter(sourceExcerptProvider, sourceExcerpt);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("error description", "source name", 1, 0, CheckLevel.ERROR);
        when(sourceExcerptProvider.getSourceExcerpt(sourceExcerpt, "source name", 1, new LightweightMessageFormatter.LineNumberingFormatter())).thenReturn("source excerpt");
        String expected = "source name:1: ERROR - error description\nsource excerpt\n";

        String result = formatter.formatError(error);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("warning description", "source name", 1, 0, CheckLevel.WARNING);
        when(sourceExcerptProvider.getSourceExcerpt(sourceExcerpt, "source name", 1, new LightweightMessageFormatter.LineNumberingFormatter())).thenReturn("source excerpt");
        String expected = "source name:1: WARNING - warning description\nsource excerpt\n";

        String result = formatter.formatWarning(warning);

        assertEquals(expected, result);
    }
}
