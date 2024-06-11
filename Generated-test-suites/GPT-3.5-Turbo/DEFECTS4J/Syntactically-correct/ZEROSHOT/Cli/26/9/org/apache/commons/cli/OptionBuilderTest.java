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
        OptionBuilder result = OptionBuilder.withLongOpt("newLongopt");
        assertEquals("newLongopt", result);
    }

    @Test
    public void testHasArg() {
        OptionBuilder result = OptionBuilder.hasArg();
        assertEquals(1, result);
    }

    @Test
    public void testHasArgWithParameter() {
        OptionBuilder result = OptionBuilder.hasArg(true);
        assertEquals(1, result);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder result = OptionBuilder.withArgName("name");
        assertEquals("name", result);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder result = OptionBuilder.isRequired();
        assertEquals(true, result);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder result = OptionBuilder.withValueSeparator(':');
        assertEquals(':', result);
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder result = OptionBuilder.withValueSeparator();
        assertEquals('=', result);
    }

    @Test
    public void testIsRequiredWithParameter() {
        OptionBuilder result = OptionBuilder.isRequired(true);
        assertEquals(true, result);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder result = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, result);
    }

    @Test
    public void testHasArgsWithParameter() {
        OptionBuilder result = OptionBuilder.hasArgs(5);
        assertEquals(5, result);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder result = OptionBuilder.hasOptionalArg();
        assertEquals(1, result);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder result = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, result);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithParameter() {
        OptionBuilder result = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, result);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder result = OptionBuilder.withType(new Object());
        assertEquals(new Object(), result);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder result = OptionBuilder.withDescription("newDescription");
        assertEquals("newDescription", result);
    }

    @Test
    public void testCreateWithChar() throws IllegalArgumentException {
        Option result = OptionBuilder.create('D');
        assertEquals('D', result.getOpt());
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("newLongopt");
        Option result = OptionBuilder.create();
        assertEquals("newLongopt", result.getLongOpt());
    }

    @Test
    public void testCreateWithString() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("newLongopt");
        Option result = OptionBuilder.create("opt");
        assertEquals("opt", result.getOpt());
        assertEquals("newLongopt", result.getLongOpt());
    }
}