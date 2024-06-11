import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
    }

    @Test
    public void testParse_noOptions_success() throws ParseException {
        String[] args = new String[0];

        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.getOptions().length == 0);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOption_throwMissingOptionException() throws ParseException {
        Option option = Option.builder("t").required().build();
        options.addOption(option);
        String[] args = new String[0];

        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_missingArgument_throwMissingArgumentException() throws ParseException {
        Option option = Option.builder("a").hasArg().build();
        OptionGroup group = new OptionGroup()
                .addOption(option);
        options.addOptionGroup(group);

        Parser spyParser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-a"};
            }
        };

        spyParser.setOptions(options);
        CommandLine cmd = new CommandLine();
        spyParser.processOption("-a", null);

        spyParser.processArgs(option, null);
    }

    @Test
    public void testProcessArgs_optionalArgument_success() throws ParseException {
        Option option = Option.builder("a").hasOptionalArg().build();
        options.addOption(option);

        Parser spyParser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-a", "value"};
            }
        };

        spyParser.setOptions(options);
        CommandLine cmd = new CommandLine();
        spyParser.processOption("-a", null);

        spyParser.processArgs(option, null);

        assertNotNull(option.getValues());
        assertTrue(option.getValues().length == 1);
        assertEquals("value", option.getValue());
    }

    @Test
    public void testProcessOption_optionWithArgument_success() throws ParseException {
        Option option = Option.builder("a").hasArg().build();
        options.addOption(option);

        Parser spyParser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-a", "value"};
            }
        };

        spyParser.setOptions(options);
        CommandLine cmd = new CommandLine();
        spyParser.processOption("-a", null);

        assertNotNull(cmd.getOptionValue("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_unrecognizedOption_throwUnrecognizedOptionException() throws ParseException {
        Parser spyParser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-b"};
            }
        };

        spyParser.setOptions(options);
        CommandLine cmd = new CommandLine();
        spyParser.processOption("-b", null);
    }

}