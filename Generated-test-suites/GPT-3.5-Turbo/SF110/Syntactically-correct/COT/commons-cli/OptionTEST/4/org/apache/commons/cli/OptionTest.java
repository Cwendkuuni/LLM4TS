import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructorWithDescription() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
    }

    @Test
    public void testConstructorWithHasArg() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidOpt() {
        Option option = new Option("inv@lid", "description");
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
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("longOpt", option.getLongOpt());
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
    public void testHasLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArg());
        option = new Option("opt", true, "description");
        assertTrue(option.hasArg());
        option = new Option("opt", "longOpt", true, "description");
        assertTrue(option.hasArg());
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
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("opt", "description");
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgName());
        option.setArgName("argName");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
        option.setArgs(Option.UNLIMITED_VALUES);
        assertTrue(option.hasArgs());
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
        option.setValueSeparator('=');
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator('=');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option = new Option("opt", "description");
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
    }

    @Test
    public void testProcessValue() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.processValue("value1,value2,value3");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }

    @Test(expected = RuntimeException.class)
    public void testAddWithListFull() {
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
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
    }

    @Test
    public void testGetValueByIndex() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue(0));
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue(0));
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue("default"));
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue("default"));
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
        assertEquals(0, option.getValuesList().size());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(2, option.getValuesList().size());
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "description");
        assertEquals("[ option: opt :: description ]", option.toString());
        option = new Option("opt", "longOpt", true, "description");
        assertEquals("[ option: opt longOpt [ARG] :: description ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertEquals(option1, option2);
        Option option3 = new Option("opt", "longOpt", true, "description");
        assertNotEquals(option1, option3);
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertEquals(option1.hashCode(), option2.hashCode());
        Option option3 = new Option("opt", "longOpt", true, "description");
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("opt", "description");
        option1.addValueForProcessing("value1");
        Option option2 = (Option) option1.clone();
        assertEquals(option1, option2);
        assertNotSame(option1, option2);
        assertNotSame(option1.getValuesList(), option2.getValuesList());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value1");
        option.clearValues();
        assertEquals(0, option.getValuesList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}