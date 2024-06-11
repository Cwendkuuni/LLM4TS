import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionConstructor1() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
    }

    @Test
    public void testOptionConstructor2() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
    }

    @Test
    public void testOptionConstructor3() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
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

        option = new Option(null, "longOpt", true, "description");
        assertEquals("longOpt", option.getKey());
    }

    @Test
    public void testGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());

        Object type = new Object();
        option.setType(type);
        assertEquals(type, option.getType());
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

        option = new Option("opt", true, "description");
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
    public void testSetArgName() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgName());

        option.setArgName("argName");
        assertTrue(option.hasArgName());
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
    public void testSetArgs() {
        Option option = new Option("opt", "description");
        assertEquals(-1, option.getArgs());

        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasValueSeparator());

        option.setValueSeparator(':');
        assertTrue(option.hasValueSeparator());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("opt", "description");

        try {
            option.addValueForProcessing("value");
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            assertEquals("NO_ARGS_ALLOWED", e.getMessage());
        }

        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
    }

    @Test
    public void testGetValue() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue());

        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueIndexOutOfBounds() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue(0));

        option.addValueForProcessing("value");
        assertNull(option.getValue(1));
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

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List<String> values = option.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "description");
        assertEquals("[ option: opt [ARG] :: description ]", option.toString());

        option.setLongOpt("longOpt");
        assertEquals("[ option: opt longOpt [ARG] :: description ]", option.toString());

        option.setArgs(2);
        assertEquals("[ option: opt longOpt [ARG...] :: description ]", option.toString());

        option.setType(new Object());
        assertEquals("[ option: opt longOpt [ARG...] :: description :: class java.lang.Object ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertTrue(option1.equals(option2));

        option2.setLongOpt("longOpt");
        assertFalse(option1.equals(option2));
        option1.setLongOpt("longOpt");
        assertTrue(option1.equals(option2));

        option2.setArgName("argName");
        assertFalse(option1.equals(option2));
        option1.setArgName("argName");
        assertTrue(option1.equals(option2));

        option2.setOptionalArg(true);
        assertFalse(option1.equals(option2));
        option1.setOptionalArg(true);
        assertTrue(option1.equals(option2));

        option2.setRequired(true);
        assertFalse(option1.equals(option2));
        option1.setRequired(true);
        assertTrue(option1.equals(option2));

        option2.setDescription("newDescription");
        assertFalse(option1.equals(option2));
        option1.setDescription("newDescription");
        assertTrue(option1.equals(option2));

        option2.setArgs(2);
        assertFalse(option1.equals(option2));
        option1.setArgs(2);
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertEquals(option1.hashCode(), option2.hashCode());

        option2.setLongOpt("longOpt");
        option1.setLongOpt("longOpt");
        assertEquals(option1.hashCode(), option2.hashCode());

        option2.setArgName("argName");
        option1.setArgName("argName");
        assertEquals(option1.hashCode(), option2.hashCode());

        option2.setOptionalArg(true);
        option1.setOptionalArg(true);
        assertEquals(option1.hashCode(), option2.hashCode());

        option2.setRequired(true);
        option1.setRequired(true);
        assertEquals(option1.hashCode(), option2.hashCode());

        option2.setDescription("newDescription");
        option1.setDescription("newDescription");
        assertEquals(option1.hashCode(), option2.hashCode());

        option2.setArgs(2);
        option1.setArgs(2);
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option option = new Option("opt", "description");
        assertNotNull(option.clone());
        assertNotSame(option, option.clone());

        option.addValueForProcessing("value");
        Option clonedOption = (Option) option.clone();
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
        assertFalse(option.getValuesList() == clonedOption.getValuesList());
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