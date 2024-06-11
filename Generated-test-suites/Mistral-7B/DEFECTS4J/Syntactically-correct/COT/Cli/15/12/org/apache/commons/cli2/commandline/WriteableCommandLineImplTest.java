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
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", true, "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        Option returnedOption = writeableCommandLine.getOption("opt1");
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", true, "o1");
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
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", true, "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchFalseByDefault() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value1");
        String propertyValue = writeableCommandLine.getProperty(option, "property");
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");
        Map properties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("property1", properties.keySet().iterator().next());
        Assert.assertEquals("value1", properties.get("property1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option1", "opt1", false, "o1"));
        writeableCommandLine.addOption(new Option("option2", "opt2", false, "o2"));
        String commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("o1 o2", commandLineString);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Assert.assertTrue(writeableCommandLine.looksLikeOption("o1"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("foo"));
    }
}