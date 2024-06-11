import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructor1() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertFalse(option.hasLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
    }

    @Test
    public void testConstructor2() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertFalse(option.hasLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
    }

    @Test
    public void testConstructor3() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertTrue(option.hasLongOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidOpt() {
        new Option(" ", "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidLongOpt() {
        new Option("opt", " ", true, "description");
    }

    @Test
    public void testGetId() {
        Option option = new Option("opt", "description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getKey());
    }

    @Test
    public void testGetOpt() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
    }

    @Test
    public void testGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("opt", "description");
        option.setType("type");
        assertEquals("type", option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt", "description");
        assertNull(option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("opt", "description");
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("opt", "description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("opt", "description");
        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("opt", "description");
        assertFalse(option.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("opt", "description");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("opt", "description");
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testGetArgName() {
        Option option = new Option("opt", "description");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("opt", "description");
        option.setValueSeparator(':');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testGetValueSeparator() {
        Option option = new Option("opt", "description");
        assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option = new Option("opt", "description");
        assertEquals(-1, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_noArgsAllowed() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
    }

    @Test
    public void testProcessValue() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.setValueSeparator(':');
        option.processValue("value1:value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAdd_listFull() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test
    public void testGetValue() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue());
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValue_indexOutOfBounds() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue(0));
        option.addValueForProcessing("value");
        assertNull(option.getValue(1));
    }

    @Test
    public void testGetValue_withDefaultValue() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue("default"));
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("opt", "description");
        assertNull(option.getValues());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("opt", "description");
        assertNotNull(option.getValuesList());
        assertEquals(0, option.getValuesList().size());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(2, option.getValuesList().size());
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "description");
        assertEquals("[ option: opt  :: description ]", option.toString());
        option = new Option("opt", "longOpt", true, "description");
        assertEquals("[ option: opt longOpt [ARG] :: description ]", option.toString());
        option.setArgs(2);
        assertEquals("[ option: opt longOpt [ARG...] :: description ]", option.toString());
        option.setType("type");
        assertEquals("[ option: opt longOpt [ARG...] :: description :: type ]", option.toString());
    }

    @Test
    public void testEquals_sameObject() {
        Option option = new Option("opt", "description");
        assertTrue(option.equals(option));
    }

    @Test
    public void testEquals_differentClass() {
        Option option = new Option("opt", "description");
        assertFalse(option.equals("string"));
    }

    @Test
    public void testEquals_nullOpt() {
        Option option1 = new Option(null, "description");
        Option option2 = new Option(null, "description");
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testEquals_differentOpt() {
        Option option1 = new Option("opt1", "description");
        Option option2 = new Option("opt2", "description");
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEquals_nullLongOpt() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", null);
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEquals_sameOptAndLongOpt() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testHashCode_sameObject() {
        Option option = new Option("opt", "description");
        assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testHashCode_sameOptions() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testHashCode_differentOptions() {
        Option option1 = new Option("opt1", "description");
        Option option2 = new Option("opt2", "description");
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("opt", "description");
        option1.addValueForProcessing("value");
        Option option2 = (Option) option1.clone();
        assertEquals(option1.toString(), option2.toString());
        assertNotSame(option1, option2);
        assertNotSame(option1.getValuesList(), option2.getValuesList());
        assertEquals(option1.getValuesList(), option2.getValuesList());
        option2.addValueForProcessing("value2");
        assertNotEquals(option1.getValuesList(), option2.getValuesList());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
        assertEquals(1, option.getValuesList().size());
        option.clearValues();
        assertEquals(0, option.getValuesList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}