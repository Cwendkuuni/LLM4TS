import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    
    @Before
    public void setup() {
    	option1 = new Option("o1", "Option1", false, "Option 1");
    	option2 = new Option("o2", "Option2", true, "Option 2");
    	
    	List arguments = new ArrayList();
    	arguments.add("arg1");
    	arguments.add("arg2");
    	
        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }
    
    @Test
    public void testAddOption() {
    	Option option3 = new Option("o3", "Option3", false, "Option 3");
    	commandLine.addOption(option3);
    	
    	assertTrue(commandLine.hasOption(option3));
    }
    
    @Test
    public void testAddValue() {
    	Argument argument1 = new Argument();
    	argument1.setValue("value1");
    	
    	commandLine.addValue(argument1, "value1");
    	
    	assertEquals(1, commandLine.getValues(argument1, null).size());
    }
    
    @Test
    public void testAddSwitch() {
    	commandLine.addSwitch(option1, true);
    	
    	assertTrue(commandLine.getSwitch(option1, false));
    }
    
    @Test
    public void testHasOption() {
    	assertTrue(commandLine.hasOption(option1));
    	assertFalse(commandLine.hasOption(option2));
    }
    
    @Test
    public void testOptionTrigger() {
    	assertTrue(commandLine.looksLikeOption("-o1"));
    	assertFalse(commandLine.looksLikeOption("arg1"));
    }
    
    @Test
    public void testGetProperty() {
    	String propertyValue = commandLine.getProperty("property");
    	assertNull(propertyValue);
    }
    
    @Test
    public void testAddProperty() {
    	commandLine.addProperty(option1, "property", "value");
    	
    	assertEquals("value", commandLine.getProperty(option1, "property", null));
    }
    
    @Test
    public void testGetProperties() {
    	Properties properties = new Properties();
    	properties.setProperty("property1", "value1");
    	option1.addProperty("property1", "Property 1");
    	commandLine.addProperty(option1, "property1", "value1");
    	
    	Set<String> propertyNames = commandLine.getProperties(option1);
    	assertEquals(1, propertyNames.size());
    	assertEquals("property1", propertyNames.iterator().next());
    }
    
    @Test
    public void testGetNormalised() {
    	List normalised = commandLine.getNormalised();
    	
    	assertEquals(2, normalised.size());
    	assertEquals("arg1", normalised.get(0));
    	assertEquals("arg2", normalised.get(1));
    }
    
    @Test
    public void testGetOptions() {
    	List options = commandLine.getOptions();
    	
    	assertEquals(1, options.size());
    	assertEquals(option1, options.get(0));
    }
    
    @Test
    public void testGetOptionTriggers() {
    	Set<String> optionTriggers = commandLine.getOptionTriggers();
    	
    	assertEquals(1, optionTriggers.size());
    	assertTrue(optionTriggers.contains("o1"));
    }
    
    @Test
    public void testSetDefaultValues() {
    	Argument argument1 = new Argument();
    	argument1.setValue("default1");
    	List values = new ArrayList();
    	values.add("default1");
    	
    	commandLine.setDefaultValues(argument1, values);
    	
    	assertEquals(values, commandLine.getValues(argument1, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
    	commandLine.setDefaultSwitch(option1, true);
    	
    	assertTrue(commandLine.getSwitch(option1, false));
    }
    
    @Test
    public void testGetValues() {
    	List values = commandLine.getValues(option2, null);
    	assertEquals(0, values.size());
    	
    	List defaultValues = new ArrayList();
    	defaultValues.add("value1");
    	commandLine.setDefaultValues(option2, defaultValues);
    	
    	values = commandLine.getValues(option2, null);
    	assertEquals(1, values.size());
    	assertEquals("value1", values.get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
    	List values = commandLine.getUndefaultedValues(option2);
    	assertEquals(0, values.size());
    	
    	List defaultValues = new ArrayList();
    	defaultValues.add("value1");
    	commandLine.setDefaultValues(option2, defaultValues);
    	
    	values = commandLine.getUndefaultedValues(option2);
    	assertEquals(0, values.size());
    }
    
    @Test
    public void testGetSwitch() {
    	Boolean switchValue = commandLine.getSwitch(option2, false);
    	assertFalse(switchValue);
    	
    	commandLine.setDefaultSwitch(option2, true);
    	
    	switchValue = commandLine.getSwitch(option2, false);
    	assertTrue(switchValue);
    }
    
    @Test
    public void testToString() {
    	String expected = "arg1 arg2";
    	String result = commandLine.toString();
    	
    	assertEquals(expected, result);
    }

}