import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }

            @Override
            protected void processArgs(Option opt, ListIterator iter) throws ParseException {

            }

            @Override
            protected void processOption(String arg, ListIterator iter) throws ParseException {

            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("h", false, "Print help");

        CommandLine commandLine = parser.parse(options, new String[]{"-h"});

        assertTrue(commandLine.hasOption("h"));
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("verbose", "true");

        parser.processProperties(properties);

        Options options = parser.getOptions();
        Option option = options.getOption("verbose");

        assertNotNull(option);
        assertEquals("true", option.getValue());
    }

    @Test
    public void testCheckRequiredOptions() {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").required().build());

        parser.setOptions(options);

        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            assertTrue(e.getMessage().contains("Missing required options: -a -b"));
        }
    }
}