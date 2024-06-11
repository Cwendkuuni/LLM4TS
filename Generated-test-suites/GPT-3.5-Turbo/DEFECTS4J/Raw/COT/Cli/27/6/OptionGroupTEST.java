import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup og = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        og.addOption(option);
        
        assertTrue(og.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup og = new OptionGroup();
        Option option1 = new Option("s", "save", false, "Save option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        og.addOption(option1);
        og.addOption(option2);

        assertTrue(og.getNames().contains("s"));
        assertTrue(og.getNames().contains("d"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup og = new OptionGroup();
        Option option1 = new Option("s", "save", false, "Save option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        og.addOption(option1);
        og.addOption(option2);

        assertTrue(og.getOptions().contains(option1));
        assertTrue(og.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        OptionGroup og = new OptionGroup();
        Option option1 = new Option("s", "save", false, "Save option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        og.addOption(option1);
        og.addOption(option2);

        og.setSelected(option1);
        assertEquals("s", og.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup og = new OptionGroup();
        Option option1 = new Option("s", "save", false, "Save option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        og.addOption(option1);
        og.addOption(option2);

        og.setSelected(option1);
        og.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        OptionGroup og = new OptionGroup();
        Option option1 = new Option("s", "save", false, "Save option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        og.addOption(option1);
        og.addOption(option2);

        og.setSelected(option1);
        assertEquals("s", og.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup og = new OptionGroup();
        og.setRequired(true);

        assertTrue(og.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup og = new OptionGroup();
        Option option1 = new Option("s", "save", false, "Save option");
        Option option2 = new Option("d", "delete", false, "Delete option");
        og.addOption(option1);
        og.addOption(option2);

        String expected = "[ -s Save option, -d Delete option ]";
        assertEquals(expected, og.toString());
    }
}