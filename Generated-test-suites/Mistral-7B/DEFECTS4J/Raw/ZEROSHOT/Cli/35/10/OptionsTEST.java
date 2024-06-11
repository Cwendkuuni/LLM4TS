import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @After
    public void tearDown() {
        options = null;
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("testGroup", "Test group options");
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testGetOptionGroups() {
        OptionGroup group1 = new OptionGroup("group1", "Group 1 options");
        OptionGroup group2 = new OptionGroup("group2", "Group 2 options");
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        Collection<OptionGroup> optionGroups = options.getOptionGroups();

        assertTrue(optionGroups.contains(group1));
        assertTrue(optionGroups.contains(group2));
    }

    @Test
    public void testAddOptionWithShortNameAndNoArgument() {
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testAddOptionWithShortNameAndArgument() {
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testAddOptionWithShortNameAndValue() {
        Option option = new Option("o", "option", true, "Option description");
        option.setArgName("value");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testAddOptionWithLongNameAndNoArgument() {
        Option option = new Option("option", "Option description");
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testAddOptionWithLongNameAndArgument() {
        Option option = new Option("option", "option", true, "Option description");
        options.addOption(option);

        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testAddOptionWithLongNameAndValue() {
        Option option = new Option("option", "option", true, "Option description");
        option.setArgName("value");
        options.addOption(option);

        assertTrue(options.hasOption("option"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithDuplicateShortName() {
        Option option1 = new Option("o", "option1", false, "Option description 1");
        Option option2 = new Option("o", "option2", false, "Option description 2");

        options.addOption(option1);
        options.addOption(option2);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithDuplicateLongName() {
        Option option1 = new Option("option1", "option1", false, "Option description 1");
        Option option2 = new Option("option1", "option2", false, "Option description 2");

        options.addOption(option1);
        options.addOption(option2);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Option description 1");
        Option option2 = new Option("o2", "option2", false, "Option description 2");
        Option option3 = new Option("o3", "option3", true, "Option description 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<Option> optionsList = options.getOptions();

        assertEquals(3, optionsList.size());
        assertTrue(optionsList.contains(option1));
        assertTrue(optionsList.contains(option2));
        assertTrue(optionsList.contains(option3));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option = new Option("option", "option", false, "Option description");
        option.setRequired(true);

        options.addOption(option);

        List<Option> requiredOptions = options.getRequiredOptions();

        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.contains(option));
    }

    @Test
    public void testParseWithNoOptions() throws Exception {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(new String[0]));

        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithShortOption() throws Exception {
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);

        String[] args = {"-o"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args));

        assertTrue(cmd.hasOption("o"));
    }

    @Test
    public void testParseWithLongOption() throws Exception {
        Option option = new Option("option", "option", false, "Option description");
        options.addOption(option);

        String[] args = {"--option"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args));

        assertTrue(cmd.hasOption("option"));
    }

    @Test
    public void testParseWithShortOptionAndValue() throws Exception {
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);

        String[] args = {"-o", "value"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args));

        String value = cmd.getOptionValue("o");
        assertEquals("value", value);
    }

    @Test
    public void testParseWithLongOptionAndValue() throws Exception {
        Option option = new Option("option", "option", true, "Option description");
        options.addOption(option);

        String[] args = {"--option", "value"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args));

        String value = cmd.getOptionValue("option");
        assertEquals("value", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidOption() throws Exception {
        Option option = new Option("o", "option", false, "Option description");
        options.addOption(option);

        String[] args = {"--invalid"};
        CommandLineParser parser = new DefaultParser();
        parser.parse(options.toBuilder().buildParser().parse(args));
    }

    @Test
    public void testParseWithOptionGroup() throws Exception {
        OptionGroup group = new OptionGroup("testGroup", "Test group options");

        Option option1 = new Option("o1", "option1", false, "Option 1 description");
        Option option2 = new Option("o2", "option2", false, "Option 2 description");
        Option option3 = new Option("o3", "option3", false, "Option 3 description");

        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        String[] args = {"--testGroup"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toBuilder().buildParser().parse(args));

        assertTrue(cmd.hasOption("o1"));
        assertTrue(cmd.hasOption("o2"));
    }

    @Test
    public void testHelpFormatter() throws Exception {
        OptionGroup group = new OptionGroup("testGroup", "Test group options");

        Option option1 = new Option("o1", "option1", false, "Option 1 description");
        Option option2 = new Option("o2", "option2", false, "Option 2 description");
        Option option3 = new Option("o3", "option3", false, "Option 3 description");

        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp(
                "Test",
                "This is a test for the Options class",
                options.toBuilder().buildParser().parse(new String[0]),
                1,
                true);

        assertTrue(help.contains("Test group options"));
        assertTrue(help.contains("option1"));
        assertTrue(help.contains("option2"));
        assertTrue(help.contains("option3"));
    }
}