import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Creating Option object
        Option option = new Option.Builder("test").create();

        // Adding option to WriteableCommandLineImpl object
        writeableCommandLine.addOption(option);

        // Asserting if the option was added successfully
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Creating Option object
        Option option = new Option.Builder("test").create();

        // Adding value to the option in WriteableCommandLineImpl object
        writeableCommandLine.addValue(option, "value");

        // Asserting if the value was added successfully
        Assert.assertEquals(1, writeableCommandLine.getValues(option, null).size());
        Assert.assertEquals("value", writeableCommandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Creating Option object
        Option option = new Option.Builder("test").create();

        // Adding switch to the option in WriteableCommandLineImpl object
        writeableCommandLine.addSwitch(option, true);

        // Asserting if the switch was added successfully
        Assert.assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Getting property from WriteableCommandLineImpl object
        String property = writeableCommandLine.getProperty("property");

        // Asserting if the property is null as there are no properties added yet
        Assert.assertNull(property);
    }

    @Test
    public void testAddProperty() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Adding property to WriteableCommandLineImpl object
        writeableCommandLine.addProperty("property", "value");

        // Getting property from WriteableCommandLineImpl object
        String property = writeableCommandLine.getProperty("property");

        // Asserting if the property was added successfully
        Assert.assertEquals("value", property);
    }

    @Test
    public void testLooksLikeOption() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Asserting if the given trigger looks like an option
        Assert.assertTrue(writeableCommandLine.looksLikeOption("-t"));
    }

    @Test
    public void testToString() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Creating normalised list
        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        normalised.add("arg2");

        // Setting normalised list in WriteableCommandLineImpl object
        writeableCommandLine.setNormalised(normalised);

        // Asserting if the toString method returns the correct string representation of the WriteableCommandLineImpl object
        Assert.assertEquals("arg1 arg2", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Creating Option objects
        Option option1 = new Option.Builder("opt1").create();
        Option option2 = new Option.Builder("opt2").create();

        // Adding options to WriteableCommandLineImpl object
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        // Getting options from WriteableCommandLineImpl object
        List<Option> options = writeableCommandLine.getOptions();

        // Asserting if the options were added and returned successfully
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Creating Option objects
        Option option1 = new Option.Builder("opt1").create();
        Option option2 = new Option.Builder("opt2").create();

        // Adding options to WriteableCommandLineImpl object
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        // Getting option triggers from WriteableCommandLineImpl object
        Set<String> optionTriggers = writeableCommandLine.getOptionTriggers();

        // Asserting if the option triggers were added and returned successfully
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("opt1"));
        Assert.assertTrue(optionTriggers.contains("opt2"));
    }

    @Test
    public void testSetDefaultValues() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Creating Option object
        Option option = new Option.Builder("test").create();

        // Creating default values list
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("value1");
        defaultValues.add("value2");

        // Setting default values in WriteableCommandLineImpl object
        writeableCommandLine.setDefaultValues(option, defaultValues);

        // Getting default values from WriteableCommandLineImpl object
        List<String> returnedDefaultValues = writeableCommandLine.getValues(option, null);

        // Asserting if the default values were set and returned successfully
        Assert.assertEquals(2, returnedDefaultValues.size());
        Assert.assertEquals("value1", returnedDefaultValues.get(0));
        Assert.assertEquals("value2", returnedDefaultValues.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Creating Option object
        Option option = new Option.Builder("test").create();

        // Setting default switch in WriteableCommandLineImpl object
        writeableCommandLine.setDefaultSwitch(option, true);

        // Getting default switch from WriteableCommandLineImpl object
        Boolean defaultSwitch = writeableCommandLine.getSwitch(option, null);

        // Asserting if the default switch was set and returned successfully
        Assert.assertNotNull(defaultSwitch);
        Assert.assertTrue(defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        // Creating WriteableCommandLineImpl object
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        // Creating normalised list
        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        normalised.add("arg2");

        // Setting normalised list in WriteableCommandLineImpl object
        writeableCommandLine.setNormalised(normalised);

        // Getting normalised list from WriteableCommandLineImpl object
        List<String> returnedNormalised = writeableCommandLine.getNormalised();

        // Asserting if the normalised list was set and returned successfully
        Assert.assertEquals(2, returnedNormalised.size());
        Assert.assertEquals("arg1", returnedNormalised.get(0));
        Assert.assertEquals("arg2", returnedNormalised.get(1));
    }
}