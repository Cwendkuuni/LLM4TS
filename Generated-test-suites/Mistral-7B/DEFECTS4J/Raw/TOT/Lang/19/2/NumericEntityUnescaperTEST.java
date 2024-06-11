import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    /**
     * Test translating simple entities
     */
    @Test
    public void testSimpleEntities() {
        String input = "&amp; &lt; &gt; &apos; &quot; &#32; &#33; &#34; &#35; &#36; &#37; &#38; &#39; &#40; &#41; &#42; &#43; &#44; &#45; &#46; &#47; &#128; &#x1f600;";
        StringWriter out = new StringWriter();

        int length = unescaper.translate(new StringReader(input), 0, out);
        String expectedOutput = "&amp; &lt; &gt; &#34; &#34; &#32; &#33; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot; &quot;";
        Assert.assertEquals(expectedOutput, out.toString().substring(0, length));
    }

    /**
     * Test translating entities with semi-colon missing
     */
    @Test
    public void testEntitiesWithoutSemiColon() {
        String input = "&amp;#x1F600";
        StringWriter out = new StringWriter();

        int length = unescaper.translate(new StringReader(input), 0, out);
        char expectedOutput = '\uD83C' + (char) 0xDF60;
        Assert.assertEquals(expectedOutput, out.toString().charAt(0));
    }

    /**
     * Test translating entities with negative values
     */
    @Test(expected = NumberFormatException.class)
    public void testInvalidEntities() {
        String input = "&amp;#-1;";
        StringWriter out = new StringWriter();
        unescaper.translate(new StringReader(input), 0, out);
    }

    /**
     * Test translating entities with values larger than UTF-16
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLargeEntities() {
        String input = "&amp;#65536;";
        StringWriter out = new StringWriter();
        unescaper.translate(new StringReader(input), 0, out);
    }

    /**
     * Utility class for creating a StringReader from a String
     */
    private static class StringReader extends CharSequence {
        private final String string;

        public StringReader(String string) {
            this.string = string;
        }

        @Override
        public int length() {
            return string.length();
        }

        @Override
        public char charAt(int index) {
            return string.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new StringSubSequence(string, start, end);
        }

        @Override
        public String toString() {
            return string;
        }
    }

    /**
     * Utility class for creating a CharSequence from a String
     */
    private static class StringSubSequence implements CharSequence {
        private final String string;
        private final int start;
        private final int end;

        public StringSubSequence(String string, int start, int end) {
            this.string = string;
            this.start = start;
            this.end = end;
        }

        @Override
        public int length() {
            return end - start;
        }

        @Override
        public char charAt(int index) {
            return string.charAt(start + index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new StringSubSequence(string, this.start + start, this.start + end);
        }

        @Override
        public String toString() {
            return string.substring(start, end);
        }
    }
}