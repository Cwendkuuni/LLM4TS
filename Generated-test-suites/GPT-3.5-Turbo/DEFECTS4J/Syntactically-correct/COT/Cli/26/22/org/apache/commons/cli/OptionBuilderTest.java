import org.junit.Test;
import org.apache.commons.cli.Option;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgWithFalse() {
        Option option = OptionBuilder.hasArg(false).create();
        assertFalse(option.hasArg());
    }

    // Add more test methods for other public methods in OptionBuilder class

    // Test for edge cases and exception handling scenarios
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("test").create();
        assertEquals("test", option.getArgName());
    }

    // Add more test methods for edge cases and exception handling scenarios

}