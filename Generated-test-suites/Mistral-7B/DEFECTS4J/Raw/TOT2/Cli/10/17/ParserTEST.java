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
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithValidOptions() throws ParseException {
        String[] args = {"-a", "-b", "--value=test"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("value", true, "Value option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertFalse(cmd.hasUnrecognizedOptions());
        Assert.assertEquals(3, cmd.getArgList().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("value"));
        Assert.assertEquals("test", cmd.getOptionValue("value"));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] args = {"-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("c", true, "Required Option C");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args, true);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: c", e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = {"--invalid"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args, true);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: invalid", e.getMessage());
        }
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        String[] args = {};
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertTrue(cmd.getArgList().isEmpty());
    }

    @Test
    public void testParseWithPropertyValues() throws ParseException {
        String[] args = {"--value1=test1", "--value2=test2"};
        Options options = new Options();
        options.addOption("value1", true, "Option with value1");
        options.addOption("value2", true, "Option with value2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Properties properties = cmd.getOptionProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("test1", properties.getProperty("value1"));
        Assert.assertEquals("test2", properties.getProperty("value2"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"--", "-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true, true);

        Assert.assertEquals(2, cmd.getArgList().size());
        Assert.assertTrue(cmd.getArgList().get(0).startsWith("-"));
        Assert.assertTrue(cmd.getArgList().get(1).startsWith("-"));
    }
}