
import org.junit.Test;
import org.junit.Assert;
import java.io.StringWriter;


public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_WithValidDecimalEntity_ReturnsCorrectTranslation() throws Exception {
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#65;", 0, out);
        Assert.assertEquals("Translation result is incorrect.", 6, result);
        Assert.assertEquals("Translated value is incorrect.", "A", out.toString());
    }

    @Test
    public void testTranslate_WithValidHexEntity_ReturnsCorrectTranslation() throws Exception {
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, out);
        Assert.assertEquals("Translation result is incorrect.", 7, result);
        Assert.assertEquals("Translated value is incorrect.", "A", out.toString());
    }

    @Test
    public void testTranslate_WithValidEntityWithoutSemicolonAndOptionSetToSemiColonRequired_ReturnsZero() throws Exception {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#65", 0, out);
        Assert.assertEquals("Translation result is incorrect.", 0, result);
        Assert.assertEquals("Output should be empty.", "", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_WithValidEntityWithoutSemicolonAndOptionSetToErrorIfNoSemiColon_ThrowsException() throws Exception {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        unescaper.translate("&#65", 0, out);
    }

    @Test
    public void testTranslate_WithInvalidEntityValue_ReturnsZero() throws Exception {
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#999999999;", 0, out);
        Assert.assertEquals("Translation result is incorrect.", 0, result);
        Assert.assertEquals("Output should be empty.", "", out.toString());
    }
}
