import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionTest {
    private Option option;

    @Before
    public void setUp() {
        option = new Option("test", "test option");
    }

    @Test
    public void testConstructor() {
        assertEquals("test", option.getOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertEquals("test option", option.getDescription());
        assertEquals(-1, option.getArgs());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
    }

    @Test
    public void testAddValue() {
        assertFalse(option.addValue("value1"));
    }

    @Test
    public void testGetId() {
        assertEquals('t', option.getId());
    }

    @Test
    public void testGetKey() {
        assertEquals("test", option.getKey());
    }

    @Test
    public void testGetOpt() {
        assertEquals("test", option.getOpt());
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
    public void testHasOptionalArg() {
        assertFalse(option.hasOptionalArg());
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
        assertEquals("test option", option.getDescription());
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
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testGetArgName() {
        assertNull(option.getArgName());
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
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
        assertEquals(',', option.getValueSeparator());
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

    @Test
    public void testAddValueForProcessing() {
        try {
            option.addValueForProcessing("value");
        } catch (RuntimeException e) {
            assertEquals("NO_ARGS_ALLOWED", e.getMessage());
        }
    }

    @Test
    public void testProcessValue() {
        option.setArgs(2);
        option.processValue("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testAdd() {
        option.add("value");
        assertEquals("value", option.getValue());
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
    public void testGetValueWithDefaultValue() {
        assertEquals("default", option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
    }

    @Test
    public void testGetValuesList() {
        assertNotNull(option.getValuesList());
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test
    public void testToString() {
        assertEquals("[ option: test test option  ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("test", "test option");
        Option option2 = new Option("test", "test option");
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("test", "test option");
        Option option2 = new Option("test", "test option");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option clone = (Option) option.clone();
        assertNotSame(option, clone);
        assertEquals(option.getOpt(), clone.getOpt());
        assertEquals(option.getLongOpt(), clone.getLongOpt());
        assertEquals(option.hasArgs(), clone.hasArgs());
        assertEquals(option.getArgs(), clone.getArgs());
        assertEquals(option.isRequired(), clone.isRequired());
    }

    @Test
    public void testClearValues() {
        option.add("value1");
        option.add("value2");
        assertEquals(2, option.getValuesList().size());

        option.clearValues();
        assertEquals(0, option.getValuesList().size());
    }
}