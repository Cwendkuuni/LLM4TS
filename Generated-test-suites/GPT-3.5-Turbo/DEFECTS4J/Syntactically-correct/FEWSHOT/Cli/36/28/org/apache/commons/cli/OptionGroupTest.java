import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "Test Option");
        OptionGroup result = optionGroup.addOption(option);

        assertEquals(optionGroup, result);
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option, optionGroup.getOptions().iterator().next());
        assertEquals(option, optionGroup.getOption(option.getKey()));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("o1", "option1", false, "Test Option 1");
        Option option2 = new Option("o2", "option2", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("o1"));
        assertTrue(optionGroup.getNames().contains("o2"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Test Option 1");
        Option option2 = new Option("o2", "option2", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("o1", "option1", false, "Test Option 1");
        Option option2 = new Option("o2", "option2", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getKey(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() {
        Option option1 = new Option("o1", "option1", false, "Test Option 1");
        Option option2 = new Option("o2", "option2", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("o1", "option1", false, "Test Option 1");
        Option option2 = new Option("o2", "option2", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());
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
        Option option1 = new Option("o1", "option1", false, "Test Option 1");
        Option option2 = new Option("o2", "option2", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o1 Test Option 1, -o2 Test Option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}