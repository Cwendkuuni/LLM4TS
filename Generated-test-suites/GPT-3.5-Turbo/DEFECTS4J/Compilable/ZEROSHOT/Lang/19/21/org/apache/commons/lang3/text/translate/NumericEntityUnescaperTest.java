package org.apache.commons.lang3.text.translate;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        Writer writer = new StringBuilderWriter(sb);

        // Test when input is empty
        CharSequence input1 = "";
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, writer);
        Assert.assertEquals(0, result1);
        Assert.assertEquals("", sb.toString());

        // Test when input is "&#x20;"
        CharSequence input2 = "&#x20;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, writer);
        Assert.assertEquals(7, result2);
        Assert.assertEquals(" ", sb.toString());

        // Test when input is "&#169;"
        CharSequence input3 = "&#169;";
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, writer);
        Assert.assertEquals(6, result3);
        Assert.assertEquals("©", sb.toString());

        // Test when input is "&#65;"
        CharSequence input4 = "&#65;";
        int index4 = 0;
        int result4 = unescaper.translate(input4, index4, writer);
        Assert.assertEquals(5, result4);
        Assert.assertEquals("A", sb.toString());

        // Test when input is "&#x1F60A;"
        CharSequence input5 = "&#x1F60A;";
        int index5 = 0;
        int result5 = unescaper.translate(input5, index5, writer);
        Assert.assertEquals(10, result5);
        Assert.assertEquals("😊", sb.toString());

        // Test when input is "&#128514;"
        CharSequence input6 = "&#128514;";
        int index6 = 0;
        int result6 = unescaper.translate(input6, index6, writer);
        Assert.assertEquals(9, result6);
        Assert.assertEquals("😂", sb.toString());
    }

    private class StringBuilderWriter extends Writer {
        private final StringBuilder builder;

        public StringBuilderWriter(StringBuilder builder) {
            this.builder = builder;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            builder.append(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }
}