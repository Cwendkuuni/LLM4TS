import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        Option option = new OptionBuilder().withName("testOption").create();

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        Option option = new OptionBuilder().withName("testOption").create();

        commandLine.addValue(option, "value1");

        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        Option option = new OptionBuilder().withName("testOption").create();

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        Option option = new OptionBuilder().withName("testOption").create();

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        Option option = new OptionBuilder().withName("testOption").create();

        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("testOption");
        Assert.assertNotNull(retrievedOption);
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        Option option = new OptionBuilder().withName("testOption").create();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        Option option = new OptionBuilder().withName("testOption").create();

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        String property1Value = commandLine.getProperty("property1", "");
        String property2Value = commandLine.getProperty("property2", "");

        Assert.assertEquals("value1", property1Value);
        Assert.assertEquals("value2", property2Value);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        commandLine.addPrefix("-");

        boolean result = commandLine.looksLikeOption("-testOption");
        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        arguments.add("arg3");

        String expectedOutput = "arg1 arg2 arg3";

        commandLine.setArguments(arguments);

        String result = commandLine.toString();

        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        Option option1 = new OptionBuilder().withName("testOption1").create();
        Option option2 = new OptionBuilder().withName("testOption2").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        Option option1 = new OptionBuilder().withName("testOption1").create();
        Option option2 = new OptionBuilder().withName("testOption2").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> optionTriggers = commandLine.getOptionTriggers();

        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("testOption1"));
        Assert.assertTrue(optionTriggers.contains("testOption2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        Option option = new OptionBuilder().withName("testOption").create();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        List<String> retrievedDefaultValues = commandLine.getValues(option, null);
        Assert.assertEquals(2, retrievedDefaultValues.size());
        Assert.assertEquals("defaultValue1", retrievedDefaultValues.get(0));
        Assert.assertEquals("defaultValue2", retrievedDefaultValues.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        Option option = new OptionBuilder().withName("testOption").create();
        
        commandLine.setDefaultSwitch(option, true);

        Boolean defaultSwitch = commandLine.getSwitch(option, false);
        Assert.assertTrue(defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        arguments.add("arg3");

        commandLine.setArguments(arguments);

        List<String> normalisedArguments = commandLine.getNormalised();

        Assert.assertEquals(3, normalisedArguments.size());
        Assert.assertEquals("arg1", normalisedArguments.get(0));
        Assert.assertEquals("arg2", normalisedArguments.get(1));
        Assert.assertEquals("arg3", normalisedArguments.get(2));
    }
}