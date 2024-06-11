import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", false, "Test option");
        cmd.addOption(option);
        
        assertTrue(cmd.hasOption("t")); // Test if option is set
        assertTrue(!cmd.hasOption("x")); // Test if option is not set
    }
    
    @Test
    public void testHasOption_Char() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", false, "Test option");
        cmd.addOption(option);
        
        assertTrue(cmd.hasOption('t')); // Test if option is set
        assertTrue(!cmd.hasOption('x')); // Test if option is not set
    }
    
    @Test
    public void testGetOptionObject_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", true, "Test option");
        option.setType(Integer.class);
        option.setValue("123");
        cmd.addOption(option);
        
        assertEquals(123, cmd.getOptionObject("t")); // Test if option value is retrieved correctly as Integer object
        assertNull(cmd.getOptionObject("x")); // Test if option value is null when option is not set
    }
    
    @Test
    public void testGetOptionObject_Char() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", true, "Test option");
        option.setType(Integer.class);
        option.setValue("123");
        cmd.addOption(option);
        
        assertEquals(123, cmd.getOptionObject('t')); // Test if option value is retrieved correctly as Integer object
        assertNull(cmd.getOptionObject('x')); // Test if option value is null when option is not set
    }
    
    @Test
    public void testGetOptionValue_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", true, "Test option");
        option.setValue("value");
        cmd.addOption(option);
        
        assertEquals("value", cmd.getOptionValue("t")); // Test if option value is retrieved correctly
        assertNull(cmd.getOptionValue("x")); // Test if option value is null when option is not set
    }
    
    @Test
    public void testGetOptionValue_Char() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", true, "Test option");
        option.setValue("value");
        cmd.addOption(option);
        
        assertEquals("value", cmd.getOptionValue('t')); // Test if option value is retrieved correctly
        assertNull(cmd.getOptionValue('x')); // Test if option value is null when option is not set
    }
    
    @Test
    public void testGetOptionValues_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", true, "Test option");
        option.setValues(new String[] {"value1", "value2"});
        cmd.addOption(option);
        
        assertEquals("value1", cmd.getOptionValues("t")[0]); // Test if option values are retrieved correctly
        assertEquals("value2", cmd.getOptionValues("t")[1]);
        assertNull(cmd.getOptionValues("x")); // Test if option values are null when option is not set
    }
    
    @Test
    public void testGetOptionValues_Char() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", true, "Test option");
        option.setValues(new String[] {"value1", "value2"});
        cmd.addOption(option);
        
        assertEquals("value1", cmd.getOptionValues('t')[0]); // Test if option values are retrieved correctly
        assertEquals("value2", cmd.getOptionValues('t')[1]);
        assertNull(cmd.getOptionValues('x')); // Test if option values are null when option is not set
    }
    
    @Test
    public void testGetOptionValue_String_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", true, "Test option");
        option.setValue("value");
        cmd.addOption(option);
        
        assertEquals("value", cmd.getOptionValue("t", "default")); // Test if option value is retrieved correctly
        assertEquals("default", cmd.getOptionValue("x", "default")); // Test if default value is returned when option is not set
    }
    
    @Test
    public void testGetOptionValue_Char_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", true, "Test option");
        option.setValue("value");
        cmd.addOption(option);
        
        assertEquals("value", cmd.getOptionValue('t', "default")); // Test if option value is retrieved correctly
        assertEquals("default", cmd.getOptionValue('x', "default")); // Test if default value is returned when option is not set
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        assertEquals("arg1", cmd.getArgs()[0]); // Test if arguments are retrieved correctly
        assertEquals("arg2", cmd.getArgs()[1]);
    }
    
    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        assertEquals("arg1", cmd.getArgList().get(0)); // Test if arguments are retrieved correctly
        assertEquals("arg2", cmd.getArgList().get(1));
    }
    
    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        assertEquals("arg1", cmd.getArgs()[0]); // Test if argument is added correctly
        assertEquals("arg2", cmd.getArgs()[1]);
    }
    
    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "test", false, "Test option");
        cmd.addOption(option);
        
        assertTrue(cmd.hasOption("t")); // Test if option is added correctly
        assertEquals(option, cmd.getOptions()[0]);
    }
}