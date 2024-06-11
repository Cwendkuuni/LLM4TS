import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OptionsTest {
    @Test
    public void addOptionTest() {
        Options options = new Options();

        Option opt = new Option("a", "add", false, "add an entry");
        options.addOption(opt);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("add"));
    }

    @Test
    public void addOptionGroupTest() {
        Options options = new Options();

        Option option1 = new Option("a", "add", false, "add an entry");
        Option option2 = new Option("d", "delete", false, "delete an entry");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasLongOption("add"));
        assertTrue(options.hasLongOption("delete"));
    }

    @Test
    public void getOptionTest() {
        Options options = new Options();

        Option option1 = new Option("a", "add", false, "add an entry");
        Option option2 = new Option("d", "delete", false, "delete an entry");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("add"));
        assertEquals(option2, options.getOption("d"));
        assertEquals(option2, options.getOption("delete"));
    }

    @Test
    public void hasOptionTest() {
        Options options = new Options();

        Option option = new Option("a", "add", false, "add an entry");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("add"));
        assertFalse(options.hasOption("delete"));
    }

    @Test
    public void getMatchingOptionsTest() {
        Options options = new Options();

        Option option1 = new Option("a", "add", false, "add an entry");
        Option option2 = new Option("d", "delete", false, "delete an entry");
        Option option3 = new Option("e", null, false, "edit an entry");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(option2, options.getMatchingOptions("d").get(0));
        assertEquals(option3, options.getMatchingOptions("e").get(0));
    }

    @Test
    public void toStringTest() {
        Options options = new Options();

        Option option1 = new Option("a", "add", false, "add an entry");
        Option option2 = new Option("d", "delete", false, "delete an entry");

        options.addOption(option1);
        options.addOption(option2);

        assertNotNull(options.toString());
    }
}