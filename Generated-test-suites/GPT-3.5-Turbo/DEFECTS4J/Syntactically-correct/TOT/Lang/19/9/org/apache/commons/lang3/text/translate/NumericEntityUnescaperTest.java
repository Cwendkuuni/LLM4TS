import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_typicalCase() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        
        try {
            int numCharsTranslated = translator.translate("&#65;", 0, output);
            Assert.assertEquals(5, numCharsTranslated);
            Assert.assertEquals("A", output.toString());
        } catch (IOException e) {
            Assert.fail("IOException thrown unexpectedly");
        }
    }
    
    @Test
    public void testTranslate_noSemiColon() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        
        try {
            int numCharsTranslated = translator.translate("&#65", 0, output);
            Assert.assertEquals(4, numCharsTranslated);
            Assert.assertEquals("A", output.toString());
        } catch (IOException e) {
            Assert.fail("IOException thrown unexpectedly");
        }
    }
    
    @Test
    public void testTranslate_hexadecimalEntity() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        
        try {
            int numCharsTranslated = translator.translate("&#x41;", 0, output);
            Assert.assertEquals(7, numCharsTranslated);
            Assert.assertEquals("A", output.toString());
        } catch (IOException e) {
            Assert.fail("IOException thrown unexpectedly");
        }
    }
    
    @Test
    public void testTranslate_invalidEntityFormat() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        
        try {
            int numCharsTranslated = translator.translate("&#A;", 0, output);
            Assert.assertEquals(0, numCharsTranslated);
            Assert.assertEquals("", output.toString());
        } catch (IOException e) {
            Assert.fail("IOException thrown unexpectedly");
        }
    }
    
    @Test
    public void testTranslate_invalidEntityNumber() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        
        try {
            int numCharsTranslated = translator.translate("&#99999;", 0, output);
            Assert.assertEquals(0, numCharsTranslated);
            Assert.assertEquals("", output.toString());
        } catch (IOException e) {
            Assert.fail("IOException thrown unexpectedly");
        }
    }
    
    @Test
    public void testTranslate_entityValueGreaterThanFFFF() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        
        try {
            int numCharsTranslated = translator.translate("&#1114112;", 0, output);
            Assert.assertEquals(5, numCharsTranslated);
            Assert.assertEquals("𐀀", output.toString());
        } catch (IOException e) {
            Assert.fail("IOException thrown unexpectedly");
        }
    }
    
    @Test
    public void testTranslate_entityValueNotGreaterThanFFFF() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();
        
        try {
            int numCharsTranslated = translator.translate("&#65;", 0, output);
            Assert.assertEquals(5, numCharsTranslated);
            Assert.assertEquals("A", output.toString());
        } catch (IOException e) {
            Assert.fail("IOException thrown unexpectedly");
        }
    }
}