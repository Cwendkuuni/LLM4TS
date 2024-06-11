import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testHasArgWithBoolean() {
        OptionBuilder option = OptionBuilder.hasArg(true);
        assertNotNull(option);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder option = OptionBuilder.withArgName("test");
        assertNotNull(option);
        assertEquals("test", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder option = OptionBuilder.isRequired();
        assertNotNull(option);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder option = OptionBuilder.withValueSeparator(':');
        assertNotNull(option);
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder option = OptionBuilder.isRequired(true);
        assertNotNull(option);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder option = OptionBuilder.hasArgs();
        assertNotNull(option);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }
    
    // Include other test cases for all public methods
 
}