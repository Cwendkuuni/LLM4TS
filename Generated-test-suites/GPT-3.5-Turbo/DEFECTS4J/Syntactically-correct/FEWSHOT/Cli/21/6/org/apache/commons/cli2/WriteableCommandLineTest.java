package org.apache.commons.cli2;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine cmdLine;

    @Before
    public void setUp() {
        cmdLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("-f", "file");
        cmdLine.addOption(option);

        assertTrue(cmdLine.hasOption("-f"));
        assertTrue(cmdLine.hasOption("file"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("-f", "file");
        cmdLine.addOption(option);

        cmdLine.addValue(option, "test.txt");

        List<String> values = cmdLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("-f", "file");
        cmdLine.addOption(option);

        cmdLine.addValue(option, "test.txt");
        cmdLine.addValue(option, "test2.txt");

        List<String> values = cmdLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("test.txt", values.get(0));
        assertEquals("test2.txt", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("-f", "file");
        cmdLine.addOption(option);

        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("default.txt");

        cmdLine.setDefaultValues(option, defaultValues);

        List<String> values = cmdLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("default.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("-q", "quiet");
        cmdLine.addOption(option);

        assertFalse(cmdLine.hasSwitch("-q"));

        cmdLine.addSwitch(option, true);

        assertTrue(cmdLine.hasSwitch("-q"));
        assertTrue(cmdLine.getSwitch("-q"));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("-q", "quiet");
        cmdLine.addOption(option);

        assertFalse(cmdLine.hasSwitch("-q"));

        cmdLine.setDefaultSwitch(option, true);

        assertTrue(cmdLine.hasSwitch("-q"));
        assertTrue(cmdLine.getSwitch("-q"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("-p", "property");
        cmdLine.addOption(option);

        cmdLine.addProperty(option, "name", "value");

        String value = cmdLine.getProperty(option, "name");
        assertEquals("value", value);
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        cmdLine.addProperty("name", "value");

        String value = cmdLine.getProperty("name");
        assertEquals("value", value);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(cmdLine.looksLikeOption("-f"));
        assertFalse(cmdLine.looksLikeOption("file"));
        assertFalse(cmdLine.looksLikeOption("test.txt"));
    }

}