import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructor_1() {
        Option option = new Option("a", "Description");
        assertEquals("a", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(false, option.hasArg());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionConstructor_2() {
        Option option = new Option("a", true, "Description");
        assertEquals("a", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(true, option.hasArg());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionConstructor_3() {
        Option option = new Option("a", "long", true, "Description");
        assertEquals("a", option.getOpt());
        assertEquals("long", option.getLongOpt());
        assertEquals(true, option.hasArg());
        assertEquals("arg", option.getArgName());
    }

    // More test cases for other methods should be added here...

}