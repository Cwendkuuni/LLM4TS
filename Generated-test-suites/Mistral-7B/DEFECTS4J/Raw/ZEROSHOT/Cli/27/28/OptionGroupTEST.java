import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());

        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        Assert.assertEquals(2, names.size());
        Assert.assertEquals(option1.getKey(), names.toString());
        Assert.assertEquals(option2.getKey(), names.toString().split(", ")[1]);
    }

    @Test
    public void testSetSelected() throws Exception {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getKey(), optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            Assert.fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        String expected = "[-o1 Option 1, -o2 Option 2]";

        Assert.assertEquals(expected, optionGroup.toString());
    }
}