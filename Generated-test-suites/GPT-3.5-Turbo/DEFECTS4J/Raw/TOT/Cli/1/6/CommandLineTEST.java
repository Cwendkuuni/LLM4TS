import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {
    
    private CommandLine commandLine;
    
    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }
    
    @Test
    public void testHasOption_WhenOptionIsSet_ShouldReturnTrue() {
        Option option = new Option("a", "test option");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption("a"));
    }
    
    @Test
    public void testHasOption_WhenOptionIsNotSet_ShouldReturnFalse() {
        assertFalse(commandLine.hasOption("b"));
    }
    
    @Test
    public void testHasOption_WhenOptionIsSetWithChar_ShouldReturnTrue() {
        Option option = new Option('c', "test option");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption('c'));
    }
    
    @Test
    public void testHasOption_WhenOptionIsNotSetWithChar_ShouldReturnFalse() {
        assertFalse(commandLine.hasOption('d'));
    }
    
    @Test
    public void testGetOptionObject_WhenOptionIsSet_ShouldReturnValue() {
        Option option = new Option("e", "test option", true, "String");
        option.addValue("value");
        commandLine.addOption(option);
        
        assertEquals("value", commandLine.getOptionObject("e"));
    }
    
    @Test
    public void testGetOptionObject_WhenOptionIsNotSet_ShouldReturnNull() {
        assertNull(commandLine.getOptionObject("f"));
    }
    
    @Test
    public void testGetOptionObject_WhenOptionIsSetWithChar_ShouldReturnValue() {
        Option option = new Option('g', "test option", true, "Integer");
        option.addValue("10");
        commandLine.addOption(option);
        
        assertEquals(10, commandLine.getOptionObject('g'));
    }
    
    @Test
    public void testGetOptionObject_WhenOptionIsNotSetWithChar_ShouldReturnNull() {
        assertNull(commandLine.getOptionObject('h'));
    }
    
    @Test
    public void testGetOptionValue_WhenOptionIsSet_ShouldReturnValue() {
        Option option = new Option("i", "test option", true, "String");
        option.addValue("value");
        commandLine.addOption(option);
        
        assertEquals("value", commandLine.getOptionValue("i"));
    }
    
    @Test
    public void testGetOptionValue_WhenOptionIsNotSet_ShouldReturnNull() {
        assertNull(commandLine.getOptionValue("j"));
    }
    
    @Test
    public void testGetOptionValue_WhenOptionIsSetWithChar_ShouldReturnValue() {
        Option option = new Option('k', "test option", true, "Integer");
        option.addValue("10");
        commandLine.addOption(option);
        
        assertEquals("10", commandLine.getOptionValue('k'));
    }
    
    @Test
    public void testGetOptionValue_WhenOptionIsNotSetWithChar_ShouldReturnNull() {
        assertNull(commandLine.getOptionValue('l'));
    }
    
    @Test
    public void testGetOptionValues_WhenOptionIsSet_ShouldReturnValues() {
        Option option = new Option("m", "test option", true, "String");
        option.addValue("value1");
        option.addValue("value2");
        commandLine.addOption(option);
        
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("m"));
    }
    
    @Test
    public void testGetOptionValues_WhenOptionIsNotSet_ShouldReturnNull() {
        assertNull(commandLine.getOptionValues("n"));
    }
    
    @Test
    public void testGetOptionValues_WhenOptionIsSetWithChar_ShouldReturnValues() {
        Option option = new Option('o', "test option", true, "Integer");
        option.addValue("10");
        option.addValue("20");
        commandLine.addOption(option);
        
        assertArrayEquals(new String[]{"10", "20"}, commandLine.getOptionValues('o'));
    }
    
    @Test
    public void testGetOptionValues_WhenOptionIsNotSetWithChar_ShouldReturnNull() {
        assertNull(commandLine.getOptionValues('p'));
    }
    
    @Test
    public void testGetOptionValue_WhenOptionIsSetWithDefaultValue_ShouldReturnValue() {
        Option option = new Option("q", "test option", true, "String");
        option.addValue("value");
        commandLine.addOption(option);
        
        assertEquals("value", commandLine.getOptionValue("q", "default"));
    }
    
    @Test
    public void testGetOptionValue_WhenOptionIsNotSetWithDefaultValue_ShouldReturnDefaultValue() {
        assertEquals("default", commandLine.getOptionValue("r", "default"));
    }
    
    @Test
    public void testGetOptionValue_WhenOptionIsSetWithCharAndDefaultValue_ShouldReturnValue() {
        Option option = new Option('s', "test option", true, "Integer");
        option.addValue("10");
        commandLine.addOption(option);
        
        assertEquals("10", commandLine.getOptionValue('s', "default"));
    }
    
    @Test
    public void testGetOptionValue_WhenOptionIsNotSetWithCharAndDefaultValue_ShouldReturnDefaultValue() {
        assertEquals("default", commandLine.getOptionValue('t', "default"));
    }
    
    @Test
    public void testGetArgs_WhenArgsAreSet_ShouldReturnArgs() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }
    
    @Test
    public void testGetArgs_WhenNoArgsAreSet_ShouldReturnEmptyArray() {
        assertArrayEquals(new String[]{}, commandLine.getArgs());
    }
    
    @Test
    public void testGetArgList_WhenArgsAreSet_ShouldReturnList() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        assertEquals(2, commandLine.getArgList().size());
        assertTrue(commandLine.getArgList().contains("arg1"));
        assertTrue(commandLine.getArgList().contains("arg2"));
    }
    
    @Test
    public void testGetArgList_WhenNoArgsAreSet_ShouldReturnEmptyList() {
        assertTrue(commandLine.getArgList().isEmpty());
    }
    
    @Test
    public void testAddArg_ShouldAddArgToList() {
        commandLine.addArg("arg");
        
        assertEquals(1, commandLine.getArgList().size());
        assertTrue(commandLine.getArgList().contains("arg"));
    }
    
    @Test
    public void testAddOption_ShouldAddOptionToMap() {
        Option option = new Option("u", "test option");
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptions().length);
        assertEquals(option, commandLine.getOptions()[0]);
    }
    
}