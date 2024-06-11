import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("test");
        assertNotNull(optionBuilder);
        assertEquals("test", optionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertNotNull(optionBuilder);
        assertEquals(1, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertNotNull(optionBuilder);
        assertEquals(1, optionBuilder.numberOfArgs);
        optionBuilder = OptionBuilder.hasArg(false);
        assertNotNull(optionBuilder);
        assertEquals(Option.UNINITIALIZED, optionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("test");
        assertNotNull(optionBuilder);
        assertEquals("test", optionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertNotNull(optionBuilder);
        assertEquals(true, optionBuilder.required);
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertNotNull(optionBuilder);
        assertEquals(':', optionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertNotNull(optionBuilder);
        assertEquals('=', optionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertNotNull(optionBuilder);
        assertEquals(true, optionBuilder.required);
        optionBuilder = OptionBuilder.isRequired(false);
        assertNotNull(optionBuilder);
        assertEquals(false, optionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertNotNull(optionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertNotNull(optionBuilder);
        assertEquals(2, optionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertNotNull(optionBuilder);
        assertEquals(1, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertNotNull(optionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        assertNotNull(optionBuilder);
        assertEquals(2, optionBuilder.numberOfArgs);
        assertTrue(optionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertNotNull(optionBuilder);
        assertEquals(String.class, optionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test");
        assertNotNull(optionBuilder);
        assertEquals("test", optionBuilder.description);
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testCreate() {
        try {
            OptionBuilder.create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        assertNotNull(option);
        assertEquals("test", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals(false, option.isRequired());
    }
}