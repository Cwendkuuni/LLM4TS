import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLine;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertFalse(commandLine.hasOption("opt"));
    }
    
    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertFalse(commandLine.hasOption('o'));
    }
    
    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertNull(commandLine.getOptionObject("opt"));
    }
    
    @Test
    public void testGetOptionObjectChar() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertNull(commandLine.getOptionObject('o'));
    }
    
    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertNull(commandLine.getOptionValue("opt"));
    }
    
    @Test
    public void testGetOptionValueChar() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertNull(commandLine.getOptionValue('o'));
    }
    
    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertNull(commandLine.getOptionValues("opt"));
    }
    
    @Test
    public void testGetOptionValuesChar() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertNull(commandLine.getOptionValues('o'));
    }
    
    @Test
    public void testGetOptionValueDefaultValue() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertEquals("default", commandLine.getOptionValue("opt", "default"));
    }
    
    @Test
    public void testGetOptionValueCharDefaultValue() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertEquals("default", commandLine.getOptionValue('o', "default"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertArrayEquals(new String[]{}, commandLine.getArgs());
    }
    
    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        
        Assert.assertEquals(new java.util.LinkedList(), commandLine.getArgList());
    }
    
    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        
        commandLine.addArg("arg");
        
        Assert.assertArrayEquals(new String[]{"arg"}, commandLine.getArgs());
    }
    
    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        
        Option option = new Option("opt", "option", true, "Option");
        commandLine.addOption(option);
        
        Assert.assertEquals(option, commandLine.getOptions()[0]);
    }

}