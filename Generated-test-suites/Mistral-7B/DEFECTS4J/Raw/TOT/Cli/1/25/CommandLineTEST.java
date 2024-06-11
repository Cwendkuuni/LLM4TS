import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

    @Test
    public void testCommandLine() throws ParseException {
        String[] args = new String[]{"-a", "1", "--optionB", "2", "--no-optionC", "3", "4", "5"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // Test hasOption
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertFalse(cmd.hasOption("d"));

        // Test getOptionValue
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getOptionValue("d"));

        // Test getOptionValues
        String[] values = cmd.getOptionValues("b");
        Assert.assertArrayEquals(new String[]{"2"}, values);

        // Test getArgs
        String[] argsList = cmd.getArgs();
        Assert.assertArrayEquals(new String[]{"3", "4", "5"}, argsList);

        // Test getArgList
        List<String> argList = cmd.getArgList();
        Assert.assertEquals(Arrays.asList(Arrays.asList(Arrays.asList("3", "4", "5"))), argList);
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException {
        String[] args = new String[]{"-a", "1", "--optionB", "2", "--no-optionC", "3", "4", "5", "--invalid"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // Test invalid option should throw ParseException
        cmd.getOptionValue("invalid");
    }
}