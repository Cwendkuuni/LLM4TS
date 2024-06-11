import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection optionNames = optionGroup.getNames();

        Assert.assertTrue(optionNames.contains("a"));
        Assert.assertTrue(optionNames.contains("b"));

        Collection options = optionGroup.getOptions();

        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            Assert.fail("Should not throw AlreadySelectedException");
        }

        Assert.assertEquals("a", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            Assert.fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected exception
        }
    }
}