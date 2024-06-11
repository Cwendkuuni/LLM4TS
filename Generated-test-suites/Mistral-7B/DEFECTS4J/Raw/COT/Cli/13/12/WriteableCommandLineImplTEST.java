import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        Option option = new Option("b", "bool");
        writeableCommandLine.addOption(option);

        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
        Assert.assertEquals(option, writeableCommandLine.getOption("b"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        writeableCommandLine.addOption(new Option("a", "arg"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "value1");

        List values = writeableCommandLine.getValues(writeableCommandLine.getOption("a"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        writeableCommandLine.addOption(new Option("a", "arg"));
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("a"), true);

        Boolean switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        writeableCommandLine.addOption(new Option("a", "arg"));
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("a"), true);
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("a"), false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);

        Assert.assertEquals(option, writeableCommandLine.getOption("a"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "value1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "value2");

        List values = writeableCommandLine.getValues(writeableCommandLine.getOption("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("a"), true);

        Boolean switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        writeableCommandLine.addProperty("property", "value");

        String propertyValue = writeableCommandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        Set properties = writeableCommandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        writeableCommandLine.addOption(new Option("a", "arg"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "value1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "value2");

        String commandLineString = writeableCommandLine.toString();
        String expected = "'a' arg value1 value2";
        Assert.assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptionsList() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        Option option1 = new Option("a", "arg1");
        Option option2 = new Option("b", "bool");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List optionsList = writeableCommandLine.getOptions();
        Assert.assertEquals(2, optionsList.size());
        Assert.assertEquals(option1, optionsList.get(0));
        Assert.assertEquals(option2, optionsList.get(1));
    }

    @Test
    public void testGetOptionTriggersSet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        Option option1 = new Option("a", "arg1");
        Option option2 = new Option("b", "bool");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("a"));
        Assert.assertTrue(optionTriggers.contains("b"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultValues(option, Arrays.asList("value1", "value2"));

        List defaultValues = writeableCommandLine.getValues(writeableCommandLine.getOption("a"), null);
        Assert.assertEquals(2, defaultValues.size());
        Assert.assertEquals("value1", defaultValues.get(0));
        Assert.assertEquals("value2", defaultValues.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);

        Boolean defaultSwitch = writeableCommandLine.getSwitch(writeableCommandLine.getOption("a"), false);
        Assert.assertTrue(defaultSwitch);
    }

    @Test
    public void testGetNormalisedList() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), null);
        writeableCommandLine.addOption(new Option("a", "arg"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "value1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("a"), "value2");

        List normalisedList = writeableCommandLine.getNormalised();
        String expected = "'a' arg value1 value2";
        Assert.assertEquals(expected, normalisedList.toString());
    }
}