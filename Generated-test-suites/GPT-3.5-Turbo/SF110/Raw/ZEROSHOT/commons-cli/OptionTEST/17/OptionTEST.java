import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("opt", "description");
    }

    @Test
    public void testGetOpt() {
        assertEquals("opt", option.getOpt());
    }

    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        assertFalse(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        assertFalse(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testSetRequired() {
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        assertFalse(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        assertFalse(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertTrue(option.hasArgs());
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());
        assertEquals(';', option.getValueSeparator());
    }

    @Test
    public void testGetValueSeparator() {
        assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing() {
        option.addValueForProcessing("value");
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        assertNull(option.getValue(0));
    }

    @Test
    public void testGetValueWithDefault() {
        assertEquals("defaultValue", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
    }

    @Test
    public void testGetValuesList() {
        List<String> valuesList = new ArrayList<>();
        assertEquals(valuesList, option.getValuesList());
    }

    @Test
    public void testToString() {
        assertEquals("[ option: opt :: description ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option otherOption = new Option("opt", "description");
        assertTrue(option.equals(otherOption));
        assertFalse(option.equals(null));
        assertFalse(option.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Option otherOption = new Option("opt", "description");
        assertEquals(option.hashCode(), otherOption.hashCode());
        otherOption.setLongOpt("longOpt");
        assertNotEquals(option.hashCode(), otherOption.hashCode());
    }

    @Test
    public void testClone() {
        Option clonedOption = (Option) option.clone();
        assertEquals(option, clonedOption);
        assertNotSame(option, clonedOption);
        option.setArgName("newArg");
        assertNotEquals(option.getArgName(), clonedOption.getArgName());
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        option.clearValues();
        assertEquals(0, option.getValuesList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}