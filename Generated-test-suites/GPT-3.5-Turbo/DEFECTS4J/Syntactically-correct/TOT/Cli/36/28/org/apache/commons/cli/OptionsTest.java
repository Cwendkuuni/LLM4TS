import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", true, "Add command");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("add"));
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("add"));
    }

    @Test
    public void testAddOption_InvalidOption() {
        Option option = new Option("c", "count", false, "Count command");
        options.addOption(option);

        assertFalse(options.hasOption("d"));
        assertNull(options.getOption("d"));
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "add", true, "Add command");
        Option option2 = new Option("d", "delete", true, "Delete command");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("d"));
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOptionGroup_RequiredOption() {
        Option option1 = new Option("a", "add", true, "Add command");
        Option option2 = new Option("d", "delete", true, "Delete command");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setRequired(true);
        options.addOptionGroup(group);

        assertTrue(options.getRequiredOptions().contains(group));

        options.addOption(option1);
        assertFalse(options.getRequiredOptions().contains(group));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "add", true, "Add command");
        Option option2 = new Option("d", "delete", true, "Delete command");
        
        options.addOption(option1);
        options.addOption(option2);

        List<String> matchingOptions1 = options.getMatchingOptions("a");
        List<String> matchingOptions2 = options.getMatchingOptions("d");
        List<String> matchingOptions3 = options.getMatchingOptions("x");

        assertEquals(1, matchingOptions1.size());
        assertTrue(matchingOptions1.contains("add"));

        assertEquals(1, matchingOptions2.size());
        assertTrue(matchingOptions2.contains("delete"));

        assertEquals(0, matchingOptions3.size());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", true, "Add command");
        Option option2 = new Option("d", "delete", true, "Delete command");
        
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option[a=add, description='Add command', required=false, hasArg=true]}, {d=Option[d=delete, description='Delete command', required=false, hasArg=true]} ] [ long {} ]";
        assertEquals(expected, options.toString());
    }

}