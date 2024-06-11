import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest
{
    @Test
    public void testFlatten()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B, takes an argument");
        options.addOption("c", 1, "Option C, takes an argument");
        options.addOption("d", false, "Option D, does not take an argument");
        options.addOption("e", false, "Option E, does not take an argument");

        String[] args = {"-a", "--b", "1", "-c", "3", "-d", "--e", "--foo=bar", "--", "arg1", "arg2"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("-a", "-b", "1", "-c", "3", "--d", "--e", "--foo=bar", "--", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        String[] argsWithNonOption = {"-a", "--nonOption", "-b", "--foo=bar", "--", "arg1", "arg2"};
        String[] resultWithNonOption = parser.flatten(options, argsWithNonOption, true);

        List<String> expectedWithNonOption = Arrays.asList("-a", "--nonOption", "--", "arg1", "arg2");
        Assert.assertArrayEquals(expectedWithNonOption.toArray(new String[expectedWithNonOption.size()]), resultWithNonOption);
    }
}