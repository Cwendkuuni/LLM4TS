import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false, "Test option");
        OptionGroup group = new OptionGroup();

        group.addOption(option);

        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup group = new OptionGroup();

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup group = new OptionGroup();

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup group = new OptionGroup();

        group.addOption(option1);
        group.addOption(option2);

        try{
            group.setSelected(option1);
            assertEquals(option1.getKey(), group.getSelected());

            group.setSelected(option2);
            assertEquals(option2.getKey(), group.getSelected());
        } catch(AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup group = new OptionGroup();

        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        OptionGroup group = new OptionGroup();

        group.addOption(option1);

        assertNull(group.getSelected());

        try {
            group.setSelected(option1);
            assertEquals(option1.getKey(), group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();

        group.setRequired(true);
        assertTrue(group.isRequired());

        group.setRequired(false);
        assertFalse(group.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup group = new OptionGroup();

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--option1 Option 1, --option2 Option 2]";
        assertEquals(expected, group.toString());
    }
}