import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {

    // Test getOpt()
    @Test
    public void testGetOpt() {
        Option option = new Option("o", "option", true, "test option");
        assertEquals("o", option.getOpt());
    }

    // Test getLongOpt()
    @Test
    public void testGetLongOpt() {
        Option option = new Option("o", "option", true, "test option");
        assertEquals("option", option.getLongOpt());
    }

    // Test hasOptionalArg()
    @Test
    public void testHasOptionalArg() {
        Option option = new Option("o", "option", true, "test option");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    // Test hasLongOpt()
    @Test
    public void testHasLongOpt() {
        Option option = new Option("o", "option", true, "test option");
        assertTrue(option.hasLongOpt());
    }

    // Test hasArg()
    @Test
    public void testHasArg() {
        Option option = new Option("o", "option", true, "test option");
        assertTrue(option.hasArg());
    }

    // Test getDescription()
    @Test
    public void testGetDescription() {
        Option option = new Option("o", "option", true, "test option");
        assertEquals("test option", option.getDescription());
    }

    // Test isRequired()
    @Test
    public void testIsRequired() {
        Option option = new Option("o", "option", true, "test option");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    // Test getArgName()
    @Test
    public void testGetArgName() {
        Option option = new Option("o", "option", true, "test option");
        assertEquals("arg", option.getArgName());
    }

    // Test hasArgName()
    @Test
    public void testHasArgName() {
        Option option = new Option("o", "option", true, "test option");
        option.setArgName("arg");
        assertTrue(option.hasArgName());
    }

    // Test hasArgs()
    @Test
    public void testHasArgs() {
        Option option = new Option("o", "option", true, "test option");
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    // Test getValueSeparator()
    @Test
    public void testGetValueSeparator() {
        Option option = new Option("o", "option", true, "test option");
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    // Test hasValueSeparator()
    @Test
    public void testHasValueSeparator() {
        Option option = new Option("o", "option", true, "test option");
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    // Test getArgs()
    @Test
    public void testGetArgs() {
        Option option = new Option("o", "option", true, "test option");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    // Test equals()
    @Test
    public void testEquals() {
        Option option1 = new Option("o", "option", true, "test option");
        Option option2 = new Option("o", "option", true, "test option");
        assertTrue(option1.equals(option2));
    }

    // Test hashCode()
    @Test
    public void testHashCode() {
        Option option = new Option("o", "option", true, "test option");
        int expectedHash = Objects.hash(option.getOpt(), option.getLongOpt());
        assertEquals(expectedHash, option.hashCode());
    }

    // Test clone()
    @Test
    public void testClone() {
        Option option1 = new Option("o", "option", true, "test option");
        Option option2 = (Option) option1.clone();
        assertEquals(option1.equals(option2), true);
    }

    // Test addValue()
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("o", "option", true, "test option");
        option.addValue("value");
    }

    // Additional Test Cases

    // Test getValue()
    @Test
    public void testGetValue() {
        Option option = new Option("o", "option", true, "test option");
        assertFalse(option.getValue() != null);
    }

    // Test getValue(int index)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtIndex() {
        Option option = new Option("o", "option", true, "test option");
        option.getValue(0);
    }

    // Test getValue(String defaultValue)
    @Test
    public void testGetValueWithDefault() {
        Option option = new Option("o", "option", true, "test option");
        assertEquals("default", option.getValue("default"));
    }

    // Test getValues()
    @Test
    public void testGetValues() {
        Option option = new Option("o", "option", true, "test option");
        assertEquals(null, option.getValues());
    }

    // Test getValuesList()
    @Test
    public void testGetValuesList() {
        Option option = new Option("o", "option", true, "test option");
        assertTrue(option.getValuesList().isEmpty());
    }

    // Test toString()
    @Test
    public void testToString() {
        Option option = new Option("o", "option", true, "test option");
        assertEquals("[ option: o option [ARG] :: test option ]", option.toString());
    }

    // Test addValueForProcessing()
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing() {
        Option option = new Option("o", "option", true, "test option");
        option.addValueForProcessing("value");
    }

    // Test clearValues()
    @Test
    public void testClearValues() {
        Option option = new Option("o", "option", true, "test option");
        option.addValueForProcessing("value");
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }
}