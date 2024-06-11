import org.apache.commons.cli.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_WithNoOptions_Success() throws ParseException {
        String[] arguments = {};
        Options options = new Options();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine commandLine = parser.parse(options, arguments);

        assertNotNull(commandLine);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_WithRequiredOptionMissing_ThrowsMissingOptionException() throws ParseException {
        String[] arguments = {};
        Options options = new Options();
        options.addRequiredOption("o");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_WithOptionalValue_Success() throws ParseException {
        String[] arguments = {"-o", "value"};
        Options options = new Options();
        options.addOption(Option.builder("o").hasOptionalArg().build());

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("o"));
        assertEquals("value", commandLine.getOptionValue("o"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_WithArgumentMissingForOption_ThrowsMissingArgumentException() throws ParseException {
        String[] arguments = {"-o"};
        Options options = new Options();
        options.addOption(Option.builder("o").hasArg().build());

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_WithUnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        String[] arguments = {"-u"};
        Options options = new Options();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessProperties_WithProperties_AddsOptionsToCommandLine() {
        Properties properties = new Properties();
        properties.setProperty("o", "value");

        Options options = new Options();
        options.addOption(Option.builder("o").hasArg().build());

        CommandLine cmd = new CommandLine();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.processProperties(properties);

        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test
    public void testCheckRequiredOptions_WithMissingRequiredOption_ThrowsMissingOptionException() {
        Options options = new Options();
        options.addOption(Option.builder("o").build());

        CommandLine cmd = new CommandLine();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs_WithValues_AddsValuesToOption() throws ParseException {
        Option option = Option.builder("o").hasArgs().build();
        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.processArgs(option, iterator);

        assertTrue(option.hasValues());
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_WithNoValuesAndNoOptionalArg_ThrowsMissingArgumentException() throws ParseException {
        Option option = Option.builder("o").hasArg().build();
        ListIterator iterator = Arrays.asList().listIterator();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption_WithOption_AddsOptionToCommandLine() throws ParseException {
        Option option = Option.builder("o").hasArg().build();
        ListIterator iterator = Arrays.asList("value").listIterator();
        Options options = new Options();
        options.addOption(option);

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            public void processArgs(Option opt, ListIterator iter) throws ParseException {
                opt.addValue((String) iter.next());
            }
        };

        CommandLine cmd = new CommandLine();

        parser.processOption("o", iterator);

        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
    }

}