import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        writeableCommandLine.addOption(option);

        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
        Assert.assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("f", "file", true, "File path");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "test.txt");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("v", "verbose", false, "Print verbose output");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("v", "verbose", false, "Print verbose output");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        writeableCommandLine.addOption(option);

        Assert.assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("f", "file", true, "File path");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "test.txt");
        writeableCommandLine.addValue(option, "sample.txt");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test.txt", values.get(0));
        Assert.assertEquals("sample.txt", values.get(1));

        values = writeableCommandLine.getValues(option, new ArrayList<>());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test.txt", values.get(0));
        Assert.assertEquals("sample.txt", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("v", "verbose", false, "Print verbose output");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine =
                new WriteableCommandLineImpl(new Option(), new ArrayList<>());

        writeableCommandLine.addProperty("api.version", "2.1.0");

        Assert.assertEquals("2.1.0", writeableCommandLine.getProperty("api.version", ""));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addProperty("api.version", "2.1.0");

        Set properties = writeableCommandline.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.containsAll(Map.of("api.version", "2.1.0").entrySet()));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addOption(new Option("f", "file", true, "File path"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("file"), "test.txt");

        Assert.assertEquals("file test.txt", writeableCommandLine.toString());
    }

    @Test
    public void testGetListOfOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addOption(new Option("t", "test", false, "Test flag"));
        writeableCommandLine.addOption(new Option("f", "file", true, "File path"));

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addOption(new Option("t", "test", false, "Test flag"));
        writeableCommandLine.addOption(new Option("f", "file", true, "File path"));

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Map.of("t", "test").entrySet()));
        Assert.assertTrue(optionTriggers.containsAll(Map.of("f", "file").entrySet()));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("f", "file", true, "File path");
        writeableCommandLine.addOption(option);

        List defaultValues = new ArrayList();
        defaultValues.add("test.txt");
        defaultValues.add("sample.txt");

        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test.txt", values.get(0));
        Assert.assertEquals("sample.txt", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("v", "verbose", false, "Print verbose output");
        writeableCommandLine.addOption(option);

        Boolean defaultSwitch = true;

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addOption(new Option("f", "file", true, "File path"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("file"), "test.txt");

        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals("file test.txt", normalised.get(0));
    }
}