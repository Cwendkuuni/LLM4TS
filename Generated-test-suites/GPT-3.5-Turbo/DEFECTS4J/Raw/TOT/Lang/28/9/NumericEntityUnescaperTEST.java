import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_typicalInput_decimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &#65; string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 7", 7, result);
        assertEquals("Expected output to be 'This is a test A string'", "This is a test A string", out.toString());
    }

    @Test
    public void testTranslate_typicalInput_hexadecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &#x41; string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 9", 9, result);
        assertEquals("Expected output to be 'This is a test A string'", "This is a test A string", out.toString());
    }

    @Test
    public void testTranslate_edgeInput_upperBoundDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &#65535; string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 10", 10, result);
        assertEquals("Expected output to be 'This is a test ? string'", "This is a test ? string", out.toString());
    }

    @Test
    public void testTranslate_edgeInput_upperBoundHexadecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &#xFFFF; string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 11", 11, result);
        assertEquals("Expected output to be 'This is a test ? string'", "This is a test ? string", out.toString());
    }

    @Test
    public void testTranslate_edgeInput_lowerBoundDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &#0; string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 9", 9, result);
        assertEquals("Expected output to be 'This is a test  string'", "This is a test  string", out.toString());
    }

    @Test
    public void testTranslate_edgeInput_lowerBoundHexadecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &#x0; string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 10", 10, result);
        assertEquals("Expected output to be 'This is a test  string'", "This is a test  string", out.toString());
    }

    @Test
    public void testTranslate_edgeInput_noEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 0", 0, result);
        assertEquals("Expected output to be unchanged", "This is a test string", out.toString());
    }
  
    @Test
    public void testTranslate_errorInput_invalidDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &#abc; string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 0", 0, result);
        assertEquals("Expected output to be unchanged", "This is a test &#abc; string", out.toString());
    }

    @Test
    public void testTranslate_errorInput_invalidHexadecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &#xzz; string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 0", 0, result);
        assertEquals("Expected output to be unchanged", "This is a test &#xzz; string", out.toString());
    }

    @Test
    public void testTranslate_errorInput_missingSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("This is a test &#65 string");

        StringWriter out = new StringWriter();
        int result = unescaper.translate(input, 13, out);
        
        assertEquals("Expected translation to return 0", 0, result);
        assertEquals("Expected output to be unchanged", "This is a test &#65 string", out.toString());
    }

}