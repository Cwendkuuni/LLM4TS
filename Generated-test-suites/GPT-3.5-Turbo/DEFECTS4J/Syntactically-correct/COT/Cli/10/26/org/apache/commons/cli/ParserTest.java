
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse_WithNullArguments_ReturnsEmptyCommandLine() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        CommandLine result = parser.parse(new Options(), null);

        assertEquals(0, result.getOptions().length);
        assertEquals(0, result.getArgs().length);
    }

    @Test
    public void testParse_WithNoOptions_ReturnsCommandLineWithArgs() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine result = parser.parse(new Options(), new String[]{"arg1", "arg2"});

        assertEquals(0, result.getOptions().length);
        assertEquals(2, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
    }

    @Test
    public void testParse_WithOptionAndArgument_ReturnsCommandLineWithOptionAndArg() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        options.addOption(Option.builder("o").build());

        CommandLine result = parser.parse(options, new String[]{"-o", "arg1"});

        assertEquals(1, result.getOptions().length);
        assertEquals(1, result.getArgs().length);
        assertTrue(result.hasOption("o"));
        assertEquals("arg1", result.getOptionValue("o"));
    }

    @Test
    public void testParse_WithRequiredOptionNotPresent_ThrowsMissingOptionException() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        options.addOption(Option.builder("o").required(true).build());

        try {
            parser.parse(options, new String[0]);
            fail("MissingOptionException not thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: o", e.getMessage());
        }
    }

    @Test
    public void testParse_WithOptionNoArgsAndPropertiesWithValueTrue_AddsOptionToCommandLine() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        Option option = Option.builder("o").build();
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("o", "true");

        CommandLine result = parser.parse(options, new String[0], properties);

        assertEquals(1, result.getOptions().length);
        assertTrue(result.hasOption("o"));
        assertNull(result.getOptionValue("o"));
    }

    @Test
    public void testParse_WithOptionWithArgsAndPropertiesWithInvalidValue_IgnoresOption() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        Option option = Option.builder("o").hasArgs().build();
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("o", "invalidValue");

        CommandLine result = parser.parse(options, new String[0], properties);

        assertEquals(0, result.getOptions().length);
    }

    @Test
    public void testParse_WithOptionWithArgsAndPropertiesWithValidValue_AddsOptionWithValueToCommandLine() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        Option option = Option.builder("o").hasArgs().build();
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("o", "value1");

        CommandLine result = parser.parse(options, new String[0], properties);

        assertEquals(1, result.getOptions().length);
        assertTrue(result.hasOption("o"));
        assertEquals("value1", result.getOptionValue("o"));
    }
}

