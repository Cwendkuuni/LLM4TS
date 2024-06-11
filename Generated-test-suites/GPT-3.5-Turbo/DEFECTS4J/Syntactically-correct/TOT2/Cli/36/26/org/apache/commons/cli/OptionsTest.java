import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    // Test for Options addOptionGroup method
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "apple", false, "Apple option"));
        group.addOption(new Option("b", "banana", false, "Banana option"));

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    // Test for Options helpOptions method
    @Test
    public void testHelpOptions() {
        Options options = new Options();
        Option option1 = new Option("c", "cat", false, "Cat option");
        Option option2 = new Option("d", "dog", false, "Dog option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.helpOptions().size());
    }

    // Test for Options getOption method
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("e", "elephant", true, "Elephant option");

        options.addOption(option);

        assertEquals(option, options.getOption("e"));
    }

    // Test for Options getMatchingOptions method
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("f", "fish", false, "Fish option");
        Option option2 = new Option("g", "goat", false, "Goat option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("f").size());
    }

    // Test for Options hasOption method
    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("h", "horse", true, "Horse option");

        options.addOption(option);

        assertTrue(options.hasOption("h"));
    }

    // Test for Options hasLongOption method
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("i", "iguana", true, "Iguana option");

        options.addOption(option);

        assertTrue(options.hasLongOption("iguana"));
    }

    // Test for Options hasShortOption method
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("j", "jesus", true, "Jesus option");

        options.addOption(option);

        assertTrue(options.hasShortOption("j"));
    }

    // Test for Options getOptionGroup method
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("k", "kite", false, "Kite option"));
        group.addOption(new Option("l", "lion", false, "Lion option"));

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(group.getOptions().get(0)));
    }

    // Test for Options toString method
    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("m", "mouse", false, "Mouse option");
        Option option2 = new Option("n", "newt", false, "Newt option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {m=Option{ opt=M, longOpt=mouse, hasArg=false, description=Mouse option }, n=Option{ opt=N, longOpt=newt, hasArg=false, description=Newt option } } ] [ long {mouse=Option{ opt=M, longOpt=mouse, hasArg=false, description=Mouse option }, newt=Option{ opt=N, longOpt=newt, hasArg=false, description=Newt option } } ]", options.toString());
    }
}