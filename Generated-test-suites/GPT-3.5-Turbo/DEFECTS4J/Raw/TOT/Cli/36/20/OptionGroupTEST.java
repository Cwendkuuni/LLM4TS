import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

import java.util.Collection;
import java.util.stream.Collectors;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1", false, "Description for option1");
        Option option2 = new Option("b", "option2", true, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        Collection<Option> options = optionGroup.getOptions();

        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));

        assertEquals(2, names.size());
        assertEquals(2, options.size());
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option1", false, "Description for option1");
        Option option2 = new Option("b", "option2", true, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }

        assertEquals("a", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            assertEquals(optionGroup, e.getOptionGroup());
            assertEquals(option2, e.getOption());
        }

        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() {
        assertNull(optionGroup.getSelected());

        Option option1 = new Option("a", "option1", false, "Description for option1");

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Description for option1");
        Option option2 = new Option("b", "option2", true, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedString = "[-a Description for option1, --b Description for option2]";

        assertEquals(expectedString, optionGroup.toString());
    }
}