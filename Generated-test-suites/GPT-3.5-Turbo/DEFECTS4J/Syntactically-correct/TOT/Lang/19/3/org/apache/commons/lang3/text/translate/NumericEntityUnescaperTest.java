
import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#65;"); // Represents the character 'A'
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append(Character.toString((char)65)); // 'A'

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (Exception e) {
            Assert.fail("Unexpected exception occurred");
        }

        Assert.assertEquals("Translated output is incorrect", expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslate_ValidHexadecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#x41;"); // Represents the character 'A'
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append(Character.toString((char)65)); // 'A'

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (Exception e) {
            Assert.fail("Unexpected exception occurred");
        }

        Assert.assertEquals("Translated output is incorrect", expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslate_MissingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#65"); // Represents the character 'A'
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append(Character.toString((char)65)); // 'A'

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (Exception e) {
            Assert.fail("Unexpected exception occurred");
        }

        Assert.assertEquals("Translated output is incorrect", expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslate_InvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#invalid;"); // Invalid numeric entity
        StringBuilder expectedOutput = new StringBuilder();

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (Exception e) {
            return; // Expected exception occurred
        }

        Assert.fail("Expected exception was not thrown");
    }
}
