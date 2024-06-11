import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = "test";
        expected = "test";
        actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = "--";
        expected = "";
        actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test1 test2\"";
        String expected = "test1 test2";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);

        str = "test1 test2";
        expected = "test1 test2";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);

        str = "\"\"";
        expected = "";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);
    }
}