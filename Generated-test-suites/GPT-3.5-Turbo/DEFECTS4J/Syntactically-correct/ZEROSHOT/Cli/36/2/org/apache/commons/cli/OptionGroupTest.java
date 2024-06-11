import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;
import java.util.Collection;

public class OptionGroupTest {

    OptionGroup group;

    @Before
    public void setUp() {
        group = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false, "Testing option");
        group.addOption(option);

        Collection<Option> options = group.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        Collection<String> names = group.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        Collection<Option> options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("a", group.getSelected());

        group.setSelected(option2);
        assertEquals("b", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        Option option = new Option("a", "option1", false, "Option 1");
        group.addOption(option);

        group.setSelected(null);
        assertNull(group.getSelected());
    }

    @Test
    public void testGetSelected() {
        assertNull(group.getSelected());

        Option option = new Option("a", "option1", false, "Option 1");
        group.addOption(option);

        group.setSelected(option);
        assertEquals("a", group.getSelected());
    }

    @Test
    public void testSetRequired() {
        assertFalse(group.isRequired());

        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());

        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-a Option 1, -b Option 2]";
        String actual = group.toString();

        assertEquals(expected, actual);
    }
}