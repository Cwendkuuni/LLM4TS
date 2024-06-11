import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testConstructor() {
        Option rootOption = new Option("rootOption", "rootOption description");
        List arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Assert.assertEquals(rootOption, writeableCommandLine.getOption(0));
        Assert.assertEquals(arguments, writeableCommandLine.getNormalised());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        Assert.assertEquals(option, writeableCommandLine.getOption(0));
        Assert.assertEquals(Arrays.asList(option), writeableCommandLine.getOptions());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch", "switch description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("switch", "switch description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);

        Assert.assertEquals(option, writeableCommandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("switch", "switch description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addProperty("property", "property value");

        Properties properties = writeableCommandLine.getProperties();
        Assert.assertEquals("property value", properties.getProperty("property"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addProperty("property", "property value");

        Assert.assertEquals("property value", writeableCommandLine.getProperty("property", ""));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addProperty("property1", "property value1");
        writeableCommandLine.addProperty("property2", "property value2");

        Set properties = writeableCommandLine.getProperties();
        Assert.assertEquals(Arrays.asList("property1", "property2"), new ArrayList(properties));
    }

    @Test
    public void testToString() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        Assert.assertEquals("arg1 arg2", writeableCommandLine.toString());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        Assert.assertFalse(writeableCommandLine.looksLikeOption(""));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option"));
    }
}