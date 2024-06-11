import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroups().contains(optionGroup));
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("r"));
    }

    @Test
    public void testAddOption_withoutArgument() {
        options.addOption("a", "add", false, "add option");

        Option option = options.getOption("a");

        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertEquals("add", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("add option", option.getDescription());
    }

    @Test
    public void testAddOption_withArgument() {
        options.addOption("f", "file", true, "file option");

        Option option = options.getOption("f");

        assertNotNull(option);
        assertEquals("f", option.getOpt());
        assertEquals("file", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("file option", option.getDescription());
    }

    @Test
    public void testAddOptionGroup_withRequiredOption() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setRequired(true);

        options.addOptionGroup(optionGroup);

        assertTrue(options.getRequiredOptions().contains(optionGroup));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", true, "add option");

        options.addOption(option);

        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", true, "add option");
        Option option2 = new Option("f", "file", true, "file option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "add", true, "add option");

        options.addOption(option);

        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("add", true, "add option");
        Option option2 = new Option("adept", true, "adept option");
        Option option3 = new Option("file", true, "file option");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("ad").size());
        assertEquals(option1.getOpt(), options.getMatchingOptions("ad").get(0));
        assertEquals(option2.getOpt(), options.getMatchingOptions("ad").get(1));
        assertEquals(0, options.getMatchingOptions("fi").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "add", true, "add option");

        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("add"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "add", true, "add option");

        options.addOption(option);

        assertTrue(options.hasLongOption("add"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "add", true, "add option");

        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup_withOptionBelongingToGroup() {
        Option option1 = new Option("a", "add", false, "add option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);

        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
    }

    @Test
    public void testGetOptionGroup_withoutOptionBelongingToGroup() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);

        options.addOptionGroup(optionGroup);

        assertNull(options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option{opt='a', longOpt='add', arg='null', description='add option', required=false}} ] [ long {add=Option{opt='a', longOpt='add', arg='null', description='add option', required=false}}, remove=Option{opt='r', longOpt='remove', arg='null', description='remove option', required=false}} ]]";

        assertEquals(expected, options.toString());
    }
}