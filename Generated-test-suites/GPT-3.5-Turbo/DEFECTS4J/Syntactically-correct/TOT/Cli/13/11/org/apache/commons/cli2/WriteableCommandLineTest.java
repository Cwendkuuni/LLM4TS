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
        Option option = new OptionImpl("opt");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption("opt"));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("opt");
        Object value = new Object();
        writeableCommandLine.addValue(option, value);
        assertEquals(value, writeableCommandLine.getValues(option).get(0));
    }

    @Test
    public void testGetValues() {
        Option option = new OptionImpl("opt");
        Object value1 = new Object();
        Object value2 = new Object();
        writeableCommandLine.addValue(option, value1);
        writeableCommandLine.addValue(option, value2);
        assertEquals(2, writeableCommandLine.getValues(option).size());
        assertTrue(writeableCommandLine.getValues(option).contains(value1));
        assertTrue(writeableCommandLine.getValues(option).contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("opt");
        List<Object> defaultValues = List.of(new Object(), new Object());
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("opt");
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsException() {
        Option option = new OptionImpl("opt");
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("opt");
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "prop";
        String value = "value";
        writeableCommandLine.addProperty(property, value);
        assertEquals(value, writeableCommandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-option"));
        assertTrue(writeableCommandLine.looksLikeOption("--option"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }
}