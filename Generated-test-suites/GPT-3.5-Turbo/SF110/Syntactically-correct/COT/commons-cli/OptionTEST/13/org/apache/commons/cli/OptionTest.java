import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    @Test
    public void testConstructor1() {
        try {
            Option opt = new Option("test", "description");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }

    @Test
    public void testConstructor2() {
        try {
            Option opt = new Option("test", true, "description");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }

    @Test
    public void testConstructor3() {
        try {
            Option opt = new Option("test", "longOpt", true, "description");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }

    @Test
    public void testGetId() {
        Option opt = new Option("test", "description");
        assertEquals('t', opt.getId());
    }

    @Test
    public void testGetOpt() {
        Option opt = new Option("test", "description");
        assertEquals("test", opt.getOpt());
    }

    @Test
    public void testGetType() {
        Option opt = new Option("test", "description");
        assertNull(opt.getType());
    }

    @Test
    public void testSetType() {
        Option opt = new Option("test", "description");
        opt.setType("type");
        assertEquals("type", opt.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option opt = new Option("test", "description");
        assertNull(opt.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        Option opt = new Option("test", "description");
        opt.setLongOpt("longOpt");
        assertEquals("longOpt", opt.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option opt = new Option("test", "description");
        opt.setOptionalArg(true);
        assertTrue(opt.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option opt = new Option("test", "description");
        assertFalse(opt.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option opt = new Option("test", "description");
        assertFalse(opt.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option opt = new Option("test", "description");
        assertEquals("description", opt.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option opt = new Option("test", "description");
        opt.setDescription("newDescription");
        assertEquals("newDescription", opt.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option opt = new Option("test", "description");
        assertFalse(opt.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option opt = new Option("test", "description");
        opt.setRequired(true);
        assertTrue(opt.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option opt = new Option("test", "description");
        opt.setArgName("argName");
        assertEquals("argName", opt.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option opt = new Option("test", "description");
        assertFalse(opt.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option opt = new Option("test", "description");
        assertFalse(opt.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option opt = new Option("test", "description");
        opt.setArgs(2);
        assertEquals(2, opt.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option opt = new Option("test", "description");
        opt.setValueSeparator(':');
        assertEquals(':', opt.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option opt = new Option("test", "description");
        assertFalse(opt.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option opt = new Option("test", "description");
        assertEquals(-1, opt.getArgs());
    }

    @Test
    public void testAddValueForProcessing() {
        Option opt = new Option("test", "description");
        try {
            opt.addValueForProcessing("value");
            fail("RuntimeException should be thrown");
        } catch (RuntimeException e) {
            assertEquals("NO_ARGS_ALLOWED", e.getMessage());
        }
    }

    @Test
    public void testProcessValue() {
        Option opt = new Option("test", "description");
        opt.setArgs(2);
        opt.processValue("value1:value2");
        assertEquals("value1", opt.getValue(0));
        assertEquals("value2", opt.getValue(1));
    }

    @Test
    public void testProcessValueWithNoValueSeparator() {
        Option opt = new Option("test", "description");
        opt.setArgs(2);
        opt.processValue("value1");
        assertEquals("value1", opt.getValue(0));
        assertNull(opt.getValue(1));
    }

    @Test
    public void testAdd() {
        Option opt = new Option("test", "description");
        opt.setArgs(2);
        opt.add("value1");
        assertEquals("value1", opt.getValue(0));
        assertNull(opt.getValue(1));
        try {
            opt.add("value2");
            fail("RuntimeException should be thrown");
        } catch (RuntimeException e) {
            assertEquals("Cannot add value, list full.", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        Option opt = new Option("test", "description");
        assertNull(opt.getValue());
        opt.add("value");
        assertEquals("value", opt.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        Option opt = new Option("test", "description");
        assertNull(opt.getValue(0));
        opt.add("value1");
        assertEquals("value1", opt.getValue(0));
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option opt = new Option("test", "description");
        assertNull(opt.getValue("defaultValue"));
        opt.add("value");
        assertEquals("value", opt.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        Option opt = new Option("test", "description");
        assertNull(opt.getValues());
        opt.add("value1");
        opt.add("value2");
        String[] values = opt.getValues();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testGetValuesList() {
        Option opt = new Option("test", "description");
        assertNotNull(opt.getValuesList());
        opt.add("value1");
        opt.add("value2");
        assertEquals(2, opt.getValuesList().size());
        assertEquals("value1", opt.getValuesList().get(0));
        assertEquals("value2", opt.getValuesList().get(1));
    }

    @Test
    public void testToString() {
        Option opt = new Option("test", "description");
        assertEquals("[ option: test  [ARG] :: description ]", opt.toString());
        opt.setLongOpt("longOpt");
        assertEquals("[ option: test longOpt [ARG] :: description ]", opt.toString());
        opt.setArgs(2);
        assertEquals("[ option: test longOpt [ARG...] :: description ]", opt.toString());
        opt.setType("type");
        assertEquals("[ option: test longOpt [ARG...] :: description :: type ]", opt.toString());
    }

    @Test
    public void testEquals() {
        Option opt1 = new Option("test", "description");
        Option opt2 = new Option("test", "description");
        Option opt3 = new Option("test", "description");
        opt3.setLongOpt("longOpt");
        Option opt4 = new Option("test", "description");
        opt4.setLongOpt("longOpt2");

        assertTrue(opt1.equals(opt1));
        assertTrue(opt1.equals(opt2));

        assertFalse(opt1.equals(null));
        assertFalse(opt1.equals("test"));

        assertFalse(opt1.equals(opt3));
        assertFalse(opt1.equals(opt4));
    }

    @Test
    public void testHashCode() {
        Option opt1 = new Option("test", "description");
        Option opt2 = new Option("test", "description");
        Option opt3 = new Option("test", "description");
        opt3.setLongOpt("longOpt");
        Option opt4 = new Option("test", "description");
        opt4.setLongOpt("longOpt2");

        assertEquals(opt1.hashCode(), opt2.hashCode());
        assertNotEquals(opt1.hashCode(), opt3.hashCode());
        assertNotEquals(opt1.hashCode(), opt4.hashCode());
    }

    @Test
    public void testClone() {
        Option opt1 = new Option("test", "description");
        opt1.setArgs(2);
        Option opt2 = (Option) opt1.clone();

        assertNotSame(opt1, opt2);
        assertNotSame(opt1.getValuesList(), opt2.getValuesList());

        assertEquals(opt1.getOpt(), opt2.getOpt());
        assertEquals(opt1.getLongOpt(), opt2.getLongOpt());
        assertEquals(opt1.getDescription(), opt2.getDescription());
        assertEquals(opt1.isRequired(), opt2.isRequired());
        assertEquals(opt1.getArgName(), opt2.getArgName());
        assertEquals(opt1.hasArg(), opt2.hasArg());
        assertEquals(opt1.hasArgs(), opt2.hasArgs());
        assertEquals(opt1.getType(), opt2.getType());
        assertEquals(opt1.getArgs(), opt2.getArgs());
        assertEquals(opt1.getValueSeparator(), opt2.getValueSeparator());
        assertEquals(opt1.hasValueSeparator(), opt2.hasValueSeparator());
        assertEquals(opt1.getValue(), opt2.getValue());
        assertEquals(opt1.getValuesList().size(), opt2.getValuesList().size());
    }

    @Test
    public void testClearValues() {
        Option opt = new Option("test", "description");
        opt.add("value1");
        opt.add("value2");
        assertEquals(2, opt.getValuesList().size());
        opt.clearValues();
        assertEquals(0, opt.getValuesList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option opt = new Option("test", "description");
        opt.addValue("value");
    }
}