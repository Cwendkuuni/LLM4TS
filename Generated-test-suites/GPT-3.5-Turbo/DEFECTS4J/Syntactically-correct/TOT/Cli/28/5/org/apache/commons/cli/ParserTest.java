import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.OptionGroup;

import java.util.Properties;

public class ParserTest {

    private Parser parser;
    private Options options;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        options = new Options();
        Option option = new Option("t", "test", false, "Testing option");
        options.addOption(option);

        arguments = new String[]{"-t"};
    }

    @Test
    public void testParse() throws ParseException {
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("t"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOption() throws ParseException {
        options.getOption("t").setRequired(true);

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        arguments = new String[]{"-u"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_stopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"-t", "-u"};

        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("t"));
        assertEquals("-u", result.getArgs()[0]);
    }

    @Test
    public void testParse_withProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("t", "true");

        CommandLine result = parser.parse(options, arguments, properties);

        assertTrue(result.hasOption("t"));
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("t", "test", false, "Testing option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        options.addOptionGroup(group);

        CommandLine result = new CommandLine();

        parser.processOption("t", result.iterator());

        assertTrue(result.hasOption("t"));
        assertSame(option, group.getSelected());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessOption_missingArgument() throws ParseException {
        Option option = new Option("t", "test", true, "Testing option");
        options.addOption(option);

        CommandLine result = new CommandLine();

        parser.processOption("t", result.iterator());
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("t", "test", true, "Testing option");
        options.addOption(option);

        CommandLine result = new CommandLine();

        parser.processArgs(option, result.iterator());

        assertTrue(result.hasOption("t"));
        assertSame(option.getValues()[0], result.getOptionValue("t"));
    }

}