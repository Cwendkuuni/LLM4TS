
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper escaper = new NumericEntityUnescaper();
        assertFalse(escaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test
    public void testTranslate() {
        NumericEntityUnescaper escaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&amp;");
        StringBuilder output = new StringBuilder();
        try {
            escaper.translate(input, 0, new StringWriter(output));
            assertEquals("&", output.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testTranslateWithSemiColonRequired() {
        NumericEntityUnescaper escaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder("&amp");
        StringBuilder output = new StringBuilder();
        try {
            escaper.translate(input, 0, new StringWriter(output));
            fail("Expected IllegalArgumentException");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
    }
    
    @Test
    public void testTranslateWithSemiColonRequiredAndNoSemiColon() {
        NumericEntityUnescaper escaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        try {
            escaper.translate(input, 0, new StringWriter(output));
            fail("Expected IllegalArgumentException");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
    }
    
    @Test
    public void testTranslateWithSemiColonOptional() {
        NumericEntityUnescaper escaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        try {
            escaper.translate(input, 0, new StringWriter(output));
            assertEquals("A", output.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testTranslateWithSemiColonOptionalAndNoSemiColon() {
        NumericEntityUnescaper escaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        try {
            escaper.translate(input, 0, new StringWriter(output));
            assertEquals("A", output.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testTranslateWithInvalidNumericEntity() {
        NumericEntityUnescaper escaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#XYZ;");
        StringBuilder output = new StringBuilder();
        try {
            int translated = escaper.translate(input, 0, new StringWriter(output));
            assertEquals(0, translated);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testTranslateWithInvalidHexNumericEntity() {
        NumericEntityUnescaper escaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#xFG;");
        StringBuilder output = new StringBuilder();
        try {
            int translated = escaper.translate(input, 0, new StringWriter(output));
            assertEquals(0, translated);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testTranslateWithHighValueEntity() {
        NumericEntityUnescaper escaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#100000;");
        StringBuilder output = new StringBuilder();
        try {
            escaper.translate(input, 0, new StringWriter(output));
            assertEquals("𐄂", output.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}
