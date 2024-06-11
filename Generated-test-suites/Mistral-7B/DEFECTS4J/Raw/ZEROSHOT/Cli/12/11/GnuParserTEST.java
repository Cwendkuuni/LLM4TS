import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = {"-Dproperty=value", "--foo", "-a", "--", "arg1", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("D", "debug", false, "debug mode");
        options.addOption("f", "foo", true, "foos the option");
        options.addOption("a", "append", false, "append mode");

        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("D");
        expectedTokens.add("property");
        expectedTokens.add("value");
        expectedTokens.add("f");
        expectedTokens.add("foo");
        expectedTokens.add("arg1");
        expectedTokens.add("arg2");
        expectedTokens.add("arg3");

        String[] flattenedArguments = new GnuParser().parse(options, arguments, true);

        Assert.assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), flattenedArguments);
    }

    @Test(expected = ParseException.class)
    public void testFlattenStopAtNonOption() throws ParseException {
        String[] arguments = {"-Dproperty=value", "--foo", "-a", "--", "arg1", "arg2", "arg3", "nonOption"};
        Options options = new Options();
        options.addOption("D", "debug", false, "debug mode");
        options.addOption("f", "foo", true, "foos the option");
        options.addOption("a", "append", false, "append mode");

        new GnuParser().parse(options, arguments, true);

        //this test should throw ParseException
    }
}