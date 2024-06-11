
import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnsNull() {
        String input = null;
        String expectedOutput = null;

        String actualOutput = HTMLFilter.filter(input);

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFilter_EmptyInput_ReturnsEmpty() {
        String input = "";
        String expectedOutput = "";

        String actualOutput = HTMLFilter.filter(input);

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFilter_InputWithNoSpecialCharacters_ReturnsSameInput() {
        String input = "Hello World!";
        String expectedOutput = "Hello World!";

        String actualOutput = HTMLFilter.filter(input);

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFilter_InputWithLessThan_ReturnsFilteredOutput() {
        String input = "This is < a test";
        String expectedOutput = "This is &lt; a test";

        String actualOutput = HTMLFilter.filter(input);

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFilter_InputWithGreaterThan_ReturnsFilteredOutput() {
        String input = "This is > a test";
        String expectedOutput = "This is &gt; a test";

        String actualOutput = HTMLFilter.filter(input);

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFilter_InputWithAmpersand_ReturnsFilteredOutput() {
        String input = "This is & a test";
        String expectedOutput = "This is &amp; a test";

        String actualOutput = HTMLFilter.filter(input);

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFilter_InputWithDoubleQuote_ReturnsFilteredOutput() {
        String input = "This is \" a test";
        String expectedOutput = "This is &quot; a test";

        String actualOutput = HTMLFilter.filter(input);

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFilter_InputWithManySpecialCharacters_ReturnsFilteredOutput() {
        String input = "<Hello & World!> \" How are you? &";
        String expectedOutput = "&lt;Hello &amp; World!&gt; &quot; How are you? &amp;";

        String actualOutput = HTMLFilter.filter(input);

        Assert.assertEquals(expectedOutput, actualOutput);
    }
}

