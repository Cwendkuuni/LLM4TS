import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    OptionBuilder optionBuilder;

    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", optionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBooleanTrue() {
        OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBooleanFalse() {
        OptionBuilder.hasArg(false);
        assertEquals(-1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("arg");
        assertEquals("arg", optionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredWithBooleanTrue() {
        OptionBuilder.isRequired(true);
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(-2, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithArgument() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(-2, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgsWithArgument() {
        OptionBuilder.hasOptionalArgs(2);
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(2, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", optionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.isOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertNull(option.getOpt());
        assertNotNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.isOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("test");
        assertNotNull(option);
        assertEquals("test", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.isOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertNull(option.getArgName());
    }
}