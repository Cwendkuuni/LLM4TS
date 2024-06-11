package org.apache.commons.cli2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = new Option("a", "option-a");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("a"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = new Option("a", "option-a");
        commandLine.addOption(option);
        commandLine.addValue(option, "value-1");
        List values = commandLine.getValues(option);
        assertEquals( Arrays.asList("value-1"), values );
        commandLine.addValue(option, "value-2");
        values = commandLine.getValues(option);
        assertEquals( Arrays.asList("value-1", "value-2"), values );
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = new Option("a", "option-a");
        commandLine.addOption(option);
        commandLine.addValue(option, "value-1");
        commandLine.addValue(option, "value-2");
        List values = commandLine.getValues(option);
        assertEquals( Arrays.asList("value-1", "value-2"), values );
        values = commandLine.getValues(new Option("b", "option-b"));
        assertNull(values);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = new Option("a", "option-a");
        List defaultValues = new ArrayList();
        defaultValues.add("default-1");
        defaultValues.add("default-2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option);
        assertEquals( defaultValues, values );
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = new Option("a", "option-a");
        commandLine.addOption(option);
        try {
            commandLine.addSwitch(option, true);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            //Expected
        }

        Option option2 = new Option("b", "option-b");
        commandLine.addOption(option2);
        commandLine.addSwitch(option2, false);
        assertFalse(commandLine.getOption("b").isSet());

        commandLine.addSwitch(option2, true);
        assertTrue(commandLine.getOption("b").isSet());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = new Option("a", "option-a");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getOption("a").isSet());

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getOption("a").isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = createMockCommandLine();
        commandLine.addProperty("property-1", "value-1");
        assertEquals("value-1", commandLine.getOptionValue("property-1"));

        commandLine.addProperty("property-2", "value-2");
        assertEquals("value-2", commandLine.getOptionValue("property-2"));

        assertEquals(null, commandLine.getOptionValue("property-3"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        assertTrue(commandLine.looksLikeOption("--option-a"));
        assertTrue(commandLine.looksLikeOption("-a"));
        assertTrue(commandLine.looksLikeOption("--no-option-b"));
        assertTrue(commandLine.looksLikeOption("-b"));
        assertFalse(commandLine.looksLikeOption("option-c"));
        assertFalse(commandLine.looksLikeOption("--"));
        assertFalse(commandLine.looksLikeOption(""));
    }

    private WriteableCommandLine createMockCommandLine() {
        return new MockWriteableCommandLine();
    }
}