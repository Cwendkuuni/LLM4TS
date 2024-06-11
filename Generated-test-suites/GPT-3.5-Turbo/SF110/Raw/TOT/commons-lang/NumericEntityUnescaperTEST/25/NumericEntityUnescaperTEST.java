import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_validDecimalNumberWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder();
        input.append("&#65;");
        Writer output = new StringWriter();

        try {
            int result = unescaper.translate(input, 0, output);
            assertEquals(5, result);
            assertEquals("A", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_validDecimalNumberWithoutSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        StringBuilder input = new StringBuilder();
        input.append("&#65");
        Writer output = new StringWriter();

        try {
            int result = unescaper.translate(input, 0, output);
            assertEquals(5, result);
            assertEquals("A", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_validDecimalNumberWithSemicolonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringBuilder input = new StringBuilder();
        input.append("&#65");
        Writer output = new StringWriter();

        try {
            int result = unescaper.translate(input, 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_validDecimalNumberWithErrorIfNoSemicolonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        StringBuilder input = new StringBuilder();
        input.append("&#65");
        Writer output = new StringWriter();

        try {
            unescaper.translate(input, 0, output);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_validHexNumberWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder();
        input.append("&#x41;");
        Writer output = new StringWriter();

        try {
            int result = unescaper.translate(input, 0, output);
            assertEquals(7, result);
            assertEquals("A", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_validHexNumberWithoutSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        StringBuilder input = new StringBuilder();
        input.append("&#x41");
        Writer output = new StringWriter();

        try {
            int result = unescaper.translate(input, 0, output);
            assertEquals(7, result);
            assertEquals("A", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_validHexNumberWithSemicolonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringBuilder input = new StringBuilder();
        input.append("&#x41");
        Writer output = new StringWriter();

        try {
            int result = unescaper.translate(input, 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_validHexNumberWithErrorIfNoSemicolonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        StringBuilder input = new StringBuilder();
        input.append("&#x41");
        Writer output = new StringWriter();

        try {
            unescaper.translate(input, 0, output);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_invalidNumber() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder();
        input.append("&#qw;");

        Writer output = new StringWriter();

        try {
            int result = unescaper.translate(input, 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTranslate_numberExceedsLimit() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder();
        input.append("&#100000;");

        Writer output = new StringWriter();

        try {
            int result = unescaper.translate(input, 0, output);
            assertEquals(5, result);
            // Since the entity value exceeds 65535, it should be converted to a surrogate pair
            assertEquals("\uD800\uDC00", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
}