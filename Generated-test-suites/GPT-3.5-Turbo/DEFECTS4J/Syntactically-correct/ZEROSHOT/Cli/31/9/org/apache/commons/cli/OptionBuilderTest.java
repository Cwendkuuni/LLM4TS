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
        OptionBuilder result = optionBuilder.withLongOpt("test");
        assertEquals(optionBuilder, result);
        assertEquals("test", result.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder result = optionBuilder.hasArg();
        assertEquals(optionBuilder, result);
        assertEquals(1, result.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithParameter() {
        OptionBuilder result = optionBuilder.hasArg(false);
        assertEquals(optionBuilder, result);
        assertEquals(Option.UNINITIALIZED, result.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder result = optionBuilder.withArgName("test");
        assertEquals(optionBuilder, result);
        assertEquals("test", result.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder result = optionBuilder.isRequired();
        assertEquals(optionBuilder, result);
        assertTrue(result.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder result = optionBuilder.withValueSeparator(':');
        assertEquals(optionBuilder, result);
        assertEquals(':', result.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder result = optionBuilder.withValueSeparator();
        assertEquals(optionBuilder, result);
        assertEquals('=', result.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithParameter() {
        OptionBuilder result = optionBuilder.isRequired(false);
        assertEquals(optionBuilder, result);
        assertFalse(result.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder result = optionBuilder.hasArgs();
        assertEquals(optionBuilder, result);
        assertEquals(Option.UNLIMITED_VALUES, result.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithParameter() {
        OptionBuilder result = optionBuilder.hasArgs(2);
        assertEquals(optionBuilder, result);
        assertEquals(2, result.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder result = optionBuilder.hasOptionalArg();
        assertEquals(optionBuilder, result);
        assertEquals(1, result.getNumberOfArgs());
        assertTrue(result.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder result = optionBuilder.hasOptionalArgs();
        assertEquals(optionBuilder, result);
        assertEquals(Option.UNLIMITED_VALUES, result.getNumberOfArgs());
        assertTrue(result.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithParameter() {
        OptionBuilder result = optionBuilder.hasOptionalArgs(3);
        assertEquals(optionBuilder, result);
        assertEquals(3, result.getNumberOfArgs());
        assertTrue(result.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Object obj = new Object();
        OptionBuilder result = optionBuilder.withType(obj);
        assertEquals(optionBuilder, result);
        assertEquals(obj, result.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder result = optionBuilder.withDescription("test");
        assertEquals(optionBuilder, result);
        assertEquals("test", result.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option result = optionBuilder.create('t');
        assertEquals("t", result.getOpt());
        assertNull(result.getDescription());
        assertNull(result.getLongOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withDescription("test");
        Option result = optionBuilder.create();
        assertNull(result.getOpt());
        assertEquals("test", result.getDescription());
        assertNotNull(result.getLongOpt());
    }

    @Test
    public void testCreateString() {
        OptionBuilder.withDescription("test");
        Option result = optionBuilder.create("t");
        assertEquals("t", result.getOpt());
        assertEquals("test", result.getDescription());
        assertNotNull(result.getLongOpt());
    }
}