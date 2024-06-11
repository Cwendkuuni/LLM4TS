import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    private OptionBuilder optionBuilder;

    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder result = optionBuilder.withLongOpt("testLongOpt");
        assertNotNull(result);
        assertEquals("testLongOpt", OptionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder result = optionBuilder.hasArg();
        assertNotNull(result);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArg_WithTrue() {
        OptionBuilder result = optionBuilder.hasArg(true);
        assertNotNull(result);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArg_WithFalse() {
        OptionBuilder result = optionBuilder.hasArg(false);
        assertNotNull(result);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder result = optionBuilder.withArgName("testArgName");
        assertNotNull(result);
        assertEquals("testArgName", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder result = optionBuilder.isRequired();
        assertNotNull(result);
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder result = optionBuilder.withValueSeparator(':');
        assertNotNull(result);
        assertEquals(':', OptionBuilder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator_Default() {
        OptionBuilder result = optionBuilder.withValueSeparator();
        assertNotNull(result);
        assertEquals('=', OptionBuilder.getValueSeparator());
    }

    @Test
    public void testIsRequired_WithTrue() {
        OptionBuilder result = optionBuilder.isRequired(true);
        assertNotNull(result);
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder result = optionBuilder.hasArgs();
        assertNotNull(result);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgs_WithNumber() {
        OptionBuilder result = optionBuilder.hasArgs(3);
        assertNotNull(result);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder result = optionBuilder.hasOptionalArg();
        assertNotNull(result);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder result = optionBuilder.hasOptionalArgs();
        assertNotNull(result);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs_WithNumber() {
        OptionBuilder result = optionBuilder.hasOptionalArgs(2);
        assertNotNull(result);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Object testType = new Object();
        OptionBuilder result = optionBuilder.withType(testType);
        assertNotNull(result);
        assertEquals(testType, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        String testDescription = "Test Option Description";
        OptionBuilder result = optionBuilder.withDescription(testDescription);
        assertNotNull(result);
        assertEquals(testDescription, OptionBuilder.getDescription());
    }

    @Test
    public void testCreate_WithChar() {
        Option result = optionBuilder.create('t');
        assertNotNull(result);
        assertEquals("t", result.getOpt());
        assertNull(result.getLongOpt());
        assertFalse(result.isRequired());
        assertFalse(result.hasOptionalArg());
        assertFalse(result.hasArgs());
        assertEquals(Option.UNINITIALIZED, result.getArgs());
        assertNull(result.getType());
        assertEquals('\u0000', result.getValueSeparator());
        assertEquals("arg", result.getArgName());
    }

    @Test
    public void testCreate_WithoutOpt() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> optionBuilder.create());
        assertEquals("must specify longopt", exception.getMessage());
    }

    @Test
    public void testCreate_WithStringOpt() {
        Option result = optionBuilder.create("testOption");
        assertNotNull(result);
        assertEquals("testOption", result.getOpt());
        assertNull(result.getLongOpt());
        assertFalse(result.isRequired());
        assertFalse(result.hasOptionalArg());
        assertFalse(result.hasArgs());
        assertEquals(Option.UNINITIALIZED, result.getArgs());
        assertNull(result.getType());
        assertEquals('\u0000', result.getValueSeparator());
        assertEquals("arg", result.getArgName());
    }
}