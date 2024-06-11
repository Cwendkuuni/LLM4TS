import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionsTest {
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", true, "description for option b");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", false, "description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(Collections.singletonList(option1), options.getOptions());
        assertEquals(Collections.singletonList(option1), options.helpOptions());
        assertEquals(Collections.emptyList(), options.getRequiredOptions());
        assertNull(options.getOption("c"));
    }

    @Test
    public void testAddOptionWithRequired() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", false, "description for option b");
        option1.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(Collections.singletonList(option1), options.getOptions());
        assertEquals(Collections.singletonList(option1), options.helpOptions());
        assertEquals(Collections.singletonList(option1), options.getRequiredOptions());
        assertNull(options.getOption("c"));
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", false, "description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("apple"));
        assertEquals(option2, options.getOption("banana"));
        assertEquals(Collections.singletonList(option1), options.getOptions());
        assertEquals(Collections.singletonList(option1), options.helpOptions());
        assertEquals(Collections.emptyList(), options.getRequiredOptions());
        assertNull(options.getOption("c"));
    }

    @Test
    public void testAddOptionWithLongOptWithRequired() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", false, "description for option b");
        option1.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("apple"));
        assertEquals(option2, options.getOption("banana"));
        assertEquals(Collections.singletonList(option1), options.getOptions());
        assertEquals(Collections.singletonList(option1), options.helpOptions());
        assertEquals(Collections.singletonList(option1), options.getRequiredOptions());
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", false, "description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(Collections.singletonList("apple"), options.getMatchingOptions("app"));
        assertEquals(Collections.singletonList("banana"), options.getMatchingOptions("ban"));
        assertEquals(Collections.emptyList(), options.getMatchingOptions("car"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", false, "description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(true, options.hasOption("a"));
        assertEquals(true, options.hasOption("apple"));
        assertEquals(true, options.hasOption("b"));
        assertEquals(true, options.hasOption("banana"));
        assertEquals(false, options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", false, "description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(true, options.hasLongOption("apple"));
        assertEquals(true, options.hasLongOption("banana"));
        assertEquals(false, options.hasLongOption("orange"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", false, "description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(true, options.hasShortOption("a"));
        assertEquals(true, options.hasShortOption("b"));
        assertEquals(false, options.hasShortOption("c"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description for option a");
        Option option2 = new Option("b", "banana", false, "description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {a=apple, b=banana} ] [ long {apple=Option [ argName=null, numberOfArgs=-1, optionalArg=false, required=false, longOpt=apple, description=description for option a ]}, {banana=Option [ argName=null, numberOfArgs=-1, optionalArg=false, required=false, longOpt=banana, description=description for option b ]}]",
                options.toString());
    }
}