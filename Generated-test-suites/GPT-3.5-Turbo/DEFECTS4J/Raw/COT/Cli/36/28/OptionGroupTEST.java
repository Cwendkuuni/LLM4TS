
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        OptionGroup updatedGroup = optionGroup.addOption(option1).addOption(option2);

        Map<String, Option> expectedOptionMap = new HashMap<>();
        expectedOptionMap.put("a", option1);
        expectedOptionMap.put("b", option2);

        Assert.assertEquals(expectedOptionMap, updatedGroup.getOptionMap());
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1).addOption(option2);
        Collection<String> names = optionGroup.getNames();

        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1).addOption(option2);
        Collection<Option> options = optionGroup.getOptions();

        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1).addOption(option2);
        optionGroup.setSelected(option1);

        Assert.assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1).addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1).addOption(option2);
        optionGroup.setSelected(option1);

        Assert.assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1).addOption(option2);

        String expectedString = "[-a Option 1, -b Option 2]";
        Assert.assertEquals(expectedString, optionGroup.toString());
    }
}
