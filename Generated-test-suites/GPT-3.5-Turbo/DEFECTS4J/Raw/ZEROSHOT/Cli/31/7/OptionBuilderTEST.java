import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOption");
        assertEquals("testOption", optionBuilder.getOption().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.getOption().getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.getOption().getArgs());

        optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(-1, optionBuilder.getOption().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArg");
        assertEquals("testArg", optionBuilder.getOption().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.getOption().isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getOption().getValueSeparator());
    }

    @Test
    public void testWithDefaultSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.getOption().getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(optionBuilder.getOption().isRequired());

        optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.getOption().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getOption().getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.getOption().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.getOption().hasOptionalArg());
        assertEquals(1, optionBuilder.getOption().getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertTrue(optionBuilder.getOption().hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getOption().getArgs());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertTrue(optionBuilder.getOption().hasOptionalArg());
        assertEquals(3, optionBuilder.getOption().getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.getOption().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", optionBuilder.getOption().getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals('t', option.getOpt());
    }

    @Test
    public void testCreateWithoutOpt() {
        try {
            OptionBuilder.create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOption");
        assertEquals("testOption", option.getOpt());
    }
}