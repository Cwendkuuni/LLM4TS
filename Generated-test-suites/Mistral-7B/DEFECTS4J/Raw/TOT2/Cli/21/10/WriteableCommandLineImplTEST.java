import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("b", "b", false, "Short for option b");
        commandLine.addOption(option);

        // Check if the option was added to the CommandLine
        Assert.assertTrue(commandLine.hasOption(option));

        // Check if the option is in the list of options
        Assert.assertEquals(option, commandLine.getOption("b"));

        // Check if the option is in the map of names to options
        Assert.assertEquals(option, commandLine.nameToOption.get("b"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "a", true, "Option A");
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "a", false, "Option A");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "a", false, "Option A");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "a", false, "Option A");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertFalse(commandLine.hasOption(new Option("b", "b", false, "Option B")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "a", false, "Option A");
        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption("a"));
        Assert.assertNull(commandLine.getOption("b"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "a", true, "Option A");
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals(1, values.size());

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals(2, values.size());
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "a", true, "Option A");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List unfaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Collections.singletonList("value1"), unfaultedValues);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "a", false, "Option A");
        commandLine.addOption(option);

        Assert.assertNull(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("p", "p", false, "Option P");
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("p", "p", false, "Option P");
        commandLine.addOption(option);

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());

        Assert.assertTrue(commandLine.looksLikeOption("--a"));
        Assert.assertTrue(commandLine.looksLikeOption("-a"));
        Assert.assertFalse(commandLine.looksLikeOption("--notAnOption"));
        Assert.assertFalse(commandLine.looksLikeOption("a"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a", "a", false, "Option A"));
        commandLine.addOption(new Option("b", "b", false, "Option B"));

        String commandLineString = commandLine.toString();
        Assert.assertEquals("-a --b", commandLineString);
    }
}