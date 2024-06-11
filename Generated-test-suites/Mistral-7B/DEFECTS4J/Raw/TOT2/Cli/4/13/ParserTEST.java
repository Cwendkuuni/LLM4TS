import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    /**
     * Test the parse method with valid arguments.
     */
    @Test
    public void testParseValidArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");

        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"-a", "--b", "1", "--c", "3"};
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertNull(cmd.getOptionValue("a"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("1", cmd.getArgs()[0]);
        Assert.assertEquals(1, cmd.getOptionValues("b").length);
        Assert.assertEquals(new Integer(1), cmd.getOptionValues("b")[0]);
        Assert.assertEquals(3, cmd.getOptionValues("c").length);
        Assert.assertEquals(new Integer(3), cmd.getOptionValues("c")[0]);
        Assert.assertEquals(new Integer(3), cmd.getOptionValues("c")[1]);
    }

    /**
     * Test the parse method with invalid arguments.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"-a", "--b", "1", "--c", "3", "--x"};
        parser.parse(options, arguments, true);
    }

    /**
     * Test the parse method with required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addRequiredOption("d", false, "Option D");

        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"-a", "--b", "--c"};
        parser.parse(options, arguments, true);
    }

    /**
     * Test the parse method with an invalid option group.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption("a", true, "Option A");
        group.addOption("b", true, "Option B");
        options.addOptionGroup(group);
        options.addOption("x", true, "Option X");

        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"--group"};
        parser.parse(options, arguments, true);
    }

    /**
     * Test the parse method with properties.
     */
    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Properties properties = new Properties();
        properties.put("a", "valueA");
        properties.put("b", "valueB");

        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"--a", "--b"};
        CommandLine cmd = parser.parse(options, arguments, properties, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }
}