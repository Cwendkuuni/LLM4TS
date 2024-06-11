import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // Check that longopt is set correctly
        assertEquals("test", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Check that numberOfArgs is set correctly
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        // Check that numberOfArgs is set correctly
        assertEquals(1, OptionBuilder.numberOfArgs);

        OptionBuilder.hasArg(false);
        // Check that numberOfArgs is set correctly
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        // Check that argName is set correctly
        assertEquals("test", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Check that required is set correctly
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // Check that valuesep is set correctly
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // Check that valuesep is set correctly
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        // Check that required is set correctly
        assertTrue(OptionBuilder.required);

        OptionBuilder.isRequired(false);
        // Check that required is set correctly
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Check that numberOfArgs is set correctly
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(2);
        // Check that numberOfArgs is set correctly
        assertEquals(2, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Check that numberOfArgs is set correctly
        assertEquals(1, OptionBuilder.numberOfArgs);
        // Check that optionalArg is set correctly
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Check that numberOfArgs is set correctly
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        // Check that optionalArg is set correctly
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(2);
        // Check that numberOfArgs is set correctly
        assertEquals(2, OptionBuilder.numberOfArgs);
        // Check that optionalArg is set correctly
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        // Check that type is set correctly
        assertEquals(Integer.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        // Check that description is set correctly
        assertEquals("test", OptionBuilder.description);
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        // Check that option is created
        assertNotNull(option);
        // Check that option has the correct opt value
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        // Check that option is created
        assertNotNull(option);
        // Check that option has the correct longopt value
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("t");
        // Check that option is created
        assertNotNull(option);
        // Check that option has the correct opt value
        assertEquals("t", option.getOpt());
    }
}