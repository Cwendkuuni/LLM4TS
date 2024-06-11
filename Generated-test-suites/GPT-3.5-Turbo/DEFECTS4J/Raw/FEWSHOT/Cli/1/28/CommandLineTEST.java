import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;

public class CommandLineTest {
    private CommandLine cmd;
    
    @Before
    public void setup() {
        cmd = new CommandLine();
    }
    
    @Test
    public void testHasOptionWithString() {
        assertFalse(cmd.hasOption("f"));  
    }
    
    @Test
    public void testHasOptionWithChar() {
        assertFalse(cmd.hasOption('f'));  
    }
    
    @Test
    public void testGetOptionObjectWithString() {
        assertNull(cmd.getOptionObject("f"));  
    }
    
    @Test
    public void testGetOptionObjectWithChar() {
        assertNull(cmd.getOptionObject('f'));  
    }
    
    @Test
    public void testGetOptionValueWithString() {
        assertNull(cmd.getOptionValue("f"));
    }
    
    @Test
    public void testGetOptionValueWithChar() {
        assertNull(cmd.getOptionValue('f'));
    }
    
    @Test
    public void testGetOptionValuesWithString() {
        assertNull(cmd.getOptionValues("f"));
    }
    
    @Test
    public void testGetOptionValuesWithChar() {
        assertNull(cmd.getOptionValues('f'));
    }
    
    @Test
    public void testGetOptionValueWithDefaultString() {
        assertEquals("default", cmd.getOptionValue("f", "default"));
    }
    
    @Test
    public void testGetOptionValueWithDefaultChar() {
        assertEquals("default", cmd.getOptionValue('f', "default"));
    }
    
    @Test
    public void testGetArgs() {
        String[] expected = {};
        assertArrayEquals(expected, cmd.getArgs());
    }
    
    @Test
    public void testGetArgList() {
        assertEquals(0, cmd.getArgList().size());
    }
    
    @Test
    public void testAddArg() {
        cmd.addArg("arg1");
        String[] expected = {"arg1"};
        assertArrayEquals(expected, cmd.getArgs());
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("f");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("f"));
    }
    
    @Test
    public void testIterator() {
        Option option1 = new Option("f");
        Option option2 = new Option("b");
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        Iterator<Option> itr = cmd.iterator();
        assertEquals(option1, itr.next());
        assertEquals(option2, itr.next());
        assertFalse(itr.hasNext());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("f");
        Option option2 = new Option("b");
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        Option[] expected = {option1, option2};
        assertArrayEquals(expected, cmd.getOptions());
    }
}