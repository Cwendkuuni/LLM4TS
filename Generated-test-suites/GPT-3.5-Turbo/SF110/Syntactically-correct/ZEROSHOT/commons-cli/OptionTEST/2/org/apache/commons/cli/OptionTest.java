import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructor1() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasArgs());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertNull(option.getArgName());
        assertNull(option.getValues());
        assertNull(option.getValue());
    }

    @Test
    public void testConstructor2() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgs());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertNull(option.getArgName());
        assertNull(option.getValues());
        assertNull(option.getValue());
    }

    @Test
    public void testConstructor3() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgs());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertNull(option.getArgName());
        assertNull(option.getValues());
        assertNull(option.getValue());
    }

    @Test
    public void testGetId() {
        Option option = new Option("opt", "description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option1 = new Option("opt", "description");
        assertEquals("opt", option1.getKey());
        Option option2 = new Option(null, "longOpt", true, "description");
        assertEquals("longOpt", option2.getKey());
    }

    @Test
    public void testGetTypeAndSetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());
        Object type = new Object();
        option.setType(type);
        assertEquals(type, option.getType());
    }

    @Test
    public void testGetAndSetLongOpt() {
        Option option = new Option("opt", "description");
        assertNull(option.getLongOpt());
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option1 = new Option("opt", "description");
        assertFalse(option1.hasLongOpt());
        Option option2 = new Option("opt", "longOpt", true, "description");
        assertTrue(option2.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option1 = new Option("opt", "description");
        assertFalse(option1.hasArg());
        Option option2 = new Option("opt", true, "description");
        assertTrue(option2.hasArg());
        Option option3 = new Option("opt", "longOpt", true, "description");
        assertTrue(option3.hasArg());
    }

    @Test
    public void testGetAndSetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("opt", "description");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testGetAndSetArgName() {
        Option option = new Option("opt", "description");
        assertNull(option.getArgName());
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option1 = new Option("opt", "description");
        assertFalse(option1.hasArgName());
        Option option2 = new Option("opt", "description");
        option2.setArgName("argName");
        assertTrue(option2.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option1 = new Option("opt", "description");
        assertFalse(option1.hasArgs());
        Option option2 = new Option("opt", true, "description");
        assertFalse(option2.hasArgs());
        Option option3 = new Option("opt", "longOpt", true, "description");
        assertFalse(option3.hasArgs());
        Option option4 = new Option("opt", "description");
        option4.setArgs(2);
        assertTrue(option4.hasArgs());
    }

    @Test
    public void testGetAndSetArgs() {
        Option option = new Option("opt", "description");
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasValueSeparator() {
        Option option1 = new Option("opt", "description");
        assertFalse(option1.hasValueSeparator());
        Option option2 = new Option("opt", "description");
        option2.setValueSeparator(',');
        assertTrue(option2.hasValueSeparator());
    }

    @Test
    public void testGetValueSeparator() {
        Option option = new Option("opt", "description");
        assertEquals('\0', option.getValueSeparator());
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testGetValue() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue());
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueAtIndex() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue(0));
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefault() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue("defaultValue"));
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("opt", "description");
        assertNull(option1.getValues());
        Option option2 = new Option("opt", "description");
        option2.addValueForProcessing("value1");
        option2.addValueForProcessing("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option2.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("opt", "description");
        assertEquals(0, option.getValuesList().size());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(2, option.getValuesList().size());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("opt", "description");
        assertEquals("[ option: opt :: description ]", option1.toString());
        Option option2 = new Option("opt", "longOpt", true, "description");
        assertEquals("[ option: opt longOpt [ARG] :: description ]", option2.toString());
        Option option3 = new Option("opt", "longOpt", false, "description");
        assertEquals("[ option: opt longOpt :: description ]", option3.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        Option option3 = new Option("opt", "longOpt", true, "description");
        Option option4 = new Option("opt", "longOpt", false, "description");
        Option option5 = new Option("newOpt", "description");
        assertFalse(option1.equals(null));
        assertFalse(option1.equals(new Object()));
        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
        assertFalse(option1.equals(option5));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        Option option3 = new Option("opt", "longOpt", true, "description");
        Option option4 = new Option("opt", "longOpt", false, "description");
        Option option5 = new Option("newOpt", "description");
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option4.hashCode());
        assertNotEquals(option1.hashCode(), option5.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("opt", "description");
        option1.addValueForProcessing("value");
        Option option2 = (Option) option1.clone();
        assertNotSame(option1, option2);
        assertArrayEquals(option1.getValues(), option2.getValues());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        assertNull(option.getValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}