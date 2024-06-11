import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        cmd.addOption(options.getOption("a"));
        
        assertFalse(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("a"));
    }
    
    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        cmd.addOption(options.getOption("a"));
        
        assertEquals(null, cmd.getOptionObject("a"));
        assertEquals(null, cmd.getOptionObject("b"));
    }
    
    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option a");
        cmd.addOption(options.getOption("a"));
        
        assertEquals(null, cmd.getOptionValue("a"));
        assertEquals(null, cmd.getOptionValue("b"));
    }
    
    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option a");
        cmd.addOption(options.getOption("a"));
        
        assertEquals(null, cmd.getOptionValues("a"));
        assertEquals(null, cmd.getOptionValues("b"));
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
        
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option a");
        
        cmd.addOption(options.getOption("a"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option a");
        
        cmd.addOption(options.getOption("a"));
        
        assertTrue(cmd.iterator().hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option a");
        
        cmd.addOption(options.getOption("a"));
        
        assertEquals(1, cmd.getOptions().length);
    }

}