
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;

import java.util.Collection;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", true, "Option A");
        optionGroup.addOption(option);

        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("a"));
        assertEquals(1, names.size());
    }

    @Test
    public void testGetNames() {
        Collection<String> names = optionGroup.getNames();
        assertNotNull(names);
        assertTrue(names.isEmpty());

        Option option = new Option("a", true, "Option A");
        optionGroup.addOption(option);
        names = optionGroup.getNames();
        assertTrue(names.contains("a"));
        assertEquals(1, names.size());
    }

    @Test
    public void testGetOptions() {
        Collection<Option> options = optionGroup.getOptions();
        assertNotNull(options);
        assertTrue(options.isEmpty());

        Option option = new Option("a", true, "Option A");
        optionGroup.addOption(option);
        options = optionGroup.getOptions();
        assertTrue(options.contains(option));
        assertEquals(1, options.size());
    }

    @Test
    public void testSetSelected() {
        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", true, "Option B");
        Option optionC = new Option("c", true, "Option C");

        try {
            optionGroup.setSelected(optionA);
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception");
        }
        assertEquals("a", optionGroup.getSelected());

        try {
            optionGroup.setSelected(optionB);
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception");
        }
        assertEquals("b", optionGroup.getSelected());

        try {
            optionGroup.setSelected(optionC);
            fail("Expected exception not thrown");
        } catch (AlreadySelectedException e) {
            assertEquals(optionGroup, e.getOptionGroup());
            assertEquals(optionC, e.getOption());
        }
        assertEquals("b", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() {
        assertNull(optionGroup.getSelected());

        Option option = new Option("a", true, "Option A");
        try {
            optionGroup.setSelected(option);
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception");
        }
        assertEquals("a", optionGroup.getSelected());

        option = new Option("b", true, "Option B");
        try {
            optionGroup.setSelected(option);
        } catch (AlreadySelectedException e) {
            fail("Unexpected exception");
        }
        assertEquals("b", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        String expected = "[-a Option A, -b Option B]";
        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", true, "Option B");
        optionGroup.addOption(optionA).addOption(optionB);

        assertEquals(expected, optionGroup.toString());
    }

}
