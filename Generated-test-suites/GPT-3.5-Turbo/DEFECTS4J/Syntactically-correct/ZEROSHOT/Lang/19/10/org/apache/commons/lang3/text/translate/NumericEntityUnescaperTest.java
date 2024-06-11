import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test input with hexadecimal number
        StringBuilder input1 = new StringBuilder();
        input1.append("&#x41;");
        StringBuilder output1 = new StringBuilder();
        unescaper.translate(input1, 0, new Writer(){
            public void write(char[] cbuf, int off, int len) {
                output1.append(new String(cbuf, off, len));
            }
            public void flush() {}
            public void close() {}
        });
        assertEquals("A", output1.toString());

        // Test input with decimal number
        StringBuilder input2 = new StringBuilder();
        input2.append("&#65;");
        StringBuilder output2 = new StringBuilder();
        unescaper.translate(input2, 0, new Writer(){
            public void write(char[] cbuf, int off, int len) {
                output2.append(new String(cbuf, off, len));
            }
            public void flush() {}
            public void close() {}
        });
        assertEquals("A", output2.toString());

        // Test input with no semicolon
        StringBuilder input3 = new StringBuilder();
        input3.append("&#65");
        StringBuilder output3 = new StringBuilder();
        unescaper.translate(input3, 0, new Writer(){
            public void write(char[] cbuf, int off, int len) {
                output3.append(new String(cbuf, off, len));
            }
            public void flush() {}
            public void close() {}
        });
        assertEquals("A", output3.toString());

        // Test input with entity value > 0xFFFF
        StringBuilder input4 = new StringBuilder();
        input4.append("&#x1F496;");
        StringBuilder output4 = new StringBuilder();
        unescaper.translate(input4, 0, new Writer(){
            public void write(char[] cbuf, int off, int len) {
                output4.append(new String(cbuf, off, len));
            }
            public void flush() {}
            public void close() {}
        });
        assertEquals("💖", output4.toString());

        // Test input with NumberFormatException
        StringBuilder input5 = new StringBuilder();
        input5.append("&#A;");
        StringBuilder output5 = new StringBuilder();
        unescaper.translate(input5, 0, new Writer(){
            public void write(char[] cbuf, int off, int len) {
                output5.append(new String(cbuf, off, len));
            }
            public void flush() {}
            public void close() {}
        });
        assertTrue(output5.toString().isEmpty());
    }
}