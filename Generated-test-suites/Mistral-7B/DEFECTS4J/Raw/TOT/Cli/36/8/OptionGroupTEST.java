package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for OptionGroup class.
 */
public class OptionGroupTest
{
    @Test
    public void testOptionGroup() throws ParseException
    {
        // Create an OptionGroup
        OptionGroup group = new OptionGroup();

        // Add some options to the group
        Option option1 = new Option("a", true, "Optional option 1");
        Option option2 = new Option("b", false, "Optional option 2");
        Option option3 = new Option("c", false, "Required option 3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        // Set the selected option
        group.setSelected(option3);

        // Parse the command line arguments with the group
        String[] args = new String[]{"--c", "--a"};
        new DefaultParser().parse(group.getOptions(), args, false, false);

        // Check that the selected option was set correctly
        Assert.assertEquals(option3.getKey(), group.getSelected());

        // Parse the command line arguments with incorrect option
        String[] incorrectArgs = new String[]{"--d", "--a"};
        try
        {
            new DefaultParser().parse(group.getOptions(), incorrectArgs, false, false);
            Assert.fail("Expected AlreadySelectedException but got none.");
        }
        catch (AlreadySelectedException e)
        {
            // Expected
        }

        // Parse the command line arguments with missing required option
        String[] missingRequiredArg = new String[]{"--a", "--b"};
        try
        {
            new DefaultParser().parse(group.getOptions(), missingRequiredArg, false, false);
            Assert.fail("Expected MissingArgumentException but got none.");
        }
        catch (MissingArgumentException e)
        {
            // Expected
        }

        // Parse the command line arguments with all options
        String[] allOptionsArgs = new String[]{"--c", "--a", "--b"};
        new DefaultParser().parse(group.getOptions(), allOptionsArgs, false, false);

        // Check that all options were parsed correctly
        Option[] parsedOptions = new DefaultParser().parse(group.getOptions(), allOptionsArgs, false, false).getOptions();
        Assert.assertArrayEquals(new Option[]{option1, option2, option3}, parsedOptions);
    }
}