import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer writer = new MockWriter();
        
        // Test input with hexadecimal entity
        CharSequence input1 = "&#x41;";
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, writer);
        assertEquals(5, result1);
        assertEquals("A", writer.toString());

        // Test input with decimal entity
        CharSequence input2 = "&#65;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, writer);
        assertEquals(5, result2);
        assertEquals("A", writer.toString());

        // Test input without semi-colon at the end
        CharSequence input3 = "&#65";
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, writer);
        assertEquals(5, result3);
        assertEquals("A", writer.toString());

        // Test input without 'x' after &# for hexadecimal entity
        CharSequence input4 = "&#65;";
        int index4 = 0;
        int result4 = unescaper.translate(input4, index4, writer);
        assertEquals(5, result4);
        assertEquals("A", writer.toString());

        // Test input with entity value greater than 0xFFFF
        CharSequence input5 = "&#128513;";
        int index5 = 0;
        int result5 = unescaper.translate(input5, index5, writer);
        assertEquals(9, result5);
        assertEquals(String.valueOf(Character.toChars(128513)), writer.toString());
    }

    private class MockWriter extends Writer {
        private StringBuilder sb = new StringBuilder();

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            sb.append(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}