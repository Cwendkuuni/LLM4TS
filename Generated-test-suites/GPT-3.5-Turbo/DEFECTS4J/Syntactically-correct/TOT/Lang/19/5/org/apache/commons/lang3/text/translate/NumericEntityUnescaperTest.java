import org.junit.Test;
import org.junit.Assert;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidHexEntity_shouldTranslateToCorrespondingCodepoint() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int numCharsWritten = translator.translate("&#x20;", 0, writer);
            Assert.assertEquals(7, numCharsWritten); // 2 characters + 1 for hex indicator + 4 for entity (&#x20;)
            Assert.assertEquals(" ", writer.toString());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_withValidDecimalEntity_shouldTranslateToCorrespondingCodepoint() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int numCharsWritten = translator.translate("&#32;", 0, writer);
            Assert.assertEquals(7, numCharsWritten); // 2 characters + 5 for entity (&#32;)
            Assert.assertEquals(" ", writer.toString());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_withMissingSemicolon_shouldThrowIOException() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int numCharsWritten = translator.translate("&#32", 0, writer);
            Assert.fail("Expected IOException, but no exception was thrown");
        } catch (IOException e) {
            // Pass
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_withInvalidHexEntity_shouldThrowNumberFormatException() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int numCharsWritten = translator.translate("&#xG;", 0, writer);
            Assert.fail("Expected NumberFormatException, but no exception was thrown");
        } catch (NumberFormatException e) {
            // Pass
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_withInvalidDecimalEntity_shouldThrowNumberFormatException() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        try {
            int numCharsWritten = translator.translate("&#3A;", 0, writer);
            Assert.fail("Expected NumberFormatException, but no exception was thrown");
        } catch (NumberFormatException e) {
            // Pass
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_withInvalidEntityFormat_shouldNotTranslate() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        String input = "not an entity";

        try {
            int numCharsWritten = translator.translate(input, 0, writer);
            Assert.assertEquals(0, numCharsWritten);
            Assert.assertEquals("not an entity", writer.toString());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}