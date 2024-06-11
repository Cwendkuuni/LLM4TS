import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("foo");
        Option option = OptionBuilder.create();
        assertEquals("foo", option.getLongOpt());

        OptionBuilder.withLongOpt("bar");
        option = OptionBuilder.create();
        assertEquals("bar", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());

        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        assertFalse(option.hasArg());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        Option option = OptionBuilder.create();
        assertEquals("name", option.getArgName());

        OptionBuilder.withArgName("arg");
        option = OptionBuilder.create();
        assertEquals("arg", option.getArgName());
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
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());

        OptionBuilder.withValueSeparator();
        option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
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
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.getOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.getOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(5);
        Option option = OptionBuilder.create();
        assertEquals(5, option.getArgs());
        assertTrue(option.getOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Option option = OptionBuilder.create();
        assertEquals(String.class, option.getType());

        OptionBuilder.withType(Integer.class);
        option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("desc");
        Option option = OptionBuilder.create();
        assertEquals("desc", option.getDescription());

        OptionBuilder.withDescription("description");
        option = OptionBuilder.create();
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreateWithCharOpt() {
        Option option = OptionBuilder.create('a');
        assertEquals("a", option.getOpt());

        option = OptionBuilder.create('b');
        assertEquals("b", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("foo");
        Option option = OptionBuilder.create();
        assertNotNull(option);

        OptionBuilder.reset();
        try {
            OptionBuilder.create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("a");
        assertEquals("a", option.getOpt());

        option = OptionBuilder.create("b");
        assertEquals("b", option.getOpt());
    }

}