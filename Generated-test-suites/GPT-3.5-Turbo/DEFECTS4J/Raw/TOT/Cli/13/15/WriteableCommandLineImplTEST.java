import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLineImpl;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root");
        List<Argument> arguments = new ArrayList<>();

        writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");

        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        Object value = "testValue";

        writeableCommandLine.addValue(option, value);

        List<Object> values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        boolean value = true;

        writeableCommandLine.addSwitch(option, value);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");

        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");

        writeableCommandLine.addOption(option);
        Option retrievedOption = writeableCommandLine.getOption("testOption");

        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");

        writeableCommandLine.addValue(option, "testValue");

        List<Object> values = writeableCommandLine.getValues(option, defaultValues);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        Boolean defaultValue = Boolean.TRUE;

        writeableCommandLine.addSwitch(option, false);

        Boolean switchValue = writeableCommandLine.getSwitch(option, defaultValue);
        Assert.assertEquals(Boolean.FALSE, switchValue);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";

        writeableCommandLine.addProperty(property, value);

        Assert.assertEquals(value, writeableCommandLine.getProperty(property, null));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-testOption";

        boolean looksLikeOption = writeableCommandLine.looksLikeOption(trigger);

        Assert.assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        CommandLineImpl commandLine = new CommandLineImpl(option1, arguments);
        writeableCommandLine = new WriteableCommandLineImpl(option2, arguments);

        String commandLineString = commandLine.toString();
        String writeableCommandLineString = writeableCommandLine.toString();

        Assert.assertEquals(commandLineString, writeableCommandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List<Option> options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Assert.assertTrue(writeableCommandLine.getOptionTriggers().contains(option1.getPreferredName()));
        for (String trigger : option1.getTriggers()) {
            Assert.assertTrue(writeableCommandLine.getOptionTriggers().contains(trigger));
        }
        Assert.assertTrue(writeableCommandLine.getOptionTriggers().contains(option2.getPreferredName()));
        for (String trigger : option2.getTriggers()) {
            Assert.assertTrue(writeableCommandLine.getOptionTriggers().contains(trigger));
        }

    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        List<Object> defaults = new ArrayList<>();
        defaults.add("defaultValue");

        writeableCommandLine.setDefaultValues(option, defaults);
        List<Object> retrievedDefaults = writeableCommandLine.getValues(option, null);

        Assert.assertEquals(defaults, retrievedDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        Boolean defaultSwitch = Boolean.TRUE;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean retrievedDefaultSwitch = writeableCommandLine.getSwitch(option, null);

        Assert.assertEquals(defaultSwitch, retrievedDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        CommandLineImpl commandLine = new CommandLineImpl(option1, arguments);
        writeableCommandLine = new WriteableCommandLineImpl(option2, arguments);

        List<String> commandLineNormalised = commandLine.getNormalised();
        List<String> writeableCommandLineNormalised = writeableCommandLine.getNormalised();

        Assert.assertEquals(commandLineNormalised, writeableCommandLineNormalised);
    }
}