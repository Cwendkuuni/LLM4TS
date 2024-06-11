package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    // Test for addOption() method
    @Test
    public void testAddOption() {
        Option option = new Option("a", "option a");
        OptionGroup result = optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();

        assertTrue(options.contains(option));
        assertEquals(optionGroup, result);
    }

    // Test for getNames() method
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    // Test for getOptions() method
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    // Test for setSelected() method
    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            String selectedOption = optionGroup.getSelected();
            assertEquals("a", selectedOption);
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    // Test for setSelected() method with already selected option
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    // Test for setSelected() method with null option
    @Test
    public void testSetSelectedNullOption() {
        Option option1 = new Option("a", "option a");
        optionGroup.addOption(option1);

        try {
            optionGroup.setSelected(null);
            String selectedOption = optionGroup.getSelected();
            assertNull(selectedOption);
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    // Test for getSelected() method
    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            String selectedOption = optionGroup.getSelected();
            assertEquals(option1.getKey(), selectedOption);
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    // Test for setRequired() method
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    // Test for isRequired() method
    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
    }

    // Test for toString() method
    @Test
    public void testToString() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[option a, option b]";
        String result = optionGroup.toString();

        assertEquals(expected, result);
    }

}