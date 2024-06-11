import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl cmdLine;
    
    @Before
    public void setUp() {
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        Option option3 = new Option("o3", "option3");
        
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        
        cmdLine = new WriteableCommandLineImpl(option1, arguments);
        
        cmdLine.addOption(option2);
        cmdLine.addOption(option3);
    }
    
    @Test
    public void testAddOption() {
        Option option4 = new Option("o4", "option4");
        cmdLine.addOption(option4);
        
        assertTrue(cmdLine.hasOption(option4));
    }
    
    @Test
    public void testAddValue() {
        Option argumentOption = new Argument();
        ArgumentList argumentList = new ArgumentList();
        
        argumentOption.setValueSeparator(',');
        
        cmdLine.addOption(argumentOption);
        cmdLine.addValue(argumentOption, "value1,value2");
        
        argumentList.add("value1");
        argumentList.add("value2");
        
        assertEquals(argumentList, cmdLine.getUndefaultedValues(argumentOption));
    }
    
    @Test
    public void testAddSwitch() {
        Option option5 = new Option("o5", "option5");
        
        cmdLine.addSwitch(option5, true);
        
        assertTrue(cmdLine.getSwitch(option5, false));
    }
    
    @Test
    public void testHasOption() {
        Option option6 = new Option("o6", "option6");
        
        assertFalse(cmdLine.hasOption(option6));
    }
    
    @Test
    public void testGetOption() {
        Option option7 = new Option("o7", "option7");
        
        assertNull(cmdLine.getOption("o7"));
        
        cmdLine.addOption(option7);
        
        assertEquals(option7, cmdLine.getOption("o7"));
    }
    
    @Test
    public void testGetValues() {
        Option option8 = new Option("o8", "option8");
        
        cmdLine.addValue(option8, "value1");
        cmdLine.addValue(option8, "value2");
        
        List<String> expectedValues = Arrays.asList("value1", "value2");
        
        assertEquals(expectedValues, cmdLine.getValues(option8, null));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option9 = new Option("o9", "option9");
        
        cmdLine.addValue(option9, "value1");
        cmdLine.addValue(option9, "value2");
        
        List<String> expectedValues = Arrays.asList("value1", "value2");
        
        assertEquals(expectedValues, cmdLine.getUndefaultedValues(option9));
    }
    
    @Test
    public void testGetSwitch() {
        Option option10 = new Option("o10", "option10");
        
        assertFalse(cmdLine.getSwitch(option10, false));
        
        cmdLine.addSwitch(option10, true);
        
        assertTrue(cmdLine.getSwitch(option10, false));
    }
    
    @Test
    public void testGetProperty() {
        assertNull(cmdLine.getProperty("property"));
        
        Option propertyOption = new PropertyOption();
        
        cmdLine.addProperty(propertyOption, "property", "value");
        
        assertEquals("value", cmdLine.getProperty(propertyOption, "property", null));
    }
    
    @Test
    public void testAddProperty() {
        Option propertyOption = new PropertyOption();
        
        cmdLine.addProperty(propertyOption, "property", "value");
        
        assertTrue(cmdLine.getProperties().contains("property"));
    }
    
    @Test
    public void testGetProperties() {
        Option propertyOption = new PropertyOption();
        
        cmdLine.addProperty(propertyOption, "property1", "value1");
        cmdLine.addProperty(propertyOption, "property2", "value2");
        
        Set<String> expectedProperties = Set.of("property1", "property2");
        
        assertEquals(expectedProperties, cmdLine.getProperties(propertyOption));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(cmdLine.looksLikeOption("-o1"));
        assertFalse(cmdLine.looksLikeOption("arg1"));
    }
    
    @Test
    public void testToString() {
        assertEquals("arg1 arg2", cmdLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        List<Option> expectedOptions = Arrays.asList(
            new Option("o1", "option1"),
            new Option("o2", "option2"),
            new Option("o3", "option3")
        );
        
        assertEquals(expectedOptions, cmdLine.getOptions());
    }
    
    @Test
    public void testGetOptionTriggers() {
        Set<String> expectedTriggers = Set.of("o1", "o2", "o3");
        
        assertEquals(expectedTriggers, cmdLine.getOptionTriggers());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option11 = new Option("o11", "option11");
        
        List<String> defaultValues = Arrays.asList("value1", "value2");
        
        cmdLine.setDefaultValues(option11, defaultValues);
        
        assertEquals(defaultValues, cmdLine.getValues(option11, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option12 = new Option("o12", "option12");
        
        cmdLine.setDefaultSwitch(option12, true);
        
        assertTrue(cmdLine.getSwitch(option12, false));
    }
    
    @Test
    public void testGetNormalised() {
        List<String> expectedNormalised = Arrays.asList("arg1", "arg2");
        
        assertEquals(expectedNormalised, cmdLine.getNormalised());
    }
}