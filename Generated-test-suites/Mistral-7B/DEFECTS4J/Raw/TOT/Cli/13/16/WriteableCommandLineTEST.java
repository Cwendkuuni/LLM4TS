import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parse.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CliOptions();
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        CommandLine commandLine = new CliOptions();
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator('=')
                .hasArg(true)
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test");

        Assert.assertEquals("test", commandLine.getOptionValue("option"));
    }

    @Test
    public void testGetValues() {
        CommandLine commandLine = new CliOptions();
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test1");
        commandLine.addValue(option, "test2");

        List<String> values = commandLine.getOptionValues("option");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1", values.get(0));
        Assert.assertEquals("test2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        CommandLine commandLine = new CliOptions();
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("test1");
        defaultValues.add("test2");
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(2, commandLine.getOptionValues("option").size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        CommandLine commandLine = new CliOptions();
        Option option = Option.builder("o").longOpt("option")
                .build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        CommandLine commandLine = new CliOptions();
        Option option = Option.builder("o").longOpt("option")
                .build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("option") && commandLine.isSet("option"));
    }

    @Test
    public void testAddProperty() {
        CommandLine commandLine = new CliOptions();
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        CommandLine commandLine = new CliOptions();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testParse() {
        DefaultParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(CliOptions.class, new String[]{"--option", "value1", "--option=value2"});
        Option option = commandLine.getOptionNamed("option");
        Assert.assertNotNull(option);
        List<String> values = option.getValues();
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOption() {
        DefaultParser parser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(CliOptions.class, new String[]{"--invalid"});
        } catch (ParseException e) {
            // Expected
        }
        Assert.assertNull(commandLine);
    }
}

class CliOptions {
}