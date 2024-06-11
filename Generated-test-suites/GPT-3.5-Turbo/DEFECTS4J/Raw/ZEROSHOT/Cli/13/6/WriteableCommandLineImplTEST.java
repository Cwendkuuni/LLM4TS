import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    
    @Before
    public void setUp() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option2);
    }
    
    @Test
    public void testAddOption() {
        Option option3 = new OptionBuilder().withPreferredName("option3").create();
        commandLine.addOption(option3);
        
        assertTrue(commandLine.hasOption(option3));
        assertEquals(option3, commandLine.getOption("option3"));
    }
    
    @Test
    public void testAddValue() {
        Option option4 = new OptionBuilder().withPreferredName("option4").create();
        commandLine.addOption(option4);
        
        commandLine.addValue(option4, "value1");
        commandLine.addValue(option4, "value2");
        
        List values = commandLine.getValues(option4, new ArrayList());
        
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }
    
    @Test
    public void testAddSwitch() {
        Option option5 = new OptionBuilder().withPreferredName("option5").create();
        commandLine.addOption(option5);
        
        commandLine.addSwitch(option5, true);
        
        assertTrue(commandLine.getSwitch(option5, false));
    }
    
    @Test
    public void testHasOption() {
        Option option6 = new OptionBuilder().withPreferredName("option6").create();
        commandLine.addOption(option6);
        
        assertTrue(commandLine.hasOption(option6));
    }
    
    @Test
    public void testGetOption() {
        Option option7 = new OptionBuilder().withPreferredName("option7").create();
        commandLine.addOption(option7);
        
        assertEquals(option7, commandLine.getOption("option7"));
    }
    
    @Test
    public void testGetValues() {
        Option option8 = new OptionBuilder().withPreferredName("option8").create();
        commandLine.addOption(option8);
        
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        commandLine.setDefaultValues(option8, defaultValues);
        
        List values = commandLine.getValues(option8, new ArrayList());
        
        assertEquals(defaultValues, values);
    }
    
    @Test
    public void testGetSwitch() {
        Option option9 = new OptionBuilder().withPreferredName("option9").create();
        commandLine.addOption(option9);
        
        commandLine.addSwitch(option9, true);
        
        assertTrue(commandLine.getSwitch(option9, false));
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        
        assertEquals("value1", commandLine.getProperty("property1", ""));
    }
    
    @Test
    public void testGetProperty() {
        commandLine.addProperty("property2", "value2");
        
        assertEquals("value2", commandLine.getProperty("property2", ""));
    }
    
    @Test
    public void testGetProperties() {
        commandLine.addProperty("property3", "value3");
        commandLine.addProperty("property4", "value4");
        
        assertEquals(2, commandLine.getProperties().size());
        assertTrue(commandLine.getProperties().contains("property3"));
        assertTrue(commandLine.getProperties().contains("property4"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("value"));
        assertTrue(commandLine.looksLikeOption("--option1"));
        assertTrue(commandLine.looksLikeOption("-o2"));
    }
    
    @Test
    public void testToString() {
        commandLine.addOption(new OptionBuilder().withPreferredName("option10").create());
        commandLine.addOption(new OptionBuilder().withPreferredName("option11").create());
        
        commandLine.addValue(commandLine.getOption("option10"), "value10");
        
        assertEquals("option10=value10", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(commandLine.getOption("option1")));
        assertTrue(commandLine.getOptions().contains(commandLine.getOption("option2")));
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertEquals(3, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option12 = new OptionBuilder().withPreferredName("option12").create();
        commandLine.addOption(option12);
        
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        commandLine.setDefaultValues(option12, defaultValues);
        
        assertEquals(defaultValues, commandLine.getValues(option12, new ArrayList()));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option13 = new OptionBuilder().withPreferredName("option13").create();
        commandLine.addOption(option13);
        
        commandLine.setDefaultSwitch(option13, true);
        
        assertTrue(commandLine.getSwitch(option13, false));
    }
    
    @Test
    public void testGetNormalised() {
        commandLine.addOption(new OptionBuilder().withPreferredName("option14").create());
        commandLine.addOption(new OptionBuilder().withPreferredName("option15").create());
        
        List normalised = new ArrayList();
        
        normalised.add("arg1");
        normalised.add("arg2");
        normalised.add("arg3");
        
        assertEquals(normalised, commandLine.getNormalised());
    }
}