import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {

    private OptionBuilder optionBuilder;

    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }

    @Test
    public void testWithLongOpt() {
        optionBuilder.withLongOpt("testOption");
        assertEquals("testOption", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        optionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithArgumentFalse() {
        optionBuilder.hasArg(false);
        assertEquals(0, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        optionBuilder.withArgName("testArg");
        assertEquals("testArg", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        optionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithSeparatorCharacter() {
        optionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValueSeparator());
    }

    @Test
    public void testWithDefaultSeparatorCharacter() {
        optionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        optionBuilder.isRequired(true);
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        optionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithLimit() {
        optionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        optionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        optionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithLimit() {
        optionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        optionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        optionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
        assertEquals("t", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOption");
        assertNotNull(option);
        assertEquals("testOption", option.getOpt());
    }
}