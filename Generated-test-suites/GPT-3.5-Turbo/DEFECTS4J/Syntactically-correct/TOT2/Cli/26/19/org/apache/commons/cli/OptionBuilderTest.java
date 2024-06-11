import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("test");
        assertEquals("test", optionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgCustom() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("customName");
        assertEquals("customName", optionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequiredCustom() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(optionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgsCustom() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArgsCustom() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, optionBuilder.create().getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, optionBuilder.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Description");
        assertEquals("Test Description", optionBuilder.create().getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getLongOpt());
    }
}