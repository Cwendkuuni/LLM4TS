package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for Options class.
 */
public class OptionsTest
{
    /**
     * Test for addOption method with single character short option and no long option.
     */
    @Test
    public void testAddOptionShortOnly()
    {
        Options options = new Options();
        options.addOption('a', "optionA", false, "Option A");

        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(0);

        assertTrue(option.isRequired());
        assertEquals("Option A", option.getDescription());
        assertEquals('a', option.getKey());
        assertFalse(option.hasLongOpt());
    }

    /**
     * Test for addOption method with single character short option and long option.
     */
    @Test
    public void testAddOptionShortAndLong()
    {
        Options options = new Options();
        options.addOption('a', "optionA", false, "Option A");
        options.addOption("optionB", "optionB", false, "Option B");

        List<Option> optionsList = options.helpOptions();
        Option optionA = optionsList.get(0);
        Option optionB = optionsList.get(1);

        assertTrue(optionA.isRequired());
        assertEquals("Option A", optionA.getDescription());
        assertEquals('a', optionA.getKey());
        assertFalse(optionA.hasLongOpt());

        assertTrue(optionB.isRequired());
        assertEquals("Option B", optionB.getDescription());
        assertEquals("optionB", optionB.getKey());
        assertTrue(optionB.hasLongOpt());
        assertEquals("optionB", optionB.getLongOpt());
    }

    /**
     * Test for addOption method with short option requiring an argument.
     */
    @Test
    public void testAddOptionShortWithArg()
    {
        Options options = new Options();
        options.addOption('a', "optionA", true, "Option A with argument");

        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(0);

        assertFalse(option.isRequired());
        assertEquals("Option A with argument", option.getDescription());
        assertEquals('a', option.getKey());
        assertTrue(option.hasArg());
    }

    /**
     * Test for addOption method with long option requiring an argument.
     */
    @Test
    public void testAddOptionLongWithArg()
    {
        Options options = new Options();
        options.addOption("optionA", true, "Option A with argument");

        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(0);

        assertFalse(option.isRequired());
        assertEquals("Option A with argument", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
    }

    /**
     * Test for addOptionGroup method.
     */
    @Test
    public void testAddOptionGroup()
    {
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup("optionGroupA", "Option Group A");
        optionGroup.addOption(new Option("groupA", "groupOptionA", false, "Group Option A"));
        optionGroup.addOption(new Option("groupB", "groupOptionB", false, "Group Option B"));

        options.addOptionGroup(optionGroup);

        List<Option> optionsList = options.helpOptions();
        Option groupOptionA = null;
        Option groupOptionB = null;

        for (Option option : optionsList) {
            if (option.getKey().equals("groupOptionA")) {
                groupOptionA = option;
            } else if (option.getKey().equals("groupOptionB")) {
                groupOptionB = option;
            }
        }

        assertTrue(groupOptionA != null);
        assertTrue(groupOptionB != null);
        assertEquals("Option Group A", optionGroup.getDescription());
    }

    /**
     * Test for getOptions method.
     */
    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        options.addOption('a', "optionA", false, "Option A");
        options.addOption("optionB", true, "Option B with argument");

        CommandLineParser parser = new BasicParser();

        String[] args = {"-a", "valueA", "--optionB"};

        try {
            CommandLine commandLine = parser.parse(options, args);
            List<Option> parsedOptions = options.helpOptions();

            for (Option option : parsedOptions) {
                if (option.getKey().equals("optionA")) {
                    assertTrue(commandLine.hasOption("optionA"));
                } else if (option.getKey().equals("optionB")) {
                    assertTrue(commandLine.hasOption("optionB"));
                    assertEquals("valueA", commandLine.getOptionValue("optionA"));
                    assertEquals("valueB", commandLine.getOptionValue("optionB"));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}