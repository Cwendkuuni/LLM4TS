import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Assert.assertEquals(Collections.singletonList(option), commandLine.getOptions());
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option")
                .required(true)
                .valueSeparator('=')
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        Assert.assertEquals(Arrays.asList("value1"), commandLine.getValues(option, null));

        commandLine.addValue(option, "value2");
        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addSwitch(option, false);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        Option result = commandLine.getOption("o");
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);

        commandLine.addValue(option, "value3");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2", "value3"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Collections.singletonList("value1"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getProperty(option, ""));
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        Properties properties = commandLine.optionToProperties.get(option);
        Assert.assertNull(properties);

        commandLine.addProperty(option, "property1", "value1");
        properties = commandLine.optionToProperties.get(option);
        Assert.assertNotNull(properties);
        Assert.assertEquals("value1", properties.getProperty("property1"));

        commandLine.addProperty(option, "property2", "value2");
        properties = commandLine.optionToProperties.get(option);
        Assert.assertEquals("value1, value2", properties.getProperty("property"));
    }

    @Test
    public void testToString() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        Assert.assertEquals("option value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptionsList() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertEquals(Arrays.asList("o1", "o2"), commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        List defaultValues = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = new String[]{"-o", "value1", "--option", "value2"};

        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .valueSeparator('=')
                .build();

        WriteableCommandLineParser parser = new WriteableCommandLineParser();
        WriteableCommandLine commandLine = parser.parse(option, args);

        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOptionValue() throws ParseException {
        String[] args = new String[]{"-o", "value1"};

        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .valueSeparator('=')
                .build();

        WriteableCommandLineParser parser = new WriteableCommandLineParser();
        WriteableCommandLine commandLine = parser.parse(option, args);

        Assert.assertEquals(Collections.emptyList(), commandLine.getValues(option, null));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        String[] args = new String[]{"value1"};

        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineParser parser = new WriteableCommandLineParser();
        WriteableCommandLine commandLine = parser.parse(option, args);

        Assert.assertEquals(Collections.emptyList(), commandLine.getOptions());
    }

    @Test
    public void testHelpFormatter() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", commandLine.getOptions(), true, true);
    }

    @Test
    public void testInvalidCommandLine() throws ParseException {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        WriteableCommandLineParser parser = new WriteableCommandLineParser();
        parser.parse(commandLine, new String[]{"-o=value1", "--option=value2"});

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testPropertyOption() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Properties properties = commandLine.optionToProperties.get(option);
        Assert.assertEquals("value1, value2", properties.getProperty("property"));
    }
}