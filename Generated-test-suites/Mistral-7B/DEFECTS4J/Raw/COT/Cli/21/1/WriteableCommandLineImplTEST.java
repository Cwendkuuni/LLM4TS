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
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionB", "optionB", false));

        Option optionB = writeableCommandline.getOption("optionB");
        Assert.assertNotNull(optionB);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", true));
        writeableCommandLine.addValue(new Option("optionA"), "value1");

        List values = writeableCommandLine.getValues(new Option("optionA"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", false));
        writeableCommandLine.addSwitch(new Option("optionA"), true);

        Boolean switchValue = writeableCommandLine.getSwitch(new Option("optionA"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", false));
        writeableCommandLine.addSwitch(new Option("optionA"), true);
        writeableCommandLine.addSwitch(new Option("optionA"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", false));

        boolean hasOption = writeableCommandLine.hasOption(new Option("optionA"));
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", false));

        Option option = writeableCommandLine.getOption("optionA");
        Assert.assertNotNull(option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", true), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", true));
        writeableCommandLine.addValue(new Option("optionA"), "value1");
        writeableCommandLine.addValue(new Option("optionA"), "value2");

        List values = writeableCommandLine.getValues(new Option("optionA"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", true), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", true));
        writeableCommandLine.addValue(new Option("optionA"), "value1");

        List values = writeableCommandLine.getUndefaultedValues(new Option("optionA"));
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", false));
        writeableCommandLine.addSwitch(new Option("optionA"), true);

        Boolean switchValue = writeableCommandLine.getSwitch(new Option("optionA"), false);
        Assert.assertTrue(switchValue);

        switchValue = writeableCommandLine.getSwitch(new Option("optionA"), null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchDefaultValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", false));

        Boolean switchValue = writeableCommandLine.getSwitch(new Option("optionA"), false);
        Assert.assertNull(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new PropertyOption("property", "property", true));
        writeableCommandLine.addProperty(new PropertyOption("property"), "value");

        String propertyValue = writeableCommandLine.getProperty("property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new PropertyOption("property1", "property1", true));
        writeableCommandLine.addOption(new PropertyOption("property2", "property2", true));
        writeableCommandLine.addProperty(new PropertyOption("property1"), "value1");
        writeableCommandLine.addProperty(new PropertyOption("property2"), "value2");

        Set properties = writeableCommandLine.getProperties(new PropertyOption());
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", false));
        writeableCommandLine.addValue(new Option("optionA"), "value1");
        writeableCommandLine.addValue(new Option("optionA"), "value2");

        String commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("value1 value2", commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", false));
        writeableCommandLine.addOption(new Option("optionB", "optionB", false));

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(new Option("optionA")));
        Assert.assertTrue(options.contains(new Option("optionB")));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "optionA", false), new ArrayList());
        writeableCommandLine.addOption(new Option("optionA", "optionA", false));
        writeableCommandLine.addOption(new Option("optionB", "optionB", false));

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("optionA"));
        Assert.assertTrue(optionTriggers.contains("optionB"));
    }
}