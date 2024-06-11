import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

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
        Option option = new Option("o", "option", false, "Test option");
        optionGroup.addOption(option);
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("o"));
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(new Option("o", "option", false, "Test option"));
        optionGroup.addOption(new Option("a", "another", false, "Another option"));
        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("o"));
        assertTrue(names.contains("a"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o", "option", false, "Test option");
        Option option2 = new Option("a", "another", false, "Another option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("o", "option", false, "Test option");
        Option option2 = new Option("a", "another", false, "Another option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("o", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("o", "option", false, "Test option");
        Option option2 = new Option("a", "another", false, "Another option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        assertNull(optionGroup.getSelected());
        Option option = new Option("o", "option", false, "Test option");
        try {
            optionGroup.setSelected(option);
        } catch (AlreadySelectedException e) {
            fail("Should not throw an exception");
        }
        assertEquals("o", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o", "option", false, "Test option");
        Option option2 = new Option("a", "another", false, "Another option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-o Test option, -a Another option]";
        assertEquals(expected, optionGroup.toString());
    }
}