import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

    @Test
    public void testTranslate_ValidNumericEntity_Success() {
        StringBuilder input = new StringBuilder();
        input.append("This is a valid numeric entity: &#65;");
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a valid numeric entity: A");
        StringBuilder actualOutput = new StringBuilder();
        try {
            unescaper.translate(input, 21, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred: " + e);
        }
        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslate_InvalidNumericEntity_NoSemiColon() {
        StringBuilder input = new StringBuilder();
        input.append("This is an invalid numeric entity: &#65");
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is an invalid numeric entity: ");
        StringBuilder actualOutput = new StringBuilder();
        try {
            unescaper.translate(input, 25, actualOutput);
            fail("No exception occurred for invalid numeric entity ending without semi-colon");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        } catch (Exception e) {
            fail("Exception occurred: " + e);
        }
        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslate_InvalidNumericEntity_NotANumber() {
        StringBuilder input = new StringBuilder();
        input.append("This is an invalid numeric entity: &#XYZ;");
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is an invalid numeric entity: ");
        StringBuilder actualOutput = new StringBuilder();
        try {
            unescaper.translate(input, 25, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred: " + e);
        }
        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslate_HexadecimalNumericEntity_Success() {
        StringBuilder input = new StringBuilder();
        input.append("This is a hexadecimal numeric entity: &#x41;");
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a hexadecimal numeric entity: A");
        StringBuilder actualOutput = new StringBuilder();
        try {
            unescaper.translate(input, 29, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred: " + e);
        }
        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslate_EntityValueGreaterThan65535_Success() {
        StringBuilder input = new StringBuilder();
        input.append("This entity value is greater than 65535: &#65536;");
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This entity value is greater than 65535: ");
        StringBuilder actualOutput = new StringBuilder();
        try {
            unescaper.translate(input, 34, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred: " + e);
        }
        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

}