import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CommandLineTest {

    @Test
    public void testBasicUsage() throws ParseException {
        String[] args = {"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, " Option A");
        options.addOption("b", true, " Option B");

        CommandLine cmd = new CommandLine(args);
        cmd.parse(options);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testMissingOption() throws ParseException {
        String[] args = {"-c", "3"};
        Options options = new Options();
        options.addOption("a", true, " Option A");
        options.addOption("b", true, " Option B");

        CommandLine cmd = new CommandLine(args);
        cmd.parse(options);

        Assert.assertFalse(cmd.hasOption("c"));
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testMissingValueOption() throws ParseException {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, " Option A");

        CommandLine cmd = new CommandLine(args);
        cmd.parse(options);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testMultipleValuesOption() throws ParseException {
        String[] args = {"-a", "1", "2", "3"};
        Options options = new Options();
        options.addOption("a", true, " Option A");

        CommandLine cmd = new CommandLine(args);
        cmd.parse(options);

        String[] values = cmd.getOptionValues("a");
        Assert.assertArrayEquals(new String[]{"1", "2", "3"}, values);
    }

    @Test
    public void testNoOptions() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        CommandLine cmd = new CommandLine(args);
        cmd.parse(options);

        Assert.assertEquals(args.length, cmd.getArgList().size());
        Assert.assertArrayEquals(args, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"-z"};
        Options options = new Options();
        options.addOption("a", true, " Option A");
        options.addOption("b", true, " Option B");

        CommandLine cmd = new CommandLine(args);
        cmd.parse(options);

        Assert.fail("Should have thrown a ParseException for invalid option '-z'");
    }

    @Test
    public void testLeftOverArguments() throws ParseException {
        String[] args = {"-a", "1", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, " Option A");

        CommandLine cmd = new CommandLine(args);
        cmd.parse(options);

        String[] leftOver = cmd.getArgs();
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, leftOver);
    }
}