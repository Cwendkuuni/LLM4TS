import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructor() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());

        Option option2 = new Option("opt", true, "description");
        assertTrue(option2.hasArg());

        Option option3 = new Option("opt", "longOpt", true, "description");
        assertEquals("longOpt", option3.getLongOpt());
    }

    @Test
    public void testOptionAddValueForProcessing() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testOptionExceptionHandling() {
        Option option = new Option("opt", "description");
        try {
            option.addValueForProcessing("value1,value2,value3");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Cannot add value, list full.", e.getMessage());
        }
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("opt", "description");
        Option clonedOption = (Option) option.clone();
        assertNotSame(option, clonedOption);
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getDescription(), clonedOption.getDescription());
    }
}