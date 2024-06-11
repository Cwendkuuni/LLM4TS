package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
                    throws ParseException {
                return arguments;
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("f", "foo", false, "foo option");
        options.addOption("b", "bar", true, "bar option");

        CommandLine cmd = parser.parse(options, new String[]{"-f", "-b", "value"});

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("bar"));
        assertEquals("value", cmd.getOptionValue("bar"));
        assertFalse(cmd.hasOption("unknown"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("f", "foo", true, "foo option");

        parser.parse(options, new String[]{"-f"});
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("foo", "true");
        properties.setProperty("bar", "value");

        Options options = new Options();
        options.addOption("f", "foo", false, "foo option");
        options.addOption("b", "bar", false, "bar option");

        CommandLine cmd = new CommandLine();
        parser.processProperties(properties);
        parser.checkRequiredOptions();

        assertTrue(cmd.hasOption("foo"));
        assertTrue(cmd.hasOption("bar"));
        assertEquals("true", cmd.getOptionValue("foo"));
        assertEquals("value", cmd.getOptionValue("bar"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        Options options = new Options();
        options.addOption("f", "foo", false, "foo option");

        parser.parse(options, new String[]{"-unknown"});
    }

    @Test
    public void testProcessOptionRequired() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());

        CommandLine cmd = parser.parse(options, new String[0]);
        parser.checkRequiredOptions();

        assertTrue(cmd.hasOption("f"));
    }

    @Test
    public void testProcessOptionGroup() throws ParseException {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("o", "orange", false, "orange option");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(group);

        CommandLine cmd = parser.parse(options, new String[]{"-a"});

        assertEquals(option1, group.getSelected());
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("o"));
    }
}