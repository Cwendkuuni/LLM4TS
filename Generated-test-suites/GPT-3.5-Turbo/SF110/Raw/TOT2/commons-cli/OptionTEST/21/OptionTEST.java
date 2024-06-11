import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testGetOpt() {
        Option option = new Option("o", "Description");
        assertEquals("o", option.getOpt());
    }

    @Test
    public void testGetType() {
        Object type = new Object();
        Option option = new Option("o", "Description");
        option.setType(type);
        assertEquals(type, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("o", "longO", true, "Description");
        assertEquals("longO", option.getLongOpt());
    }

    // Add more test cases for other public methods

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("o", "Description");
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValues().get(0));
        assertEquals("value2", option.getValues().get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_NoArgsAllowed() {
        Option option = new Option("o", "Description");
        option.setArgs(-1);
        option.addValueForProcessing("value");
    }

    // Add more edge case and exception handling test cases

}