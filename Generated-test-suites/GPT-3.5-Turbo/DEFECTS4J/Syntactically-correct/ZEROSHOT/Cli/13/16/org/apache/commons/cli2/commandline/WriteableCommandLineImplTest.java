import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option testOption = OptionBuilder.withLongName("test")
                .withDescription("Test option")
                .create();
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(testOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = OptionBuilder.withLongName("test")
                .withDescription("Test option")
                .create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetValue() {
        Option argumentOption = ArgumentBuilder.withName("argument")
                .create();

        commandLine.addValue(argumentOption, "value1");
        commandLine.addValue(argumentOption, "value2");
        
        List<String> values = commandLine.getValues(argumentOption, new ArrayList<>());
        
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }
    
    @Test
    public void testGetSwitch() {
        Option switchOption = OptionBuilder.withLongName("switch")
                .withDescription("Test switch")
                .create();
        
        commandLine.addSwitch(switchOption, true);
        
        Boolean value = commandLine.getSwitch(switchOption, Boolean.FALSE);
        
        assertTrue(value);
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty("key", "value");
        String value = commandLine.getProperty("key", "defaultValue");
        assertEquals("value", value);
    }
    
    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("value"));
        assertTrue(commandLine.looksLikeOption("--option"));
    }
    
    @Test
    public void testToString() {
        Option argumentOption1 = ArgumentBuilder.withName("arg1")
                .create();
        Option argumentOption2 = ArgumentBuilder.withName("arg2")
                .create();
        
        List<String> arguments = new ArrayList<>();
        arguments.add("value1");
        arguments.add("value2");
        commandLine = new WriteableCommandLineImpl(argumentOption1, arguments);
        
        commandLine.addOption(argumentOption2);
        
        String result = commandLine.toString();
        assertEquals("value1 value2 --arg2", result);
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = OptionBuilder.withLongName("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = OptionBuilder.withLongName("option2")
                .withDescription("Option 2")
                .create();
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        List<Option> options = commandLine.getOptions();
        
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = OptionBuilder.withLongName("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = OptionBuilder.withLongName("option2")
                .withDescription("Option 2")
                .create();
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        Set<String> triggers = commandLine.getOptionTriggers();
        
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option argumentOption = ArgumentBuilder.withName("argument")
                .create();
        
        List<String> defaults = new ArrayList<>();
        defaults.add("defaultValue");
        
        commandLine.setDefaultValues(argumentOption, defaults);
        
        List<String> values = commandLine.getValues(argumentOption, new ArrayList<>());
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option switchOption = OptionBuilder.withLongName("switch")
                .withDescription("Test switch")
                .create();
        
        commandLine.setDefaultSwitch(switchOption, Boolean.TRUE);
        
        Boolean value = commandLine.getSwitch(switchOption, Boolean.FALSE);
        assertTrue(value);
    }
    
    @Test
    public void testGetNormalised() {
        Option argumentOption = ArgumentBuilder.withName("argument")
                .create();
        
        List<String> arguments = new ArrayList<>();
        arguments.add("value1");
        arguments.add("value2");
        commandLine = new WriteableCommandLineImpl(argumentOption, arguments);
        
        List<String> normalised = commandLine.getNormalised();
        
        assertEquals(2, normalised.size());
        assertEquals("value1", normalised.get(0));
        assertEquals("value2", normalised.get(1));
    }
}