
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.ArgumentImpl;
import org.apache.commons.cli2.option.OptionImpl;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option1 = new OptionImpl("option1");
        Option option2 = new OptionImpl("option2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();

        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentImpl("argument");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option, null);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);

        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionImpl("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new OptionImpl("option1");
        Option option2 = new OptionImpl("option2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertEquals(option1, commandLine.getOption("option1"));
        Assert.assertEquals(option2, commandLine.getOption("option2"));
    }

    @Test
    public void testGetValues() {
        Option option = new ArgumentImpl("argument");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValues(option, null);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        values = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new ArgumentImpl("argument");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getUndefaultedValues(option);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionImpl("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);

        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new OptionImpl("option"), new ArrayList());

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Assert.assertEquals("value1", commandLine.getProperty(new OptionImpl("option"), "property1", null));
        Assert.assertEquals("value2", commandLine.getProperty(new OptionImpl("option"), "property2", null));
        Assert.assertEquals(null, commandLine.getProperty(new OptionImpl("option"), "property3", null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new OptionImpl("option"), new ArrayList());

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Assert.assertEquals("value1", commandLine.getProperty("property1"));
        Assert.assertEquals("value2", commandLine.getProperty("property2"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new OptionImpl("option"), new ArrayList());

        commandLine.addProperty("property1", "value1");
        Set<String> properties = commandLine.getProperties();

        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new OptionImpl("option"), new ArrayList());

        Assert.assertTrue(commandLine.looksLikeOption("-option1"));
        Assert.assertTrue(commandLine.looksLikeOption("--option2"));
        Assert.assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testToString() {
        Option option = new ArgumentImpl("argument");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addValue(option, "value");
        String result = commandLine.toString();

        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionImpl("option1");
        Option option2 = new OptionImpl("option2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();

        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new OptionImpl("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.addOption(option);
        Set<String> triggers = commandLine.getOptionTriggers();

        Assert.assertEquals(1, triggers.size());
        Assert.assertTrue(triggers.contains("option"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new ArgumentImpl("argument");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValues(option, defaultValues);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));

        commandLine.setDefaultValues(option, null);
        values = commandLine.getValues(option, defaultValues);

        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);

        Assert.assertTrue(switchValue);

        commandLine.setDefaultSwitch(option, null);
        switchValue = commandLine.getSwitch(option, true);

        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = new ArgumentImpl("argument");
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        List<String> normalised = commandLine.getNormalised();

        Assert.assertEquals(arguments, normalised);
    }
}
