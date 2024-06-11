import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for the WriteableCommandLineImpl class.
 */
public class WriteableCommandLineImplTest {

    /**
     * Test method for {@link WriteableCommandLineImpl#WriteableCommandLineImpl(Option, List)} constructor.
     */
    @Test
    public void testWriteableCommandLineImplConstruction() {
        Option option = new Option("i", "input", true, "input file").required(false);
        List arguments = Arrays.asList("test.txt");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals(option, commandLine.getOption(0));
        Assert.assertEquals(arguments, commandLine.getArguments());
    }

    /**
     * Test method for {@link WriteableCommandLine#addOption(Option)} method.
     */
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("i", "input", true), new ArrayList<>());

        Option option = new Option("o", "output", true, "output file");
        commandLine.addOption(option);

        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#addValue(Option, Object)} method.
     */
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("i", "input", true), new ArrayList<>());

        commandLine.addValue(commandLine.getOption(0), "test.txt");

        List values = commandLine.getValues(commandLine.getOption(0), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test.txt", values.get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)} method.
     */
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("v", "verbose", false), new ArrayList<>());

        commandLine.addSwitch(commandLine.getOption(0), true);

        Assert.assertTrue(commandLine.getSwitch(commandLine.getOption(0), false));
    }

    /**
     * Test method for {@link WriteableCommandLine#hasOption(Option)} method.
     */
    @Test
    public void testHasOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("i", "input", true), new ArrayList<>());

        Option option = new Option("o", "output", true);

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    /**
     * Test method for {@link WriteableCommandLine#getOption(String)} method.
     */
    @Test
    public void testGetOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("i", "input", true), new ArrayList<>());

        Option option = new Option("o", "output", true);

        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    /**
     * Test method for {@link WriteableCommandLine#getValues(Option, List)} method.
     */
    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("i", "input", true), new ArrayList<>());

        commandLine.addValue(commandLine.getOption(0), "test.txt");
        commandLine.addValue(commandLine.getOption(0), "output.txt");

        List values = commandLine.getValues(commandLine.getOption(0), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test.txt", values.get(0));
        Assert.assertEquals("output.txt", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#getUndefaultedValues(Option)} method.
     */
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("i", "input", true), new ArrayList<>());
        commandLine.addValue(commandLine.getOption(0), "test.txt");

        List values = commandLine.getUndefaultedValues(commandLine.getOption(0));
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test.txt", values.get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#getSwitch(Option, Boolean)} method.
     */
    @Test
    public void testGetSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("v", "verbose", false), new ArrayList<>());

        commandLine.addSwitch(commandLine.getOption(0), true);

        Assert.assertTrue(commandLine.getSwitch(commandLine.getOption(0), false));
    }

    /**
     * Test method for {@link WriteableCommandLine#getProperty(String)} method.
     */
    @Test
    public void testGetProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("p", "property", true), new ArrayList<>());
        commandLine.addProperty(commandLine.getOption(0), "property", "value");

        Assert.assertEquals("value", commandLine.getProperty(commandLine.getOption(0), "defaultValue"));
    }

    /**
     * Test method for {@link WriteableCommandLine#addProperty(Option, String, String)} method.
     */
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("p", "property", true), new ArrayList<>());

        commandLine.addProperty(commandLine.getOption(0), "property1", "value1");
        commandLine.addProperty(commandLine.getOption(0), "property2", "value2");

        Map<String, String> properties = commandLine.getProperties(commandLine.getOption(0));
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("value1", properties.get("property1"));
        Assert.assertEquals("value2", properties.get("property2"));
    }

    /**
     * Test method for {@link WriteableCommandLine#getProperty(Option, String, String)} method.
     */
    @Test
    public void testGetPropertyWithDefaultValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("p", "property", true), new ArrayList<>());
        commandLine.addProperty(commandLine.getOption(0), "property", "defaultValue");

        Assert.assertEquals("defaultValue", commandLine.getProperty(commandLine.getOption(0), null));
    }

    /**
     * Test method for {@link WriteableCommandLine#getProperties()} method.
     */
    @Test
    public void testGetPropertiesWithOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("p", "property", true), new ArrayList<>());
        commandLine.addProperty(commandLine.getOption(0), "property1", "value1");
        commandLine.addProperty(commandLine.getOption(0), "property2", "value2");

        Map<String, String> properties = commandLine.getProperties(null);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("value1", properties.get("property.0"));
        Assert.assertEquals("value2", properties.get("property.1"));
    }

    /**
     * Test method for {@link WriteableCommandLine#toString()} method.
     */
    @Test
    public void testToString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("i", "input", true), new ArrayList<>());
        commandLine.addValue(commandLine.getOption(0), "test.txt");

        String cmdLineString = commandLine.toString();
        List cmdLineArgs = Arrays.asList(cmdLineString.split(" "));

        Assert.assertEquals(2, cmdLineArgs.size());
        Assert.assertEquals("test.txt", cmdLineArgs.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#getOptions()} method.
     */
    @Test
    public void testGetOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("i", "input", true), new ArrayList<>());
        Option option = new Option("o", "output", true);
        commandLine.addOption(option);

        List options = commandLine.getOptions();

        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option, options.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#getOptionTriggers()} method.
     */
    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("i", "input", true), new ArrayList<>());
        Option option1 = new Option("o1", "output1", true);
        Option option2 = new Option("o2", "output2", true);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();

        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("i"));
        Assert.assertTrue(optionTriggers.contains("o1"));
        Assert.assertTrue(optionTriggers.contains("o2"));
    }
}