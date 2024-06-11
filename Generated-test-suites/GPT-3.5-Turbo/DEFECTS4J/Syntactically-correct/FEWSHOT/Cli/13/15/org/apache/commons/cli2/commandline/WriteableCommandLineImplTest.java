package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl cmd;

    @Before
    public void setUp() {
        Option option1 = new Option("", "", true, "");
        Option option2 = new Option("", "", false, "");
        cmd = new WriteableCommandLineImpl(option1, new ArrayList<>());
        cmd.addOption(option2);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("", "", true, "");
        cmd.addOption(option3);

        assertTrue(cmd.getOptions().contains(option3));
    }

    @Test
    public void testAddValue() {
        Option option4 = new Option("", "", true, "");
        cmd.addOption(option4);
        cmd.addValue(option4, "value1");

        assertTrue(cmd.getValues(option4, new ArrayList<>()).contains("value1"));
    }

    @Test
    public void testAddSwitch() {
        Option option5 = new Option("", "", false, "");
        cmd.addOption(option5);
        cmd.addSwitch(option5, true);

        assertTrue(cmd.getSwitch(option5, false));
    }

    @Test
    public void testHasOption() {
        Option option6 = new Option("", "", true, "");

        cmd.addOption(option6);

        assertTrue(cmd.hasOption(option6));
    }

    @Test
    public void testGetOption() {
        Option option7 = new Option("", "", true, "");
        
        cmd.addOption(option7);

        assertEquals(option7, cmd.getOption(""));
    }

    @Test
    public void testGetValues() {
        Option option8 = new Option("", "", true, "");
        
        cmd.addOption(option8);
        cmd.addValue(option8, "value1");
        cmd.addValue(option8, "value2");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        assertEquals(expectedValues, cmd.getValues(option8, new ArrayList<>()));
    }

    @Test
    public void testGetSwitch() {
        Option option9 = new Option("", "", false, "");
        
        cmd.addOption(option9);
        cmd.addSwitch(option9, true);

        assertTrue(cmd.getSwitch(option9, false));
    }

    @Test
    public void testAddProperty() {
        cmd.addProperty("property1", "value1");

        assertEquals("value1", cmd.getProperty("property1", ""));
    }

    @Test
    public void testGetProperty() {
        cmd.addProperty("property2", "value2");

        assertEquals("value2", cmd.getProperty("property2", ""));
    }

    @Test
    public void testGetProperties() {
        cmd.addProperty("property3", "value3");
        cmd.addProperty("property4", "value4");

        Set<String> expectedProperties = new HashSet<>();
        expectedProperties.add("property3");
        expectedProperties.add("property4");

        assertEquals(expectedProperties, cmd.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(cmd.looksLikeOption("option1"));
    }

    @Test
    public void testToString() {
        cmd.addValue(new Argument(), "arg1");
        cmd.addValue(new Argument(), "arg2");

        assertEquals("arg1 arg2", cmd.toString());
    }

    @Test
    public void testGetOptions() {
        Option option10 = new Option("", "", true, "");
        
        cmd.addOption(option10);

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option2);
        expectedOptions.add(option10);

        assertEquals(expectedOptions, cmd.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option11 = new Option("", "", true, "");

        cmd.addOption(option11);

        Set<String> expectedTriggers = new HashSet<>();
        expectedTriggers.add("");
        expectedTriggers.add("");

        assertEquals(expectedTriggers, cmd.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option12 = new Option("", "", true, "");

        cmd.addOption(option12);
        cmd.setDefaultValues(option12, new ArrayList<>());

        assertTrue(cmd.getValues(option12, new ArrayList<>()).isEmpty());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option13 = new Option("", "", false, "");

        cmd.addOption(option13);
        cmd.setDefaultSwitch(option13, false);

        assertFalse(cmd.getSwitch(option13, true));
    }

    @Test
    public void testGetNormalised() {
        List<String> expectedNormalised = new ArrayList<>();
        expectedNormalised.add("arg1");
        expectedNormalised.add("arg2");

        cmd.addValue(new Argument(), "arg1");
        cmd.addValue(new Argument(), "arg2");

        assertEquals(expectedNormalised, cmd.getNormalised());
    }
}