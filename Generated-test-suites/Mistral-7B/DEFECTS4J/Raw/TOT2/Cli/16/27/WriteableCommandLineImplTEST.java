import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertNotNull(commandLine.getOption("o"));
        Assert.assertNotNull(commandLine.getOption("option"));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("a").valueSeparator('=').build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = Option.builder("s").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("h").longOpt("help").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("o");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("a").valueSeparator('=').build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("a").valueSeparator('=').build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitchFalse() {
        Option option = Option.builder("s").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("p").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        String property = commandLine.getProperty("p", "defaultValue");
        Assert.assertEquals("value", property);
    }

    @Test
    public void testGetProperties() {
        Option option = Option.builder("p").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(Option.builder("test").build());
        Assert.assertTrue(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testToString() {
        Option option = Option.builder("t").longOpt("test").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        String commandLineString = commandLine.toString();
        String expected = "testValue test";
        Assert.assertEquals(expected, commandLineString);
    }
}