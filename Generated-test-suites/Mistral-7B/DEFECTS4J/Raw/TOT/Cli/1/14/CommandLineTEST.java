import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for {@link CommandLine} class.
 */
public class CommandLineTest {

    private CommandLine commandLine;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with an argument");
        options.addOption("c", "defaultValue", "Option C with a default value");

        CommandLineParser parser = new DefaultParser();

        try {
            commandLine = parser.parse(options, new String[]{"-a", "--optionB", "1", "-c"});
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test {@link CommandLine#hasOption(String)} method.
     */
    @Test
    public void testHasOption() {
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertTrue(commandLine.hasOption("c"));
        Assert.assertFalse(commandLine.hasOption("d"));
    }

    /**
     * Test {@link CommandLine#getOptionValue(String)} method.
     */
    @Test
    public void testGetOptionValue() {
        Assert.assertNull(commandLine.getOptionValue("d"));
        Assert.assertEquals("true", commandLine.getOptionValue("a"));
        Assert.assertEquals("1", commandLine.getOptionValue("b"));
        Assert.assertEquals("defaultValue", commandLine.getOptionValue("c"));
    }

    /**
     * Test {@link CommandLine#getOptionValues(String)} method.
     */
    @Test
    public void testGetOptionValues() {
        String[] values = commandLine.getOptionValues("b");
        Assert.assertArrayEquals(new String[]{"1"}, values);

        String[] emptyValues = commandLine.getOptionValues("x");
        Assert.assertArrayEquals(new String[0], emptyValues);
    }

    /**
     * Test {@link CommandLine#getArgs()} method.
     */
    @Test
    public void testGetArgs() {
        List<String> expectedArgs = Arrays.asList(new String[]{"--optionB", "1", "-c"});
        Assert.assertArrayEquals(commandLine.getArgs().toArray(new String[0]), expectedArgs.toArray(new String[0]));
    }
}