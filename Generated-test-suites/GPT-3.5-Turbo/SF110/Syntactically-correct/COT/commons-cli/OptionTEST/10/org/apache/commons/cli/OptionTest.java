import org.junit.*;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructor() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testConstructorWithHasArg() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.hasArg());
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("opt", "longOpt", false, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new Option("opt", "description");
        assertEquals(111, option.getId());
    }

    @Test
    public void testGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
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
        Option option = new Option("opt", "description");
        assertFalse(option.hasLongOpt());
        option.setLongOpt("longOpt");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArg());
        option.setArgs(1);
        assertTrue(option.hasArg());
    }

    @Test
    public void testGetDescription() {
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
    public void testGetArgName() {
        Option option = new Option("opt", "description");
        assertNull(option.getArgName());
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
        assertEquals(',', option.getValueSeparator());
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
    public void testAddValueForProcessing() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithListFull() {
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
    public void testGetValueByIndex() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue(0));
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("opt", "description");
        assertEquals("defaultValue", option.getValue("defaultValue"));
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("defaultValue"));
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
        assertTrue(option.getValuesList().isEmpty());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "description");
        assertEquals("[ option: opt :: description ]", option.toString());
        option.setLongOpt("longOpt");
        option.setArgs(2);
        option.setType(Integer.class);
        assertEquals("[ option: opt longOpt [ARG...] :: description :: class java.lang.Integer ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        Option option3 = new Option("opt1", "description");
        Option option4 = new Option("opt", "description1");
        Option option5 = new Option("opt1", "description1");
        Option option6 = new Option(null, null);
        assertFalse(option1.equals(null));
        assertFalse(option1.equals("string"));
        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
        assertFalse(option1.equals(option5));
        assertFalse(option1.equals(option6));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("opt", "description");
        Option option2 = (Option) option1.clone();
        assertTrue(option1 != option2);
        assertEquals(option1.getClass(), option2.getClass());
        assertEquals(option1.getOpt(), option2.getOpt());
        assertEquals(option1.getDescription(), option2.getDescription());
        assertEquals(option1.getType(), option2.getType());
        assertEquals(option1.getArgName(), option2.getArgName());
        assertEquals(option1.getLongOpt(), option2.getLongOpt());
        assertEquals(option1.isRequired(), option2.isRequired());
        assertEquals(option1.hasOptionalArg(), option2.hasOptionalArg());
        assertEquals(option1.hasLongOpt(), option2.hasLongOpt());
        assertEquals(option1.hasArg(), option2.hasArg());
        assertEquals(option1.hasArgName(), option2.hasArgName());
        assertEquals(option1.hasArgs(), option2.hasArgs());
        assertEquals(option1.getValueSeparator(), option2.getValueSeparator());
        assertEquals(option1.getArgs(), option2.getArgs());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
        assertFalse(option.hasNoValues());
        option.clearValues();
        assertTrue(option.hasNoValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}