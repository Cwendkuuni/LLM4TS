import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "alpha", false, "Description");
        group.addOption(option);
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Description");
        Option option2 = new Option("b", "beta", false, "Description");
        group.addOption(option1);
        group.addOption(option2);
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("c", "gamma", false, "Description");
        group.setSelected(option);
        assertEquals("c", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("d", "delta", false, "Description 1");
        Option option2 = new Option("e", "epsilon", false, "Description 2");
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "phi", false, "Description 1");
        Option option2 = new Option("g", "gamma", false, "Description 2");
        group.addOption(option1);
        group.addOption(option2);
        assertEquals("[ -f Description 1, -g Description 2]", group.toString());
    }
}