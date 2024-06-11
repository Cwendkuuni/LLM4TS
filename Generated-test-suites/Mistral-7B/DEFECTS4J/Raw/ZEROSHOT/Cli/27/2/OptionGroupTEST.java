import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Description of option1");
        Option option2 = new Option("o2", "option2", false, "Description of option2");
        Option option3 = new Option("o3", "option3", false, "Description of option3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // test getNames()
        Map<String, Option> expectedOptions = new HashMap<String, Option>() {{
            put("o1", option1);
            put("o2", option2);
            put("o3", option3);
        }};
        Map<String, Option> actualOptions = new HashMap<String, Option>();
        for (Object name : optionGroup.getNames()) {
            actualOptions.put(name.toString(), optionGroup.getOption((String) name));
        }
        Assert.assertEquals(expectedOptions, actualOptions);

        // test getOptions()
        Assert.assertEquals(Arrays.asList(option1, option2, option3), optionGroup.getOptions());

        // test setSelected() and getSelected()
        optionGroup.setSelected(option1);
        Assert.assertEquals(option1.getOpt(), optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            Assert.fail();
        } catch (org.apache.commons.cli.AlreadySelectedException e) {
            // expected
        }

        // test isRequired()
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        // test toString()
        String expectedString = "[-o1 Description of option1, -o2 Description of option2, --option3 Description of option3]";
        Assert.assertEquals(expectedString, optionGroup.toString());
    }

    @Test
    public void testEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertEquals(Collections.emptySet(), optionGroup.getNames());
        Assert.assertEquals(Collections.emptyList(), optionGroup.getOptions());
        Assert.assertNull(optionGroup.getSelected());
        Assert.assertFalse(optionGroup.isRequired());
        Assert.assertEquals("", optionGroup.toString());
    }
}