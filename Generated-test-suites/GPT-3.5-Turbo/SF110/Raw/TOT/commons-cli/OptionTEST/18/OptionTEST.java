import org.junit.Test;
import static org.junit.Assert.*;


public class OptionTest {

    @Test
    public void testGetOpt() {
        try {
            Option option = new Option("o", "option", true, "description");
            assertEquals("o", option.getOpt());
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testGetType() {
        Option option = new Option("o", "option", true, "description");
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        try {
            Option option = new Option("o", "option", true, "description");
            assertEquals("option", option.getLongOpt());
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("o", "option", true, "description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        try {
            Option option1 = new Option("o", "option", true, "description");
            Option option2 = new Option("o", true, "description");
            assertTrue(option1.hasLongOpt());
            assertFalse(option2.hasLongOpt());
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testHasArg() {
        try {
            Option option1 = new Option("o", true, "description");
            Option option2 = new Option("o", false, "description");
            Option option3 = new Option("o", "option", true, "description");
            assertTrue(option1.hasArg());
            assertFalse(option2.hasArg());
            assertTrue(option3.hasArg());
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testGetDescription() {
        try {
            Option option = new Option("o", "option", true, "description");
            assertEquals("description", option.getDescription());
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("o", "option", true, "description");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("o", "option", true, "description");
        option.setArgName("argument");
        assertEquals("argument", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option1 = new Option("o", "option", true, "description");
        Option option2 = new Option("o", "option", true, "description");
        option2.setArgName("");
        assertTrue(option1.hasArgName());
        assertFalse(option2.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option1 = new Option("o", true, "description");
        Option option2 = new Option("o", "option", true, "description");
        option1.setArgs(2);
        option2.setArgs(2);
        assertTrue(option1.hasArgs());
        assertTrue(option2.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("o", "option", true, "description");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasValueSeparator() {
        Option option1 = new Option("o", true, "description");
        Option option2 = new Option("o", "option", true, "description");
        option1.setValueSeparator(',');
        option2.setValueSeparator(',');
        assertTrue(option1.hasValueSeparator());
        assertTrue(option2.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option1 = new Option("o", true, "description");
        Option option2 = new Option("o", "option", true, "description");
        option1.setArgs(2);
        option2.setArgs(2);
        assertEquals(2, option1.getArgs());
        assertEquals(2, option2.getArgs());
    }

    @Test
    public void testAddValueForProcessing() {
        try {
            Option option = new Option("o", "option", true, "description");
            option.setArgs(2);
            option.addValueForProcessing("value1,value2");
            assertEquals("value1", option.getValue(0));
            assertEquals("value2", option.getValue(1));
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testGetValue() {
        try {
            Option option = new Option("o", "option", true, "description");
            option.setArgs(2);
            option.addValueForProcessing("value1,value2");
            assertEquals("value1", option.getValue());
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithIndexOutOfBounds() {
        Option option = new Option("o", "option", true, "description");
        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2");
        option.getValue(2);
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("o", "option", true, "description");
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals("defaultValue", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        try {
            Option option = new Option("o", "option", true, "description");
            option.setArgs(2);
            option.addValueForProcessing("value1,value2");
            String[] expected = {"value1", "value2"};
            assertArrayEquals(expected, option.getValues());
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testGetValuesList() {
        try {
            Option option = new Option("o", "option", true, "description");
            option.setArgs(2);
            option.addValueForProcessing("value1,value2");
            assertEquals("[value1, value2]", option.getValuesList().toString());
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testEquals() {
        try {
            Option option1 = new Option("o", "option", true, "description");
            Option option2 = new Option("o", "option", true, "description");
            Option option3 = new Option("o", true, "description");
            Option option4 = new Option("o", "option2", true, "description");
            assertTrue(option1.equals(option2));
            assertFalse(option1.equals(option3));
            assertFalse(option1.equals(option4));
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("o", "option", true, "description");
        Option option2 = new Option("o", "option", true, "description");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        try {
            Option option1 = new Option("o", "option", true, "description");
            option1.addValueForProcessing("value1");
            Option option2 = (Option) option1.clone();
            option1.addValueForProcessing("value2");
            assertFalse(option1.getValuesList().equals(option2.getValuesList()));
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test
    public void testClearValues() {
        try {
            Option option = new Option("o", "option", true, "description");
            option.addValueForProcessing("value1,value2");
            option.clearValues();
            assertEquals("[]", option.getValuesList().toString());
        } catch (Exception e) {
            fail("Unexpected exception!");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueThrowsException() {
        Option option = new Option("o", "option", true, "description");
        option.addValue("value1");
    }

}