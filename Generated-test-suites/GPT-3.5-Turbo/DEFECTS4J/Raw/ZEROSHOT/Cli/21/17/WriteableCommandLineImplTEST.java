import org.junit.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Test case for addOption() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.getOptions().contains(option));
        Assert.assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        // Test case for addValue() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Argument argument = new Argument("test");
        Object value = "value";

        commandLine.addValue(argument, value);

        Assert.assertTrue(commandLine.getValues(argument, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        // Test case for addSwitch() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        boolean switchValue = true;

        commandLine.addSwitch(option, switchValue);

        Assert.assertTrue(commandLine.getSwitch(option, null).equals(switchValue));
    }

    @Test
    public void testHasOption() {
        // Test case for hasOption() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        // Test case for getOption() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");

        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        // Test case for getValues() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        List values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        Assert.assertEquals(values, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Test case for getUndefaultedValues() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        List values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        Assert.assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        // Test case for getSwitch() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        boolean switchValue = true;

        commandLine.addSwitch(option, switchValue);

        Assert.assertTrue(commandLine.getSwitch(option, null).equals(switchValue));
    }

    @Test
    public void testGetProperty() {
        // Test case for getProperty() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        String property = "test";
        String value = "value";

        commandLine.addProperty(property, value);

        Assert.assertTrue(commandLine.getProperty(property).equals(value));
    }

    @Test
    public void testAddProperty() {
        // Test case for addProperty() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        String property = "test";
        String value = "value";

        commandLine.addProperty(option, property, value);

        Assert.assertTrue(commandLine.getProperty(option, property, null).equals(value));
        Assert.assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testSetDefaultValues() {
        // Test case for setDefaultValues() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        List values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.setDefaultValues(option, values);

        Assert.assertEquals(values, commandLine.getValues(option, null));

        commandLine.setDefaultValues(option, null);

        Assert.assertEquals(values, commandLine.getValues(option, values));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Test case for setDefaultSwitch() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        boolean switchValue = true;

        commandLine.setDefaultSwitch(option, switchValue);

        Assert.assertTrue(commandLine.getSwitch(option, null).equals(switchValue));

        commandLine.setDefaultSwitch(option, null);

        Assert.assertTrue(commandLine.getSwitch(option, switchValue).equals(switchValue));
    }

    @Test
    public void testGetNormalised() {
        // Test case for getNormalised() method

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        List normalised = new ArrayList<>();
        normalised.add("argument1");
        normalised.add("argument2");

        Assert.assertEquals(normalised, commandLine.getNormalised());
    }
}