import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt(null);
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());

        OptionBuilder.withLongOpt("test");
        option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());

        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        assertEquals(-1, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName(null);
        Option option = OptionBuilder.create();
        assertEquals("arg", option.getArgName());

        OptionBuilder.withArgName("test");
        option = OptionBuilder.create();
        assertEquals("test", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());

        OptionBuilder.isRequired(false);
        option = OptionBuilder.create();
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());

        OptionBuilder.hasOptionalArg();
        option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());

        OptionBuilder.hasOptionalArg(false);
        option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator((char) 0);
        Option option = OptionBuilder.create();
        assertEquals((char) 0, option.getValueSeparator());

        OptionBuilder.withValueSeparator(':');
        option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());

        OptionBuilder.hasArgs(3);
        option = OptionBuilder.create();
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(null);
        Option option = OptionBuilder.create();
        assertNull(option.getType());

        OptionBuilder.withType(Integer.class);
        option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription(null);
        Option option = OptionBuilder.create();
        assertNull(option.getDescription());

        OptionBuilder.withDescription("test");
        option = OptionBuilder.create();
        assertEquals("test", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMissingLongOpt() {
        OptionBuilder.create();
    }

}