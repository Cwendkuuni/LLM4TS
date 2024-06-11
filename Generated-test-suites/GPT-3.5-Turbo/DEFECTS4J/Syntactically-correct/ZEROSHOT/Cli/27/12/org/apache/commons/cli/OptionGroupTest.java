import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "test option 1");
        group.addOption(option1);

        Option option2 = new Option("b", "test option 2");
        group.addOption(option2);

        assertNotNull(group.getOptions());
        assertEquals(2, group.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "test option 1");
        group.addOption(option1);

        Option option2 = new Option("b", "test option 2");
        group.addOption(option2);

        assertNotNull(group.getNames());
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "test option 1");
        option1.setOpt("a");
        group.addOption(option1);

        Option option2 = new Option("b", "test option 2");
        option2.setOpt("b");
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("a", group.getSelected());

        group.setSelected(option2);
        assertEquals("b", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "test option 1");
        option1.setOpt("a");
        group.addOption(option1);

        Option option2 = new Option("b", "test option 2");
        option2.setOpt("b");
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("a", group.getSelected());

        group.setSelected(option2);
        assertEquals("b", group.getSelected());

        group.setSelected(option1); // Should throw AlreadySelectedException
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "test option 1");
        option1.setOpt("a");
        group.addOption(option1);

        Option option2 = new Option("b", "test option 2");
        option2.setOpt("b");
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("a", group.getSelected());

        group.setSelected(option2);
        assertEquals("b", group.getSelected());
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
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "test option 1");
        group.addOption(option1);

        Option option2 = new Option("b", "test option 2");
        group.addOption(option2);

        String expected = "[--a test option 1, --b test option 2]";
        assertEquals(expected, group.toString());
    }
}