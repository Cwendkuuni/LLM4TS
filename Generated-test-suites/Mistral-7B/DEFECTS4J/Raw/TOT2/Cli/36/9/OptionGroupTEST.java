import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    // Test for addOption method
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("test", "test", false, "Test Option");
        optionGroup.addOption(option);
        Option option2 = new Option("test", "test", false, "Test Option");
        optionGroup.addOption(option2);

        // Check if the same Option object can be added multiple times
        optionGroup.addOption(option);

        Assert.assertEquals(2, optionGroup.getOptions().size());
    }

    // Test for getNames method
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Opt 1", false, "Opt 1 Description");
        Option option2 = new Option("opt2", "Opt 2", false, "Opt 2 Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("opt1"));
        Assert.assertTrue(names.contains("opt2"));
    }

    // Test for setSelected method with an already selected option
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Opt 1", false, "Opt 1 Description");
        Option option2 = new Option("opt2", "Opt 2", false, "Opt 2 Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    // Test for setSelected method with a valid option
    @Test
    public void testSetSelectedValidOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Opt 1", false, "Opt 1 Description");
        Option option2 = new Option("opt2", "Opt 2", false, "Opt 2 Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        Assert.assertEquals("opt1", optionGroup.getSelected());
    }

    // Test for isRequired method
    @Test
    public void testIsRequired() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup(true);

        Assert.assertFalse(optionGroup1.isRequired());
        Assert.assertTrue(optionGroup2.isRequired());
    }

    // Test for toString method
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Opt 1", false, "Opt 1 Description");
        Option option2 = new Option("opt2", "Opt 2", false, "Opt 2 Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[--opt1, --opt2]", optionGroup.toString());
    }
}