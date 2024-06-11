import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        // Test adding option with only short name
        options.addOption("f","force",false,"Force the action");
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("force"));
        
        // Test adding option with short name and argument
        options.addOption("o","output",true,"Specify the output file");
        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("output"));
        assertTrue(options.getOption("o").hasArg());
        
        // Test adding option with short name, long name, and argument
        options.addOption("v","verbose",true,"Enable verbose output");
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasOption("verbose"));
        assertTrue(options.getOption("v").hasArg());
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup()
                            .addOption(new Option("a","add",false,"Add an item"))
                            .addOption(new Option("d","delete",false,"Delete an item"))
                            .addOption(new Option("e","edit",false,"Edit an item"));
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("e"));
        
        assertEquals(group, options.getOptionGroup(options.getOption("a")));
        assertEquals(group, options.getOptionGroup(options.getOption("d")));
        assertEquals(group, options.getOptionGroup(options.getOption("e")));
    }

    @Test
    public void testGetOptions() {
        options.addOption("f","force",false,"Force the action");
        options.addOption("o","output",true,"Specify the output file");
        
        assertEquals(2, options.getOptions().size());
        
        assertTrue(options.getOptions().contains(options.getOption("f")));
        assertTrue(options.getOptions().contains(options.getOption("o")));
    }

    @Test
    public void testGetRequiredOptions() {
        OptionGroup group = new OptionGroup()
                            .addOption(new Option("a","add",false,"Add an item"))
                            .addOption(new Option("d","delete",false,"Delete an item"))
                            .addOption(new Option("e","edit",false,"Edit an item"))
                            .setRequired(true);
        options.addOptionGroup(group);
        
        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(group));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("f","force",false,"Force the action");
        options.addOption(option1);
        
        assertEquals(option1, options.getOption("f"));
        assertEquals(option1, options.getOption("force"));
        
        Option option2 = new Option("o","output",true,"Specify the output file");
        options.addOption(option2);
        
        assertEquals(option2, options.getOption("o"));
        assertEquals(option2, options.getOption("output"));
    }

    @Test
    public void testGetMatchingOptions() {
        options.addOption("f","force",false,"Force the action");
        options.addOption("o","output",true,"Specify the output file");
        options.addOption("v","verbose",true,"Enable verbose output");
        
        assertEquals(1, options.getMatchingOptions("f").size());
        assertTrue(options.getMatchingOptions("f").contains("force"));
        
        assertEquals(2, options.getMatchingOptions("o").size());
        assertTrue(options.getMatchingOptions("o").contains("output"));
        assertTrue(options.getMatchingOptions("o").contains("output"));
        
        assertEquals(1, options.getMatchingOptions("v").size());
        assertTrue(options.getMatchingOptions("v").contains("verbose"));
    }

    @Test
    public void testHasOption() {
        options.addOption("f","force",false,"Force the action");
        options.addOption("o","output",true,"Specify the output file");
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("force"));
        
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("output"));
    }

    @Test
    public void testHasLongOption() {
        options.addOption("f","force",false,"Force the action");
        options.addOption("o","output",true,"Specify the output file");
        
        assertFalse(options.hasLongOption("f"));
        assertFalse(options.hasLongOption("force"));
        
        assertFalse(options.hasLongOption("o"));
        assertFalse(options.hasLongOption("output"));
    }

    @Test
    public void testHasShortOption() {
        options.addOption("f","force",false,"Force the action");
        options.addOption("o","output",true,"Specify the output file");
        
        assertTrue(options.hasShortOption("f"));
        assertTrue(options.hasShortOption("force"));
        
        assertTrue(options.hasShortOption("o"));
        assertTrue(options.hasShortOption("output"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup()
                            .addOption(new Option("a","add",false,"Add an item"))
                            .addOption(new Option("d","delete",false,"Delete an item"))
                            .addOption(new Option("e","edit",false,"Edit an item"));
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(options.getOption("a")));
        assertEquals(group, options.getOptionGroup(options.getOption("d")));
        assertEquals(group, options.getOptionGroup(options.getOption("e")));
    }
}