import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("testOption");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        writeableCommandLine.addOption(option);
        
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Argument argument = new ArgumentImpl("testArgument");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(argument, new ArrayList());
        String value = "testValue";
        writeableCommandLine.addValue(argument, value);
        
        List expectedValues = new ArrayList();
        expectedValues.add(value);
        
        Assert.assertEquals(expectedValues, writeableCommandLine.getValues(argument, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("testOption");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        
        Assert.assertEquals(value, writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionImpl("testOption");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        writeableCommandLine.addOption(option);
        
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionImpl("testOption");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        writeableCommandLine.addOption(option);
        
        Assert.assertEquals(option, writeableCommandLine.getOption("testOption"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new ArgumentImpl("testArgument");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(argument, new ArrayList());
        String value1 = "testValue1";
        String value2 = "testValue2";
        writeableCommandLine.addValue(argument, value1);
        writeableCommandLine.addValue(argument, value2);
        
        List expectedValues = new ArrayList();
        expectedValues.add(value1);
        expectedValues.add(value2);
        
        Assert.assertEquals(expectedValues, writeableCommandLine.getValues(argument, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new ArgumentImpl("testArgument");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(argument, new ArrayList());
        String value1 = "testValue1";
        String value2 = "testValue2";
        writeableCommandLine.addValue(argument, value1);
        writeableCommandLine.addValue(argument, value2);
        
        List expectedValues = new ArrayList();
        expectedValues.add(value1);
        expectedValues.add(value2);
        
        Assert.assertEquals(expectedValues, writeableCommandLine.getUndefaultedValues(argument));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionImpl("testOption");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        
        Assert.assertEquals(value, writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new OptionImpl("testOption");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String property = "testProperty";
        String value = "testValue";
        writeableCommandLine.addProperty(option, property, value);
        
        Assert.assertEquals(value, writeableCommandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("testOption");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String property = "testProperty";
        String value = "testValue";
        writeableCommandLine.addProperty(option, property, value);
        
        Properties expectedProperties = new Properties();
        expectedProperties.setProperty(property, value);
        
        Assert.assertEquals(expectedProperties, writeableCommandLine.optionToProperties.get(option));
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionImpl("testOption");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String property = "testProperty";
        String value = "testValue";
        writeableCommandLine.addProperty(option, property, value);
        
        List propertiesExpected = new ArrayList();
        propertiesExpected.add(property);
        
        Assert.assertEquals(propertiesExpected, writeableCommandLine.getProperties(option));
    }

    @Test
    public void testToString() {
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        Option option1 = new OptionImpl("option1");
        Option option2 = new OptionImpl("option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, arguments);
        writeableCommandLine.addOption(option2);
        
        String expectedString = "arg1 arg2";
        
        Assert.assertEquals(expectedString, writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionImpl("option1");
        Option option2 = new OptionImpl("option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        writeableCommandLine.addOption(option2);
        
        List optionsExpected = new ArrayList();
        optionsExpected.add(option1);
        optionsExpected.add(option2);
        
        Assert.assertEquals(optionsExpected, writeableCommandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionImpl("option1");
        Option option2 = new OptionImpl("option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        writeableCommandLine.addOption(option2);
        
        List triggersExpected = new ArrayList();
        triggersExpected.add("option1");
        triggersExpected.add("option2");
        
        Assert.assertEquals(triggersExpected, writeableCommandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("testOption");
        List defaults = new ArrayList();
        defaults.add("defaultValue");
        
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        writeableCommandLine.setDefaultValues(option, defaults);
        
        Assert.assertEquals(defaults, writeableCommandLine.defaultValues.get(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("testOption");
        Boolean defaultSwitch = true;
        
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        
        Assert.assertEquals(defaultSwitch, writeableCommandLine.defaultSwitches.get(option));
    }

    @Test
    public void testGetNormalised() {
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new OptionImpl("option"), arguments);
        
        Assert.assertEquals(arguments, writeableCommandLine.getNormalised());
    }
}