import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("b", "optionB"));

        Option optionB = commandLine.getOption("b");
        Assert.assertNotNull(optionB);
        Assert.assertEquals("optionB", optionB.getLongOpt());
        Assert.assertFalse(optionB.isRequired());
        Assert.assertEquals(0, optionB.getValues().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA", true, "valueA"));
        commandLine.addValue(new Option("a", "optionA"), "valueB");

        List values = commandLine.getValues(new Option("a", "optionA"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("valueA", values.get(0));
        Assert.assertEquals("valueB", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA", false));
        commandLine.addSwitch(new Option("a", "optionA"), true);

        Boolean switchValue = commandLine.getSwitch(new Option("a", "optionA"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA", false));
        commandLine.addSwitch(new Option("a", "optionA"), true);
        commandLine.addSwitch(new Option("a", "optionA"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA"));

        Assert.assertTrue(commandLine.hasOption(new Option("a", "optionA")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA"));

        Option optionA = commandLine.getOption("a");
        Assert.assertEquals("optionA", optionA.getLongOpt());
        Assert.assertFalse(optionA.isRequired());
        Assert.assertEquals(0, optionA.getValues().size());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), null);
        commandLine.addValue(new Option("a", "optionA"), "value1");
        commandLine.addValue(new Option("a", "optionA"), "value2");

        List values = commandLine.getValues(new Option("a", "optionA"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), null);
        commandLine.addValue(new Option("a", "optionA"), "value1");

        List values = commandLine.getUndefaultedValues(new Option("a", "optionA"));
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA", false));
        commandLine.addSwitch(new Option("a", "optionA"), false);

        Boolean switchValue = commandLine.getSwitch(new Option("a", "optionA"), false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetSwitchDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA", false));

        Boolean switchValue = commandLine.getSwitch(new Option("a", "optionA"), true);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new PropertyOption("p", "property", "defaultValue"));
        commandLine.addProperty(new PropertyOption("p", "property"), "testValue");

        String propertyValue = commandLine.getProperty("property");
        Assert.assertEquals("testValue", propertyValue);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new PropertyOption("p", "property"));

        String propertyValue = commandLine.getProperty("property");
        Assert.assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new PropertyOption("p1", "property1"));
        commandLine.addOption(new PropertyOption("p2", "property2"));
        commandLine.addProperty(new PropertyOption("p1", "property1"), "value1");
        commandLine.addProperty(new PropertyOption("p2", "property2"), "value2");

        Set properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA"));
        commandLine.addOption(new Option("b", "optionB"));
        commandLine.addValue(new Option("a", "optionA"), "value1");
        commandLine.addValue(new Option("a", "optionA"), "value2");

        String commandLineString = commandLine.toString();
        String expected = "'optionA' 'optionB' value1 value2";
        Assert.assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA"));
        commandLine.addOption(new Option("b", "optionB"));

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(Option.class, options.get(0).getClass());
        Assert.assertEquals(Option.class, options.get(1).getClass());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA"));
        commandLine.addOption(new Option("b", "optionB"));

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("a"));
        Assert.assertTrue(optionTriggers.contains("b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithInvalidTrigger() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA"));

        commandLine.addOption(new Option("", "optionB"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithInvalidOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "optionA"), null);
        commandLine.addOption(new Option("a", "optionA"));

        commandLine.addValue(new Option("b", "optionB"), "value");
    }

    @Test
    public void testParseCommandLine() {
        Option optionA = new Option("a", "optionA", false, "aValue");
        Option optionB = new Option("b", "optionB", true, "bValue");
        Option optionC = new Option("c", "optionC", false);

        Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        String[] args = {"--optionA", "--optionB", "aValue1", "aValue2", "--c"};

        CommandLineParser parser = new CommandLineParser();
        try {
            WriteableCommandLine commandLine = parser.parse(options, args);
            Assert.assertTrue(commandLine instanceof WriteableCommandLineImpl);

            Assert.assertEquals(3, commandLine.getOptions().size());
            Assert.assertEquals(2, commandLine.getValues(optionA, null).size());
            Assert.assertEquals("aValue1", commandLine.getValues(optionA, null).get(0));
            Assert.assertEquals("aValue2", commandLine.getValues(optionA, null).get(1));
            Assert.assertFalse(commandLine.hasOption(optionB));
            Assert.assertEquals(true, commandLine.getSwitch(optionC, false));

        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}