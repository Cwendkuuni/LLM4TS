import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder option = OptionBuilder.withLongOpt("test");
        assertNotNull(option);
        assertEquals("test", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder option = OptionBuilder.hasArg();
        assertNotNull(option);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder option = OptionBuilder.withArgName("name");
        assertNotNull(option);
        assertEquals("name", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder option = OptionBuilder.isRequired();
        assertNotNull(option);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder option = OptionBuilder.withDescription("description");
        assertNotNull(option);
        assertEquals("description", OptionBuilder.description);
    }

    // Add more test cases for the remaining methods

}