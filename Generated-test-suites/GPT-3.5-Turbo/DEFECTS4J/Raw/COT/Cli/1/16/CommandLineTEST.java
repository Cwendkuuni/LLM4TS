import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "apple"));
        assertTrue(cmd.hasOption("apple"));
    }

    @Test
    public void testHasOption_Char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "apple"));
        assertTrue(cmd.hasOption('a'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        cmd.addOption(new Option("n", "name", true, "name of the file"));
        cmd.addOption(new Option("v", "verbose"));
        cmd.addOption(new Option("r", "recursive"));
        
        cmd.setOptionValue("name", "test.txt");
        assertEquals("test.txt", cmd.getOptionObject("name"));
    }

    @Test
    public void testGetOptionObject_Char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        cmd.addOption(new Option("n", "name", true, "name of the file"));
        cmd.addOption(new Option("v", "verbose"));
        cmd.addOption(new Option("r", "recursive"));
        
        cmd.setOptionValue("name", "test.txt");
        assertEquals("test.txt", cmd.getOptionObject('n'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        cmd.addOption(new Option("n", "name", true, "name of the file"));
        cmd.addOption(new Option("v", "verbose"));
        cmd.addOption(new Option("r", "recursive"));
        
        cmd.setOptionValue("name", "test.txt");
        assertEquals("test.txt", cmd.getOptionValue("name"));
    }

    @Test
    public void testGetOptionValue_Char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        cmd.addOption(new Option("n", "name", true, "name of the file"));
        cmd.addOption(new Option("v", "verbose"));
        cmd.addOption(new Option("r", "recursive"));
        
        cmd.setOptionValue("name", "test.txt");
        assertEquals("test.txt", cmd.getOptionValue('n'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("c", "count", true, "number of items"));
        
        String[] values = {"1", "2", "3"};
        cmd.setOptionValues("count", values);
        
        assertArrayEquals(values, cmd.getOptionValues("count"));
    }

    @Test
    public void testGetOptionValues_Char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("c", "count", true, "number of items"));
        
        String[] values = {"1", "2", "3"};
        cmd.setOptionValues("count", values);
        
        assertArrayEquals(values, cmd.getOptionValues('c'));
    }

    @Test
    public void testGetOptionValue_DefaultValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        cmd.addOption(new Option("n", "name", true, "name of the file"));
        cmd.addOption(new Option("v", "verbose"));
        cmd.addOption(new Option("r", "recursive"));
        
        assertEquals("default.txt", cmd.getOptionValue("name", "default.txt"));
    }

    @Test
    public void testGetOptionValue_Char_DefaultValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        cmd.addOption(new Option("n", "name", true, "name of the file"));
        cmd.addOption(new Option("v", "verbose"));
        cmd.addOption(new Option("r", "recursive"));
        
        assertEquals("default.txt", cmd.getOptionValue('n', "default.txt"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, cmd.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("a", "apple");
        Option option2 = new Option("b", "banana");
        
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        assertEquals(option1, cmd.getOptions()[0]);
        assertEquals(option2, cmd.getOptions()[1]);
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("a", "apple");
        Option option2 = new Option("b", "banana");
        
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        Iterator iterator = cmd.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("a", "apple");
        Option option2 = new Option("b", "banana");
        
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        Option[] expected = {option1, option2};
        assertArrayEquals(expected, cmd.getOptions());
    }
}