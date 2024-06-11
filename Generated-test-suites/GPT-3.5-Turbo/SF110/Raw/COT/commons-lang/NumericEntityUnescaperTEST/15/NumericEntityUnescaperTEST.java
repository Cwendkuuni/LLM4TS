
import org.junit.Test;
import org.junit.Assert;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("This is a test &amp;#65;&#66;&#67;");
        StringBuilder result = new StringBuilder();
        
        try {
            unescaper.translate(sb, 0, result);
        } catch (Exception e) {
            Assert.fail("Unexpected exception " + e.getMessage());
        }
        
        Assert.assertEquals("This is a test ABC", result.toString());
    }
    
    @Test
    public void testTranslateWithSemicolonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder sb = new StringBuilder();
        sb.append("This is a test &amp;#65;&#66;&#67;");
        StringBuilder result = new StringBuilder();
        
        try {
            unescaper.translate(sb, 0, result);
        } catch (Exception e) {
            Assert.fail("Unexpected exception " + e.getMessage());
        }
        
        Assert.assertEquals("This is a test ABC", result.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithSemicolonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder sb = new StringBuilder();
        sb.append("This is a test &amp;#65;&#66;&#67");
        StringBuilder result = new StringBuilder();
        
        unescaper.translate(sb, 0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder sb = new StringBuilder();
        sb.append("This is a test &amp;#65;&#66;&#67");
        StringBuilder result = new StringBuilder();
        
        unescaper.translate(sb, 0, result);
    }
    
    @Test
    public void testTranslateWithHexadecimalValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("This is a test &amp;#x41;&#x42;&#x43;");
        StringBuilder result = new StringBuilder();
        
        try {
            unescaper.translate(sb, 0, result);
        } catch (Exception e) {
            Assert.fail("Unexpected exception " + e.getMessage());
        }
        
        Assert.assertEquals("This is a test ABC", result.toString());
    }
    
    @Test
    public void testTranslateWithHighEntityValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("This is a test &amp;#65536;");
        StringBuilder result = new StringBuilder();
        
        try {
            unescaper.translate(sb, 0, result);
        } catch (Exception e) {
            Assert.fail("Unexpected exception " + e.getMessage());
        }
        
        Assert.assertEquals("This is a test \uD800\uDC00", result.toString());
    }
}
