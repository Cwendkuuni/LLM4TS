import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        Options options = new Options();

        Option option1 = new Option("a", "option1", false, "Option 1");
        options.addOption(option1);

        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option2);

        OptionGroup optionGroup = new OptionGroup();
        Option option3 = new Option("c", "option3", false, "Option 3");
        optionGroup.addOption(option3);
        Option option4 = new Option("d", "option4", false, "Option 4");
        optionGroup.addOption(option4);
        options.addOptionGroup(optionGroup);

        String[] arguments = {"-a", "-b", "value", "-c"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        try {
            CommandLine commandLine = parser.parse(options, arguments);

            assertTrue(commandLine.hasOption("a"));
            assertFalse(commandLine.hasOption("b"));
            assertTrue(commandLine.hasOption("c"));
            assertFalse(commandLine.hasOption("d"));
            assertEquals("value", commandLine.getOptionValue("b"));

        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() {
        Option option = new Option("a", "option", true, "Option");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();

        try {
            parser.processArgs(option, iterator);
            assertEquals("value1", option.getValue());
            assertEquals("value2", option.getValues()[0]);

        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();

        Option option1 = new Option("a", "option1", true, "Option 1");
        options.addOption(option1);

        Option option2 = new Option("b", "option2", false, "Option 2");
        options.addOption(option2);

        String[] arguments = {"-a", "-b"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator<String> iterator = Arrays.asList(arguments).listIterator();

        try {
            parser.processOption("-a", iterator);
            assertTrue(option1.hasArg());

            parser.processOption("-b", iterator);
            assertFalse(option2.hasArg());

        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCheckRequiredOptions() {
        Options options = new Options();

        Option option1 = new Option("a", "option1", false, "Option 1");
        option1.setRequired(true);
        options.addOption(option1);

        Option option2 = new Option("b", "option2", false, "Option 2");
        option2.setRequired(true);
        options.addOption(option2);

        String[] arguments = {"-a"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        try {
            parser.parse(options, arguments);
            fail("MissingOptionException should be thrown");

        } catch (MissingOptionException e) {
            assertEquals("b", e.getMissingOptions().get(0));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();

        Option option1 = new Option("a", "option1", true, "Option 1");
        options.addOption(option1);

        String[] arguments = {};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Properties properties = new Properties();
        properties.put("option1", "value");

        try {
            CommandLine commandLine = parser.parse(options, arguments, properties);

            assertEquals("value", commandLine.getOptionValue("option1"));

        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
}