import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "apple", false, "Option 1");
        option2 = new Option("b", "banana", false, "Option 2");
        option3 = new Option("c", "cherry", false, "Option 3");
    }

    @Test
    public void testAddOption_validOption_addedToGroup() {
        optionGroup.addOption(option1);
        assertTrue(optionGroup.getOptions().contains(option1));
    }

    @Test
    public void testGetNames_noOptions_returnEmptyCollection() {
        assertTrue(optionGroup.getNames().isEmpty());
    }

    @Test
    public void testGetOptions_noOptions_returnEmptyCollection() {
        assertTrue(optionGroup.getOptions().isEmpty());
    }

    @Test
    public void testSetSelected_validOption_optionSelected() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_sameOptionAlreadySelected_throwAlreadySelectedException() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testSetSelected_nullOption_optionReset() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsRequired_defaultValue_returnFalse() {
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired_setRequired_returnTrue() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString_noOptions_returnEmptyString() {
        String expected = "[]";
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToString_singleOption_returnFormattedString() {
        optionGroup.addOption(option1);
        String expected = "[-a apple]";
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToString_multipleOptions_returnFormattedString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        String expected = "[-a apple, -b banana, -c cherry]";
        assertEquals(expected, optionGroup.toString());
    }
}