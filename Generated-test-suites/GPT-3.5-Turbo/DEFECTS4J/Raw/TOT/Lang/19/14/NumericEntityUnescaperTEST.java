import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper numericEntityUnescaper;

    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testValidDecimalCodepoint() throws IOException {
        // Typical use case - valid decimal codepoint
        String input = "&#65;";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);

        Assert.assertEquals(5, result);
        Assert.assertEquals("A", out.toString());
    }

    @Test
    public void testValidHexadecimalCodepoint() throws IOException {
        // Typical use case - valid hexadecimal codepoint
        String input = "&#x20AC;";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);

        Assert.assertEquals(8, result);
        Assert.assertEquals("€", out.toString());
    }

    @Test
    public void testEmptyInput() throws IOException {
        // Edge case - empty input
        String input = "";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void testNoNumericEntity() throws IOException {
        // Edge case - input without numeric entity
        String input = "abc";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void testInvalidNumericEntity() throws IOException {
        // Error scenario - invalid numeric entity
        String input = "&#XYZ;";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);

        Assert.assertEquals(0, result);
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void testCodepointGreaterThanFFFF() throws IOException {
        // Edge case - codepoint greater than FFFF
        String input = "&#65536;";
        StringWriter out = new StringWriter();
        int result = numericEntityUnescaper.translate(input, 0, out);

        Assert.assertEquals(11, result);
        Assert.assertEquals("\ud800\udc00", out.toString());
    }

}