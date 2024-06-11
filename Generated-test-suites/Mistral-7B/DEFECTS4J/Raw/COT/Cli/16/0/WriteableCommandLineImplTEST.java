import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("o", "option"), new ArrayList());
        cmd.addOption(new Option("t", "test"));

        Option option = new Option("t", "test2");
        cmd.addOption(option);

        List options = cmd.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertEquals(option.getLongOpt(), options.get(2).getLongOpt());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
        cmd.addOption(new Option("a", "arg"));
        cmd.addValue(cmd.getOption("a"), "value1");
        cmd.addValue(cmd.getOption("a"), "value2");

        List values = cmd.getValues(cmd.getOption("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("s", "switch"), new ArrayList());
        cmd.addSwitch(cmd.getOption("s"), true);
        cmd.addSwitch(cmd.getOption("s"), false);

        Boolean switchValue = cmd.getSwitch(cmd.getOption("s"), false);
        Assert.assertEquals(true, switchValue.booleanValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("s", "switch"), new ArrayList());
        cmd.addSwitch(cmd.getOption("s"), true);
        cmd.addSwitch(cmd.getOption("s"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("o", "option"), new ArrayList());
        cmd.addOption(new Option("t", "test"));

        boolean hasOption = cmd.hasOption(cmd.getOption("t"));
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("o", "option"), new ArrayList());
        cmd.addOption(new Option("t", "test"));

        Option option = cmd.getOption("t");
        Assert.assertNotNull(option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
        cmd.addOption(new Option("a", "arg"));
        cmd.addValue(cmd.getOption("a"), "value1");
        cmd.addValue(cmd.getOption("a"), "value2");

        List values = cmd.getValues(cmd.getOption("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList());
        cmd.addOption(new Option("a", "arg"));

        List values = cmd.getUndefaultedValues(cmd.getOption("a"));
        Assert.assertTrue(values.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("s", "switch"), new ArrayList());
        cmd.addSwitch(cmd.getOption("s"), true);

        Boolean switchValue = cmd.getSwitch(cmd.getOption("s"), false);
        Assert.assertTrue(switchValue.booleanValue());
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("p", "property"), new ArrayList());
        cmd.addProperty(cmd.getOption("p"), "property", "value1");

        String property = cmd.getProperty(cmd.getOption("p"), "defaultValue");
        Assert.assertEquals("value1", property);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("p", "property"), new ArrayList());
        Properties properties = new HashMap();
        properties.setProperty("key1", "value1");
        cmd.addProperty(cmd.getOption("p"), "property", properties);

        Map propertiesMap = cmd.getProperties(cmd.getOption("p"));
        Assert.assertEquals(1, propertiesMap.size());
        Assert.assertEquals("key1", propertiesMap.keySet().iterator().next());
        Assert.assertEquals("value1", propertiesMap.get("key1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("c", "command"), new ArrayList());
        cmd.addOption(new Option("a", "arg"));
        cmd.addValue(cmd.getOption("a"), "value");
        cmd.addSwitch(cmd.getOption("a"), true);

        String expected = "-c arg=\"value\" -a";
        String actual = cmd.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("o", "option"), new ArrayList());
        cmd.addOption(new Option("t", "test"));

        List options = cmd.getOptions();
        Assert.assertEquals(1, options.size());
        Option option = (Option) options.get(0);
        Assert.assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("o", "option"), new ArrayList());
        cmd.addOption(new Option("t", "test"));

        Set optionTriggers = cmd.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertEquals("t", optionTriggers.iterator().next());
    }
}