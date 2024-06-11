
import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;

public class OptionTest {

    @Test
    public void testConstructorWithOptAndDescription() {
        Option option = new Option("o", "description");
        assertEquals("o", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testConstructorWithOptHasArgAndDescription() {
        Option option = new Option("o", true, "description");
        assertEquals("o", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testConstructorWithOptLongOptHasArgAndDescription() {
        Option option = new Option("o", "longOpt", true, "description");
        assertEquals("o", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidOpt() {
        Option option = new Option("inval!d", "description");
    }

    @Test
    public void testGetId() {
        Option option = new Option("o", "description");
        assertEquals(111, option.getId());
    }

    @Test
    public void testGetKeyWithoutOpt() {
        Option option = new Option(null, "longOpt", true, "description");
        assertEquals("longOpt", option.getKey());
    }

    @Test
    public void testGetKeyWithOpt() {
        Option option = new Option("o", "description");
        assertEquals("o", option.getKey());
    }

    @Test
    public void testGetType() {
        Option option = new Option("o", "description");
        assertNull(option.getType());

        Object type = new Object();
        option.setType(type);
        assertSame(type, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("o", "description");
        assertNull(option.getLongOpt());

        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("o", "description");
        assertFalse(option.hasOptionalArg());

        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("o", "description");
        assertFalse(option.hasLongOpt());

        option.setLongOpt("longOpt");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("o", "description");
        assertFalse(option.hasArg());
        option.setArgs(1);
        assertTrue(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("o", "description");
        assertEquals("description", option.getDescription());

        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("o", "description");
        assertFalse(option.isRequired());

        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("o", "description");
        assertFalse(option.hasArgName());

        option.setArgName("argName");
        assertTrue(option.hasArgName());
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("o", "description");
        assertFalse(option.hasArgs());
        option.setArgs(3);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("o", "description");
        assertFalse(option.hasValueSeparator());

        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option = new Option("o", "description");
        assertEquals(-1, option.getArgs());

        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        Option option = new Option("o", "description");
        option.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("o", "description");
        option.setArgs(3);
        option.setValueSeparator(',');

        option.addValueForProcessing("value1,value2,value3");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithListFull() {
        Option option = new Option("o", "description");
        option.setArgs(2);

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test
    public void testGetValue() {
        Option option = new Option("o", "description");
        assertNull(option.getValue());

        option.setArgs(3);
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
    }

    @Test
    public void testGetValueAtIndex() {
        Option option = new Option("o", "description");
        assertNull(option.getValue(0));

        option.setArgs(3);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("o", "description");
        assertNull(option.getValue());

        String defaultValue = "default";
        assertEquals(defaultValue, option.getValue(defaultValue));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "description");
        assertNull(option.getValues());

        option.setArgs(3);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("o", "description");
        assertNotNull(option.getValuesList());
        assertTrue(option.getValuesList().isEmpty());

        option.setArgs(3);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
        assertEquals(3, option.getValuesList().size());
        assertTrue(option.getValuesList().contains("value1"));
        assertTrue(option.getValuesList().contains("value2"));
        assertTrue(option.getValuesList().contains("value3"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "description");
        assertEquals("[ option: o  : description ]", option.toString());

        option.setArgName("argName");
        assertEquals("[ option: o  ARG : description ]", option.toString());

        option.setArgs(3);
        option.setOptionalArg(true);
        assertEquals("[ option: o  [ARG...] : description ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("o", "description");
        Option option2 = new Option("o", "description");
        Option option3 = new Option("o", "new description");
        Option option4 = new Option("o", "description");
        
        assertFalse(option1.equals(null));
        assertFalse(option1.equals(new Object()));
        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));

        option2.setLongOpt("longOpt");
        assertFalse(option1.equals(option2));
        assertFalse(option2.equals(option1));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("o", "description");
        Option option2 = new Option("o", "description");
        Option option3 = new Option("o", "new description");
        Option option4 = new Option("o", "description");
        
        assertTrue(option1.hashCode() == option1.hashCode());
        assertTrue(option1.hashCode() == option2.hashCode());
        assertFalse(option1.hashCode() == option3.hashCode());
        assertFalse(option1.hashCode() == option4.hashCode());

        option2.setLongOpt("longOpt");
        assertFalse(option1.hashCode() == option2.hashCode());
        assertFalse(option2.hashCode() == option1.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("o", "description");
        option1.addValueForProcessing("value1");

        Option option2 = (Option)option1.clone();
        assertEquals(option1.getOpt(), option2.getOpt());
        assertEquals(option1.getLongOpt(), option2.getLongOpt());
        assertEquals(option1.getDescription(), option2.getDescription());
        assertEquals(option1.getValuesList(), option2.getValuesList());
    }
}
