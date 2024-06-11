
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setup() {
        option = new Option("opt", "description");
    }

    @Test
    public void testGetOpt() {
        assertEquals("opt", option.getOpt());
    }

    @Test
    public void testGetType() {
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        option.setType("type");
        assertEquals("type", option.getType());
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
        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
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
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
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
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(';');
        assertEquals(';', option.getValueSeparator());
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
    public void testAddValueForProcessingNoArgsAllowed() {
        option.setArgs(-1);
        option.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessing() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueListFull() {
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
    }

    @Test
    public void testGetValueAtIndex() {
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
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test
    public void testToString() {
        String expected = "[ option: opt  [ARG] :: description ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        Option otherOption = new Option("opt", "description");
        assertTrue(option.equals(otherOption));
    }

    @Test
    public void testHashCode() {
        Option otherOption = new Option("opt", "description");
        assertEquals(option.hashCode(), otherOption.hashCode());
    }

    @Test
    public void testClone() {
        Option clonedOption = (Option) option.clone();
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getDescription(), clonedOption.getDescription());
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueUnsupportedOperation() {
        option.addValue("value");
    }
}
