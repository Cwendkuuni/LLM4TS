package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };

        options = new Options();
    }

    @Test
    public void testParse_OptionsArguments_ReturnsCommandLine() throws ParseException {
        String[] arguments = {"-f", "file.txt"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParse_OptionsProperties_ReturnsCommandLine() throws ParseException {
        String[] arguments = {};
        Properties properties = new Properties();
        properties.setProperty("f", "file.txt");

        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testCheckRequiredOptions_AllRequiredOptionsPresent_DoesNotThrowException() throws MissingOptionException {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("v", "verbose", false, "verbose option");
        options.addOption(option1);
        options.addOption(option2);

        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_MissingRequiredOption_ThrowsException() throws MissingOptionException {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("v", "verbose", false, "verbose option");
        options.addOption(option1);
        options.addOption(option2);

        parser.setOptions(options);
        parser.checkRequiredOptions();
    }
}