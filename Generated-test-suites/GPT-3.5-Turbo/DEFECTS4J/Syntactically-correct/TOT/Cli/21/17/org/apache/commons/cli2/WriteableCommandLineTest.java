import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void addOption_validOption_optionAddedSuccessfully() {
        // Arrange
        Option option = new Option("testOption");

        // Act
        writeableCommandLine.addOption(option);

        // Assert
        assertTrue(writeableCommandLine.hasOption("testOption"));
    }

    @Test
    public void addValue_validOptionAndValue_valueAddedSuccessfully() {
        // Arrange
        Option option = new Option("testOption");
        Object value = "testValue";

        // Act
        writeableCommandLine.addValue(option, value);

        // Assert
        assertTrue(writeableCommandLine.getValues(option).contains(value));
    }

    @Test
    public void getUndefaultedValues_optionWithValues_valuesReturnedSuccessfully() {
        // Arrange
        Option option = new Option("testOption");
        Object value1 = "testValue1";
        Object value2 = "testValue2";
        writeableCommandLine.addValue(option, value1);
        writeableCommandLine.addValue(option, value2);

        // Act
        List<Object> undefaultedValues = writeableCommandLine.getUndefaultedValues(option);

        // Assert
        assertTrue(undefaultedValues.contains(value1));
        assertTrue(undefaultedValues.contains(value2));
    }

    @Test
    public void setDefaultValues_optionWithDefaultValues_defaultValuesSetSuccessfully() {
        // Arrange
        Option option = new Option("testOption");
        List<Object> defaultValues = List.of("defaultValue1", "defaultValue2");

        // Act
        writeableCommandLine.setDefaultValues(option, defaultValues);

        // Assert
        assertTrue(writeableCommandLine.getDefaultValues(option).containsAll(defaultValues));
    }

    @Test(expected = IllegalStateException.class)
    public void addSwitch_switchAlreadyAdded_exceptionThrown() {
        // Arrange
        Option option = new Option("testOption");

        // Act
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false); // Should throw IllegalStateException
    }

    @Test
    public void setDefaultSwitch_optionWithDefaultSwitch_defaultSwitchSetSuccessfully() {
        // Arrange
        Option option = new Option("testOption");
        Boolean defaultSwitch = true;

        // Act
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        // Assert
        assertEquals(defaultSwitch, writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void addProperty_validOption_validPropertyAddedSuccessfully() {
        // Arrange
        Option option = new Option("testOption");
        String property = "testProperty";
        String value = "testValue";

        // Act
        writeableCommandLine.addProperty(option, property, value);

        // Assert
        assertTrue(writeableCommandLine.getProperty(option, property).equals(value));
    }

    @Test
    public void addProperty_validProperty_validPropertyAddedSuccessfully() {
        // Arrange
        String property = "testProperty";
        String value = "testValue";

        // Act
        writeableCommandLine.addProperty(property, value);

        // Assert
        assertTrue(writeableCommandLine.getProperty(property).equals(value));
    }

    @Test
    public void looksLikeOption_argumentIsOption_trueReturned() {
        // Arrange
        String argument = "-testOption";

        // Act
        boolean looksLikeOption = writeableCommandLine.looksLikeOption(argument);

        // Assert
        assertTrue(looksLikeOption);
    }

    @Test
    public void setCurrentOption_validOption_currentOptionSetSuccessfully() {
        // Arrange
        Option option = new Option("testOption");

        // Act
        writeableCommandLine.setCurrentOption(option);

        // Assert
        assertEquals(option, writeableCommandLine.getCurrentOption());
    }
}