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
    }

    @Test
    public void testConstructorWithHasArg() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new Option("opt", "description");
        assertEquals("o", option.getKey());
        assertEquals('o', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option(null, "description");
        assertNull(option.getOpt());
        assertEquals("description", option.getKey());
    }

    @Test
    public void testSetAndGetOpt() {
        Option option = new Option("opt", "description");
        option.setOpt("newOpt");
        assertEquals("newOpt", option.getOpt());
    }

    @Test
    public void testGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("opt", "description");
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetAndGetLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        option.setLongOpt("newLongOpt");
        assertEquals("newLongOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("opt", true, "description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option1 = new Option("opt", true, "description");
        Option option2 = new Option("opt", "longOpt", true, "description");
        assertFalse(option1.hasLongOpt());
        assertTrue(option2.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", true, "description");
        Option option3 = new Option("opt", "longOpt", true, "description");
        assertFalse(option1.hasArg());
        assertTrue(option2.hasArg());
        assertTrue(option3.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testSetAndGetDescription() {
        Option option = new Option("opt", "description");
        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", true, "description");
        assertFalse(option1.isRequired());
        assertFalse(option2.isRequired());
    }

    @Test
    public void testSetAndGetRequired() {
        Option option = new Option("opt", "description");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetAndGetArgName() {
        Option option = new Option("opt", "description");
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        option2.setArgName("argName");
        assertFalse(option1.hasArgName());
        assertTrue(option2.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", true, "description");
        Option option3 = new Option("opt", "longOpt", true, "description");
        assertFalse(option1.hasArgs());
        assertFalse(option2.hasArgs());
        assertFalse(option3.hasArgs());
    }

    @Test
    public void testSetAndGetValueSeparator() {
        Option option = new Option("opt", "description");
        option.setValueSeparator(':');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        option2.setValueSeparator(':');
        assertFalse(option1.hasValueSeparator());
        assertTrue(option2.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingException() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
    }

    @Test
    public void testProcessValue() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        option2.setValueSeparator(':');

        option1.addValueForProcessing("value");
        assertEquals("value", option1.getValue());

        option2.addValueForProcessing("value1:value2");
        assertEquals("value1", option2.getValue(0));
        assertEquals("value2", option2.getValue(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueException() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }

    @Test
    public void testGetValue() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        option2.addValueForProcessing("value");

        assertNull(option1.getValue());
        assertEquals("value", option2.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("opt", "description");
        assertEquals("defaultValue", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        option2.addValueForProcessing("value1");
        option2.addValueForProcessing("value2");

        assertNull(option1.getValues());
        assertArrayEquals(new String[] {"value1", "value2"}, option2.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertEquals(2, option.getValuesList().size());
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", null, true, "description");
        Option option3 = new Option("opt", "longOpt", true, "description");

        assertEquals("[ option: opt :: description ]", option1.toString());
        assertEquals("[ option: opt [ARG] :: description ]", option2.toString());
        assertEquals("[ option: opt longOpt [ARG] :: description ]", option3.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        Option option3 = new Option("newOpt", "description");
        Option option4 = new Option("opt", "newDescription");

        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        Option option3 = new Option("newOpt", "description");
        Option option4 = new Option("opt", "newDescription");

        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("opt", "description");
        option1.addValueForProcessing("value");

        Option option2 = (Option) option1.clone();
        option2.addValueForProcessing("newValue");

        assertEquals(option1.getValues().length, 1);
        assertEquals(option2.getValues().length, 2);
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");

        option.clearValues();
        assertEquals(option.getValues().length, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueException() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}