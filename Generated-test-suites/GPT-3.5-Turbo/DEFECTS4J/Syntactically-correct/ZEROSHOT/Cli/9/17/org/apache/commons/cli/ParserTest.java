import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help message");

        String[] args = {"-h"};

        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("h"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("p", "port", true, "Port number");

        Properties properties = new Properties();
        properties.setProperty("p", "8080");

        String[] args = {};

        CommandLine cmd = parser.parse(options, args, properties);

        Assert.assertTrue(cmd.hasOption("p"));
        Assert.assertEquals("8080", cmd.getOptionValue("p"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required option");
        option.setRequired(true);
        options.addOption(option);

        String[] args = {};

        parser.parse(options, args);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", "arg", true, "Option with argument");
        option.setArgs(2);

        ListIterator iterator = Arrays.asList("arg1", "arg2").listIterator();

        parser.processArgs(option, iterator);

        Assert.assertEquals("arg1", option.getValue(0));
        Assert.assertEquals("arg2", option.getValue(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = new Option("a", "arg", true, "Option with argument");
        option.setArgs(1);

        ListIterator iterator = Arrays.asList().listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "flag", false, "Flag option");
        options.addOption(option);
        
        ListIterator iterator = Arrays.asList("-f").listIterator();

        parser.processOption("-f", iterator);

        Assert.assertTrue(parser.getOptions().hasOption("f"));
        Assert.assertTrue(parser.getRequiredOptions().isEmpty());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Options options = new Options();

        ListIterator iterator = Arrays.asList("-u").listIterator();

        parser.processOption("-u", iterator);
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        Option option = new Option("p", "port", true, "Port number");
        options.addOption(option);

        CommandLine cmd = new CommandLine();

        Properties properties = new Properties();
        properties.setProperty("p", "8080");

        parser.processProperties(properties);

        Assert.assertTrue(cmd.hasOption("p"));
        Assert.assertEquals("8080", cmd.getOptionValue("p"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissingRequiredOption() throws MissingOptionException {
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required option");
        option.setRequired(true);
        options.addOption(option);

        parser.setOptions(options);

        parser.checkRequiredOptions();
    }

    @Test
    public void testCheckRequiredOptionsNoMissingRequiredOption() throws MissingOptionException {
        Options options = new Options();

        parser.setOptions(options);

        parser.checkRequiredOptions();
    }
}