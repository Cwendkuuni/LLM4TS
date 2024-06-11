import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

public class CommandLineTest {
    
    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("f"));
        
        Option option1 = new Option("f");
        cmd.addOption(option1);
        assertTrue(cmd.hasOption("f"));
        
        Option option2 = new Option("g");
        cmd.addOption(option2);
        assertTrue(cmd.hasOption("g"));
    }
    
    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        
        Option option1 = new Option("f");
        option1.setType(Integer.class);
        option1.setValue("123");
        cmd.addOption(option1);
        assertEquals(123, cmd.getOptionObject("f"));
        
        Option option2 = new Option("g");
        option2.setType(Double.class);
        option2.setValue("3.14");
        cmd.addOption(option2);
        assertEquals(3.14, cmd.getOptionObject("g"));
    }
    
    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        
        Option option1 = new Option("f");
        option1.setValue("foo");
        cmd.addOption(option1);
        assertEquals("foo", cmd.getOptionValue("f"));
        
        Option option2 = new Option("g");
        option2.setValue("bar");
        cmd.addOption(option2);
        assertEquals("bar", cmd.getOptionValue("g"));
    }
    
    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        
        Option option1 = new Option("f");
        option1.setValues(new String[]{"foo", "bar"});
        cmd.addOption(option1);
        assertArrayEquals(new String[]{"foo", "bar"}, cmd.getOptionValues("f"));
        
        Option option2 = new Option("g");
        option2.setValues(new String[]{"baz"});
        cmd.addOption(option2);
        assertArrayEquals(new String[]{"baz"}, cmd.getOptionValues("g"));
    }
    
    @Test
    public void testGetOptionValueWithDefaultValue() {
        CommandLine cmd = new CommandLine();
        
        Option option1 = new Option("f");
        cmd.addOption(option1);
        assertEquals("default", cmd.getOptionValue("f", "default"));
        
        Option option2 = new Option("g");
        cmd.addOption(option2);
        assertNull(cmd.getOptionValue("g", null));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }
    
    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertEquals(2, cmd.getArgList().size());
        assertTrue(cmd.getArgList().contains("arg1"));
        assertTrue(cmd.getArgList().contains("arg2"));
    }
    
    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        
        Option option1 = new Option("f");
        cmd.addOption(option1);
        
        Option option2 = new Option("g");
        cmd.addOption(option2);
        
        int count = 0;
        for (Iterator<Option> it = cmd.iterator(); it.hasNext();) {
            Option option = it.next();
            assertNotNull(option);
            count++;
        }
        assertEquals(2, count);
    }
    
    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        
        Option option1 = new Option("f");
        cmd.addOption(option1);
        
        Option option2 = new Option("g");
        cmd.addOption(option2);
        
        Option[] options = cmd.getOptions();
        assertEquals(2, options.length);
        assertTrue(options[0].equals(option1) || options[0].equals(option2));
        assertTrue(options[1].equals(option1) || options[1].equals(option2));
    }
}