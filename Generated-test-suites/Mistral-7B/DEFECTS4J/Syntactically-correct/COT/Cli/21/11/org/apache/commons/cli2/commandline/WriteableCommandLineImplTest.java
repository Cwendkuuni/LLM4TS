import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("option", "option description");
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("option", "option description");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        Option option = writeableCommandLine.getOption("option");
        Assert.assertNotNull(option);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionNullTrigger() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = writeableCommandLine.getOption(null);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        writeableCommandLine.addValue(writeableCommandLine.getOption("option"), "value1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("option"), "value2");
        List values = writeableCommandLine.getValues(writeableCommandLine.getOption("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetValuesEmpty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        List values = writeableCommandLine.getValues(writeableCommandLine.getOption("option"), null);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetValuesDefault() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        writeableCommandLine.addValue(writeableCommandLine.getOption("option"), "value1");
        writeableCommandLine.setDefaultValues(writeableCommandLine.getOption("option"), new ArrayList("defaultValue"));
        List values = writeableCommandLine.getValues(writeableCommandLine.getOption("option"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("option"), true);
        Boolean switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchFalse() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("option"), false);
        Boolean switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("option"), true);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        writeableCommandLine.addProperty(writeableCommandLine.getOption("option"), "property", "value");
        String propertyValue = writeableCommandLine.getProperty(writeableCommandLine.getOption("option"), "property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetPropertyEmpty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        String propertyValue = writeableCommandLine.getProperty(writeableCommandLine.getOption("option"), "property");
        Assert.assertNull(propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        writeableCommandLine.addProperty(writeableCommandLine.getOption("option"), "property1", "value1");
        writeableCommandLine.addProperty(writeableCommandLine.getOption("option"), "property2", "value2");
        Set properties = writeableCommandline.getProperties(writeableCommandLine.getOption("option"));
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option", "option description"), new ArrayList());
        writeableCommandLine.addValue(writeableCommandLine.getOption("option"), "value1");
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("option"), true);
        writeableCommandLine.addProperty(writeableCommandLine.getOption("option"), "property", "value");
        String commandLineString = writeableCommandLine.toString();
        Assert.assertTrue(commandLineString.contains("value1"));
        Assert.assertTrue(commandLineString.contains("option"));
        Assert.assertTrue(commandLineString.contains("property=value"));
    }
}