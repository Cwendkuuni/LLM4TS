import org.junit.Test;
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
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("argName").create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("description").create();
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testWithOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertTrue(option.getOptionalArg());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs(2).create();
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
    }

}