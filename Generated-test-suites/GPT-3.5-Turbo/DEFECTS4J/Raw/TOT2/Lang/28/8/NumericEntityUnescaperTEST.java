import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_NormalDecimal() {
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#65;", 0, output);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown during normal decimal translation");
        }
    }

    @Test
    public void testTranslate_NormalHex() {
        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate("&#x41;", 0, output);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown during normal hex translation");
        }
    }

    @Test
    public void testTranslate_NoNumericEntity() {
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&A;", 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown when no numeric entity is present");
        }
    }

    @Test
    public void testTranslate_InvalidNumericEntity() {
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#xERROR;", 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown when invalid numeric entity is present");
        }
    }

    @Test
    public void testTranslate_OutOfBounds() {
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("&#1234567890;", 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown when entity value is out of bounds");
        }
    }

    @Test
    public void testTranslate_EmptyInput() {
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate("", 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown when input is empty");
        }
    }
}