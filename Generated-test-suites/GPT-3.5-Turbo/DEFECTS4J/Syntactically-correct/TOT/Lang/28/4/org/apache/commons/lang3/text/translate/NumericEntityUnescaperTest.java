import java.io.IOException;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper;
    private StringWriter writer;
    
    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
        writer = new StringWriter();
    }
    
    @Test
    public void testTranslate_withValidDecimalEntity_shouldTranslateCorrectly() throws IOException {
        String input = "This is a valid decimal entity: &#65;";
        int expectedOutput = 65;
        int actualOutput = unescaper.translate(input, 18, writer);
        
        Assert.assertEquals(expectedOutput, actualOutput);
        Assert.assertEquals((char) expectedOutput, writer.toString().charAt(0));
    }
    
    @Test
    public void testTranslate_withValidHexEntity_shouldTranslateCorrectly() throws IOException {
        String input = "This is a valid hex entity: &#x41;";
        int expectedOutput = 65;
        int actualOutput = unescaper.translate(input, 20, writer);
        
        Assert.assertEquals(expectedOutput, actualOutput);
        Assert.assertEquals((char) expectedOutput, writer.toString().charAt(0));
    }
    
    @Test
    public void testTranslate_withInvalidEntityFormat_shouldReturnZero() throws IOException {
        String input = "This is an invalid entity: &#123";
        int expectedOutput = 0;
        int actualOutput = unescaper.translate(input, 19, writer);
        
        Assert.assertEquals(expectedOutput, actualOutput);
        Assert.assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_withInvalidHexEntityValue_shouldReturnZero() throws IOException {
        String input = "This is an invalid hex entity: &#xZZ;";
        int expectedOutput = 0;
        int actualOutput = unescaper.translate(input, 22, writer);
        
        Assert.assertEquals(expectedOutput, actualOutput);
        Assert.assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_withEmptyInput_shouldReturnZero() throws IOException {
        String input = "";
        int expectedOutput = 0;
        int actualOutput = unescaper.translate(input, 0, writer);
        
        Assert.assertEquals(expectedOutput, actualOutput);
        Assert.assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_withValidDecimalEntityAtEndOfString_shouldTranslateCorrectly() throws IOException {
        String input = "This is a valid decimal entity at the end: &#65;";
        int expectedOutput = 65;
        int actualOutput = unescaper.translate(input, 40, writer);
        
        Assert.assertEquals(expectedOutput, actualOutput);
        Assert.assertEquals((char) expectedOutput, writer.toString().charAt(0));
    }
    
    @Test
    public void testTranslate_withValidHexEntityAtEndOfString_shouldTranslateCorrectly() throws IOException {
        String input = "This is a valid hex entity at the end: &#x41;";
        int expectedOutput = 65;
        int actualOutput = unescaper.translate(input, 42, writer);
        
        Assert.assertEquals(expectedOutput, actualOutput);
        Assert.assertEquals((char) expectedOutput, writer.toString().charAt(0));
    }
}