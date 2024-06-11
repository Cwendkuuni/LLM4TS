import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

import java.util.List;
import java.util.Collection;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group1 = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description");
        Option option2 = new Option("b", "option2", false, "description");
        group1.addOption(option1);
        group1.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(group1);

        Collection<OptionGroup> optionGroups = options.getOptionGroups();

        Assert.assertEquals(1, optionGroups.size());
        OptionGroup retrievedGroup = optionGroups.iterator().next();
        List<Option> groupOptions = retrievedGroup.getOptions();

        Assert.assertEquals(2, groupOptions.size());
        Assert.assertEquals(option1, groupOptions.get(0));
        Assert.assertEquals(option2, groupOptions.get(1));
    }

    @Test
    public void testAddOption_withShortNameAndDescription() {
        Options options = new Options();
        options.addOption("a", "description");

        Collection<Option> retrievedOptions = options.getOptions();

        Assert.assertEquals(1, retrievedOptions.size());

        Option retrievedOption = retrievedOptions.iterator().next();
        Assert.assertEquals("a", retrievedOption.getOpt());
        Assert.assertEquals("description", retrievedOption.getDescription());
        Assert.assertNull(retrievedOption.getLongOpt());
        Assert.assertFalse(retrievedOption.hasArg());
    }

    @Test
    public void testAddOption_withShortNameHasArgAndDescription() {
        Options options = new Options();
        options.addOption("a", true, "description");

        Collection<Option> retrievedOptions = options.getOptions();

        Assert.assertEquals(1, retrievedOptions.size());

        Option retrievedOption = retrievedOptions.iterator().next();
        Assert.assertEquals("a", retrievedOption.getOpt());
        Assert.assertEquals("description", retrievedOption.getDescription());
        Assert.assertNull(retrievedOption.getLongOpt());
        Assert.assertTrue(retrievedOption.hasArg());
    }

    @Test
    public void testAddOption_withShortNameAndLongNameHasArgAndDescription() {
        Options options = new Options();
        options.addOption("a", "option", true, "description");

        Collection<Option> retrievedOptions = options.getOptions();

        Assert.assertEquals(1, retrievedOptions.size());

        Option retrievedOption = retrievedOptions.iterator().next();
        Assert.assertEquals("a", retrievedOption.getOpt());
        Assert.assertEquals("option", retrievedOption.getLongOpt());
        Assert.assertEquals("description", retrievedOption.getDescription());
        Assert.assertTrue(retrievedOption.hasArg());
    }

    @Test
    public void testAddOption_withOptionInstance() {
        Option option = new Option("a", "option", true, "description");

        Options options = new Options();
        options.addOption(option);

        Collection<Option> retrievedOptions = options.getOptions();

        Assert.assertEquals(1, retrievedOptions.size());

        Option retrievedOption = retrievedOptions.iterator().next();
        Assert.assertEquals("a", retrievedOption.getOpt());
        Assert.assertEquals("option", retrievedOption.getLongOpt());
        Assert.assertEquals("description", retrievedOption.getDescription());
        Assert.assertTrue(retrievedOption.hasArg());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", true, "description2");
        options.addOption(option1);
        options.addOption(option2);

        Collection<Option> retrievedOptions = options.getOptions();

        Assert.assertEquals(2, retrievedOptions.size());
        Assert.assertTrue(retrievedOptions.contains(option1));
        Assert.assertTrue(retrievedOptions.contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", true, "description2");
        options.addOption(option1);
        options.addOption(option2);

        List requiredOptions = options.getRequiredOptions();

        Assert.assertEquals(0, requiredOptions.size());

        option1.setRequired(true);
        options.addOption(option1);

        requiredOptions = options.getRequiredOptions();

        Assert.assertEquals(1, requiredOptions.size());
        Assert.assertEquals(option1, requiredOptions.get(0));
    }

    @Test
    public void testGetOption_withShortName() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", true, "description2");
        options.addOption(option1);
        options.addOption(option2);

        Option retrievedOption = options.getOption("a");

        Assert.assertEquals(option1, retrievedOption);
    }

    @Test
    public void testGetOption_withLongName() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", true, "description2");
        options.addOption(option1);
        options.addOption(option2);

        Option retrievedOption = options.getOption("option2");

        Assert.assertEquals(option2, retrievedOption);
    }

    @Test
    public void testHasOption_withShortName() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "description");
        options.addOption(option);

        boolean hasOption = options.hasOption("a");

        Assert.assertTrue(hasOption);
    }

    @Test
    public void testHasOption_withLongName() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "description");
        options.addOption(option);

        boolean hasOption = options.hasOption("option");

        Assert.assertTrue(hasOption);
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "description");
        options.addOption(option);

        boolean hasLongOption = options.hasLongOption("option");

        Assert.assertTrue(hasLongOption);
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "description");
        options.addOption(option);

        boolean hasShortOption = options.hasShortOption("a");

        Assert.assertTrue(hasShortOption);
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", false, "description2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        OptionGroup retrievedGroup = options.getOptionGroup(option1);

        Assert.assertEquals(group, retrievedGroup);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", true, "description2");
        options.addOption(option1);
        options.addOption(option2);

        String optionsString = options.toString();

        Assert.assertEquals("[ Options: [ short {a=org.apache.commons.cli.Option@a, b=org.apache.commons.cli.Option@b} ] [ long {option1=org.apache.commons.cli.Option@option1, option2=org.apache.commons.cli.Option@option2} ]", optionsString);
    }
}