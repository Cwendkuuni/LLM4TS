import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option option1 = new OptionBuilder().withName("option1").withDescription("Option 1").create();
        Option option2 = new OptionBuilder().withName("option2").withDescription("Option 2").create();
        Argument argument1 = new ArgumentBuilder().withName("argument1").withDescription("Argument 1").create();
        
        commandLine = new WriteableCommandLineImpl(new ArrayList<>(), option1);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addArgument(argument1);
    }

    @Test
    public void testAddOption() {
        Option option3 = new OptionBuilder().withName("option3").withDescription("Option 3").create();
        
        commandLine.addOption(option3);
        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        Option option1 = commandLine.getOption("option1");
        Option option2 = commandLine.getOption("option2");
        Argument argument1 = commandLine.getArgument("argument1");
        
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");
        
        assertEquals(2, commandLine.getValues(option1).size());
        assertEquals(1, commandLine.getValues(option2).size());
        assertEquals("value1", commandLine.getValues(option1).get(0));
        assertEquals("value2", commandLine.getValues(option1).get(1));
        assertEquals("value3", commandLine.getValues(option2).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = commandLine.getOption("option1");
        Option option2 = commandLine.getOption("option2");
        
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);
        
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option1));
        assertEquals(Boolean.FALSE, commandLine.getSwitch(option2));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("property1", "value1");
        
        assertEquals("value1", commandLine.getProperty("property1", "defaultValue"));
        assertEquals("defaultValue", commandLine.getProperty("property2", "defaultValue"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("argument1"));
    }

    @Test
    public void testToString() {
        Option option1 = commandLine.getOption("option1");
        Option option2 = commandLine.getOption("option2");
        Argument argument1 = commandLine.getArgument("argument1");
        
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");
        
        String expected = "option1-value1 value2 option2-value3";
        
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = commandLine.getOption("option1");
        Option option2 = commandLine.getOption("option2");
        
        List<Option> options = commandLine.getOptions();
        
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
        assertFalse(optionTriggers.contains("argument1"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = commandLine.getOption("option1");
        
        commandLine.setDefaultValues(option1, Arrays.asList("default1", "default2"));
        
        assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option1));
    }


    @Test
    public void testSetDefaultSwitch() {
        Option option1 = commandLine.getOption("option1");
        
        commandLine.setDefaultSwitch(option1, true);
        
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option1));
    }

    @Test
    public void testGetArgs() {
        List<String> args = commandLine.getArgs();
        
        assertEquals(1, args.size());
        assertEquals("argument1", args.get(0));
    }
}