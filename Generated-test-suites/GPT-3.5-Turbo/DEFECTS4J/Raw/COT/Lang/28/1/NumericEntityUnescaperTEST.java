import org.junit.Test;
import org.junit.Assert;

public class NumericEntityUnescaperTest {

    @Test
    public void Test1TranslateMethod_BasicCase() {
        // Test the translate method with a basic case
        // where the input starts with '&#' and ends with a ';'.
        // Verify that the correct entity value is written to the output writer.

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();

        int result = unescaper.translate(input, 0, output);

        Assert.assertEquals(4, result);
        Assert.assertEquals("A", output.toString());
    }

    @Test
    public void Test2TranslateMethod_InputWithoutDecimalEntity() {
        // Test the translate method with an input that does not start with '&#'.
        // Verify that the method returns 0 and does not write anything to the output writer.

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder("ABC");
        StringBuilder output = new StringBuilder();

        int result = unescaper.translate(input, 0, output);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", output.toString());
    }

    @Test
    public void Test3TranslateMethod_InputWithoutSemicolon() {
        // Test the translate method with an input that does not end with ';'.
        // Verify that the method returns 0 and does not write anything to the output writer.

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();

        int result = unescaper.translate(input, 0, output);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", output.toString());
    }

    @Test
    public void Test4TranslateMethod_InvalidDecimalEntity() {
        // Test the translate method with an input that contains an invalid decimal entity value.
        // Verify that the method returns 0 and does not write anything to the output writer.

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder("&#XYZ;");
        StringBuilder output = new StringBuilder();

        int result = unescaper.translate(input, 0, output);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", output.toString());
    }

    @Test
    public void Test5TranslateMethod_InvalidHexadecimalEntity() {
        // Test the translate method with an input that contains invalid hexadecimal characters in the entity value.
        // Verify that the method returns 0 and does not write anything to the output writer.

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder("&#X1;");
        StringBuilder output = new StringBuilder();

        int result = unescaper.translate(input, 0, output);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", output.toString());
    }

    @Test
    public void Test6TranslateMethod_NumberFormatException() {
        // Test the translate method with an input that contains a valid entity value,
        // but causes a NumberFormatException when parsing it.
        // Verify that the method returns 0 and does not write anything to the output writer.

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringBuilder input = new StringBuilder("&#1234567890;");
        StringBuilder output = new StringBuilder();

        int result = unescaper.translate(input, 0, output);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", output.toString());
    }

}