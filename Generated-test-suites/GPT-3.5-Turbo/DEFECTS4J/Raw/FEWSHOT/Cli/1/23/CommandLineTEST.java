import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {
    
    @Test
    public void testHasOptionWithValidShortName() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        
        assertTrue(cmd.hasOption("f"));
    }
    
    @Test
    public void testHasOptionWithInvalidShortName() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        
        assertFalse(cmd.hasOption("o"));
    }
    
    @Test
    public void testHasOptionWithValidLongName() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        
        assertTrue(cmd.hasOption("file"));
    }
    
    @Test
    public void testHasOptionWithInvalidLongName() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        
        assertFalse(cmd.hasOption("output"));
    }
    
    @Test
    public void testHasOptionWithValidCharName() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        
        assertTrue(cmd.hasOption('f'));
    }
    
    @Test
    public void testHasOptionWithInvalidCharName() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        
        assertFalse(cmd.hasOption('o'));
    }
    
    @Test
    public void testGetOptionObjectWithValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file").setType(String.class));
        cmd.addOptionValue("f", "test.txt");
        
        assertEquals("test.txt", (String) cmd.getOptionObject("f"));
    }
    
    @Test
    public void testGetOptionObjectWithoutValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file").setType(String.class));
        
        assertNull(cmd.getOptionObject("f"));
    }
    
    @Test
    public void testGetOptionValueWithStringValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file").setType(String.class));
        cmd.addOptionValue("f", "test.txt");
        
        assertEquals("test.txt", cmd.getOptionValue("f"));
    }
    
    @Test
    public void testGetOptionValueWithNullValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file").setType(String.class));
        
        assertNull(cmd.getOptionValue("f"));
    }
    
    @Test
    public void testGetOptionValuesWithValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file").setType(String[].class));
        cmd.addOptionValues("f", new String[] {"test1.txt", "test2.txt"});
        
        assertArrayEquals(new String[] {"test1.txt", "test2.txt"}, cmd.getOptionValues("f"));
    }
    
    @Test
    public void testGetOptionValuesWithoutValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file").setType(String[].class));
        
        assertNull(cmd.getOptionValues("f"));
    }
    
    @Test
    public void testGetOptionValueWithDefaultValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file").setType(String.class));
        
        assertEquals("default.txt", cmd.getOptionValue("f", "default.txt"));
    }
    
    @Test
    public void testGetOptionValueWithNullDefaultValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file").setType(String.class));
        
        assertNull(cmd.getOptionValue("f", null));
    }
    
    @Test
    public void testGetArgsWithValues() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        assertArrayEquals(new String[] {"arg1", "arg2"}, cmd.getArgs());
    }
    
    @Test
    public void testGetArgsWithoutValues() {
        CommandLine cmd = new CommandLine();
        
        assertArrayEquals(new String[] {}, cmd.getArgs());
    }
    
    @Test
    public void testGetArgListWithValues() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        assertEquals(2, cmd.getArgList().size());
    }
    
    @Test
    public void testGetArgListWithoutValues() {
        CommandLine cmd = new CommandLine();
        
        assertEquals(0, cmd.getArgList().size());
    }
    
    @Test
    public void testIteratorWithValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        cmd.addOption(new Option("o", "output"));
        
        assertEquals(2, cmd.iterator().size());
    }
    
    @Test
    public void testIteratorWithoutValues() {
        CommandLine cmd = new CommandLine();
        
        assertEquals(0, cmd.iterator().size());
    }
    
    @Test
    public void testGetOptionsWithValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file"));
        cmd.addOption(new Option("o", "output"));
        
        assertEquals(2, cmd.getOptions().length);
    }
    
    @Test
    public void testGetOptionsWithoutValues() {
        CommandLine cmd = new CommandLine();
        
        assertEquals(0, cmd.getOptions().length);
    }
}