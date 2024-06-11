
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        // Test basic numeric entity
        String input1 = "&#65;";
        int result1 = unescaper.translate(input1, 0, writer);
        String output1 = writer.toString();
        Assert.assertEquals(5, result1);
        Assert.assertEquals("A", output1);

        writer.getBuffer().setLength(0);

        // Test hexadecimal numeric entity
        String input2 = "&#x41;";
        int result2 = unescaper.translate(input2, 0, writer);
        String output2 = writer.toString();
        Assert.assertEquals(5, result2);
        Assert.assertEquals("A", output2);

        writer.getBuffer().setLength(0);

        // Test entity with missing semicolon
        String input3 = "&#65";
        int result3 = unescaper.translate(input3, 0, writer);
        String output3 = writer.toString();
        Assert.assertEquals(4, result3);
        Assert.assertEquals("A", output3);

        writer.getBuffer().setLength(0);

        // Test entity with invalid decimal value
        String input4 = "&#999999;";
        int result4 = unescaper.translate(input4, 0, writer);
        String output4 = writer.toString();
        Assert.assertEquals(10, result4);
        Assert.assertEquals("", output4);

        writer.getBuffer().setLength(0);

        // Test entity with invalid hexadecimal value
        String input5 = "&#xZZ;";
        int result5 = unescaper.translate(input5, 0, writer);
        String output5 = writer.toString();
        Assert.assertEquals(6, result5);
        Assert.assertEquals("", output5);

        writer.getBuffer().setLength(0);

        // Test entity with codepoint greater than 0xFFFF
        String input6 = "&#65536;";
        int result6 = unescaper.translate(input6, 0, writer);
        String output6 = writer.toString();
        Assert.assertEquals(9, result6);
        Assert.assertEquals(String.valueOf(Character.toChars(65536)), output6);
    }
}
