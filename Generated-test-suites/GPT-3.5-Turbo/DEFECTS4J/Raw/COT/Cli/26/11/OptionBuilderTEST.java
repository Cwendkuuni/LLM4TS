import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("test");
        assertEquals("test", optionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("test");
        assertEquals("test", optionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueseparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValuesep());
    }

    @Test
    public void testWithValueseparatorDefault() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(2, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test");
        assertEquals("test", optionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateEmpty() {
        try {
            OptionBuilder.create();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
}