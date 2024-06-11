import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "optionA", false, "Option A");
        option2 = new Option("b", "optionB", false, "Option B");
    }

    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertNull(optionGroup.getSelected());
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
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
        String expectedString = "[-a Option A, -b Option B]";
        assertEquals(expectedString, optionGroup.toString());
    }
}