import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder ob = OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder ob = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder ob = OptionBuilder.withArgName("test");
        assertEquals("test", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder ob = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder ob = OptionBuilder.withDescription("test description");
        assertEquals("test description", OptionBuilder.description);
    }

    // Add more test cases for other methods

}