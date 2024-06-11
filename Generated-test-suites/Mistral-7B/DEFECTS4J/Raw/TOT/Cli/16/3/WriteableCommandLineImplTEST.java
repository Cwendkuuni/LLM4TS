package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.ParseResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Test case for WriteableCommandLineImpl class.
 */
public class WriteableCommandLineImplTest {

    private CommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        Option exampleOption = new Option("e", "example", true, "Example value");
        Option anotherOption = new Option("a", "another", false, "This is another option");
        Option propertyOption = new Option("p", "property", true, "Property value")
                .withLongOpt("--property")
                .withValues(new String[]{"key", "value"});

        options.addOption(exampleOption);
        options.addOption(propertyOption);
        options.addOption(anotherOption);
    }

    /**
     * Test that WriteableCommandLineImpl can parse command line arguments correctly.
     */
    @Test
    public void testParseCommandLine() {
        String[] args = {"-e", "value1", "--property=key=value1", "--another", "-p", "key2=value2"};
        ParseResult parseResult = options.parse(args);
        commandLine = new WriteableCommandLineImpl(options.getOption("example"), parseResult.getArgs());

        // Check options
        Assert.assertTrue(commandLine.hasOption(exampleOption));
        Assert.assertEquals("value1", commandLine.getValue(exampleOption, null));

        Assert.assertTrue(commandLine.hasOption(propertyOption));
        Map<String, String> properties = commandLine.getProperties(propertyOption);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value1", properties.get("key"));

        Assert.assertFalse(commandLine.hasOption(anotherOption));

        // Check switches
        Assert.assertFalse(commandLine.getSwitch(anotherOption, false));
        Assert.assertTrue(commandLine.getSwitch(exampleOption, false));
    }

    /**
     * Test that WriteableCommandLineImpl can normalize command line arguments.
     */
    @Test
    public void testNormalizeCommandLine() {
        String[] args = {"--example=value1", "--property=key=value1", "another", "-p=key2=value2"};
        ParseResult parseResult = options.parse(args);
        commandLine = new WriteableCommandLineImpl(options.getOption("example"), parseResult.getArgs());

        List<String> normalized = commandLine.getNormalised();
        Assert.assertEquals(4, normalized.size());
        Assert.assertEquals("--example=value1", normalized.get(0));
        Assert.assertEquals("--property=key=value1", normalized.get(1));
        Assert.assertEquals("another", normalized.get(2));
        Assert.assertEquals("-p=key2=value2", normalized.get(3));
    }
}