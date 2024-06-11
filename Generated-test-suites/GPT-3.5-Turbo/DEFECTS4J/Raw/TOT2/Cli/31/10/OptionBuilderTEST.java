import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgCustom() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", OptionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequiredCustom() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgsCustom() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArgsCustom() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.create().getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertEquals('t', option.getOpt());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }
}