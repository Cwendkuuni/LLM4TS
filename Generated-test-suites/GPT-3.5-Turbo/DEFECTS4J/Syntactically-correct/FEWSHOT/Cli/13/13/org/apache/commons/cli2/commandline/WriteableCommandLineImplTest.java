
package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WriteableCommandLineImplTest {

    WriteableCommandLineImpl commandLine;
    Option option1;
    Option option2;
    Option option3;
    Argument argument1;
    Argument argument2;
    Argument argument3;

    @Before
    public void setUp() {
        option1 = new Option("--option1");
        option2 = new Option("--option2");
        option3 = new Option("--option3");

        argument1 = new Argument();
        argument2 = new Argument();
        argument3 = new Argument();

        commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
    }

    @Test
    public void testAddOption() {
        assertFalse(commandLine.hasOption(option1));

        commandLine.addOption(option1);
        
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testAddValue() {
        List<Object> expectedValues = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));

        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument1, "value2");
        commandLine.addValue(argument1, "value3");

        assertEquals(expectedValues, commandLine.getValues(argument1, Collections.emptyList()));
    }

    @Test
    public void testAddSwitch() {
        assertFalse(commandLine.getSwitch(option1, false));
        
        commandLine.addSwitch(option1, true);
        
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option1));
        
        commandLine.addOption(option1);
        
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("--unknownOption"));
        
        commandLine.addOption(option1);
        
        assertEquals(option1, commandLine.getOption("--option1"));
    }

    @Test
    public void testGetValues() {
        List<Object> expectedValues = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));

        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument1, "value2");
        commandLine.addValue(argument1, "value3");

        assertEquals(expectedValues, commandLine.getValues(argument1, Collections.emptyList()));
    }

    @Test
    public void testGetSwitch() {
        assertFalse(commandLine.getSwitch(option1, false));
        
        commandLine.addSwitch(option1, true);
        
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testAddProperty() {
        assertNull(commandLine.getProperty("property1", "defaultValue"));

        commandLine.addProperty("property1", "value1");

        assertEquals("value1", commandLine.getProperty("property1", "defaultValue"));
    }

    @Test
    public void testGetProperty() {
        assertEquals("defaultValue", commandLine.getProperty("property1", "defaultValue"));
        
        commandLine.addProperty("property1", "value1");
        
        assertEquals("value1", commandLine.getProperty("property1", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals(new HashSet<String>(Arrays.asList("property1", "property2")),
                commandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option1"));
        assertTrue(commandLine.looksLikeOption("--option2"));
        assertFalse(commandLine.looksLikeOption("value1"));
        assertFalse(commandLine.looksLikeOption("value2"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");

        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(new HashSet<String>(Arrays.asList("--option1", "--option2")),
                commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> expectedValues = new ArrayList<>(Arrays.asList("defaultValue1", "defaultValue2"));

        assertNull(commandLine.getValues(option1, Collections.emptyList()));
        
        commandLine.setDefaultValues(option1, expectedValues);
        
        assertEquals(expectedValues, commandLine.getValues(option1, Collections.emptyList()));
    }

    @Test
    public void testSetDefaultSwitch() {
        assertNull(commandLine.getSwitch(option1, null));
        
        commandLine.setDefaultSwitch(option1, true);
        
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> expectedValues = new ArrayList<>(Arrays.asList("value1", "value2"));

        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");

        assertEquals(expectedValues, commandLine.getNormalised());
    }

}
