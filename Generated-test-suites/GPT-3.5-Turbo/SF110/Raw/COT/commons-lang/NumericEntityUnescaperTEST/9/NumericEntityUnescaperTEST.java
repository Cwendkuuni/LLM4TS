import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test input with valid numeric entity
        String input1 = "&amp;";
        StringWriter out1 = new StringWriter();
        unescaper.translate(input1, 0, out1);
        assertEquals("&", out1.toString());

        // Test input with invalid numeric entity
        String input2 = "&abc;";
        StringWriter out2 = new StringWriter();
        unescaper.translate(input2, 0, out2);
        assertEquals("", out2.toString());

        // Test input with hexadecimal numeric entity
        String input3 = "&#x41;";
        StringWriter out3 = new StringWriter();
        unescaper.translate(input3, 0, out3);
        assertEquals("A", out3.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateSemicolonRequiredError() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        // Test input without semicolon
        String input = "&#65";
        StringWriter out = new StringWriter();
        unescaper.translate(input, 0, out);
    }

    @Test
    public void testTranslateSemicolonRequiredSuccess() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        // Test input with semicolon
        String input1 = "&#65;";
        StringWriter out1 = new StringWriter();
        unescaper.translate(input1, 0, out1);
        assertEquals("A", out1.toString());

        // Test input without semicolon when semicolon is not required
        String input2 = "&#65";
        StringWriter out2 = new StringWriter();
        unescaper.translate(input2, 0, out2);
        assertEquals("", out2.toString());
    }

    @Test
    public void testTranslateSemicolonOptional() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        // Test input with semicolon
        String input1 = "&#65;";
        StringWriter out1 = new StringWriter();
        unescaper.translate(input1, 0, out1);
        assertEquals("A", out1.toString());

        // Test input without semicolon
        String input2 = "&#65";
        StringWriter out2 = new StringWriter();
        unescaper.translate(input2, 0, out2);
        assertEquals("A", out2.toString());
    }

    @Test
    public void testTranslateErrorIfNoSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        // Test input with semicolon
        String input1 = "&#65;";
        StringWriter out1 = new StringWriter();
        unescaper.translate(input1, 0, out1);
        assertEquals("A", out1.toString());

        // Test input without semicolon
        String input2 = "&#65";
        StringWriter out2 = new StringWriter();
        unescaper.translate(input2, 0, out2);
        assertEquals("", out2.toString());
    }
}