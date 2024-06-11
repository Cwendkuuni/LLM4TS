import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", optionBuilder.getLongopt());
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
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", optionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.getRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValuesep());
    }

    @Test
    public void testWithDefaultValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(optionBuilder.getRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.getOptionalArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertTrue(optionBuilder.getOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(4);
        assertTrue(optionBuilder.getOptionalArg());
        assertEquals(4, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, optionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertEquals("description", optionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('c');
        assertEquals("c", option.getOpt());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("option");
        assertEquals("option", option.getOpt());
    }
}