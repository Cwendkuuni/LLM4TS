import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;

public class WriteableCommandLineImplTest {

    // Test case for addOption method
    @Test
    public void testAddOption() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addOption(option);
        
        Assert.assertTrue(commandLine.hasOption(option));
    }

    // Test case for addValue method
    @Test
    public void testAddValue() {
        Option option = new Argument("arg1");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addValue(option, "value1");
        
        Assert.assertEquals("value1", commandLine.getValues(option, new ArrayList()));
    }

    // Test case for addSwitch method
    @Test
    public void testAddSwitch() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addSwitch(option, true);
        
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    // Test case for hasOption method
    @Test
    public void testHasOption() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addOption(option);
        
        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertFalse(commandLine.hasOption(new Option("b", "option-b")));
    }

    // Test case for getOption method
    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "option-a");
        Option option2 = new Option("b", "option-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertEquals(option1, commandLine.getOption("a"));
        Assert.assertEquals(option2, commandLine.getOption("b"));
        Assert.assertNull(commandLine.getOption("c"));
    }

    // Test case for getValues method
    @Test
    public void testGetValues() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        
        Assert.assertEquals(expectedValues, commandLine.getValues(option, new ArrayList()));
    }

    // Test case for getSwitch method
    @Test
    public void testGetSwitch() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, false));
        Assert.assertFalse(commandLine.getSwitch(new Option("b", "option-b"), false));
    }

    // Test case for addProperty method
    @Test
    public void testAddProperty() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addProperty("property1", "value1");

        Assert.assertEquals("value1", commandLine.getProperty("property1", null));
    }

    // Test case for getProperty method
    @Test
    public void testGetProperty() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        commandLine.addProperty("property1", "value1");

        Assert.assertEquals("value1", commandLine.getProperty("property1", null));
        Assert.assertNull(commandLine.getProperty("property2", null));
        Assert.assertEquals("defaultValue", commandLine.getProperty("property2", "defaultValue"));
    }

    // Test case for looksLikeOption method
    @Test
    public void testLooksLikeOption() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        Assert.assertTrue(commandLine.looksLikeOption("-a"));
        Assert.assertTrue(commandLine.looksLikeOption("--option-a"));
        Assert.assertFalse(commandLine.looksLikeOption("value1"));
    }

    // Test case for toString method
    @Test
    public void testToString() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        
        List<String> arguments = new ArrayList<>();
        arguments.add("-a");
        arguments.add("value1");
        arguments.add("value2");

        commandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals("-a value1 value2", commandLine.toString());
    }

    // Test case for getOptions method
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option-a");
        Option option2 = new Option("b", "option-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        
        Assert.assertEquals(expectedOptions, commandLine.getOptions());
    }

    // Test case for getOptionTriggers method
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("a", "option-a");
        Option option2 = new Option("b", "option-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> expectedTriggers = new HashSet<>();
        expectedTriggers.add("a");
        expectedTriggers.add("b");
        
        Assert.assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    // Test case for setDefaultValues method
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        
        commandLine.setDefaultValues(option, defaultValues);

        Assert.assertEquals(defaultValues, commandLine.getValues(option, new ArrayList()));
    }

    // Test case for setDefaultSwitch method
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    // Test case for getNormalised method
    @Test
    public void testGetNormalised() {
        Option option = new Option("a", "option-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        List<String> arguments = new ArrayList<>();
        arguments.add("-a");
        arguments.add("value1");
        arguments.add("value2");

        commandLine = new WriteableCommandLineImpl(option, arguments);

        List<String> expectedArguments = new ArrayList<>();
        expectedArguments.add("-a");
        expectedArguments.add("value1");
        expectedArguments.add("value2");
        
        Assert.assertEquals(expectedArguments, commandLine.getNormalised());
    }

}