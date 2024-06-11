import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class OptionsTest {

    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @After
    public void tearDown() {
        options = null;
        parser = null;
    }

    @Test
    public void testAddOption() {
        options.addOption("a", "optionA", false, "Option A");
        Assert.assertEquals("Option A", options.getOption("a").getLongOpt());
        Assert.assertFalse(options.getOption("a").hasArg());

        options.addOption("b", "optionB", true, "Option B with argument");
        Assert.assertEquals("optionB", options.getOption("b").getKey());
        Assert.assertEquals("Option B with argument", options.getOption("b").getLongOpt());
        Assert.assertTrue(options.getOption("b").hasArg());
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("group", "Group Options");
        options.addOptionGroup(group);

        group.addOption("g", "groupOptionA", false, "Group Option A");
        Assert.assertEquals("groupOptionA", options.getOption("g").getKey());
        Assert.assertEquals("Group Option A", options.getOption("g").getLongOpt());
        Assert.assertFalse(options.getOption("g").hasArg());
        Assert.assertEquals("Group Options", options.getOptionGroup("group").getLongOpt());

        group.addOption("h", "groupOptionB", true, "Group Option B with argument");
        Assert.assertEquals("groupOptionB", options.getOption("h").getKey());
        Assert.assertEquals("Group Option B with argument", options.getOption("h").getLongOpt());
        Assert.assertTrue(options.getOption("h").hasArg());
    }

    @Test
    public void testParse() {
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B with argument");

        String[] args = {"test", "--optionA", "valueA", "--optionB", "valueB"};
        try {
            CommandLine cmd = parser.parse(options, args);

            Assert.assertTrue(cmd.hasOption("optionA"));
            Assert.assertEquals("valueA", cmd.getOptionValue("optionA"));
            Assert.assertTrue(cmd.hasOption("optionB"));
            Assert.assertEquals("valueB", cmd.getOptionValue("optionB"));

        } catch (ParseException e) {
            Assert.fail("Parsing failed: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() {
        options.addOption("a", "optionA", false, "Option A");

        String[] args = {"test", "--invalid"};
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() {
        options.addOption("a", "optionA", false, "Option A");

        String[] args = {"test"};
        parser.parse(options, args);
    }

    @Test
    public void testGetRequiredOptions() {
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOptionGroup(new OptionGroup("group", "Group Options")
                .required(true)
                .addOption("g", "groupOption", false, "Group Option")
        );

        List<Option> requiredOptions = options.getOptions();
        List<Option> groupRequiredOptions = options.getOptionGroup("group").getOptions();

        Assert.assertEquals(3, requiredOptions.size());
        Assert.assertTrue(requiredOptions.get(0).isRequired());
        Assert.assertTrue(requiredOptions.get(1).isRequired());
        Assert.assertTrue(requiredOptions.get(2).isRequired());
        Assert.assertFalse(requiredOptions.get(0).hasArg());
        Assert.assertFalse(requiredOptions.get(1).hasArg());
        Assert.assertFalse(requiredOptions.get(2).hasArg());

        Assert.assertEquals(1, groupRequiredOptions.size());
        Assert.assertTrue(groupRequiredOptions.get(0).isRequired());
        Assert.assertFalse(groupRequiredOptions.get(0).hasArg());
    }
}