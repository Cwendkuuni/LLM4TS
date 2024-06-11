import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructorWithDescription() {
        Option option = new Option("test", "description");
        assertNotNull(option);
    }

    @Test
    public void testOptionConstructorWithHasArgAndDescription() {
        Option option = new Option("test", true, "description");
        assertNotNull(option);
    }

    @Test
    public void testOptionConstructorWithLongOpt() {
        Option option = new Option("test", "longTest", false, "description");
        assertEquals("longTest", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("test", "description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("test", "description");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    // Add more test cases for other methods and edge cases

}