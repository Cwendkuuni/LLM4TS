import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b", true, "desc");
        writeableCommandLine.addOption(option);
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
        Assert.assertEquals(option, writeableCommandLine.getOption(writeableCommandLine.getOptions().get(0).getLongOpt()));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b", true, "desc");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b", false, "desc");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b", false, "desc");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b", true, "desc");
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b", true, "desc");
        writeableCommandLine.addOption(option);
        Assert.assertEquals(option, writeableCommandLine.getOption("b"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b", true, "desc");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b", true, "desc");
        writeableCommandLine.addOption(option);
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b", false, "desc");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
        switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("p", "p", true, "property");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty(option, "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandlineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("p", "p", true, "property");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");
        Set properties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        writeableCommandLine.addOption(new Option("b", "b", false, "desc"));
        writeableCommandLine.addOption(new Option("c", "c", true, "desc"));
        writeableCommandLine.addValue(new Option("b"), "arg1");
        writeableCommandLine.addValue(new Option("c"), "arg2");
        String commandLineString = writeableCommandLine.toString();
        String expected = "arg1 b arg2 c";
        Assert.assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option1 = new Option("b", "b", false, "desc1");
        Option option2 = new Option("c", "c", false, "desc2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option1 = new Option("b", "b", false, "desc1");
        Option option2 = new Option("c", "c", false, "desc2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("b", "c")));
    }
}