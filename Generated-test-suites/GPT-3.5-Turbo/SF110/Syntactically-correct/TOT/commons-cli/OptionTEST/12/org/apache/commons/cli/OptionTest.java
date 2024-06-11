import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    // Test cases for Option constructors
    @Test
    public void testOptionConstructor1() {
        try {
            Option option = new Option("t", "test");
            assertEquals("t", option.getOpt());
            assertEquals("test", option.getDescription());
            assertFalse(option.hasArg());
        } catch (IllegalArgumentException e) {
            fail("Exception thrown incorrectly");
        }
    }

    @Test
    public void testOptionConstructor2() {
        try {
            Option option = new Option("f", true, "file");
            assertEquals("f", option.getOpt());
            assertEquals("file", option.getDescription());
            assertTrue(option.hasArg());
        } catch (IllegalArgumentException e) {
            fail("Exception thrown incorrectly");
        }
    }

    @Test
    public void testOptionConstructor3() {
        try {
            Option option = new Option("v", "version", true, "display version");
            assertEquals("v", option.getOpt());
            assertEquals("version", option.getLongOpt());
            assertEquals("display version", option.getDescription());
            assertTrue(option.hasArg());
        } catch (IllegalArgumentException e) {
            fail("Exception thrown incorrectly");
        }
    }

    // Test cases for Option getters and setters
    @Test
    public void testGetSetType() {
        Option option = new Option("t", "test");
        assertNull(option.getType());

        String type = "testType";
        option.setType(type);
        assertEquals(type, option.getType());
    }

    @Test
    public void testGetSetLongOpt() {
        Option option = new Option("t", "test");
        assertNull(option.getLongOpt());

        String longOpt = "testOpt";
        option.setLongOpt(longOpt);
        assertEquals(longOpt, option.getLongOpt());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = new Option("t", "test");
        assertFalse(option.hasOptionalArg());

        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option1 = new Option("t", "test");
        assertFalse(option1.hasLongOpt());

        Option option2 = new Option("o", true, "test");
        assertFalse(option2.hasLongOpt());

        Option option3 = new Option("v", "version", true, "display version");
        assertTrue(option3.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option1 = new Option("t", "test");
        assertFalse(option1.hasArg());

        Option option2 = new Option("o", true, "test");
        assertTrue(option2.hasArg());

        Option option3 = new Option("v", "version", false, "display version");
        assertFalse(option3.hasArg());
    }

    @Test
    public void testGetSetDescription() {
        Option option = new Option("t", "test");
        assertNull(option.getDescription());

        String description = "testDescription";
        option.setDescription(description);
        assertEquals(description, option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("t", "test");
        assertFalse(option.isRequired());

        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testGetSetArgName() {
        Option option = new Option("t", "test");
        assertNull(option.getArgName());

        String argName = "testArg";
        option.setArgName(argName);
        assertEquals(argName, option.getArgName());
    }

    @Test
    public void testHasArgs() {
        Option option1 = new Option("t", "test");
        assertFalse(option1.hasArgs());

        Option option2 = new Option("o", true, "test");
        assertFalse(option2.hasArgs());

        Option option3 = new Option("v", "version", false, "display version");
        assertFalse(option3.hasArgs());

        Option option4 = new Option("m", "multiarg", true, "multi args");
        assertTrue(option4.hasArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("t", "test");
        assertEquals('\0', option.getValueSeparator());

        char sep = ',';
        option.setValueSeparator(sep);
        assertEquals(sep, option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option option1 = new Option("t", "test");
        assertFalse(option1.hasValueSeparator());

        Option option2 = new Option("m", "multiarg", true, "multi args");
        assertFalse(option2.hasValueSeparator());

        Option option3 = new Option("c", "csv", true, "comma separated values");
        option3.setValueSeparator(',');
        assertTrue(option3.hasValueSeparator());
    }

    @Test
    public void testGetSetArgs() {
        Option option = new Option("t", "test");
        assertEquals(-1, option.getArgs());

        int num = 3;
        option.setArgs(num);
        assertEquals(num, option.getArgs());
    }

    // Additional test cases

    @Test
    public void testAddValueForProcessing() {
        Option option1 = new Option("t", "test");
        assertThrows(RuntimeException.class, () -> option1.addValueForProcessing("value"));

        Option option2 = new Option("m", "multiarg", true, "multi args");
        assertDoesNotThrow(() -> option2.addValueForProcessing("value1,value2"));
        assertEquals("value1", option2.getValue(0));
        assertEquals("value2", option2.getValue(1));
    }

    @Test
    public void testGetValue() {
        Option option1 = new Option("t", "test");
        assertNull(option1.getValue());

        Option option2 = new Option("m", "multiarg", true, "multi args");
        option2.addValueForProcessing("value1,value2");
        assertEquals("value1", option2.getValue());
    }

    @Test
    public void getSetClearValues() {
        Option option = new Option("t", "test");
        assertTrue(option.getValuesList().isEmpty());

        option.addValueForProcessing("value1");
        assertFalse(option.getValuesList().isEmpty());

        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }
}