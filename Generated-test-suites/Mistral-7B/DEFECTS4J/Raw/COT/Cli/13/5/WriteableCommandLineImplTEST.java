import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("b"));

        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("b"));
        writeableCommandLine.addOption(new Option("b"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("a"));
        writeableCommandLine.addValue(new Option("a"), "value1");
        writeableCommandLine.addValue(new Option("a"), "value2");

        List values = writeableCommandLine.getValues(new Option("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("a"));
        writeableCommandLine.addSwitch(new Option("a"), true);

        Boolean switchValue = writeableCommandLine.getSwitch(new Option("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("a"));

        Assert.assertTrue(writeableCommandLine.hasOption(new Option("a")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("a"));

        Option option = writeableCommandLine.getOption("a");
        Assert.assertEquals(option.getLongOpt(), "a");
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("a"));
        writeableCommandLine.addValue(new Option("a"), "value1");
        writeableCommandLine.addValue(new Option("a"), "value2");

        List values = writeableCommandLine.getValues(new Option("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("a"));
        writeableCommandLine.addSwitch(new Option("a"), true);

        Boolean switchValue = writeableCommandLine.getSwitch(new Option("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addProperty("property", "value");

        String propertyValue = writeableCommandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addProperty("property", "value");

        String propertyValue = writeableCommandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        Set properties = writeableCommandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("a"));
        writeableCommandLine.addValue(new Option("a"), "value1");
        writeableCommandLine.addValue(new Option("a"), "value2");

        String commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("value1 value2", commandLineString);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptionWithInvalidTrigger() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.getOption("invalidTrigger");
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("-a"));

        Assert.assertTrue(writeableCommandLine.looksLikeOption("--a"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("-a"));
    }
}