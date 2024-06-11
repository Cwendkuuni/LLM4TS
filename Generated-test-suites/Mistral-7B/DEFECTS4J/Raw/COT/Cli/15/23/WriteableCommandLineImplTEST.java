import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("a", "alpha", true, "Description of option a");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addOption(option);
        Assert.assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addValue(option, "value1");
        Assert.assertEquals(Arrays.asList("value1"), cmdLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addSwitch(option, true);
        Assert.assertTrue(cmdLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addSwitch(option, true);
        cmdLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addOption(option);
        Assert.assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addOption(option);
        Assert.assertEquals(option, cmdLine.getOption("a"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        Assert.assertEquals(Arrays.asList("value1", "value2"), cmdLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addValue(option, "value1");
        Assert.assertEquals(Arrays.asList("value1"), cmdLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addSwitch(option, true);
        Assert.assertTrue(cmdLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", cmdLine.getProperty(option, "property", null));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList());
        cmdLine.addProperty(option, "property1", "value1");
        cmdLine.addProperty(option, "property2", "value2");
        Map<String, String> properties = cmdLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("value1", properties.get("property1"));
        Assert.assertEquals("value2", properties.get("property2"));
    }

    @Test
    public void testToString() {
        Option option = new Option("a", "alpha");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        cmdLine.addValue(option, "value1");
        String expected = "arg1 arg2 --alpha value1";
        String actual = cmdLine.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "alpha");
        Option option2 = new Option("b", "beta");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option1, Arrays.asList());
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        List<Option> options = cmdLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("a", "alpha");
        Option option2 = new Option("b", "beta");
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option1, Arrays.asList());
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        Set<String> triggers = cmdLine.getOptionTriggers();
        Assert.assertEquals(2, triggers.size());
        Assert.assertTrue(triggers.containsAll(Arrays.asList("a", "b")));
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithInvalidOption() {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("a", "alpha");
        options.addOption(option);
        try {
            WriteableCommandLine cmdLine = parser.parse(options, Arrays.asList("--invalid"));
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithValidOption() {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("a", "alpha");
        options.addOption(option);
        try {
            WriteableCommandLine cmdLine = parser.parse(options, Arrays.asList("--alpha", "value"));
            List<Object> values = cmdLine.getValues(option, null);
            Assert.assertEquals(1, values.size());
            Assert.assertEquals("value", values.get(0));
        } catch (ParseException e) {
            // should not reach here
        }
    }

    @Test
    public void testParseWithSwitchOption() {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("a", "alpha");
        options.addOption(option);
        try {
            WriteableCommandLine cmdLine = parser.parse(options, Arrays.asList("--alpha"));
            Boolean switchValue = cmdLine.getSwitch(option, false);
            Assert.assertTrue(switchValue);
        } catch (ParseException e) {
            // should not reach here
        }
    }

    @Test
    public void testParseWithPropertyOption() {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("a", "alpha");
        options.addOption(option);
        try {
            WriteableCommandLine cmdLine = parser.parse(options, Arrays.asList("--alpha", "property=value"));
            Map<String, String> properties = cmdLine.getProperties(option);
            Assert.assertEquals(1, properties.size());
            Assert.assertEquals("value", properties.get("property"));
        } catch (ParseException e) {
            // should not reach here
        }
    }
}