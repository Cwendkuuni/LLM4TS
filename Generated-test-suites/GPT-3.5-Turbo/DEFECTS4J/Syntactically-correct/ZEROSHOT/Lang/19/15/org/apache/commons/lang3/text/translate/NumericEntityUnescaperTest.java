import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();

        // Testing numeric entity with decimal value
        String input1 = "&#65;";
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, new java.io.StringWriter());
        Assert.assertEquals(5, result1);

        // Testing numeric entity with hexadecimal value
        String input2 = "&#x41;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, new java.io.StringWriter());
        Assert.assertEquals(6, result2);

        // Testing empty input
        String input3 = "";
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, new java.io.StringWriter());
        Assert.assertEquals(0, result3);
    }

}