import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    // Test for isSet() method
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        Assert.assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        Assert.assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        Assert.assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    // Test for translate() method - typical use case
    @Test
    public void testTranslateTypical() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        int translatedCharacters = unescaper.translate("&#65;", 0, writer);

        Assert.assertEquals(6, translatedCharacters);
        Assert.assertEquals("A", writer.toString());
    }

    // Test for translate() method - edge case with hex value and semi-colon required
    @Test
    public void testTranslateHexSemiColonRequired() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringWriter writer = new StringWriter();
        int translatedCharacters = unescaper.translate("&#x41;", 0, writer);

        Assert.assertEquals(8, translatedCharacters);
        Assert.assertEquals("A", writer.toString());
    }

    // Test for translate() method - edge case with decimal value and semi-colon optional
    @Test
    public void testTranslateDecimalSemiColonOptional() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        StringWriter writer = new StringWriter();
        int translatedCharacters = unescaper.translate("&#65", 0, writer);

        Assert.assertEquals(5, translatedCharacters);
        Assert.assertEquals("A", writer.toString());
    }

    // Test for translate() method - error scenario without semi-colon
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateErrorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        StringWriter writer = new StringWriter();
        unescaper.translate("&#65", 0, writer);
    }

    // Test for translate() method - error scenario with non-numeric entity value
    @Test
    public void testTranslateErrorNonNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        int translatedCharacters = unescaper.translate("&#XYZ;", 0, writer);

        Assert.assertEquals(0, translatedCharacters);
        Assert.assertEquals("", writer.toString());
    }

    // Test for translate() method - edge case with entity value greater than 65535
    @Test
    public void testTranslateEntityValueGreaterThan65535() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        int translatedCharacters = unescaper.translate("&#65536;", 0, writer);

        Assert.assertEquals(8, translatedCharacters);
        Assert.assertEquals("\uD800\uDC00", writer.toString());
    }

}