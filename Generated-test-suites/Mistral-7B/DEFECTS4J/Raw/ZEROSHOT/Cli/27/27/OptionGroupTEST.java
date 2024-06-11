import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    /**
     * Test method for {@link OptionGroup#addOption(Option)} and {@link OptionGroup#getNames()}.
     */
    @Test
    public void testAddOptionAndGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option optA = new Option("a", "option A");
        Option optB = new Option("b", "option B");
        Option optC = new Option("c", "option C");

        optionGroup.addOption(optA);
        optionGroup.addOption(optB);
        optionGroup.addOption(optC);

        Set<String> expectedNames = new HashSet<>();
        expectedNames.add("a");
        expectedNames.add("b");
        expectedNames.add("c");

        Set<String> actualNames = new HashSet<>(optionGroup.getNames());

        Assert.assertEquals(expectedNames, actualNames);
    }

    /**
     * Test method for {@link OptionGroup#addOption(Option)} and {@link OptionGroup#getOptions()}.
     */
    @Test
    public void testAddOptionAndGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option optA = new Option("a", "option A");
        Option optB = new Option("b", "option B");
        Option optC = new Option("c", "option C");

        optionGroup.addOption(optA);
        optionGroup.addOption(optB);
        optionGroup.addOption(optC);

        Set<Option> expectedOptions = new HashSet<>();
        expectedOptions.add(optA);
        expectedOptions.add(optB);
        expectedOptions.add(optC);

        Collection<Option> actualOptions = optionGroup.getOptions();

        Assert.assertEquals(expectedOptions, actualOptions);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)} and {@link OptionGroup#getSelected()}.
     */
    @Test
    public void testSetSelectedAndGetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option optA = new Option("a", "option A");
        Option optB = new Option("b", "option B");

        optionGroup.addOption(optA);
        optionGroup.addOption(optB);

        try
        {
            optionGroup.setSelected(optA);
            String expectedSelected = optA.getOpt();
            String actualSelected = optionGroup.getSelected();

            Assert.assertEquals(expectedSelected, actualSelected);
        }
        catch (AlreadySelectedException e)
        {
            // expected when selecting the same option twice
        }

        try
        {
            optionGroup.setSelected(optB);
            String expectedSelected = optB.getOpt();
            String actualSelected = optionGroup.getSelected();

            Assert.assertEquals(expectedSelected, actualSelected);
        }
        catch (AlreadySelectedException e)
        {
            // expected when selecting an option that has already been selected
        }

        optionGroup.setSelected(optA);
        String expectedSelected = optA.getOpt();
        String actualSelected = optionGroup.getSelected();

        Assert.assertEquals(expectedSelected, actualSelected);
    }

    /**
     * Test method for {@link OptionGroup#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#toString()}.
     */
    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option optA = new Option("a", "option A");
        Option optB = new Option("b", "option B");
        Option optC = new Option("c", "option C");

        optionGroup.addOption(optA);
        optionGroup.addOption(optB);
        optionGroup.addOption(optC);

        String expectedString = "[-a option A, --option-a option A, -b option B, --option-b option B, -c option C, --option-c option C]";

        String actualString = optionGroup.toString();

        Assert.assertEquals(expectedString, actualString);
    }
}