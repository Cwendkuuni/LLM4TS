import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);

        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option, optionGroup.getOptions().iterator().next());
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertEquals(true, optionGroup.getNames().contains("a"));
        assertEquals(true, optionGroup.getNames().contains("r"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals(true, optionGroup.getOptions().contains(option1));
        assertEquals(true, optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() {
        Option option1 = new Option("a", "add", false, "Add option");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(option1);
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "add", false, "Add option");

        optionGroup.addOption(option1);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);

        assertEquals(true, optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[ -a Add option, -r Remove option]";

        assertEquals(expected, optionGroup.toString());
    }
}