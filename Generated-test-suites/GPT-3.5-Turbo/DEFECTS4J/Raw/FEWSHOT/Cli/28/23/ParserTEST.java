
package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {
    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        String[] args = {"arg1", "arg2"};
        CommandLine result = parser.parse(options, args);

        assertEquals(result.getArgList().size(), 2);
        assertEquals(result.getArgList().get(0), "arg1");
        assertEquals(result.getArgList().get(1), "arg2");
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        Option option1 = new Option("f", false, "Option 1");
        Option option2 = new Option("v", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        String[] args = {"-f", "--optionValue", "value"};
        CommandLine result = parser.parse(options, args);

        assertTrue(result.hasOption("f"));
        assertTrue(result.hasOption("optionValue"));
        assertEquals(result.getOptionValue("optionValue"), "value");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Option propertyOption = Option.builder()
                .longOpt("property")
                .desc("Property option")
                .hasArg()
                .build();
        options.addOption(propertyOption);

        Properties properties = new Properties();
        properties.setProperty("property", "value");

        String[] args = {"-D", "property=value"};
        CommandLine result = parser.parse(options, args, properties);

        assertTrue(result.hasOption("property"));
        assertEquals(result.getOptionValue("property"), "value");
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Option requiredOption = new Option("r", true, "Required option");
        options.addOption(requiredOption);

        String[] args = {};
        parser.parse(options, args);
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        Option requiredOption = new Option("r", true, "Required option");
        options.addOption(requiredOption);

        String[] args = {"-r", "value"};
        CommandLine result = parser.parse(options, args);

        assertTrue(result.hasOption("r"));
        assertEquals(result.getOptionValue("r"), "value");
    }
}

