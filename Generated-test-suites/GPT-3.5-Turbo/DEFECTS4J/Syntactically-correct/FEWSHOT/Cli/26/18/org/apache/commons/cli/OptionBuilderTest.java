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
    public void hasArgWithHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void hasArgWithNoHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
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
    public void isRequiredWithIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void isRequiredWithNoIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.getValuesep());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test description");
        assertEquals("test description", optionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("test");
        assertNull(option.getOpt());
        assertEquals("test", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt() {
        OptionBuilder.create();
    }
}