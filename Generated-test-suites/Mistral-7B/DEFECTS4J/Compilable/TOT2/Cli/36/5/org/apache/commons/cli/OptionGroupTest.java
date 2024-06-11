package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option = new Option("optionName", "optionDescription", true, "optionAlias");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);

        Assert.assertEquals("optionName", optionGroup.getOptions().iterator().next().getKey());
        Assert.assertEquals(1, optionGroup.getOptions().size());
    }
}