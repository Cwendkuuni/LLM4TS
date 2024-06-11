import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

/**
 * Test class for PosixParser.
 */
@RunWith(JUnit4.class)
public class PosixParserTest
{
    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with basic usage.
     */
    @Test
    public void testBasicUsage()
    {
        String[] args = {"-a", "--optionB=valueB", "--optionC=valueC", "--optionD"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", true, "value", "Option B with value");
        options.addOption("c", true, "value", "Option C with value");
        options.addOption("d", true, null, "Option D");

        String[] result = new PosixParser().flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "valueB", "--optionC", "valueC", "--optionD"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with long options.
     */
    @Test
    public void testLongOptions()
    {
        String[] args = {"--optionLongA", "--optionLongB=valueB", "--no-option"};
        Options options = new Options();
        options.addOption("longA", false, null, "Option Long A");
        options.addOption("longB", true, "value", "Option Long B with value");

        String[] result = new PosixParser().flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--optionLongA", "--optionLongB", "valueB"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with short and long options.
     */
    @Test
    public void testShortAndLongOptions()
    {
        String[] args = {"-a", "--optionB=valueB", "-c", "--optionC=valueC", "--optionD"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", true, "value", "Option B with value");
        options.addOption("c", true, "value", "Option C with value");
        options.addOption("d", true, null, "Option D");

        String[] result = new PosixParser().flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "valueB", "-c", "--optionC", "valueC", "--optionD"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOption()
    {
        String[] args = {"--invalidOption"};
        Options options = new Options();
        new PosixParser().flatten(options, args, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with stopAtNonOption.
     */
    @Test
    public void testStopAtNonOption()
    {
        String[] args = {"--optionA", "nonOption"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");

        String[] result = new PosixParser().flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--optionA", "nonOption"}, result);
    }
}