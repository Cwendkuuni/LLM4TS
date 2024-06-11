import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.Arrays;

public class PosixParserTest
{
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with an optional argument");
    }

    /**
     * Test flattening an empty arguments array
     */
    @Test
    public void testEmptyArguments()
    {
        String[] arguments = {};
        String[] expected = {};

        String[] result = new PosixParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flattening command line arguments with valid options
     */
    @Test
    public void testValidOptions()
    {
        String[] arguments = {"--a", "--b=1", "optionC", "--c=2"};
        String[] expected = {"--a", "--b", "optionC", "--c"};

        String[] result = new PosixParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flattening command line arguments with invalid options and stopAtNonOption set to true
     */
    @Test
    public void testInvalidOptionsStopAtNonOption()
    {
        String[] arguments = {"--invalidOption", "optionA", "--c=2"};
        String[] expected = {"--invalidOption", "optionA", "--c"};

        String[] result = new PosixParser().flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flattening command line arguments with invalid options and stopAtNonOption set to false
     */
    @Test
    public void testInvalidOptionsNoStopAtNonOption()
    {
        String[] arguments = {"--invalidOption", "optionA", "--c=2"};
        String[] expected = {"--invalidOption", "--", "optionA", "--c"};

        String[] result = new PosixParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flattening command line arguments with long options
     */
    @Test
    public void testLongOptions()
    {
        String[] arguments = {"--longOptionA", "--longOptionB=valueB", "--longOptionC=valueC"};
        String[] expected = {"--longOptionA", "--longOptionB", "--longOptionC"};

        String[] result = new PosixParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test exception when flattening command line arguments with an invalid option
     */
    @Test(expected = ParseException.class)
    public void testInvalidOptionException()
    {
        String[] arguments = {"--invalidOptionX", "--a"};
        new PosixParser().flatten(options, arguments, false);
    }
}