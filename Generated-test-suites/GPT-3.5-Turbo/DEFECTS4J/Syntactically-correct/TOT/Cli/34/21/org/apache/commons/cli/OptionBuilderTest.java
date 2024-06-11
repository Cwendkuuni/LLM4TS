import org.junit.*;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    private OptionBuilder optionBuilder;

    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder returnedOptionBuilder = optionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", OptionBuilder.getLongopt());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testHasArg() {
        OptionBuilder returnedOptionBuilder = optionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testHasArgWithParameter() {
        OptionBuilder returnedOptionBuilder = optionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder returnedOptionBuilder = optionBuilder.withArgName("testArgName");
        assertEquals("testArgName", OptionBuilder.getArgName());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder returnedOptionBuilder = optionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testWithSeparator() {
        OptionBuilder returnedOptionBuilder = optionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValuesep());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testWithDefaultSeparator() {
        OptionBuilder returnedOptionBuilder = optionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testIsRequiredWithParameter() {
        OptionBuilder returnedOptionBuilder = optionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder returnedOptionBuilder = optionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testHasArgsWithParameter() {
        OptionBuilder returnedOptionBuilder = optionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        assertFalse(OptionBuilder.isOptionalArg());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder returnedOptionBuilder = optionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testWithType() {
        OptionBuilder returnedOptionBuilder = optionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.getType());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder returnedOptionBuilder = optionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.getDescription());
        assertEquals(optionBuilder, returnedOptionBuilder);
    }

    @Test
    public void testCreateWithOptChar() {
        optionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create('t');
        assertEquals('t', option.getOpt());
        assertEquals("testLongOpt", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgs());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals('\u0000', option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test
    public void testCreateWithOptString() {
        optionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create("testOpt");
        assertEquals("testOpt", option.getOpt());
        assertEquals("testLongOpt", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgs());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals('\u0000', option.getValueSeparator());
        assertNull(option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        optionBuilder.create();
    }

    @After
    public void tearDown() {
        optionBuilder = null;
    }
}