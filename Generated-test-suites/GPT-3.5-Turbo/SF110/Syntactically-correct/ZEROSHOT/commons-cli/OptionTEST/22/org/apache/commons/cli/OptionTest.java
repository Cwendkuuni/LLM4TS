import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("opt", "description");
    }

    @After
    public void tearDown() {
        option = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(option);
    }

    @Test
    public void testGetId() {
        assertEquals('o', option.getId());
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
        Object type = new Object();
        option.setType(type);
        assertEquals(type, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        String longOpt = "longOpt";
        option.setLongOpt(longOpt);
        assertEquals(longOpt, option.getLongOpt());
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
        String description = "new description";
        option.setDescription(description);
        assertEquals(description, option.getDescription());
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
        String argName = "arg";
        option.setArgName(argName);
        assertEquals(argName, option.getArgName());
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
        char sep = ':';
        option.setValueSeparator(sep);
        assertEquals(sep, option.getValueSeparator());
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
    public void testAddValueForProcessingNoArgs() {
        option.addValueForProcessing("value");
    }

    @Test
    public void testProcessValue() {
        option.setArgs(2);
        option.processValue("value1,value2");
        List<String> values = option.getValuesList();
        assertNotNull(values);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueFullList() {
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
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
        String defaultValue = "default";
        assertEquals(defaultValue, option.getValue(defaultValue));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
    }

    @Test
    public void testGetValuesList() {
        List<String> values = option.getValuesList();
        assertNotNull(values);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testToString() {
        String expected = "[ option: opt :: description ]";
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
        assertNotSame(option, clonedOption);
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getType(), clonedOption.getType());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
        assertEquals(option.hasLongOpt(), clonedOption.hasLongOpt());
        assertEquals(option.hasArg(), clonedOption.hasArg());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.isRequired(), clonedOption.isRequired());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.hasArgName(), clonedOption.hasArgName());
        assertEquals(option.hasArgs(), clonedOption.hasArgs());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
        assertEquals(option.hasValueSeparator(), clonedOption.hasValueSeparator());
        assertEquals(option.getValues(), clonedOption.getValues());
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
    }

    @Test
    public void testClearValues() {
        option.addValue("value");
        option.addValue("value");
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueUnsupported() {
        option.addValue("value");
    }
}