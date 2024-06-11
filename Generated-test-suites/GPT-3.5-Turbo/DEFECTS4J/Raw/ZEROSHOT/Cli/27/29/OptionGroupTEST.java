import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Test
    public void testAddOption() {
        optionGroup = new OptionGroup();
        Option option = new Option("t", "test", false, "Test Option");
        optionGroup.addOption(option);
        Collection options = optionGroup.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "update", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection names = optionGroup.getNames();
        assertTrue(names.contains("t"));
        assertTrue(names.contains("u"));
    }

    @Test
    public void testGetOptions() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "update", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "update", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
        optionGroup.setSelected(option2);
        assertEquals(option2.getOpt(), optionGroup.getSelected());
        try {
            optionGroup.setSelected(option1);
            fail("Exception not thrown");
        } catch (AlreadySelectedException e) {
            // Exception is expected
        }
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test Option 1");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "update", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[ -t Test Option 1, -u Test Option 2]";
        assertEquals(expected, optionGroup.toString());
    }

}