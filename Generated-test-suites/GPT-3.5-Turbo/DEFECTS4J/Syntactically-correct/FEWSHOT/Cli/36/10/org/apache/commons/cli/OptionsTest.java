import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup optionGroup1 = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "option 1");
        Option option2 = new Option("b", "bbb", false, "option 2");

        optionGroup1.addOption(option1);
        optionGroup1.addOption(option2);

        options.addOptionGroup(optionGroup1);

        List<OptionGroup> optionGroups = new ArrayList<>();
        optionGroups.add(optionGroup1);

        Collection<OptionGroup> result = options.getOptionGroups();
        assertEquals(optionGroups, result);
    }

    @Test
    public void testAddOptionShortName() {
        Options options = new Options();

        options.addOption("a", "option a");

        Option expectedOption = new Option("a", null, false, "option a");

        Collection<Option> result = options.getOptions();
        assertEquals(1, result.size());
        assertTrue(result.contains(expectedOption));
    }

    @Test
    public void testAddOptionShortNameWithArgument() {
        Options options = new Options();

        options.addOption("a", true, "option a");

        Option expectedOption = new Option("a", null, true, "option a");

        Collection<Option> result = options.getOptions();
        assertEquals(1, result.size());
        assertTrue(result.contains(expectedOption));
    }

    @Test
    public void testAddOptionShortAndLongName() {
        Options options = new Options();

        options.addOption("a", "aaa", false, "option a");

        Option expectedOption = new Option("a", "aaa", false, "option a");

        Collection<Option> result = options.getOptions();
        assertEquals(1, result.size());
        assertTrue(result.contains(expectedOption));
    }

    @Test
    public void testGetOptionByShortName() {
        Options options = new Options();

        Option option1 = new Option("a", "aaa", false, "option a");
        Option option2 = new Option("b", "bbb", false, "option b");

        options.addOption(option1);
        options.addOption(option2);

        Option result = options.getOption("a");
        assertEquals(option1, result);
    }

    @Test
    public void testGetOptionByLongName() {
        Options options = new Options();

        Option option1 = new Option("a", "aaa", false, "option a");
        Option option2 = new Option("b", "bbb", false, "option b");

        options.addOption(option1);
        options.addOption(option2);

        Option result = options.getOption("aaa");
        assertEquals(option1, result);
    }

    @Test
    public void testHasOptionByShortName() {
        Options options = new Options();

        Option option1 = new Option("a", "aaa", false, "option a");
        Option option2 = new Option("b", "bbb", false, "option b");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasOptionByLongName() {
        Options options = new Options();

        Option option1 = new Option("a", "aaa", false, "option a");
        Option option2 = new Option("b", "bbb", false, "option b");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("aaa"));
        assertFalse(options.hasOption("ccc"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();

        Option option1 = new Option("a", "aaa", false, "option a");
        Option option2 = new Option("b", "bbb", false, "option b");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();

        Option option1 = new Option("a", "aaa", false, "option a");
        Option option2 = new Option("b", "bbb", false, "option b");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("aaa"));
        assertFalse(options.hasLongOption("ccc"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();

        OptionGroup optionGroup1 = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "option 1");
        Option option2 = new Option("b", "bbb", false, "option 2");

        optionGroup1.addOption(option1);
        optionGroup1.addOption(option2);

        options.addOptionGroup(optionGroup1);

        assertEquals(optionGroup1, options.getOptionGroup(option1));
        assertNull(options.getOptionGroup(new Option("c", "ccc", false, "option 3")));
    }
}