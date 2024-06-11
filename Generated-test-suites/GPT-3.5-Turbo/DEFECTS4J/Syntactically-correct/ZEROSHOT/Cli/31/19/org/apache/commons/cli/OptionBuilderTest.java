import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test");
        assertEquals("test", builder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertEquals(1, builder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithArg() {
        OptionBuilder builder = OptionBuilder.hasArg(true);
        assertEquals(1, builder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithoutArg() {
        OptionBuilder builder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, builder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("test");
        assertEquals("test", builder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertTrue(builder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', builder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        assertEquals('=', builder.getValueSeparator());
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder builder = OptionBuilder.isRequired(false);
        assertFalse(builder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, builder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithValue() {
        OptionBuilder builder = OptionBuilder.hasArgs(2);
        assertEquals(2, builder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertTrue(builder.isOptionalArg());
        assertEquals(1, builder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertTrue(builder.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, builder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgsWithValue() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
        assertTrue(builder.isOptionalArg());
        assertEquals(3, builder.getNumberOfArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, builder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("Test option");
        assertEquals("Test option", builder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreateWithoutOpt() {
        try {
            OptionBuilder.create();
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }
}