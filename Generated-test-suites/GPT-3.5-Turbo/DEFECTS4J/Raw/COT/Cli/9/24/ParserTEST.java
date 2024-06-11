
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;

import static org.junit.Assert.*;

public class ParserTest {

    // Test parse(Options options, String[] arguments)
    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));

        String[] arguments = {"-a", "-b"};

        Parser parser = createParser();
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
    }

    // Test parse(Options options, String[] arguments, Properties properties)
    @Test
    public void testParseWithOptionsArgumentsAndProperties() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));

        String[] arguments = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = createParser();
        CommandLine result = parser.parse(options, arguments, properties);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
    }

    // Test parse(Options options, String[] arguments, boolean stopAtNonOption)
    @Test
    public void testParseWithOptionsArgumentsAndStopAtNonOptionEnabled() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));

        String[] arguments = {"-a", "nonOption"};

        Parser parser = createParser();
        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertTrue(result.getArgList().contains("nonOption"));
    }

    // Test parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
    @Test
    public void testParseWithOptionsArgumentsPropertiesAndStopAtNonOptionEnabled() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));

        String[] arguments = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = createParser();
        CommandLine result = parser.parse(options, arguments, properties, true);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
    }

    // Test flatten(Options opts, String[] arguments, boolean stopAtNonOption)
    @Test
    public void testFlattenWithOptionsArgumentsAndStopAtNonOptionEnabled() {
        Options options = new Options();
        options.addOption(new Option("a", "option a"));
        options.addOption(new Option("b", "option b"));

        String[] arguments = {"-a", "nonOption"};

        Parser parser = createParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-a", "nonOption"}, flattenedArguments);
    }

    // Test processArgs(Option opt, ListIterator iter)
    @Test
    public void testProcessArgsWithOptionAndIterator() throws ParseException {
        Option option = new Option("a", "option a");
        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();

        Parser parser = createParser();
        parser.processArgs(option, iterator);

        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    // Test processOption(String arg, ListIterator iter)
    @Test
    public void testProcessOptionWithArgAndIterator() throws ParseException {
        Option option = new Option("a", "option a");
        ListIterator<String> iterator = Arrays.asList("-a").listIterator();

        Parser parser = createParser();
        parser.processOption("-a", iterator);

        assertTrue(option.hasArg());
    }

    // Test processProperties(Properties properties)
    @Test
    public void testProcessPropertiesWithProperties() {
        Option option = new Option("a", "option a");
        Options options = new Options();
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("a", "value");

        Parser parser = createParser();
        parser.setOptions(options);
        parser.processProperties(properties);

        assertArrayEquals(new String[]{"value"}, option.getValues());
    }

    // Test checkRequiredOptions()
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingOption() throws MissingOptionException {
        Option option = new Option("a", "option a");
        option.setRequired(true);
        Options options = new Options();
        options.addOption(option);

        Parser parser = createParser();
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    private Parser createParser() {
        return new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            protected void processArgs(Option opt, ListIterator iter) throws ParseException {
                while (iter.hasNext()) {
                    String str = (String) iter.next();
                    opt.addValueForProcessing(str);
                }
            }

            @Override
            protected void processOption(String arg, ListIterator iter) throws ParseException {
                Option option = new Option(arg, null);
                processArgs(option, iter);
                cmd.addOption(option);
            }
        };
    }
}
