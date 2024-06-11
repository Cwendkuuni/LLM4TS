import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
        commandLine.addOption(new Option("opt2", "option2"));

        Option option = commandLine.getOption("opt2");
        Assert.assertNotNull(option);
        Assert.assertEquals("option2", option.getLongOpt());
        Assert.assertFalse(commandLine.hasOption(new Option("opt")));
        Assert.assertTrue(commandLine.hasOption(option));

        // Test triggering with different prefixes
        Assert.assertTrue(commandLine.looksLikeOption("--option2"));
        Assert.assertTrue(commandLine.looksLikeOption("--opt2"));
        Assert.assertTrue(commandLine.looksLikeOption("-o"));
        Assert.assertTrue(commandLine.looksLikeOption("-opt2"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        commandLine.addOption(new Option("opt", "option"));
        commandLine.addValue(commandLine.getOption("opt"), "value1");
        commandLine.addValue(commandLine.getOption("opt"), "value2");

        List values = commandLine.getValues(commandLine.getOption("opt"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
        commandLine.addOption(new Option("switch", "switch"));
        commandLine.addSwitch(commandLine.getOption("switch"), true);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("switch"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
        commandLine.addOption(new Option("switch", "switch"));
        commandLine.addSwitch(commandLine.getOption("switch"), true);
        commandLine.addSwitch(commandLine.getOption("switch"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
        Assert.assertFalse(commandLine.hasOption(new Option("opt2", "option2")));
        commandLine.addOption(new Option("opt2", "option2"));
        Assert.assertTrue(commandLine.hasOption(new Option("opt2")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
        commandLine.addOption(new Option("opt", "option"));

        Option option = commandLine.getOption("opt");
        Assert.assertNotNull(option);
        Assert.assertEquals("option", option.getShortOpt());
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals("option", option.getName());
        Assert.assertEquals("option", option.getDescription());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        commandLine.addOption(new Option("opt", "option"));
        commandLine.addValue(commandLine.getOption("opt"), "value1");
        commandLine.addValue(commandLine.getOption("opt"), "value2");

        List values = commandLine.getValues(commandLine.getOption("opt"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        commandLine.addOption(new Option("opt", "option"));
        commandLine.addValue(commandLine.getOption("opt"), "value1");
        commandLine.addValue(commandLine.getOption("opt"), "value2");

        List values = commandLine.getUndefaultedValues(commandLine.getOption("opt"));
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
        commandLine.addOption(new Option("switch", "switch"));
        commandLine.addSwitch(commandLine.getOption("switch"), true);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("switch"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("prop", "property"), Collections.emptyList());
        commandLine.addOption(new PropertyOption("property", "property", "defaultValue", "description"));
        commandLine.addProperty(commandLine.getOption("prop"), "value1");

        String propertyValue = commandLine.getProperty(commandLine.getOption("prop"), "defaultValue");
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("prop", "property"), Collections.emptyList());
        commandLine.addOption(new PropertyOption("property", "property", "defaultValue", "description"));
        commandLine.addProperty(commandLine.getOption("prop"), "value1");

        Properties properties = commandLine.getProperties(commandLine.getOption("prop"));
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("property", properties.stringPropertyNames().nextElement());
        Assert.assertEquals("value1", properties.getProperty("property"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        commandLine.addOption(new Option("opt", "option"));
        commandLine.addValue(commandLine.getOption("opt"), "value1");
        commandLine.addValue(commandLine.getOption("opt"), "value2");

        String commandLineString = commandLine.toString();
        String expected = "--option value1 value2";
        Assert.assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
        commandLine.addOption(new Option("opt", "option"));

        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Option option = (Option) options.get(0);
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals("option", option.getName());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
        commandLine.addOption(new Option("opt", "option"));
        commandLine.addOption(new Option("opt2", "option2"));

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("opt"));
        Assert.assertTrue(optionTriggers.contains("opt2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
        commandLine.addOption(new Option("opt", "option"));
        List defaults = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(commandLine.getOption("opt"), defaults);

        List values = commandLine.getValues(commandLine.getOption("opt"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
        commandLine.addOption(new Option("switch", "switch"));
        commandLine.setDefaultSwitch(commandLine.getOption("switch"), true);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("switch"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        commandLine.addOption(new Option("opt", "option"));
        commandLine.addValue(commandLine.getOption("opt"), "value1");
        commandLine.addValue(commandLine.getOption("opt"), "value2");

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(4, normalised.size());
        Assert.assertEquals("--option", ((String) normalised.get(0)).substring(1));
        Assert.assertEquals("value1", ((String) normalised.get(1)));
        Assert.assertEquals("value2", ((String) normalised.get(2)));
        Assert.assertEquals("", ((String) normalised.get(3)));
    }
}