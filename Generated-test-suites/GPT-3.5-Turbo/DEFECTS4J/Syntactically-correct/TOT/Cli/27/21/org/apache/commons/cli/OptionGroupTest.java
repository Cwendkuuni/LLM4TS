
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;

import java.util.Collection;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "Test option");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("o", "option", false, "Test option 1");
        Option option2 = new Option("t", "test", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        assertEquals(2, names.size());
        assertTrue(names.contains("o"));
        assertTrue(names.contains("t"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o", "option", false, "Test option 1");
        Option option2 = new Option("t", "test", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("o", "option", false, "Test option 1");
        Option option2 = new Option("t", "test", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            assertEquals("o", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option2);
            assertEquals("t", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option1);
            fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // test passed
        }
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("o", "option", false, "Test option 1");
        optionGroup.addOption(option1);

        assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            assertEquals("o", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o", "option", false, "Test option 1");
        Option option2 = new Option("t", "test", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String groupToString = optionGroup.toString();
        String expectedString = "[-o Test option 1, -t Test option 2]";

        assertEquals(expectedString, groupToString);
    }
}
