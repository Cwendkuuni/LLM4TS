import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithOptions() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addOption("b", true, "B option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("nonexistent"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        String[] arguments = new String[]{"--nonexistent"};
        Options options = new Options();
        options.addOption("a", true, "A option");

        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"-z", "--nonexistent"};
        Options options = new Options();
        options.addOption("a", true, "A option");

        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        String[] arguments = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "A option");
        options.addOption("b", true, "B option");
        options.addOption("c", 1, "C option");

        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = new String[]{"--property1=value1", "--property2=value2"};
        Options options = new Options();
        options.addOption("p", true, "Prints property value");
        options.addRequiredOption("p", "prop", true, "Property name");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, new Properties());

        Assert.assertEquals(true, cmd.hasOption("p"));
        Assert.assertEquals("value1", cmd.getOptionValue("prop"));
        Assert.assertEquals("value2", cmd.getOptionValue("p", "defaultValue"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"--optionA", "arg1", "-n", "arg2", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] arguments = new String[]{"--optionA", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("a", "requiredA");
        options.addRequiredOption("b", "requiredB");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals(true, cmd.hasOption("requiredA"));
        Assert.assertEquals(true, cmd.hasOption("requiredB"));

    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("a", "requiredA");

        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] arguments = new String[]{"--group1", "--optionA"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption("a", true, "Option A");
        group1.addOption("b", true, "Option B");
        options.addOptionGroup(group1);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertEquals("group1", cmd.getOptionGroup("group1").getName());
        Assert.assertEquals("group1", cmd.getOptionGroup("group1").getLongOpt());
    }
}