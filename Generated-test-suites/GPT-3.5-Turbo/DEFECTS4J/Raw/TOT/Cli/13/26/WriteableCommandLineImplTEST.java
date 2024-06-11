import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import java.util.List;
import java.util.Arrays;

public class WriteableCommandLineImplTest {

    // Test case for addOption method
    @Test
    public void testAddOption() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        cli.addOption(option);
        
        assert(cli.getOption("testOption") != null);
    }

    // Test case for addValue method
    @Test
    public void testAddValue() {
        Option option = new Argument("testArgument", "Test Argument");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        cli.addValue(option, "value1");
        cli.addValue(option, "value2");
        
        assert(cli.getValues(option, null).size() == 2);
    }

    // Test case for addSwitch method
    @Test
    public void testAddSwitch() {
        Option option = new Option("testSwitch", "Test Switch");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        cli.addSwitch(option, true);
        
        assert(cli.getSwitch(option, null));
    }

    // Test case for hasOption method
    @Test
    public void testHasOption() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        cli.addOption(option);
        
        assert(cli.hasOption(option));
    }

    // Test case for getOption method
    @Test
    public void testGetOption() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        cli.addOption(option);
        
        assert(cli.getOption("testOption") != null);
    }

    // Test case for getValues method
    @Test
    public void testGetValues() {
        Option option = new Argument("testArgument", "Test Argument");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        List<String> defaultValues = Arrays.asList("default1", "default2");
        cli.setDefaultValues(option, defaultValues);
        
        assert(cli.getValues(option, null).equals(defaultValues));
    }

    // Test case for getSwitch method
    @Test
    public void testGetSwitch() {
        Option option = new Option("testSwitch", "Test Switch");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        Boolean defaultValue = true;
        cli.setDefaultSwitch(option, defaultValue);
        
        assert(cli.getSwitch(option, null) == defaultValue);
    }

    // Test case for addProperty method
    @Test
    public void testAddProperty() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        cli.addProperty("property1", "value1");
        cli.addProperty("property2", "value2");
        
        assert(cli.getProperty("property1", null).equals("value1"));
        assert(cli.getProperty("property2", null).equals("value2"));
    }

    // Test case for getProperty method
    @Test
    public void testGetProperty() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        cli.addProperty("property1", "value1");
        
        assert(cli.getProperty("property1", null).equals("value1"));
    }

    // Test case for looksLikeOption method
    @Test
    public void testLooksLikeOption() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        assert(cli.looksLikeOption("-test"));
        assert(cli.looksLikeOption("--test"));
        assert(cli.looksLikeOption("/test"));
        assert(!cli.looksLikeOption("test"));
    }

    // Test case for toString method
    @Test
    public void testToString() {
        Option option = new Option("testOption", "Test Option");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        List<String> arguments = Arrays.asList("argument1", "argument2");
        cli = new WriteableCommandLineImpl(option, arguments);
        
        assert(cli.toString().equals("argument1 argument2"));
    }

    // Test case for getOptions method
    @Test
    public void testGetOptions() {
        Option option1 = new Option("testOption1", "Test Option 1");
        Option option2 = new Option("testOption2", "Test Option 2");
        
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, null);
        cli.addOption(option1);
        cli.addOption(option2);
        
        assert(cli.getOptions().size() == 2);
    }

    // Test case for getOptionTriggers method
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("testOption1", "Test Option 1");
        Option option2 = new Option("testOption2", "Test Option 2");
        
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option1, null);
        cli.addOption(option1);
        cli.addOption(option2);
        
        assert(cli.getOptionTriggers().size() == 2);
    }

    // Test case for setDefaultValues method
    @Test
    public void testSetDefaultValues() {
        Option option = new Argument("testArgument", "Test Argument");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        List<String> defaultValues = Arrays.asList("default1", "default2");
        cli.setDefaultValues(option, defaultValues);
        
        assert(cli.getValues(option, null).equals(defaultValues));
    }

    // Test case for setDefaultSwitch method
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testSwitch", "Test Switch");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, null);
        
        Boolean defaultValue = true;
        cli.setDefaultSwitch(option, defaultValue);
        
        assert(cli.getSwitch(option, null) == defaultValue);
    }

    // Test case for getNormalised method
    @Test
    public void testGetNormalised() {
        Option option = new Option("testOption", "Test Option");
        
        List<String> arguments = Arrays.asList("argument1", "argument2");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);
        
        assert(cli.getNormalised().equals(arguments));
    }
}