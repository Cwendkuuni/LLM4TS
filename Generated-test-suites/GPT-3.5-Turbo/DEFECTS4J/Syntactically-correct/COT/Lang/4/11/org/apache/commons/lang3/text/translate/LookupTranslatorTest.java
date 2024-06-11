
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{
            {"123", "abc"},
            {"456", "def"}
        });
        
        // Basic test case
        String input1 = "123456";
        StringWriter output1 = new StringWriter();
        assertEquals(6, translator.translate(input1, 0, output1));
        assertEquals("abcdef", output1.toString());
        
        // Edge case: Empty input
        String input2 = "";
        StringWriter output2 = new StringWriter();
        assertEquals(0, translator.translate(input2, 0, output2));
        assertEquals("", output2.toString());
        
        // Edge case: Empty lookup table
        LookupTranslator translator2 = new LookupTranslator();
        String input3 = "123456";
        StringWriter output3 = new StringWriter();
        assertEquals(0, translator2.translate(input3, 0, output3));
        assertEquals("", output3.toString());
        
        // Exception handling: IOException
        LookupTranslator translator3 = new LookupTranslator(new CharSequence[][]{{"123", "abc"}});
        String input4 = "123456";
        Writer writer4 = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }

            @Override
            public void flush() throws IOException { }

            @Override
            public void close() throws IOException { }
        };
        try {
            translator3.translate(input4, 0, writer4);
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("Test IOException", e.getMessage());
        }
    }
}
