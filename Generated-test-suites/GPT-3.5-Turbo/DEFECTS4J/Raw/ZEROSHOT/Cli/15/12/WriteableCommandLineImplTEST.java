
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option rootOption;
    private List arguments;
    
    @Before
    public void setUp() {
        rootOption = new Option("root");
        arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }
    
    // TODO: Write test methods to comprehensively test all methods in WriteableCommandLineImpl
    
    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        commandLine.addValue(argument, "testValue");
        List values = commandLine.getValues(argument, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("testValue", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        assertNull(commandLine.getOption("testOption"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("testOption"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, commandLine.getValues(option, Collections.emptyList()));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        Argument argument = new Argument();
        commandLine.addOption(option);
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        String property = "testProperty";
        assertNull(commandLine.getProperty(property));
        commandLine.addProperty(property, "testValue");
        assertEquals("testValue", commandLine.getProperty(property));
    }
    
    @Test
    public void testAddProperty() {
        String property = "testProperty";
        commandLine.addProperty(property, "testValue");
        assertEquals("testValue", commandLine.getProperty(property));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("testOption");
        String property = "testProperty";
        commandLine.addProperty(option, property, "testValue");
        assertEquals(1, commandLine.getProperties(option).size());
        assertTrue(commandLine.getProperties(option).contains(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "--testOption";
        assertFalse(commandLine.looksLikeOption(trigger));
        rootOption.addPrefix("--");
        assertTrue(commandLine.looksLikeOption(trigger));
    }
    
    @Test
    public void testToString() {
        String argument1 = "arg1";
        String argument2 = "arg2";
        arguments.add(argument1);
        arguments.add(argument2);
        assertEquals(argument1 + " " + argument2, commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("testOption"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, Collections.emptyList()));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetNormalised() {
        String argument1 = "arg1";
        String argument2 = "arg2";
        arguments.add(argument1);
        arguments.add(argument2);
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertTrue(normalised.contains(argument1));
        assertTrue(normalised.contains(argument2));
    }
}
