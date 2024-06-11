import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testConstructor1() {
        Option option = new Option("-a", "Description");
        assertEquals("-a", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertFalse(option.hasLongOpt());
    }

    @Test
    public void testConstructor2() {
        Option option = new Option("-a", true, "Description");
        assertEquals("-a", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertTrue(option.hasArg());
    }

    @Test
    public void testConstructor3() {
        Option option = new Option("-a", "--arg", true, "Description");
        assertEquals("-a", option.getOpt());
        assertEquals("--arg", option.getLongOpt());
        assertEquals("Description", option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testGetType() {
        Option option = new Option("-a", "Description");
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("-a", "Description");
        option.setType("type");
        assertEquals("type", option.getType());
    }

    // Add more test methods for other public methods

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("-a", "Description");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(2, option.getValuesList().size());
    }

    @Test
    public void testProcessValue() {
        Option option = new Option("-a", "Description");
        option.setArgs(2);
        option.processValue("value1,value2");
        assertEquals(2, option.getValuesList().size());
    }

    // Test edge cases and exception scenarios

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_NoArgsAllowed() {
        Option option = new Option("-a", "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }

}