import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "add", false, "Add an item");
        option2 = new Option("u", "update", false, "Update an item");
        option3 = new Option("d", "delete", false, "Delete an item");
    }

    @Test
    public void testAddOption() {
        assertEquals(optionGroup, optionGroup.addOption(option1));
        assertEquals(optionGroup, optionGroup.addOption(option2));
        assertEquals(optionGroup, optionGroup.addOption(option3));
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<String> names = optionGroup.getNames();

        assertTrue(names.contains("a"));
        assertTrue(names.contains("u"));
        assertTrue(names.contains("d"));
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<Option> options = optionGroup.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testSetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Exception not expected");
        }

        assertEquals("a", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            fail("Exception expected");
        } catch (AlreadySelectedException e) {
            assertEquals("Option '-u' was already selected in this group.", e.getMessage());
        }
    }

    @Test
    public void testGetSelected() {
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("u", optionGroup.getSelected());
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
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals("[-a Add an item, -u Update an item, -d Delete an item]", optionGroup.toString());
    }
}