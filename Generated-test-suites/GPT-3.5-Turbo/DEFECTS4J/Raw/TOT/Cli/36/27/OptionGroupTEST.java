import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();

        option1 = new Option("a", "option1", false, "Option 1");
        option2 = new Option("b", "option2", true, "Option 2");
        option3 = new Option("c", "option3", false, "Option 3");

        optionGroup.addOption(option1)
                .addOption(option2)
                .addOption(option3);
    }

    @Test
    public void testAddOption() {
        Option option4 = new Option("d", "option4", true, "Option 4");
        optionGroup.addOption(option4);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(4, options.size());
        assertTrue(options.contains(option4));
    }

    @Test
    public void testGetNames() {
        Collection<String> names = optionGroup.getNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        assertTrue(names.contains("c"));
    }

    @Test
    public void testGetOptions() {
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testSetSelected_ValidOption() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        String selected = optionGroup.getSelected();
        assertEquals("a", selected);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedOption() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1); // selecting the same option again should throw AlreadySelectedException
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null); // selecting null option should reset the selected option
        String selected = optionGroup.getSelected();
        assertNull(selected);
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        String expected = "[ -a Option 1, -b Option 2, -c Option 3]";
        String actual = optionGroup.toString();
        assertEquals(expected, actual);
    }

}