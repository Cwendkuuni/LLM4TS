import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "description1");
        Option addedOption = optionGroup.addOption(option);
        Assert.assertEquals(option, addedOption);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        List<String> names = new ArrayList<>();
        Option option1 = new Option("option1", "description1");
        Option option2 = new Option("option2", "description2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        names.addAll(optionGroup.getNames());
        Assert.assertEquals(names.size(), 2);
        Assert.assertEquals(names.get(0), option1.getKey());
        Assert.assertEquals(names.get(1), option2.getKey());
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("option1", "description1");
        Option option2 = new Option("option2", "description2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addAll((Collection<? extends Option>) optionGroup.getOptions());
        Assert.assertEquals(options.size(), 2);
        Assert.assertEquals(options.get(0), option1);
        Assert.assertEquals(options.get(1), option2);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "description1");
        optionGroup.setSelected(option);
        optionGroup.setSelected(null);
    }

    @Test
    public void testSetSelectedSame() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "description1");
        optionGroup.setSelected(option);
        optionGroup.setSelected(option);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferent() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "description1");
        Option option2 = new Option("option2", "description2");
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.setRequired(false);
        Assert.assertFalse(optionGroup2.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "description1");
        Option option2 = new Option("option2", "description2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-option1 description1, --option2 description2]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
}