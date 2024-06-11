import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_ValidNumericEntity_ReturnsTranslatedCodepoint() {
        StringBuilder input = new StringBuilder();
        input.append("This is a test &#65; string");

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a test A string");

        StringBuilder actualOutput = new StringBuilder();
        numericEntityUnescaper.translate(input, 12, actualOutput);

        Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslate_InvalidNumericEntity_ReturnsZero() {
        StringBuilder input = new StringBuilder();
        input.append("This is a test &#invalidNumericEntity; string");

        StringBuilder actualOutput = new StringBuilder();
        int actualReturnVal = numericEntityUnescaper.translate(input, 12, actualOutput);

        Assert.assertEquals(0, actualReturnVal);
    }

    @Test
    public void testTranslate_NumericEntityWithHex_ReturnsTranslatedCodepoint() {
        StringBuilder input = new StringBuilder();
        input.append("This is a test &#x41; string");

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a test A string");

        StringBuilder actualOutput = new StringBuilder();
        numericEntityUnescaper.translate(input, 12, actualOutput);

        Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslate_NoNumericEntity_ReturnsZero() {
        StringBuilder input = new StringBuilder();
        input.append("This is a test string");

        StringBuilder actualOutput = new StringBuilder();
        int actualReturnVal = numericEntityUnescaper.translate(input, 12, actualOutput);

        Assert.assertEquals(0, actualReturnVal);
    }

    @Test
    public void testTranslate_EmptyInput_ReturnsZero() {
        StringBuilder input = new StringBuilder();

        StringBuilder actualOutput = new StringBuilder();
        int actualReturnVal = numericEntityUnescaper.translate(input, 0, actualOutput);

        Assert.assertEquals(0, actualReturnVal);
    }

    @Test
    public void testTranslate_NullInput_ReturnsZero() {
        StringBuilder actualOutput = new StringBuilder();
        int actualReturnVal = numericEntityUnescaper.translate(null, 0, actualOutput);

        Assert.assertEquals(0, actualReturnVal);
    }
}