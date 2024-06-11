import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testParseWithOptionsAndArguments() {
        String[] arguments = new String[]{"-p", "123", "-o", "output.txt"};
        options.addOption("p", true, "Port option");
        options.addOption("o", "output", true, "Output option");
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments);

            assertEquals("Unexpected argument value", "123", cmd.getOptionValue("p"));
            assertEquals("Unexpected argument value", "output.txt", cmd.getOptionValue("o"));
        } catch (ParseException e) {
            fail("ParseException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionsAndProperties() {
        String[] arguments = new String[]{"-p", "123", "-o", "output.txt"};
        Properties properties = new Properties();
        properties.setProperty("p", "456");
        properties.setProperty("o", "output2.txt");
        options.addOption("p", true, "Port option");
        options.addOption("o", "output", true, "Output option");
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments, properties);

            assertEquals("Unexpected argument value", "123", cmd.getOptionValue("p"));
            assertEquals("Unexpected argument value", "output.txt", cmd.getOptionValue("o"));
        } catch (ParseException e) {
            fail("ParseException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithStopAtNonOption() {
        String[] arguments = new String[]{"-p", "123", "arg1", "-o", "output.txt"};
        options.addOption("p", true, "Port option");
        options.addOption("o", "output", true, "Output option");
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments, true);

            assertEquals("Unexpected argument value", "123", cmd.getOptionValue("p"));
            assertNull("Unexpected argument value", cmd.getOptionValue("o"));
            assertEquals("Unexpected argument value", "arg1", cmd.getArgs()[0]);
            assertEquals("Unexpected argument value", "-o", cmd.getArgs()[1]);
            assertEquals("Unexpected argument value", "output.txt", cmd.getArgs()[2]);
        } catch (ParseException e) {
            fail("ParseException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testProcessProperties() {
        String[] arguments = new String[]{"-p", "123", "-o", "output.txt"};
        Properties properties = new Properties();
        properties.setProperty("p", "456");
        properties.setProperty("o", "output2.txt");
        options.addOption("p", true, "Port option");
        options.addOption("o", "output", true, "Output option");
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments);
            ((Parser) parser).processProperties(properties);

            assertEquals("Unexpected argument value", "456", cmd.getOptionValue("p"));
            assertEquals("Unexpected argument value", "output2.txt", cmd.getOptionValue("o"));
        } catch (ParseException e) {
            fail("ParseException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCheckRequiredOptions() {
        options.addOption(Option.builder("p").hasArg().required().build());
        options.addOption(Option.builder("o").longOpt("output").hasArg().required().build());
        CommandLineParser parser = new DefaultParser();

        try {
            parser.parse(options, new String[]{});
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            assertTrue("Unexpected exception message", e.getMessage().contains("Missing required option"));
        } catch (ParseException e) {
            fail("ParseException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testProcessArgs() {
        Option option = Option.builder("p").hasArgs().build();
        ListIterator<String> iterator = Arrays.asList("-p", "123", "456").listIterator();
        CommandLineParser parser = new DefaultParser();

        try {
            ((Parser) parser).processArgs(option, iterator);

            assertEquals("Unexpected argument value", "123", option.getValues()[0]);
            assertEquals("Unexpected argument value", "456", option.getValues()[1]);

        } catch (ParseException e) {
            fail("ParseException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testProcessOption() {
        Option option = Option.builder("p").hasArg().build();
        ListIterator<String> iterator = Arrays.asList("-p", "123").listIterator();
        CommandLineParser parser = new DefaultParser();

        try {
            ((Parser) parser).processOption("p", iterator);

            assertEquals("Unexpected option value", "123", option.getValue());
        } catch (ParseException e) {
            fail("ParseException occurred: " + e.getMessage());
        }
    }
}