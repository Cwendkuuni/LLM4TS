import org.junit.Test;
import org.junit.Assert;
import java.util.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLine cli = new WriteableCommandLineImpl();

        cli.addOption(option);

        // Test if option is added correctly
        Assert.assertEquals(option, cli.getOption("o"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLine cli = new WriteableCommandLineImpl();
        Object value = 100;

        cli.addValue(option, value);

        // Test if value is added correctly
        Assert.assertTrue(cli.getValues(option).contains(value));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLine cli = new WriteableCommandLineImpl();
        List<Object> values = Arrays.asList(1, 2, 3);

        cli.setDefaultValues(option, values);

        // Test if default values are retrieved correctly
        Assert.assertEquals(values, cli.getValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLine cli = new WriteableCommandLineImpl();
        List<Object> values = Arrays.asList(1, 2, 3);

        cli.setDefaultValues(option, values);

        // Test if default values are set correctly
        Assert.assertEquals(values, cli.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLine cli = new WriteableCommandLineImpl();
        boolean value = true;

        cli.addSwitch(option, value);

        // Test if switch value is added correctly
        Assert.assertEquals(value, cli.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLine cli = new WriteableCommandLineImpl();

        // Add switch twice
        cli.addSwitch(option, true);
        cli.addSwitch(option, false);

        // Expect IllegalStateException to be thrown
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLine cli = new WriteableCommandLineImpl();
        Boolean defaultSwitch = true;

        cli.setDefaultSwitch(option, defaultSwitch);

        // Test if default switch value is set correctly
        Assert.assertEquals(defaultSwitch, cli.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cli = new WriteableCommandLineImpl();
        String property = "key";
        String value = "value";

        cli.addProperty(property, value);

        // Test if property value is added correctly
        Assert.assertEquals(value, cli.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cli = new WriteableCommandLineImpl();
        String validOption = "-o";
        String invalidOption = "option";

        // Test if valid option is recognized correctly
        Assert.assertTrue(cli.looksLikeOption(validOption));

        // Test if invalid option is recognized correctly
        Assert.assertFalse(cli.looksLikeOption(invalidOption));
    }
}