import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Test addOption() method
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        Assert.assertTrue(cmdLine.hasOption(option1));
        Assert.assertTrue(cmdLine.hasOption(option2));
        Assert.assertEquals(option1, cmdLine.getOption("option1"));
        Assert.assertEquals(option2, cmdLine.getOption("option2"));
    }

    @Test
    public void testAddValue() {
        // Test addValue() method
        Option option = new Option("option");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        Assert.assertEquals(2, cmdLine.getValues(option, null).size());
        Assert.assertTrue(cmdLine.getValues(option, null).contains("value1"));
        Assert.assertTrue(cmdLine.getValues(option, null).contains("value2"));
    }

    @Test
    public void testAddSwitch() {
        // Test addSwitch() method
        Option option = new Option("option");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        cmdLine.addSwitch(option, true);

        Assert.assertTrue(cmdLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        // Test hasOption() method
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        Assert.assertTrue(cmdLine.hasOption(option1));
        Assert.assertTrue(cmdLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        // Test getOption() method
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        Assert.assertEquals(option1, cmdLine.getOption("option1"));
        Assert.assertEquals(option2, cmdLine.getOption("option2"));
    }

    @Test
    public void testGetValues() {
        // Test getValues() method
        Option option = new Option("option");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        cmdLine.setDefaultValues(option, defaultValues);

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        Assert.assertEquals(2, cmdLine.getValues(option, null).size());
        Assert.assertTrue(cmdLine.getValues(option, null).contains("value1"));
        Assert.assertTrue(cmdLine.getValues(option, null).contains("value2"));

        Assert.assertEquals(defaultValues, cmdLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Test getUndefaultedValues() method
        Option option = new Option("option");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        Assert.assertEquals(2, cmdLine.getUndefaultedValues(option).size());
        Assert.assertTrue(cmdLine.getUndefaultedValues(option).contains("value1"));
        Assert.assertTrue(cmdLine.getUndefaultedValues(option).contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        // Test getSwitch() method
        Option option = new Option("option");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        cmdLine.addSwitch(option, true);

        Assert.assertTrue(cmdLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        // Test getProperty(String) method
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());

        cmdLine.addProperty("property", "value");

        Assert.assertEquals("value", cmdLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        // Test addProperty(Option, String, String) method
        Option option = new Option("option");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        cmdLine.addProperty(option, "property", "value");

        Assert.assertEquals("value", cmdLine.getProperty(option, "property", null));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Test getProperty(Option, String, String) method with default value
        Option option = new Option("option");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Assert.assertEquals("default", cmdLine.getProperty(option, "property", "default"));
    }

    @Test
    public void testGetProperties() {
        // Test getProperties(Option) method
        Option option = new Option("option");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        cmdLine.addProperty(option, "property1", "value1");
        cmdLine.addProperty(option, "property2", "value2");

        Assert.assertEquals(2, cmdLine.getProperties(option).size());
        Assert.assertTrue(cmdLine.getProperties(option).contains("property1"));
        Assert.assertTrue(cmdLine.getProperties(option).contains("property2"));
    }

    @Test
    public void testGetPropertiesAll() {
        // Test getProperties() method
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());

        cmdLine.addProperty("property1", "value1");
        cmdLine.addProperty("property2", "value2");

        Assert.assertEquals(2, cmdLine.getProperties().size());
        Assert.assertTrue(cmdLine.getProperties().contains("property1"));
        Assert.assertTrue(cmdLine.getProperties().contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        // Test looksLikeOption() method
        Option option = new Option("option");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Assert.assertTrue(cmdLine.looksLikeOption("-option"));
        Assert.assertFalse(cmdLine.looksLikeOption("value"));
    }
}