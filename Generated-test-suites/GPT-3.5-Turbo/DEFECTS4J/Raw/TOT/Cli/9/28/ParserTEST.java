
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                // Stubbed for testing purposes
                return new String[0];
            }

            @Override
            public void processArgs(Option opt, ListIterator iter) throws ParseException {
                // Stubbed for testing purposes
            }

            @Override
            protected void processOption(String arg, ListIterator iter) throws ParseException {
                // Stubbed for testing purposes
            }
        };
    }

    @Test
    public void parse_WithNoOptionsAndNoArguments_ShouldReturnEmptyCommandLine() throws ParseException {
        Options options = new Options();
        String[] arguments = {};

        CommandLine result = parser.parse(options, arguments);

        assertThat(result.getOptions().length, is(0));
        assertThat(result.getArgs().length, is(0));
    }

    @Test
    public void parse_WithNoOptionsAndArguments_ShouldReturnCommandLineWithArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};

        CommandLine result = parser.parse(options, arguments);

        assertThat(result.getOptions().length, is(0));
        assertThat(result.getArgs().length, is(2));
        assertThat(result.getArgs()[0], is("arg1"));
        assertThat(result.getArgs()[1], is("arg2"));
    }

    @Test
    public void parse_WithSingleOptionAndNoArguments_ShouldReturnCommandLineWithOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", false, "Optional option");
        String[] arguments = {};

        CommandLine result = parser.parse(options, arguments);

        assertThat(result.getOptions().length, is(1));
        assertThat(result.getOptionValue("o"), nullValue());
    }

    @Test
    public void parse_WithSingleOptionAndArgument_ShouldReturnCommandLineWithOptionAndArgument() throws ParseException {
        Options options = new Options();
        options.addOption("o", false, "Optional option");
        String[] arguments = {"arg1"};

        CommandLine result = parser.parse(options, arguments);

        assertThat(result.getOptions().length, is(1));
        assertThat(result.getOptionValue("o"), nullValue());
        assertThat(result.getArgs().length, is(1));
        assertThat(result.getArgs()[0], is("arg1"));
    }

    @Test
    public void parse_WithRequiredOptionAndNoArguments_ShouldThrowMissingOptionException() {
        Options options = new Options();
        options.addOption(Option.builder("r").required().build());
        String[] arguments = {};

        ParseException exception = null;
        try {
            parser.parse(options, arguments);
        } catch (ParseException ex) {
            exception = ex;
        }

        assertThat(exception, instanceOf(MissingOptionException.class));
    }

    @Test
    public void parse_WithRequiredOptionAndArgument_ShouldReturnCommandLineWithOptionAndArgument() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("r").required().build());
        String[] arguments = {"arg1"};

        CommandLine result = parser.parse(options, arguments);

        assertThat(result.getOptions().length, is(1));
        assertThat(result.getOptionValue("r"), nullValue());
        assertThat(result.getArgs().length, is(1));
        assertThat(result.getArgs()[0], is("arg1"));
    }

    @Test
    public void parse_WithInvalidOption_ShouldThrowUnrecognizedOptionException() {
        Options options = new Options();
        String[] arguments = {"-a"};

        ParseException exception = null;
        try {
            parser.parse(options, arguments);
        } catch (ParseException ex) {
            exception = ex;
        }

        assertThat(exception, instanceOf(UnrecognizedOptionException.class));
        assertThat(exception.getMessage(), is("Unrecognized option: -a"));
    }

    @Test
    public void parse_WithOptionTakingValueAndNoArgument_ShouldThrowMissingArgumentException() {
        Options options = new Options();
        options.addOption(Option.builder("v").hasArg().build());
        String[] arguments = {"-v"};

        ParseException exception = null;
        try {
            parser.parse(options, arguments);
        } catch (ParseException ex) {
            exception = ex;
        }

        assertThat(exception, instanceOf(MissingArgumentException.class));
        assertThat(exception.getMessage(), is("Missing argument for option:v"));
    }

    @Test
    public void parse_WithOptionTakingValueAndArgument_ShouldReturnCommandLineWithOptionAndArgument() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("v").hasArg().build());
        String[] arguments = {"-v", "value"};

        CommandLine result = parser.parse(options, arguments);

        assertThat(result.getOptions().length, is(1));
        assertThat(result.getOptionValue("v"), is("value"));
        assertThat(result.getArgs().length, is(0));
    }

    @Test
    public void parse_WithStopAtNonOptionAndRemainingArguments_ShouldReturnCommandLineWithOptionAndRemainingArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", false, "Optional option");
        String[] arguments = {"-o", "arg1", "arg2"};

        CommandLine result = parser.parse(options, arguments, true);

        assertThat(result.getOptions().length, is(1));
        assertThat(result.getOptionValue("o"), nullValue());
        assertThat(result.getArgs().length, is(2));
        assertThat(result.getArgs()[0], is("arg1"));
        assertThat(result.getArgs()[1], is("arg2"));
    }
}

