import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper numericEntityUnescaper;
    
    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }
    
    @Test
    public void testTranslate_basicNumericEntity() throws IOException {
        StringBuilder input = new StringBuilder();
        input.append("&#65;");
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append('A');
        Writer out = new StringWriter();
        int numCharsWritten = numericEntityUnescaper.translate(input, 0, out);
        assertEquals("Translated characters written", expectedOutput.length(), numCharsWritten);
        assertEquals("Translated output", expectedOutput.toString(), out.toString());
    }
    
    @Test
    public void testTranslate_hexadecimalNumericEntity() throws IOException {
        StringBuilder input = new StringBuilder();
        input.append("&#x41;");
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append('A');
        Writer out = new StringWriter();
        int numCharsWritten = numericEntityUnescaper.translate(input, 0, out);
        assertEquals("Translated characters written", expectedOutput.length(), numCharsWritten);
        assertEquals("Translated output", expectedOutput.toString(), out.toString());
    }
    
    @Test
    public void testTranslate_invalidNumericEntity() throws IOException {
        StringBuilder input = new StringBuilder();
        input.append("&#invalid;");
        StringBuilder expectedOutput = new StringBuilder();
        Writer out = new StringWriter();
        int numCharsWritten = numericEntityUnescaper.translate(input, 0, out);
        assertEquals("Translated characters written", expectedOutput.length(), numCharsWritten);
        assertEquals("Translated output", expectedOutput.toString(), out.toString());
    }
    
}