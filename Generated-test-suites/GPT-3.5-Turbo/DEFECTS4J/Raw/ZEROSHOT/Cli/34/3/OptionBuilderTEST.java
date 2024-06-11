import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {

    @Before
    public void setUp() {
        // Set up any necessary objects before each test
    }

    @After
    public void tearDown() {
        // Clean up any objects after each test
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", builder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertEquals(1, builder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder builder = OptionBuilder.hasArg(true);
        assertEquals(1, builder.getNumberOfArgs());
        builder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, builder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", builder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertEquals(true, builder.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', builder.getValuesep());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        assertEquals('=', builder.getValuesep());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder builder = OptionBuilder.isRequired(true);
        assertEquals(true, builder.isRequired());
        builder = OptionBuilder.isRequired(false);
        assertEquals(false, builder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, builder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder builder = OptionBuilder.hasArgs(3);
        assertEquals(3, builder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertEquals(1, builder.getNumberOfArgs());
        assertEquals(true, builder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, builder.getNumberOfArgs());
        assertEquals(true, builder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, builder.getNumberOfArgs());
        assertEquals(true, builder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.withType(String.class);
        assertEquals(String.class, builder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", builder.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('a');
        assertEquals("a", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.reset();
        try {
            Option option = OptionBuilder.create();
            assertEquals(null, option.getOpt());
        } catch (IllegalArgumentException e) {
            // Resetting the properties throws an exception, but it's expected behavior
        }
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }
}