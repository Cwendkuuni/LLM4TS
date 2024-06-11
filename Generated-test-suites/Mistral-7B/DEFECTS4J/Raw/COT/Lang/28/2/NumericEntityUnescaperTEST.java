package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * JUnit test cases for NumericEntityUnescaper class
 *
 * @author Johnson.D'Costa
 */
public class NumericEntityUnescaperTest {

    private static final NumericEntityUnescaper UNESCAPER = new NumericEntityUnescaper();

    @Test
    public void testTranslateSimpleNumericEntity() throws IOException {
        Writer writer = new Writer() {
            private ByteArrayOutputStream output = new ByteArrayOutputStream();

            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Do nothing
            }

            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };

        String testString = "&amp; &lt; &gt; &apos; &quot; &nbsp; &#10; &#x1F600;";
        int index = 0;

        int length = UNESCAPER.translate(new StringSource(testString), index, writer);
        String expected = "&amp; &lt; &gt; &apos; &quot; &nbsp; \u000A \uD83D\uDC00";
        String result = writer.output.toString("UTF-8");

        assertEquals(expected, result);
        assertEquals(15, length);
    }

    @Test
    public void testTranslateHexNumericEntity() throws IOException {
        Writer writer = new Writer() {
            private ByteArrayOutputStream output = new ByteArrayOutputStream();

            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Do nothing
            }

            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };

        String testString = "&amp; &lt; &gt; &apos; &quot; &nbsp; &#x3B; &#x412;";
        int index = 0;

        int length = UNESCAPER.translate(new StringSource(testString), index, writer);
        String expected = "&amp; &lt; &gt; &apos; &quot; &nbsp; ⅋ 𐐀";
        String result = writer.output.toString("UTF-8");

        assertEquals(expected, result);
        assertEquals(11, length);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidNumericEntity() throws IOException {
        Writer writer = new Writer() {
            private ByteArrayOutputStream output = new ByteArrayOutputStream();

            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Do nothing
            }

            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };

        String testString = "&amp; &lt; &gt; &apos; &quot; &nbsp; &#xX; &#x412;";
        int index = 0;

        UNESCAPER.translate(new StringSource(testString), index, writer);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateInvalidEntityStart() throws IOException {
        Writer writer = new Writer() {
            private ByteArrayOutputStream output = new ByteArrayOutputStream();

            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Do nothing
            }

            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };

        String testString = "&amp; &lt; &gt; &apos; &quot; &nbsp; &amp;";
        int index = 2; // Invalid index

        UNESCAPER.translate(new StringSource(testString), index, writer);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateInvalidEntityEnd() throws IOException {
        Writer writer = new Writer() {
            private ByteArrayOutputStream output = new ByteArrayOutputStream();

            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Do nothing
            }

            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };

        String testString = "&amp; &lt; &gt; &apos; &quot; &nbsp; &amp;#";
        int index = 10; // Invalid index

        UNESCAPER.translate(new StringSource(testString), index, writer);
    }

    @Test(expected = IOException.class)
    public void testTranslateNullInput() throws IOException {
        Writer writer = new Writer() {
            private ByteArrayOutputStream output = new ByteArrayOutputStream();

            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Do nothing
            }

            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };

        UNESCAPER.translate(null, 0, writer);
    }

    @Test(expected = IOException.class)
    public void testTranslateNullWriter() throws IOException {
        String testString = "&amp; &lt; &gt; &apos; &quot; &nbsp; &amp;#";
        int index = 0;

        UNESCAPER.translate(new StringSource(testString), index, null);
    }

}