import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.access$getLongopt(optionBuilder));
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.access$getNumberOfArgs(optionBuilder));
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.access$getNumberOfArgs(optionBuilder));

        optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(-1, OptionBuilder.access$getNumberOfArgs(optionBuilder));
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("test");
        assertEquals("test", OptionBuilder.access$getArgName(optionBuilder));
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.access$getRequired(optionBuilder));
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.access$getValuesep(optionBuilder));
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.access$getValuesep(optionBuilder));
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.access$getRequired(optionBuilder));

        optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.access$getRequired(optionBuilder));
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Integer.MAX_VALUE, OptionBuilder.access$getNumberOfArgs(optionBuilder));
    }

    @Test
    public void testHasArgsWithArgNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.access$getNumberOfArgs(optionBuilder));
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.access$getNumberOfArgs(optionBuilder));
        assertTrue(OptionBuilder.access$getOptionalArg(optionBuilder));
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Integer.MAX_VALUE, OptionBuilder.access$getNumberOfArgs(optionBuilder));
        assertTrue(OptionBuilder.access$getOptionalArg(optionBuilder));
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.access$getNumberOfArgs(optionBuilder));
        assertTrue(OptionBuilder.access$getOptionalArg(optionBuilder));
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.access$getType(optionBuilder));
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.access$getDescription(optionBuilder));
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        try {
            OptionBuilder.create();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }
}