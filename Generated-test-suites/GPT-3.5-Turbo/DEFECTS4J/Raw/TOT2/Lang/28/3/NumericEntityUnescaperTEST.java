import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_WithValidDecimalEntity_ReturnsEntityValue() {
        StringBuilder sb = new StringBuilder();
        try {
            numericEntityUnescaper.translate("&#65;", 0, sb);
            assertEquals("A", sb.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_WithValidHexEntity_ReturnsEntityValue() {
        StringBuilder sb = new StringBuilder();
        try {
            numericEntityUnescaper.translate("&#x41;", 0, sb);
            assertEquals("A", sb.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_WithInvalidEntity_ReturnsZero() {
        StringBuilder sb = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate("&#xINVALID;", 0, sb);
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}