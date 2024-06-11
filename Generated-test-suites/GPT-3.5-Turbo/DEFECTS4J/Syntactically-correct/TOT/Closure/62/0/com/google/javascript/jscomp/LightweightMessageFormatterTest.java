
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;
    private JSError error;
    private JSError warning;

    @Before
    public void setUp() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        formatter = new LightweightMessageFormatter(source);

        error = new JSError("error message", "error source", 10, CheckLevel.ERROR);
        warning = new JSError("warning message", "warning source", 20, CheckLevel.WARNING);
    }

    @Test
    public void testFormatError() {
        String expectedResult = "error source:10: ERROR - error message\n";

        String actualResult = formatter.formatError(error);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarning() {
        String expectedResult = "warning source:20: WARNING - warning message\n";

        String actualResult = formatter.formatWarning(warning);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();

        String expectedResult = "ERROR - error message\n";

        String actualResult = formatterWithoutSource.formatError(error);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();

        String expectedResult = "WARNING - warning message\n";

        String actualResult = formatterWithoutSource.formatWarning(warning);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatErrorWithSourceExcerpt() {
        String sourceExcerpt = "  9| if (foo) {\n" +
                               " 10|   alert('bar');\n" +
                               " 11| }";

        SourceExcerptProvider source = new TestSourceExcerptProvider(sourceExcerpt);
        formatter = new LightweightMessageFormatter(source);

        String expectedResult = "error source:10: ERROR - error message\n" +
                                "  9| if (foo) {\n" +
                                " 10|   alert('bar');\n" +
                                "       ^\n";

        String actualResult = formatter.formatError(error);

        assertEquals(expectedResult, actualResult);
    }

    class TestSourceExcerptProvider implements SourceExcerptProvider {

        private String sourceExcerpt;

        public TestSourceExcerptProvider(String sourceExcerpt) {
            this.sourceExcerpt = sourceExcerpt;
        }

        @Override
        public SourceExcerpt getSourceExcerpt(String sourceName) {
            return new SourceExcerpt() {

                @Override
                public String get(SourceExcerptProvider sourceExcerpts, String sourceName, int lineNumber,
                        ExcerptFormatter formatter) {
                    return sourceExcerpt;
                }
            };
        }
    }
}
