import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void addOption_validOption_optionAddedSuccessfully() {
        Option option = new Option("name", "description");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void addValue_validOptionAndValue_valueAddedSuccessfully() {
        Option option = new Option("name", "description");
        String value = "test";
        commandLine.addValue(option, value);

        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void addValue_optionNotPresent_throwException() {
        Option option = new Option("name", "description");
        String value = "test";

        boolean exceptionThrown = false;
        try {
            commandLine.addValue(option, value);
        } catch (Exception e) {
            exceptionThrown = true;
            assertEquals("Option not present in command line", e.getMessage());
        }

        assertTrue(exceptionThrown);
    }

    @Test
    public void getValues_optionPresent_returnListWithValues() {
        Option option = new Option("name", "description");
        String value1 = "test1";
        String value2 = "test2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        assertEquals(expectedValues, commandLine.getValues(option));
    }

    @Test
    public void setDefaultValues_validOption_defaultValuesSetSuccessfully() {
        Option option = new Option("name", "description");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void addSwitch_validOption_switchAddedSuccessfully() {
        Option option = new Option("name", "description");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitches().contains(option));
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void addSwitch_switchAlreadyAdded_throwException() {
        Option option = new Option("name", "description");

        commandLine.addSwitch(option, true);

        boolean exceptionThrown = false;
        try {
            commandLine.addSwitch(option, false);
        } catch (Exception e) {
            exceptionThrown = true;
            assertEquals("Switch already added in command line", e.getMessage());
        }

        assertTrue(exceptionThrown);
    }

    @Test
    public void setDefaultSwitch_validOption_defaultSwitchSetSuccessfully() {
        Option option = new Option("name", "description");

        commandLine.setDefaultSwitch(option, false);

        assertEquals(false, commandLine.getDefaultSwitchValue(option));
    }

    @Test
    public void addProperty_validProperty_propertyAddedSuccessfully() {
        String property = "name";
        String value = "test";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void addProperty_existingProperty_existingPropertyReplacedSuccessfully() {
        String property = "name";
        String value1 = "test1";
        String value2 = "test2";

        commandLine.addProperty(property, value1);
        commandLine.addProperty(property, value2);

        assertEquals(value2, commandLine.getProperty(property));
    }

    @Test
    public void looksLikeOption_argumentIsOption_returnTrue() {
        String argument = "-option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void looksLikeOption_argumentIsNotOption_returnFalse() {
        String argument = "argument";

        assertFalse(commandLine.looksLikeOption(argument));
    }
}