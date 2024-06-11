import org.junit.Test;
import org.apache.commons.cli.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        // Test case 1: options exist for the first character of arguments entry
        Options options1 = new Options();
        options1.addOption("D", false, "");
        String[] arguments1 = {"-D", "property=value"};
        String[] expected1 = {"-D", "property=value"};
        GnuParser parser1 = new GnuParser();
        String[] result1 = parser1.flatten(options1, arguments1, false);
        Assert.assertArrayEquals(expected1, result1);

        // Test case 2: options do not exist for the whole argument
        Options options2 = new Options();
        options2.addOption("foo", false, "");
        String[] arguments2 = {"-abc"};
        String[] expected2 = {"-a", "-b", "-c"};
        GnuParser parser2 = new GnuParser();
        String[] result2 = parser2.flatten(options2, arguments2, false);
        Assert.assertArrayEquals(expected2, result2);

        // Test case 3: stopAtNonOption is true
        Options options3 = new Options();
        options3.addOption("a", false, "");
        String[] arguments3 = {"-abc", "nonOption"};
        String[] expected3 = {"-a", "-b", "-c"};
        GnuParser parser3 = new GnuParser();
        String[] result3 = parser3.flatten(options3, arguments3, true);
        Assert.assertArrayEquals(expected3, result3);

        // Test case 4: -- is encountered
        Options options4 = new Options();
        options4.addOption("a", false, "");
        String[] arguments4 = {"-abc", "--", "-def"};
        String[] expected4 = {"-a", "-b", "-c", "--", "-def"};
        GnuParser parser4 = new GnuParser();
        String[] result4 = parser4.flatten(options4, arguments4, false);
        Assert.assertArrayEquals(expected4, result4);

        // Test case 5: - is encountered
        Options options5 = new Options();
        options5.addOption("a", false, "");
        String[] arguments5 = {"-abc", "-"};
        String[] expected5 = {"-a", "-b", "-c", "-"};
        GnuParser parser5 = new GnuParser();
        String[] result5 = parser5.flatten(options5, arguments5, false);
        Assert.assertArrayEquals(expected5, result5);
    }
}