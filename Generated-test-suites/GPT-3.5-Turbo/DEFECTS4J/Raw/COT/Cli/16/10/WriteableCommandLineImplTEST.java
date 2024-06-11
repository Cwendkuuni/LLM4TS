import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("rootOption");
        List arguments = new ArrayList();
        writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        
        assert(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        Object value = "testValue";
        writeableCommandLine.addValue(option, value);

        List values = writeableCommandLine.getValues(option, null);
        assert(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testSwitch");
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        
        assert(writeableCommandLine.getSwitch(option, null) == value);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        
        assert(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        
        Option retrievedOption = writeableCommandLine.getOption("testOption");
        assert(retrievedOption == option);
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        List values = Arrays.asList("value1", "value2");
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List retrievedValues = writeableCommandLine.getValues(option, null);
        assert(retrievedValues.equals(values));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        List values = Arrays.asList("value1", "value2");
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List retrievedValues = writeableCommandLine.getUndefaultedValues(option);
        assert(retrievedValues.equals(values));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testSwitch");
        boolean defaultValue = false;
        writeableCommandLine.addSwitch(option, true);
        
        assert(writeableCommandLine.getSwitch(option, defaultValue) == true);
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        writeableCommandLine.addProperty(property, value);

        String retrievedValue = writeableCommandLine.getProperty(property);
        assert(retrievedValue.equals(value));
    }

    @Test
    public void testAddProperty() {
        Option option = new PropertyOption();
        String property = "testProperty";
        String value = "testValue";
        writeableCommandLine.addProperty(option, property, value);

        String retrievedValue = writeableCommandLine.getProperty(option, property, null);
        assert(retrievedValue.equals(value));
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption();
        String property = "testProperty";
        String value = "testValue";
        writeableCommandLine.addProperty(option, property, value);

        Set properties = writeableCommandLine.getProperties(option);
        assert(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "testOption";
        boolean result = writeableCommandLine.looksLikeOption(trigger);
        assert(result == false);
    }

    @Test
    public void testToString() {
        writeableCommandLine.addValue(new Argument(), "value");
        String stringRepresentation = writeableCommandLine.toString();
        assert(stringRepresentation.equals("value"));
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        
        List options = writeableCommandLine.getOptions();
        assert(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        
        Set triggers = writeableCommandLine.getOptionTriggers();
        assert(triggers.contains("testOption"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        List defaults = Arrays.asList("default1", "default2");
        writeableCommandLine.setDefaultValues(option, defaults);

        List retrievedDefaults = writeableCommandLine.getValues(option, null);
        assert(retrievedDefaults.equals(defaults));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testSwitch");
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean retrievedDefaultSwitch = writeableCommandLine.getSwitch(option, null);
        assert(retrievedDefaultSwitch == defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Argument();
        List arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        List retrievedArguments = writeableCommandLine.getNormalised();
        assert(retrievedArguments.equals(arguments));
    }
}