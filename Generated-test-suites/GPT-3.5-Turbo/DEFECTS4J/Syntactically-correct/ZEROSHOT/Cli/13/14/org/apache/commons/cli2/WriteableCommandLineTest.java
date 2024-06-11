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
    public void testAddOption() {
        Option option = new OptionImpl();
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl();
        Object value = new Object();
        writeableCommandLine.addValue(option, value);
        assertTrue(writeableCommandLine.hasValue(option, value));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new OptionImpl();
        Object value1 = new Object();
        Object value2 = new Object();
        writeableCommandLine.addValue(option, value1);
        writeableCommandLine.addValue(option, value2);
        List argumentValues = writeableCommandLine.getArgumentValues(option);
        assertEquals(2, argumentValues.size());
        assertTrue(argumentValues.contains(value1));
        assertTrue(argumentValues.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl();
        List defaultValues = new ArrayList();
        defaultValues.add(new Object());
        defaultValues.add(new Object());
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl();
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        assertTrue(writeableCommandLine.hasSwitch(option));
        assertEquals(value, writeableCommandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl();
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        writeableCommandLine.addProperty(property, value);
        assertEquals(value, writeableCommandLine.getPropertyValue(property));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        assertTrue(writeableCommandLine.looksLikeOption(argument));
    }
}