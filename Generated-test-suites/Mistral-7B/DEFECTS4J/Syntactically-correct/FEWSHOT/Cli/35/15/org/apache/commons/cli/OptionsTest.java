package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();

        String optionShort = "o";
        String optionLong = "output";
        boolean hasArg = false;
        String description = "Output file";

        Option option = new Option(optionShort, optionLong, hasArg, description);
        options.addOption(option);

        assertEquals(1, options.getOptions().size());
        assertEquals(option, options.getOptions().get(0));
        assertEquals(optionShort, options.getOptions().get(0).getKey());
        assertEquals(optionLong, options.getOptions().get(0).getLongOpt());
        assertFalse(options.getOptions().get(0).hasArg());
        assertEquals(description, options.getOptions().get(0).getDescription());

        assertEquals(1, options.shortOpts.size());
        assertEquals(option, options.shortOpts.get(optionShort));
        assertEquals(optionLong, options.shortOpts.get(optionShort).getLongOpt());

        assertEquals(1, options.longOpts.size());
        assertEquals(option, options.longOpts.get(optionLong));
        assertEquals(optionShort, options.longOpts.get(optionLong).getKey());
    }

    @Test
    public void testAddOptionWithArgument() {
        Options options = new Options();

        String optionShort = "o";
        String optionLong = "output";
        boolean hasArg = true;
        String description = "Output file";

        Option option = new Option(optionShort, optionLong, hasArg, description);
        options.addOption(option);

        assertEquals(1, options.getOptions().size());
        assertEquals(option, options.getOptions().get(0));
        assertEquals(optionShort, options.getOptions().get(0).getKey());
        assertEquals(optionLong, options.getOptions().get(0).getLongOpt());
        assertTrue(options.getOptions().get(0).hasArg());
        assertEquals(description, options.getOptions().get(0).getDescription());

        assertEquals(1, options.shortOpts.size());
        assertEquals(option, options.shortOpts.get(optionShort));
        assertEquals(optionLong, options.shortOpts.get(optionShort).getLongOpt());

        assertEquals(1, options.longOpts.size());
        assertEquals(option, options.longOpts.get(optionLong));
        assertEquals(optionShort, options.longOpts.get(optionLong).getKey());
    }

    @Test
    public void testAddOptionWithShortAndLong() {
        Options options = new Options();

        String optionShort = "o";
        String optionLong = "output";
        boolean hasArg = false;
        String description = "Output file";

        Option option = new Option(optionShort, optionLong, hasArg, description);
        options.addOption(option);

        assertEquals(1, options.getOptions().size());
        assertEquals(option, options.getOptions().get(0));
        assertEquals(optionShort, options.getOptions().get(0).getKey());
        assertEquals(optionLong, options.getOptions().get(0).getLongOpt());
        assertFalse(options.getOptions().get(0).hasArg());
        assertEquals(description, options.getOptions().get(0).getDescription());

        assertEquals(1, options.shortOpts.size());
        assertEquals(option, options.shortOpts.get(optionShort));
        assertEquals(optionLong, options.shortOpts.get(optionShort).getLongOpt());

        assertEquals(1, options.longOpts.size());
        assertEquals(option, options.longOpts.get(optionLong));
        assertEquals(optionShort, options.longOpts.get(optionLong).getKey());
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        String groupName = "group";

        OptionGroup group = new OptionGroup(groupName, "Group");
        options.addOptionGroup(group);

        assertEquals(1, options.getOptionGroups().size());
        assertEquals(group, options.getOptionGroups().iterator().next());
        assertEquals(groupName, group.getName());

        assertTrue(options.requiredOpts.isEmpty());
        assertTrue(options.optionGroups.isEmpty());
    }

    @Test
    public void testAddRequiredOptionToGroup() {
        Options options = new Options();

        String groupName = "group";
        String optionShort = "g";
        String optionLong = "group";
        boolean hasArg = false;        String description = "Group";

        OptionGroup group = new OptionGroup(groupName, "Group");
        Option option = new Option(optionShort, optionLong, hasArg, description);

        group.addOption(option);
        options.addOptionGroup(group);

        assertEquals(1, options.getOptionGroups().size());
        assertEquals(group, options.getOptionGroups().iterator().next());
        assertEquals(groupName, group.getName());

        assertEquals(1, group.getOptions().size());
        assertEquals(option, group.getOptions().get(0));
        assertEquals(optionShort, option.getKey());
        assertEquals(optionLong, option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals(description, option.getDescription());

        assertEquals(1, options.shortOpts.size());
        assertEquals(option, options.shortOpts.get(optionShort));
        assertEquals(optionLong, options.shortOpts.get(optionShort).getLongOpt());

        assertEquals(1, options.longOpts.size());
        assertEquals(option, options.longOpts.get(optionLong));
        assertEquals(optionShort, options.longOpts.get(optionLong).getKey());

        assertTrue(options.requiredOpts.isEmpty());
        assertTrue(options.optionGroups.isEmpty());
    }

    @Test
    public void testAddOptionalOptionToGroup() {
        Options options = new Options();

        String groupName = "group";
        String optionShort = "o";
        String optionLong = "output";
        boolean hasArg = false;        String description = "Output file";

        OptionGroup group = new OptionGroup(groupName, "Group");
        Option option = new Option(optionShort, optionLong, hasArg, description);

        group.addOption(option);
        options.addOptionGroup(group);

        assertEquals(1, options.getOptionGroups().size());
        assertEquals(group, options.getOptionGroups().iterator().next());
        assertEquals(groupName, group.getName());

        assertEquals(1, group.getOptions().size());
        assertEquals(option, group.getOptions().get(0));
        assertEquals(optionShort, option.getKey());
        assertEquals(optionLong, option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals(description, option.getDescription());

        assertEquals(1, options.shortOpts.size());
        assertEquals(option, options.shortOpts.get(optionShort));
        assertEquals(optionLong, options.shortOpts.get(optionShort).getLongOpt());

        assertEquals(1, options.longOpts.size());
        assertEquals(option, options.longOpts.get(optionLong));
        assertEquals(optionShort, options.longOpts.get(optionLong).getKey());

        assertFalse(option.isRequired());
        assertTrue(options.requiredOpts.isEmpty());
    }

    @Test
    public void testAddRequiredOptionDirectly() {
        Options options = new Options();

        String optionName = "o";
        String description = "Output file";

        Option option = new Option(optionName, null, true, description);
        options.addOption(option);

        assertEquals(1, options.getOptions().size());
        assertEquals(option, options.getOptions().get(0));
        assertEquals(optionName, option.getKey());
        assertNull(option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(description, option.getDescription());

        assertTrue(options.requiredOpts.size() > 0);
        assertEquals(option, options.requiredOpts.get(0));
        assertEquals(optionName, options.requiredOpts.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddRequiredOptionToRequiredOption() {
        Options options = new Options();

        String optionName1 = "o1";
        String description1 = "Option 1";

        Option option1 = new Option(optionName1, null, true, description1);
        options.addOption(option1);

        String optionName2 = "o2";
        String description2 = "Option 2";

        Option option2 = new Option(optionName2, null, true, description2);
        options.addOption(option2);

        assertFalse(option1.isRequired());
        assertTrue(options.requiredOpts.isEmpty());

        try {
            option2.setRequired(true);
        } catch (IllegalStateException e) {
            throw e;
        }

        assertEquals(2, options.getOptions().size());
        assertEquals(option1, options.getOptions().get(0));
        assertEquals(option2, options.getOptions().get(1));

        assertEquals(1, options.shortOpts.size());
        assertEquals(option1, options.shortOpts.get(optionName1));
        assertEquals(optionName1, options.shortOpts.get(optionName1).getKey());
        assertEquals(null, options.shortOpts.get(optionName1).getLongOpt());
        assertFalse(options.shortOpts.get(optionName1).isRequired());

        assertEquals(1, options.longOpts.size());
        assertEquals(option2, options.longOpts.get(optionName2));
        assertEquals(optionName2, options.longOpts.get(optionName2).getKey());
        assertEquals(optionName2, options.longOpts.get(optionName2).getLongOpt());
        assertTrue(options.longOpts.get(optionName2).isRequired());

        assertEquals(2, options.requiredOpts.size());
        assertEquals(option1, options.requiredOpts.get(0));
        assertEquals(option2, options.requiredOpts.get(1));
    }
}