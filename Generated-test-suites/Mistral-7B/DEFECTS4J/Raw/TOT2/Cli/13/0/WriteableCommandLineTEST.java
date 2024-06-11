package org.apache.commons.cli2.test;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLineParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = CommandLineBuilder.parse("--testOption");
        Option option = Option.builder("testOption").build();
        cmd.addOption(option);
        assertFalse(cmd.getOptions().isEmpty());
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("testOption"));
    }

    // Add test cases for other methods and edge cases here

    // ...

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = CommandLineBuilder.parse("");
        assertFalse(cmd.looksLikeOption("test"));
        assertTrue(cmd.looksLikeOption("--test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_AlreadyAdded() {
        WriteableCommandLine cmd = CommandLineBuilder.parse("");
        Option option = Option.builder("testSwitch").build();
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);
    }

    // Add test cases for other edge cases and exception handling scenarios here

    // ...

    @Test
    public void testGetValues() {
        WriteableCommandLine cmd = CommandLineBuilder.parse("--testOption value1 value2");
        Option option = Option.builder("testOption").build();
        List<String> values = cmd.getOptionValues("testOption");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    // Add test cases for other methods here

    // ...

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = CommandLineBuilder.parse("");
        Option option = Option.builder("testOption").build();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        cmd.setDefaultValues(option, defaultValues);
        List<String> values = cmd.getOptionValues("testOption");
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    // Add test cases for other methods here

    // ...

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = CommandLineBuilder.parse("");
        cmd.addProperty("testProperty", "testValue");
        assertEquals("testValue", cmd.getOptionValue("testProperty"));
    }

    // Add test cases for other methods here

    // ...

}