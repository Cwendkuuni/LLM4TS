package org.apache.commons.cli2.test;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = new Option("input", true, "i");
        commandLine.addOption(option);
        assertArrayEquals(new Option[]{option}, commandLine.getOptions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionThrowsExceptionWhenInterfaceIsImplemented() {
        // This test case is not applicable as the WriteableCommandLine is an interface
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = new Option("input", true, "i");
        commandLine.addOption(option);
        commandLine.addValue(option, "file.txt");
        assertArrayEquals(new Object[]{"file.txt"}, commandLine.getOptionValues("input"));
    }

    @Test
    public void testGetOptionValuesEmpty() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        assertNull(commandLine.getOptionValues("optionName"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = new Option("input", true, "i");
        commandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        assertArrayEquals(new Object[]{"defaultValue1", "defaultValue2"}, commandLine.getOptionValues("input"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSwitchThrowsExceptionWhenOptionIsNotPresent() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = new Option("input", true, "i");
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = new Option("input", true, "i");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = new Option("input", true, "i");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        commandLine.addProperty("propertyName", "propertyValue");
        assertEquals("propertyValue", commandLine.getProperty("propertyName"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        commandLine.addOption(new Option("option", true, "o"));
        assertTrue(commandLine.looksLikeOption("o"));
    }

}