import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionsTest {

    Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "option1");
        Option option2 = new Option("b", "option2", false, "option2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(1, optionGroups.size());
        assertEquals(group, optionGroups.iterator().next());
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", false, "option1");
        Option option2 = new Option("b", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        Collection<Option> optionList = options.getOptions();
        assertEquals(2, optionList.size());
        assertTrue(optionList.contains(option1));
        assertTrue(optionList.contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", false, "option1");
        Option option2 = new Option("b", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", false, "option1");
        Option option2 = new Option("abc", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOpts = options.getMatchingOptions("a");
        assertEquals(1, matchingOpts.size());
        assertEquals("a", matchingOpts.get(0));
        matchingOpts = options.getMatchingOptions("ab");
        assertEquals(1, matchingOpts.size());
        assertEquals("abc", matchingOpts.get(0));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", false, "option1");
        Option option2 = new Option("b", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "option1", false, "option1");
        Option option2 = new Option("b", "option2", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.hasLongOption("option1"));
        assertTrue(options.hasLongOption("option2"));
        assertFalse(options.hasLongOption("option3"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "option1", false, "option1");
        Option option2 = new Option("b", "option2", false, "option2");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("c"));
    }
}