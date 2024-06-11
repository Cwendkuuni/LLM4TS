package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "test1", false, "Testing option 1");
        Option option2 = new Option("b", "test2", true, "Testing option 2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);
        
        assertEquals(2, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("c", "test3", false, "Testing option 3");

        options.addOption(option);

        assertTrue(options.hasOption("c"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("d", "test4", false, "Testing option 4");

        options.addOption(option);

        assertNotNull(options.getOption("d"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("e", "test5", false, "Testing option 5");
        Option option2 = new Option("f", "testing", false, "Test option 6");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("t").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("g", "test6", false, "Testing option 6");
        options.addOption(option);

        assertTrue(options.hasOption("g"));
    }
    
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("h", "test7", false, "Testing option 7");
        options.addOption(option);

        assertTrue(options.hasLongOption("test7"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("i", "test8", false, "Testing option 8");
        options.addOption(option);

        assertTrue(options.hasShortOption("i"));
    }
}