import org.junit.Test;
import org.junit.Assert;

public class NumericEntityUnescaperTest {

    // Test case for translate method
    @Test
    public void testTranslate() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        // Typical use case
        StringBuilder input1 = new StringBuilder("This is a test string with numeric entities &#65; and &#x41;");
        StringBuilder expectedOutput1 = new StringBuilder("This is a test string with numeric entities A and A");
        StringBuilder actualOutput1 = new StringBuilder();
        int translationLength1 = translator.translate(input1, 0, actualOutput1);
        Assert.assertEquals("Translation length is incorrect", 15, translationLength1);
        Assert.assertEquals("Translated output is incorrect", expectedOutput1.toString(), actualOutput1.toString());

        // Edge case - Empty input
        StringBuilder input2 = new StringBuilder("");
        StringBuilder expectedOutput2 = new StringBuilder("");
        StringBuilder actualOutput2 = new StringBuilder();
        int translationLength2 = translator.translate(input2, 0, actualOutput2);
        Assert.assertEquals("Translation length is incorrect", 0, translationLength2);
        Assert.assertEquals("Translated output is incorrect", expectedOutput2.toString(), actualOutput2.toString());

        // Edge case - Input with no numeric entities
        StringBuilder input3 = new StringBuilder("This input contains no numeric entities");
        StringBuilder expectedOutput3 = new StringBuilder("This input contains no numeric entities");
        StringBuilder actualOutput3 = new StringBuilder();
        int translationLength3 = translator.translate(input3, 0, actualOutput3);
        Assert.assertEquals("Translation length is incorrect", 0, translationLength3);
        Assert.assertEquals("Translated output is incorrect", expectedOutput3.toString(), actualOutput3.toString());

        // Error scenario - Invalid numeric entity
        StringBuilder input4 = new StringBuilder("This input contains an invalid numeric entity &#xg;");
        StringBuilder expectedOutput4 = new StringBuilder("This input contains an invalid numeric entity &#xg;");
        StringBuilder actualOutput4 = new StringBuilder();
        int translationLength4 = translator.translate(input4, 0, actualOutput4);
        Assert.assertEquals("Translation length is incorrect", 0, translationLength4);
        Assert.assertEquals("Translated output is incorrect", expectedOutput4.toString(), actualOutput4.toString());
    }
}